package com.ks.fengoj.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 题目提交语言枚举
 */
public enum JudgeInfoMessageEnum {

    ACCEPTED("成功", "Accepted"),
    WRONG_ANSWER("答案错误", "WrongAnswer"),
    COMPILE_ERROR("编译错误", "CompileError"),
    MEMORY_LIMIT_EXCEEDED("内存溢出", "MemoryLimitExceeded"),
    TIME_LIMIT_EXCEEDED("超时", "TimeLimitExceeded"),
    PRESENTATION_ERROR("展示格式错误","PresentationError"),
    OUTPUT_LIMIT_EXCEEDED( "输出溢出","OutputLimitExceeded"),
    WAITING("等待中","Waiting"),
    DANGEROUS_OPERATION( "危险操作","DangerousOperation"),
    RUNTIME_ERROR( "运行错误","RuntimeError"),
    SYSTEM_ERROR( "系统错误","SystemError");

    private final String text;

    private final String value;

    JudgeInfoMessageEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static JudgeInfoMessageEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (JudgeInfoMessageEnum anEnum : JudgeInfoMessageEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
