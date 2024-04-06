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
        List<JudgeInfo> judgeInfo = judgeContext.getJudgeInfo();
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        List<String> outputCaseList = judgeContext.getOutputCaseList();

        Question question = judgeContext.getQuestion();
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long needMemoryLimit = judgeConfig.getMemoryLimit();
        Long needTimeLimit = judgeConfig.getTimeLimit();

        ResponseJudgeInfo resp = new ResponseJudgeInfo();
        List<JudgeInfo> judgeInfoList = new ArrayList<>();

        // TODO 编译错误

        // 判断题目配置条件
        JudgeInfoMessageEnum j = ACCEPTED;
        long maxMemory = 0L, maxTime = 0L;
        for (int i = 0; i < outputList.size(); i ++) {
            JudgeInfo info = new JudgeInfo();
            String input = outputCaseList.get(i);
            String output = outputList.get(i);
            Long memory = judgeInfo.get(i).getMemory();
            Long time = judgeInfo.get(i).getTime();

            maxTime = Math.max(maxTime, time);
            maxMemory = Math.max(maxMemory, memory);

            info.setMessage(ACCEPTED.getValue());
            info.setTime(time);
            info.setMemory(memory);

            // 如果不相等
            if (!input.equals(output) && !output.equals(input + "\r\n")) {
                info.setMessage(WRONG_ANSWER.getValue());
                j = WRONG_ANSWER;
            }

            // 判断内存
            if (memory > needMemoryLimit) {
                info.setMessage(MEMORY_LIMIT_EXCEEDED.getValue());
                j = MEMORY_LIMIT_EXCEEDED;
            }

            // 判断时间
            if (time > needTimeLimit) {
                info.setMessage(TIME_LIMIT_EXCEEDED.getValue());
                j = TIME_LIMIT_EXCEEDED;
            }

            judgeInfoList.add(info);
        }

        resp.setJudgeInfoList(judgeInfoList);
        resp.setMemory(maxMemory);
        resp.setTime(maxTime);
        resp.setStatus(j.getValue());

        return resp;
    }
}
