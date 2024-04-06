package com.ks.fengoj.judge.codeSanbox.remote;

import com.ks.fengoj.judge.codeSanbox.model.ExecuteCodeRequest;
import com.ks.fengoj.judge.codeSanbox.model.ExecuteCodeResponse;
import org.springframework.stereotype.Component;

@Component
public class JavaCodeSandbox extends remoteTemplate{

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        return super.executeCode(executeCodeRequest);
    }
}
