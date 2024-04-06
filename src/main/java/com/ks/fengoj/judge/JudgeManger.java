package com.ks.fengoj.judge;

import com.ks.fengoj.judge.codeSanbox.model.ResponseJudgeInfo;
import com.ks.fengoj.judge.strategy.DefaultJudgeStrategy;
import com.ks.fengoj.judge.strategy.OIJudgeStrategy;
import com.ks.fengoj.judge.strategy.JudgeContext;
import com.ks.fengoj.judge.strategy.JudgeStrategy;
import com.ks.fengoj.judge.codeSanbox.model.JudgeInfo;
import com.ks.fengoj.model.enums.JudgeModeEnum;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class JudgeManger {
    /***
     * 执行判题
     * @param judgeContext judgeContext
     * @return JudgeInfo
     */
    ResponseJudgeInfo doJudge(JudgeContext judgeContext) {
        String mode = judgeContext.getMode();
        JudgeModeEnum modeEnum = JudgeModeEnum.getEnumByValue(mode);

        switch (Objects.requireNonNull(modeEnum)) {
            case ACM:
                return new DefaultJudgeStrategy().doJudge(judgeContext);
            case OI:
                return new OIJudgeStrategy().doJudge(judgeContext);
            default:
                return new DefaultJudgeStrategy().doJudge(judgeContext);
        }
    }
}
