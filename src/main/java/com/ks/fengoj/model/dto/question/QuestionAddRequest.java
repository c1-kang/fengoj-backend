package com.ks.fengoj.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建请求
 */
@Data
public class QuestionAddRequest implements Serializable {
    /**
     * 标题
     */
    private String title;

    /**
     * 内容(json 对象)
     */
    private QuestionContent content;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 判题用例
     */
    private List<JudgeCase> judgeCase;

    /**
     * 判题配置
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