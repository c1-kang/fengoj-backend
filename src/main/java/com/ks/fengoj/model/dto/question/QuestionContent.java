package com.ks.fengoj.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * 题目内容
 */
@Data
public class QuestionContent implements Serializable {

    /**
     * 题目描述
     */
    private String questionDesc;

    /**
     * 输入描述
     */
    private String inputDesc;

    /**
     * 输出描述
     */
    private String outputDesc;

    private static final long serialVersionUID = 1L;
}
