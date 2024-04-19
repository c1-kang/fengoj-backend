package com.ks.fengoj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.ks.fengoj.annotation.AuthCheck;
import com.ks.fengoj.common.BaseResponse;
import com.ks.fengoj.common.DeleteRequest;
import com.ks.fengoj.common.ErrorCode;
import com.ks.fengoj.common.ResultUtils;
import com.ks.fengoj.constant.UserConstant;
import com.ks.fengoj.exception.BusinessException;
import com.ks.fengoj.exception.ThrowUtils;
import com.ks.fengoj.judge.codeSanbox.CodeSandbox;
import com.ks.fengoj.judge.codeSanbox.CodeSandboxFactory;
import com.ks.fengoj.judge.codeSanbox.CodeSandboxProxy;
import com.ks.fengoj.judge.codeSanbox.model.ExecuteCodeRequest;
import com.ks.fengoj.judge.codeSanbox.model.ExecuteCodeResponse;
import com.ks.fengoj.judge.codeSanbox.model.JudgeInfo;
import com.ks.fengoj.model.dto.question.*;
import com.ks.fengoj.model.dto.questionSubmit.QuestionQuerySubmitRequest;
import com.ks.fengoj.model.dto.questionSubmit.QuestionSubmitAddRequest;
import com.ks.fengoj.model.dto.questionSubmit.QuestionSubmitTestRequest;
import com.ks.fengoj.model.dto.questionSubmit.QuestionSubmitTestResponse;
import com.ks.fengoj.model.entity.Question;
import com.ks.fengoj.model.entity.QuestionSubmit;
import com.ks.fengoj.model.entity.User;
import com.ks.fengoj.model.vo.QuestionSubmitVO;
import com.ks.fengoj.model.vo.QuestionVO;
import com.ks.fengoj.service.QuestionService;
import com.ks.fengoj.service.QuestionSubmitService;
import com.ks.fengoj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 题目接口
 */
@RestController
@RequestMapping("/question")
@Slf4j
public class QuestionController {

    private final static Gson GSON = new Gson();
    @Resource
    private QuestionService questionService;
    @Resource
    private UserService userService;
    @Resource
    private QuestionSubmitService questionSubmitService;

    @Value("${codesandbox.type:example}")
    private String type;


    // region 增删改查

    /**
     * 递归删除文件夹
     *
     * @param folder 文件夹
     */
    public static void deleteFolder(File folder) {
        // 获取文件夹中的所有文件和子文件夹
        File[] files = folder.listFiles();
        if (files != null) { // 确保文件夹不为空
            for (File file : files) {
                // 如果是文件，直接删除
                if (file.isFile()) {
                    file.delete();
                } else if (file.isDirectory()) { // 如果是子文件夹，递归删除
                    deleteFolder(file);
                }
            }
        }
        // 删除空文件夹
        folder.delete();
    }

    /**
     * 创建
     *
     * @param questionAddRequest questionAddRequest
     * @param request            request
     * @return 新问题 ID
     */
    @PostMapping("/add")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addQuestion(@RequestBody QuestionAddRequest questionAddRequest, HttpServletRequest request) {
        if (questionAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionAddRequest, question);
        List<String> tags = questionAddRequest.getTags();
        if (tags != null) {
            question.setTags(GSON.toJson(tags));
        }
        List<JudgeCase> judgeCase = questionAddRequest.getJudgeCase();
        if (judgeCase != null) {
            question.setJudgeCase(GSON.toJson(judgeCase));
        }
        JudgeConfig judgeConfig = questionAddRequest.getJudgeConfig();
        if (judgeConfig != null) {
            question.setJudgeConfig(GSON.toJson(judgeConfig));
        }
        QuestionContent questionContent = questionAddRequest.getContent();
        if (questionContent != null) {
            question.setContent(GSON.toJson(questionContent));
        }
        questionService.validQuestion(question, true);
        User loginUser = userService.getLoginUser(request);
        question.setUserId(loginUser.getId());
        boolean result = questionService.save(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newQuestionId = question.getId();
        return ResultUtils.success(newQuestionId);
    }

    /**
     * 删除
     *
     * @param deleteRequest deleteRequest
     * @param request       request
     * @return Boolean
     */
    @PostMapping("/delete")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteQuestion(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);

        // 暂时，只有 chanchan 管理员能删除题目
        if (!user.getUserAccount().equals("chanchan")) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldQuestion.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        String judgeCaseUrl = oldQuestion.getJudgeCaseUrl();
        File file = new File(judgeCaseUrl);
        deleteFolder(file);
        Boolean res = questionSubmitService.deleteSubmitsByQuestionId(id);
        if (!res) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "所删题目关联提交删除失败");
        }
        boolean b = questionService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param questionUpdateRequest questionUpdateRequest
     * @return Boolean
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateQuestion(@RequestBody QuestionUpdateRequest questionUpdateRequest, HttpServletRequest request) {
        if (questionUpdateRequest == null || questionUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionUpdateRequest, question);
        List<String> tags = questionUpdateRequest.getTags();
        if (tags != null) {
            question.setTags(GSON.toJson(tags));
        }
        List<JudgeCase> judgeCase = questionUpdateRequest.getJudgeCase();
        if (judgeCase != null) {
            question.setJudgeCase(GSON.toJson(judgeCase));
        }
        JudgeConfig judgeConfig = questionUpdateRequest.getJudgeConfig();
        if (judgeConfig != null) {
            question.setJudgeConfig(GSON.toJson(judgeConfig));
        }
        QuestionContent questionContent = questionUpdateRequest.getContent();
        if (questionContent != null) {
            question.setContent(GSON.toJson(questionContent));
        }
        // 参数校验
        questionService.validQuestion(question, false);
        long id = questionUpdateRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);

        // 删除过期的样例数据
        String mainPath = System.getProperty("user.dir") + File.separator + "JudgeCaseDir";
        User loginUser = userService.getLoginUser(request);
        String loginUserPath = mainPath + File.separator + loginUser.getId();
        File file = new File(loginUserPath);
        File[] files = file.listFiles();
        for (File tmpFile : files) {
            if (!tmpFile.getAbsolutePath().equals(questionUpdateRequest.getJudgeCaseUrl())) {
                deleteFolder(tmpFile);
            }
        }

        boolean result = questionService.updateById(question);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取（脱敏）
     *
     * @param id question ID
     * @return QuestionVO
     */
    @GetMapping("/get/vo")
    public BaseResponse<QuestionVO> getQuestionVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = questionService.getById(id);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(questionService.getQuestionVO(question, request));
    }

    /**
     * 根据 id 获取
     *
     * @param id ID
     * @return Question
     */
    @GetMapping("/get")
    public BaseResponse<Question> getQuestionById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = questionService.getById(id);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        // 不是本人或管理员
        if (!question.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return ResultUtils.success(question);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param questionQueryRequest questionQueryRequest
     * @param request              request
     * @return Page<QuestionVO>
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                               HttpServletRequest request) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }

    // endregion

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param questionQueryRequest questionQueryRequest
     * @param request              request
     * @return Page<QuestionVO>
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listMyQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                                 HttpServletRequest request) {
        if (questionQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        questionQueryRequest.setUserId(loginUser.getId());
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }

    /**
     * 编辑（用户）
     *
     * @param questionEditRequest questionEditRequest
     * @param request             request
     * @return Boolean
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editQuestion(@RequestBody QuestionEditRequest questionEditRequest, HttpServletRequest request) {
        if (questionEditRequest == null || questionEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionEditRequest, question);
        List<String> tags = questionEditRequest.getTags();
        if (tags != null) {
            question.setTags(GSON.toJson(tags));
        }
        List<JudgeCase> judgeCase = questionEditRequest.getJudgeCase();
        if (judgeCase != null) {
            question.setJudgeCase(GSON.toJson(judgeCase));
        }
        JudgeConfig judgeConfig = questionEditRequest.getJudgeConfig();
        if (judgeConfig != null) {
            question.setJudgeConfig(GSON.toJson(judgeConfig));
        }
        // 参数校验
        questionService.validQuestion(question, false);
        User loginUser = userService.getLoginUser(request);
        long id = questionEditRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldQuestion.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = questionService.updateById(question);
        return ResultUtils.success(result);
    }

    /**
     * 分页获取用户列表（仅管理员）
     *
     * @param questionQueryRequest 查询问题请求
     * @return Page<Question>
     */
    @PostMapping("/list/page")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Question>> listQuestionByPage(@RequestBody QuestionQueryRequest questionQueryRequest) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionPage);
    }

    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest questionSubmitAddRequest
     * @param request                  request
     * @return resultNum 本次题目提交变化数
     */
    @PostMapping("/question_submit/do")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
                                               HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能题目提交
        final User loginUser = userService.getLoginUser(request);
        // User loginUser = new User();
        // loginUser.setId(1L);
        Long result = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 测试题目代码
     *
     * @param questionSubmitTestRequest questionSubmitTestRequest
     * @return QuestionSubmitTestResponse
     */
    @PostMapping("/question_submit/test")
    public BaseResponse<QuestionSubmitTestResponse> testJudge(@RequestBody QuestionSubmitTestRequest questionSubmitTestRequest) {
        if (questionSubmitTestRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String language = questionSubmitTestRequest.getLanguage();
        String code = questionSubmitTestRequest.getCode();
        String input = questionSubmitTestRequest.getInput();

        // 参数不能为空
        if (StringUtils.isAnyBlank(language, code, input)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数不能为空");
        }

        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        List<String> inputList = new ArrayList<>();
        inputList.add(input);
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        QuestionSubmitTestResponse result = new QuestionSubmitTestResponse();
        List<String> outputList = executeCodeResponse.getOutputList();
        String message = executeCodeResponse.getMessage();
        Integer status = executeCodeResponse.getStatus();
        List<JudgeInfo> judgeInfos = executeCodeResponse.getJudgeInfo();
        JudgeInfo judgeInfo = judgeInfos.get(0);

        if (status.equals(3)) {
            result.setStatus(judgeInfo.getMessage());
            result.setErrorMessage(judgeInfo.getErrorMessage());
            return ResultUtils.success(result);
        }
        result.setStatus(judgeInfo.getMessage());
        result.setTime(judgeInfo.getTime());
        result.setResult(outputList.get(0));
        return ResultUtils.success(result);
    }

    /**
     * 分页获取提交列表
     *
     * @param questionQuerySubmitRequest questionQuerySubmitRequest
     * @param request                    request
     * @return Page<QuestionSubmitVO>
     */
    @PostMapping("/question_submit/list/page")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionQuerySubmitRequest questionQuerySubmitRequest,
                                                                         HttpServletRequest request) {
        long current = questionQuerySubmitRequest.getCurrent();
        long size = questionQuerySubmitRequest.getPageSize();
        // 从数据库中查询题目提交信息
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
                questionSubmitService.getQueryWrapper(questionQuerySubmitRequest));
        // 返回脱敏后的信息
        final User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, loginUser));
    }

    /**
     * 根据 ID 获取提交
     *
     * @param id ID
     * @return QuestionSubmitVO
     */
    @GetMapping("/question_submit/get")
    public BaseResponse<QuestionSubmitVO> getSubmitById(@RequestParam("submitId") String id, HttpServletRequest request) {
        Long submitId = Long.parseLong(id);
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // Long userId = loginUser.getId();
        QuestionSubmit submit = questionSubmitService.getById(submitId);
        // Long submitUserId = result.getUserId();

        // 既不是管理员，又不是自己提交的，看不到提交代码
        // if (!Objects.equals(userId, submitUserId) && !userService.isAdmin(loginUser)) {
        //     result.setCode("");
        // }
        QuestionSubmitVO questionSubmitVO = questionSubmitService.getQuestionSubmitVO(submit, loginUser);

        return ResultUtils.success(questionSubmitVO);
    }
}
