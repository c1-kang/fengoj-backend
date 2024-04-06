package com.ks.fengoj.judge;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ks.fengoj.common.ErrorCode;
import com.ks.fengoj.exception.BusinessException;
import com.ks.fengoj.judge.codeSanbox.CodeSandbox;
import com.ks.fengoj.judge.codeSanbox.CodeSandboxFactory;
import com.ks.fengoj.judge.codeSanbox.CodeSandboxProxy;
import com.ks.fengoj.judge.codeSanbox.model.ExecuteCodeRequest;
import com.ks.fengoj.judge.codeSanbox.model.ExecuteCodeResponse;
import com.ks.fengoj.judge.codeSanbox.model.JudgeInfo;
import com.ks.fengoj.judge.codeSanbox.model.ResponseJudgeInfo;
import com.ks.fengoj.judge.strategy.JudgeContext;
import com.ks.fengoj.model.entity.Question;
import com.ks.fengoj.model.entity.QuestionSubmit;
import com.ks.fengoj.model.enums.JudgeInfoMessageEnum;
import com.ks.fengoj.model.enums.QuestionSubmitStatusEnum;
import com.ks.fengoj.service.QuestionService;
import com.ks.fengoj.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class JudgeServiceImpl implements JudgeService {
    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private JudgeManger judgeManger;

    @Value("${codesandbox.type:example}")
    private String type;

    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        // 1）传入题目的提交 id，获取到对应的题目、提交信息（包含代码、编程语言等）
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        Integer status = questionSubmit.getStatus();

        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }

        // 2）如果题目提交状态不为等待中，就不用重复执行了
        if (!status.equals(QuestionSubmitStatusEnum.WAITTING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
        }

        // 3）更改判题（题目提交）的状态为 “判题中”，防止重复执行，也能让用户即时看到状态
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean res = questionSubmitService.updateById(questionSubmitUpdate);
        if (!res) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }

        // 4）调用沙箱，获取到执行结果
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        String judgeCaseUrl = question.getJudgeCaseUrl();
        String inputUrl = judgeCaseUrl + File.separator + "in";
        String outputUrl = judgeCaseUrl + File.separator + "out";
        File inputFile = new File(inputUrl);
        List<String> inputList = new ArrayList<>();
        List<String> outputCaseList = new ArrayList<>();
        try {
            List<String> caseNameList = new ArrayList<>();
            // 遍历文件列表，把文件转换成字符串，然后合并成 List
            for (File listFile : Objects.requireNonNull(inputFile.listFiles())) {
                String absolutePath = listFile.getAbsolutePath();
                caseNameList.add(StrUtil.removeSuffix(listFile.getName(), ".in"));
                String s = new String(Files.readAllBytes(Paths.get(absolutePath)));
                inputList.add(s);
            }

            // 保证输入样例和输出样例一样的顺序
            for (String caseName : caseNameList) {
                String absolutePath = outputUrl + File.separator + caseName + ".out";
                String s = new String(Files.readAllBytes(Paths.get(absolutePath)));
                outputCaseList.add(s);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);

        // 5）根据沙箱的执行结果，设置题目的判题状态和信息
        List<String> outputList = executeCodeResponse.getOutputList();

        // 运用策略模式来执行
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setMode("ACM");
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setOutputCaseList(outputCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);
        judgeContext.setErrorMessage(executeCodeResponse.getMessage());

        ResponseJudgeInfo responseJudgeInfo = judgeManger.doJudge(judgeContext);

        // 6）修改数据库中的判题结果
        String message = responseJudgeInfo.getStatus();
        if (message.equals(JudgeInfoMessageEnum.ACCEPTED.getValue())) {
            Question questionTmp = new Question();
            questionTmp.setId(question.getId());
            questionTmp.setAcceptedNum(question.getAcceptedNum() + 1);
            boolean b = questionService.updateById(questionTmp);
            if (!b) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目正确提交数目更新失败");
            }
        }
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(responseJudgeInfo));
        res = questionSubmitService.updateById(questionSubmitUpdate);
        if (!res) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        return questionSubmitService.getById(questionId);
    }

}
