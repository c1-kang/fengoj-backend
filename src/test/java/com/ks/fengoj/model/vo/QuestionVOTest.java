package com.ks.fengoj.model.vo;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.ks.fengoj.model.dto.question.JudgeCase;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class QuestionVOTest {

    @Test
    void objToVo() {
        String str = "[{\"input\":\"1 2\",\"output\":\"3\"},{\"input\":\"6 8\",\"output\":\"14\"}]";
        JSONArray objects = JSONUtil.parseArray(str);
        List<JudgeCase> list = new ArrayList<>();
        for (Object s : objects) {
            String string = s.toString();
            JudgeCase bean = JSONUtil.toBean(string, JudgeCase.class);
            list.add(bean);
//            System.out.println(bean);
        }
        System.out.println(list);
//        System.out.println(objects.get(0));
    }
}