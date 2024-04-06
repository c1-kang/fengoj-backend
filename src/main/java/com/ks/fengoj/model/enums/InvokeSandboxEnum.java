package com.ks.fengoj.model.enums;

import lombok.Getter;

/**
 * 调用沙箱枚举
 */
@Getter
public enum InvokeSandboxEnum {

    REMOTE("remote"),
    THIRDPARTY("third-party"),
    LOCAL("local");
    private final String key;

    InvokeSandboxEnum(String key) {
        this.key = key;
    }

    public static InvokeSandboxEnum getEnumByName(String name) {
        for (InvokeSandboxEnum i : InvokeSandboxEnum.values()) {
            if (i.key.equals(name)) return i;
        }
        return null;
    }
}
