package com.ks.fengoj.judge.codeSanbox;

import com.ks.fengoj.judge.codeSanbox.model.ExecuteCodeRequest;
import com.ks.fengoj.judge.codeSanbox.model.ExecuteCodeResponse;

/**
 * 代码沙箱接口
 */
public interface CodeSandbox {
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
