package com.ks.fengoj.judge.codeSanbox.model.remote;

import lombok.Data;

/**
 * 返回结果的 files
 * <p>
 * "files": {
 * "stderr": "",
 * "stdout": "3\r\n"
 * }
 */
@Data
public class RespFile {
    private String stderr;

    private String stdout;
}
