package com.ks.fengoj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ks.fengoj.model.dto.questionSubmit.QuestionQuerySubmitRequest;
import com.ks.fengoj.model.dto.questionSubmit.QuestionSubmitAddRequest;
import com.ks.fengoj.model.entity.QuestionSubmit;
import com.ks.fengoj.model.entity.User;
import com.ks.fengoj.model.vo.QuestionSubmitVO;

/**
* @author 28356
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2023-09-10 10:30:33
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {
    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest questionSubmitAddRequest
     * @param loginUser 登录用户
     * @return 提交题目 ID
     */
    Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);
    /**
     * 获取查询条件
     *
     * @param questionQuerySubmitRequest questionQuerySubmitRequest
     * @return QueryWrapper<QuestionSubmit>
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionQuerySubmitRequest questionQuerySubmitRequest);

    /**
     * 获取题目封装
     *
     * @param questionSubmit questionSubmit
     * @param loginUser loginUser
     * @return QuestionSubmitVO
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目封装
     *
     * @param questionSubmitPage questionSubmitPage
     * @param loginUser loginUser
     * @return Page<QuestionSubmitVO>
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);

    /**
     * 根据 questionId 删除所有关联提交
     * @param questionId questionId
     * @return Boolean
     */
    Boolean deleteSubmitsByQuestionId(Long questionId);
}
