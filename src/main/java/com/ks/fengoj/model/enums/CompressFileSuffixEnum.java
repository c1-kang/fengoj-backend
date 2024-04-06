package com.ks.fengoj.model.enums;

import org.apache.commons.lang3.ObjectUtils;

public enum CompressFileSuffixEnum {

    ZIP("zip"),
    SEVEN_Z("7z"),
    RAR("rar");

    private final String value;

    CompressFileSuffixEnum(String value) {
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     * @param value value
     * @return CompressFileSuffixEnum
     */
    public static CompressFileSuffixEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (CompressFileSuffixEnum anEnum : CompressFileSuffixEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}
