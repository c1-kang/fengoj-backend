package com.ks.fengoj.model.dto.questionSubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * 测试代码返回结果
 */
@Data
public class QuestionSubmitTestResponse implements Serializable {
    /**
     * 状态
     */
    private String status;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 运行时间
     */
    private Long time;

    /**
     * 运行内存
     */
    private Long memory;

    /**
     * 运行结果
     */
    private String result;

    private static final long serialVersionUID = 1L;
}