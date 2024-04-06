package com.ks.fengoj.model.dto.question;

import lombok.Data;

/**
 * 判题用例
 */
@Data
public class JudgeCase {
    /**
     * 输入用例
     */
    private String input;
    /**
     * 输出用例
     */
    private String output;

    /**
     * 样例说明
     */
    private String desc;
}
