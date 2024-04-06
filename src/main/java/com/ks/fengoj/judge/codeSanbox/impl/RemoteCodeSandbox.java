package com.ks.fengoj.judge.codeSanbox.impl;

import com.ks.fengoj.common.ErrorCode;
import com.ks.fengoj.exception.BusinessException;
import com.ks.fengoj.judge.codeSanbox.CodeSandbox;
import com.ks.fengoj.judge.codeSanbox.model.ExecuteCodeRequest;
import com.ks.fengoj.judge.codeSanbox.model.ExecuteCodeResponse;
import com.ks.fengoj.judge.codeSanbox.model.JudgeInfo;
import com.ks.fengoj.judge.codeSanbox.remote.CppCodeSandbox;
import com.ks.fengoj.judge.codeSanbox.remote.JavaCodeSandbox;
import com.ks.fengoj.judge.codeSanbox.remote.PythonCodeSandbox;
import com.ks.fengoj.judge.codeSanbox.remote.remoteTemplate;
import com.ks.fengoj.model.enums.JudgeInfoMessageEnum;
import com.ks.fengoj.model.enums.QuestionSubmitLanguageEnum;
import com.ks.fengoj.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * 远程调用代码沙箱（go-judge）
 */
public class RemoteCodeSandbox implements CodeSandbox {

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        String language = executeCodeRequest.getLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "语言不合法");
        }
        switch(languageEnum) {
            case JAVA:
                return new JavaCodeSandbox().executeCode(executeCodeRequest);
            case CPLUSPLUS:
                return new CppCodeSandbox().executeCode(executeCodeRequest);
            case PYTHON:
                return new PythonCodeSandbox().executeCode(executeCodeRequest);
            default:
                return new JavaCodeSandbox().executeCode(executeCodeRequest);
        }
    }
}
