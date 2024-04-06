package com.ks.fengoj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ks.fengoj.common.BaseResponse;
import com.ks.fengoj.common.ErrorCode;
import com.ks.fengoj.common.ResultUtils;
import com.ks.fengoj.exception.BusinessException;
import com.ks.fengoj.model.dto.questionSubmit.QuestionQuerySubmitRequest;
import com.ks.fengoj.model.dto.questionSubmit.QuestionSubmitAddRequest;
import com.ks.fengoj.model.entity.QuestionSubmit;
import com.ks.fengoj.model.entity.User;
import com.ks.fengoj.model.vo.QuestionSubmitVO;
import com.ks.fengoj.service.QuestionSubmitService;
import com.ks.fengoj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 */
@RestController
//@RequestMapping("/question_submit")
@Slf4j
@Deprecated
public class QuestionSubmitController {

//    @Resource
//    private QuestionSubmitService questionSubmitService;
//
//    @Resource
//    private UserService userService;
//
//    /**
//     * 题目提交
//     *
//     * @param questionSubmitAddRequest
//     * @param request
//     * @return resultNum 本次题目提交变化数
//     */
//    @PostMapping("/")
//    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
//                                         HttpServletRequest request) {
//        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        // 登录才能题目提交
//        final User loginUser = userService.getLoginUser(request);
//        Long result = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
//        return ResultUtils.success(result);
//    }
//    /**
//     * 分页获取提交列表（仅管理员和普通用户自己）
//     *
//     * @param questionQuerySubmitRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/list/page")
//    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionQuerySubmitRequest questionQuerySubmitRequest,
//                                                                          HttpServletRequest request) {
//        long current = questionQuerySubmitRequest.getCurrent();
//        long size = questionQuerySubmitRequest.getPageSize();
//        // 从数据库中查询题目提交信息
//        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
//                questionSubmitService.getQueryWrapper(questionQuerySubmitRequest));
//        // 返回脱敏后的信息
//        final User loginUser = userService.getLoginUser(request);
//        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, loginUser));
//    }
}
