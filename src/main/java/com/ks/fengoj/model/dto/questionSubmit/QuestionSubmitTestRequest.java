package com.ks.fengoj.model.dto.questionSubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * 测试代码请求
 */
@Data
public class QuestionSubmitTestRequest implements Serializable {

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 输入样例
     */
    private String input;

    private static final long serialVersionUID = 1L;
}