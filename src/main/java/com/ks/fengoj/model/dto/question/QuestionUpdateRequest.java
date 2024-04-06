package com.ks.fengoj.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新请求
 */
@Data
public class QuestionUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private QuestionContent content;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 判题用例（json 数组）
     */
    private List<JudgeCase> judgeCase;

    /**
     * 判题配置（json 对象）
     */
    private JudgeConfig judgeConfig;

    /**
     * 难度
     */
    private Integer level;

    /**
     * 样例所在地址
     */
    private String judgeCaseUrl;

    private static final long serialVersionUID = 1L;
}