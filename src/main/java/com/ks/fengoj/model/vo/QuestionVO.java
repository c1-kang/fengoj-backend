package com.ks.fengoj.model.vo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.ks.fengoj.model.dto.question.JudgeCase;
import com.ks.fengoj.model.dto.question.JudgeConfig;
import com.ks.fengoj.model.dto.question.QuestionContent;
import com.ks.fengoj.model.entity.Question;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 题目封装类
 */
@Data
public class QuestionVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private QuestionContent content;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;

    /**
     * 题目提交数
     */
    private Integer submitNum;

    /**
     * 题目通过数
     */
    private Integer acceptedNum;

    /**
     * 困难级别
     */
    private Integer level;

    /**
     * 判题配置（json 对象）
     */
    private JudgeConfig judgeConfig;

    /**
     * 判题样例展示(JSON 数组)
     */
    private List<JudgeCase> judgeCase;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 创建题目人的信息
     */
    private UserVO userVO;
    /**
     * 包装类转对象
     *
     * @param questionVO questionVO
     * @return Question
     */
    public static Question voToObj(QuestionVO questionVO) {
        if (questionVO == null) {
            return null;
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionVO, question);
        List<String> tagList = questionVO.getTags();
        if (tagList != null) {
            question.setTags(JSONUtil.toJsonStr(tagList));
        }
        JudgeConfig judgeConfig1 = questionVO.getJudgeConfig();
        if (judgeConfig1 != null) {
            question.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig1));
        }
        List<JudgeCase> judgeCase1 = questionVO.getJudgeCase();
        if (judgeCase1 != null) {
            ArrayList<JudgeCase> judgeCases = CollUtil.newArrayList(judgeCase1);
            question.setJudgeCase(JSONUtil.parseArray(judgeCases).toString());
        }
        QuestionContent content1 = questionVO.getContent();
        if (content1 != null) {
            question.setContent(JSONUtil.toJsonStr(content1));
        }
        return question;
    }

    /**
     * 对象转包装类
     *
     * @param question question
     * @return QuestionVO
     */
    public static QuestionVO objToVo(Question question) {
        if (question == null) {
            return null;
        }
        QuestionVO questionVO = new QuestionVO();
        BeanUtils.copyProperties(question, questionVO);
        List<String> tagList = JSONUtil.toList(question.getTags(), String.class);
        questionVO.setTags(tagList);

        String str = question.getJudgeCase();
        JSONArray objects = JSONUtil.parseArray(str);
        List<JudgeCase> judgeCaseList = new ArrayList<>();
        for (Object s : objects) {
            String string = s.toString();
            JudgeCase bean = JSONUtil.toBean(string, JudgeCase.class);
            judgeCaseList.add(bean);
        }
        questionVO.setJudgeCase(judgeCaseList);
        questionVO.setJudgeConfig(JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class));
        questionVO.setContent(JSONUtil.toBean(question.getContent(), QuestionContent.class));
        return questionVO;
    }

    private static final long serialVersionUID = 1L;
}
