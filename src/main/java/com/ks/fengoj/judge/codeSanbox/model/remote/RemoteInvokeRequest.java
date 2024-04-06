package com.ks.fengoj.judge.codeSanbox.model.remote;

import lombok.Data;

import java.util.List;

/**
 * 调用远程接口请求
 * <p>
 * interface Request {
 * requestId?: string; // for WebSocket requests
 * cmd: Cmd[];
 * pipeMapping?: PipeMap[];
 * }
 */
@Data
public class RemoteInvokeRequest {
    private String requestId;

    private List<CmdInfo> cmd;
}
