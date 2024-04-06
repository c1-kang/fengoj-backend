package com.ks.fengoj.judge.codeSanbox;

import com.ks.fengoj.judge.codeSanbox.impl.RemoteCodeSandbox;
import com.ks.fengoj.judge.codeSanbox.impl.LocalCodeSandbox;
import com.ks.fengoj.judge.codeSanbox.impl.ThirdpartyCodeSandbox;
import com.ks.fengoj.model.enums.InvokeSandboxEnum;

import java.util.Objects;

/**
 * 代码沙箱工厂（根据字符串参数创建指定的代码沙箱实例）
 */
public class CodeSandboxFactory {
    /**
     * 创建代码沙箱实例
     * @param type 沙箱类型
     * @return 实例
     */
    public static CodeSandbox newInstance(String type) {

        InvokeSandboxEnum i = InvokeSandboxEnum.getEnumByName(type);

        switch(Objects.requireNonNull(i)) {
            case REMOTE:
                return new RemoteCodeSandbox();
            case LOCAL:
                return new LocalCodeSandbox();
            case THIRDPARTY:
                return new ThirdpartyCodeSandbox();
            default:
                return new RemoteCodeSandbox();
        }
    }
}
