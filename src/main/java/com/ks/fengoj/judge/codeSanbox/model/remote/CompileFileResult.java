package com.ks.fengoj.judge.codeSanbox.model.remote;

import lombok.Data;

/**
 * 编译文件的结果
 */
@Data
public class CompileFileResult {

    /**
     * 状态 0-Accepted 1-编译错误
     */
    private Integer status;

    /**
     * 根据状态定义，0-FileId 1-错误原因
     */
    private String content;
}
