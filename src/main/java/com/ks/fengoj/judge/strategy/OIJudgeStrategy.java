package com.ks.fengoj.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.ks.fengoj.judge.codeSanbox.model.ResponseJudgeInfo;
import com.ks.fengoj.model.dto.question.JudgeConfig;
import com.ks.fengoj.judge.codeSanbox.model.JudgeInfo;
import com.ks.fengoj.model.entity.Question;
import com.ks.fengoj.model.enums.JudgeInfoMessageEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class OIJudgeStrategy implements JudgeStrategy {

    @Override
    public ResponseJudgeInfo doJudge(JudgeContext judgeContext) {
        // List<JudgeInfo> judgeInfo = judgeContext.getJudgeInfo();
        // List<String> inputList = judgeContext.getInputList();
        // List<String> outputList = judgeContext.getOutputList();
        // List<String> outputCaseList = judgeContext.getOutputCaseList();
        //
        // // Long memory = Optional.ofNullable(judgeInfo.getMemory()).orElse(0L);
        // // Long time = Optional.ofNullable(judgeInfo.getTime()).orElse(0L);
        //
        // Question question = judgeContext.getQuestion();
        // JudgeInfo judgeInfoResponse = new JudgeInfo();
        // // judgeInfoResponse.setMemory(memory);
        // // judgeInfoResponse.setTime(time);
        //
        // JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        //
        // // String message = judgeInfo.getMessage();
        // String message = "Acceped";
        // if (StringUtils.isNotBlank(message) && message.equals("CompileError")) {
        //     judgeInfoMessageEnum = JudgeInfoMessageEnum.COMPILE_ERROR;
        //     judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
        //     judgeInfoResponse.setErrorMessage(judgeContext.getErrorMessage());
        //     return judgeInfoResponse;
        // }
        // // 先判断沙箱执行的结果输出数量是否和预期输出数量相等
        // if (outputList.size() != inputList.size()) {
        //     judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
        //     judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
        //     return judgeInfoResponse;
        // }
        // // 依次判断每一项输出和预期输出是否相等
        // for (int i = 0; i < outputList.size(); i ++) {
        //     String input = outputCaseList.get(i);
        //     input = input.replace("\r", "");
        //     String output = outputList.get(i);
        //     // 如果不相等
        //     if (!input.equals(output)) {
        //         judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
        //         judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
        //         return judgeInfoResponse;
        //     }
        // }
        // // 判断题目限制
        // String judgeConfigStr = question.getJudgeConfig();
        // JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        // Long needMemoryLimit = judgeConfig.getMemoryLimit();
        // Long needTimeLimit = judgeConfig.getTimeLimit();
        // // if (memory > needMemoryLimit) {
        // //     judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
        // //     judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
        // //     return judgeInfoResponse;
        // // }
        // // if (time > needTimeLimit) {
        // //     judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
        // //     judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
        // //     return judgeInfoResponse;
        // // }
        // judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
        // return judgeInfoResponse;

        return null;
    }
}
