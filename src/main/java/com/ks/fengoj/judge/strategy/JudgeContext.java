package com.ks.fengoj.judge.strategy;

import com.ks.fengoj.model.dto.question.JudgeCase;
import com.ks.fengoj.judge.codeSanbox.model.JudgeInfo;
import com.ks.fengoj.model.entity.Question;
import com.ks.fengoj.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 上下文（用于在策略中传递的参数）
 */
@Data
public class JudgeContext {
    /**
     * 判题信息
     */
    private List<JudgeInfo> judgeInfo;

    /**
     * 判题模式 -ACM OI
     */
    private String mode;

    /**
     * 判题状态
     */
    private Integer status;

    /**
     * 输入
     */
    private List<String> inputList;

    /**
     * 代码沙箱输出
     */
    private List<String> outputList;

    /**
     * 输出样例列表
     */
    private List<String> outputCaseList;

    /**
     * 题目
     */
    private Question question;

    /**
     * 题目提交
     */
    private QuestionSubmit questionSubmit;

    /**
     * 报错信息
     */
    private String errorMessage;
}
