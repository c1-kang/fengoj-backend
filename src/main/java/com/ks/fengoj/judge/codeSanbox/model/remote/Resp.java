package com.ks.fengoj.judge.codeSanbox.model.remote;

import lombok.Data;

/**
 * 返回结果
 */
@Data
public class Resp {
    private String status;
    private Integer exitStatus;
    private Long time;
    private Long memory;
    private Long runTime;
    private RespFile files;
}
