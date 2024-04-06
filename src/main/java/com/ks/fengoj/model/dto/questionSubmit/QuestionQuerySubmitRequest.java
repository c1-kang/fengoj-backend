package com.ks.fengoj.model.dto.questionSubmit;

import com.ks.fengoj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionQuerySubmitRequest extends PageRequest implements Serializable {
    /**
     * 编程语言
     */
    private String language;
    /**
     * 提交状态
     */
    private Integer status;
    /**
     * 题目 ID
     */
    private Long questionId;
    /**
     * 创建用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}