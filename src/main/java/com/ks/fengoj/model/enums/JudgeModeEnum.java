package com.ks.fengoj.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 判题模式枚举
 */
public enum JudgeModeEnum {

    ACM("ACM"),
    OI("OI"),
    SPJ("SPJ");

    private final String key;

    JudgeModeEnum(String key) {
        this.key = key;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param key key
     * @return JudgeModeEnum
     */
    public static JudgeModeEnum getEnumByValue(String key) {
        if (ObjectUtils.isEmpty(key)) {
            return null;
        }
        for (JudgeModeEnum anEnum : JudgeModeEnum.values()) {
            if (anEnum.key.equals(key)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getKey() {
        return key;
    }
}
