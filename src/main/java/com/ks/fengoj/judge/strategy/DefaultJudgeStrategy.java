package com.ks.fengoj.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.ks.fengoj.judge.codeSanbox.model.JudgeInfo;
import com.ks.fengoj.judge.codeSanbox.model.ResponseJudgeInfo;
import com.ks.fengoj.model.dto.question.JudgeConfig;
import com.ks.fengoj.model.entity.Question;
import com.ks.fengoj.model.enums.JudgeInfoMessageEnum;

import java.util.ArrayList;
import java.util.List;

import static com.ks.fengoj.model.enums.JudgeInfoMessageEnum.*;

/**
 * ACM 模式
 */
public class DefaultJudgeStrategy implements JudgeStrategy {

    @Override
    public ResponseJudgeInfo doJudge(JudgeContext judgeContext) {
        ResponseJudgeInfo resp = new ResponseJudgeInfo();

        List<JudgeInfo> judgeInfo = judgeContext.getJudgeInfo();
        List<String> outputList = judgeContext.getOutputList();
        List<String> outputCaseList = judgeContext.getOutputCaseList();
        Integer status = judgeContext.getStatus();

        if (status == 3) {
            resp.setStatus(COMPILE_ERROR.getValue());
            resp.setErrorMessage(judgeInfo.get(0).getErrorMessage());
            return resp;
        }

        Question question = judgeContext.getQuestion();
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long needMemoryLimit = judgeConfig.getMemoryLimit();
        Long needTimeLimit = judgeConfig.getTimeLimit();

        // 判断题目配置条件
        JudgeInfoMessageEnum j = ACCEPTED;
        long maxMemory = 0L, maxTime = 0L;
        for (int i = 0; i < outputList.size(); i ++) {
            String input = outputCaseList.get(i);
            String output = outputList.get(i);
            Long memory = judgeInfo.get(i).getMemory();
            Long time = judgeInfo.get(i).getTime();
            String message = judgeInfo.get(i).getMessage();

            if (message.equals(RUNTIME_ERROR.getValue())) {
                j = RUNTIME_ERROR;
                resp.setErrorMessage(judgeInfo.get(i).getErrorMessage());
                break;
            }

            maxTime = Math.max(maxTime, time);
            maxMemory = Math.max(maxMemory, memory);

            // 如果不相等
            if (!input.equals(output) && !output.equals(input + "\r\n")) {
                j = WRONG_ANSWER;
                break;
            }

            // 判断内存
            if (memory > needMemoryLimit) {
                j = MEMORY_LIMIT_EXCEEDED;
                break;
            }

            // 判断时间
            if (time > needTimeLimit) {
                j = TIME_LIMIT_EXCEEDED;
                break;
            }
        }

        resp.setMemory(maxMemory);
        resp.setTime(maxTime);
        resp.setStatus(j.getValue());

        return resp;
    }
}
