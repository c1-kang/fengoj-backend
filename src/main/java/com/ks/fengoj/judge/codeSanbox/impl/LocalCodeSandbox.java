package com.ks.fengoj.judge.codeSanbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.util.StringUtils;
import com.ks.fengoj.common.ErrorCode;
import com.ks.fengoj.exception.BusinessException;
import com.ks.fengoj.judge.codeSanbox.CodeSandbox;
import com.ks.fengoj.judge.codeSanbox.model.ExecuteCodeRequest;
import com.ks.fengoj.judge.codeSanbox.model.ExecuteCodeResponse;

/**
 * 本地代码沙箱（自己实现的简陋版）
 */
public class LocalCodeSandbox implements CodeSandbox {
    // 定义鉴权请求头和密钥
    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_SECRET = "secretKey";


    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("本地代码沙箱");
        String url = "http://localhost:8090/executeCode";
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        String responseStr = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .body(json)
                .execute()
                .body();
        if (StringUtils.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "executeCode remoteSandbox error, message = " + responseStr);
        }
        return JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);
    }
}
