package com.ks.fengoj.judge.codeSanbox.model;

import lombok.Data;

import java.util.List;

/**
 * 响应判题信息对象
 */
@Data
public class ResponseJudgeInfo {
    private List<JudgeInfo> judgeInfoList;

    /**
     * ACM 判题状态
     */
    private String status;

    /**
     * OI 判题分数
     */
    private Integer score;

    /**
     * 最大使用内存
     */
    private Long memory;

    /**
     * 最大运行时间
     */
    private Long time;
}
