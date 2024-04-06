package com.ks.fengoj.judge.strategy;

import com.ks.fengoj.judge.codeSanbox.model.ResponseJudgeInfo;

/**
 * 判题策略
 */
public interface JudgeStrategy {
    /***
     * 执行判题
     * @param judgeContext judgeContext
     * @return JudgeInfo
     */
    ResponseJudgeInfo doJudge(JudgeContext judgeContext);
}
