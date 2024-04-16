package com.ks.fengoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.ks.fengoj.common.ErrorCode;
import com.ks.fengoj.constant.CommonConstant;
import com.ks.fengoj.exception.BusinessException;
import com.ks.fengoj.judge.JudgeService;
import com.ks.fengoj.judge.codeSanbox.model.JudgeInfo;
import com.ks.fengoj.mapper.QuestionSubmitMapper;
import com.ks.fengoj.model.dto.questionSubmit.QuestionQuerySubmitRequest;
import com.ks.fengoj.model.dto.questionSubmit.QuestionSubmitAddRequest;
import com.ks.fengoj.model.entity.Question;
import com.ks.fengoj.model.entity.QuestionSubmit;
import com.ks.fengoj.model.entity.User;
import com.ks.fengoj.model.enums.JudgeInfoMessageEnum;
import com.ks.fengoj.model.enums.QuestionSubmitLanguageEnum;
import com.ks.fengoj.model.enums.QuestionSubmitStatusEnum;
import com.ks.fengoj.model.vo.QuestionSubmitVO;
import com.ks.fengoj.model.vo.UserVO;
import com.ks.fengoj.rabbitMQ.MyMessageProducer;
import com.ks.fengoj.service.QuestionService;
import com.ks.fengoj.service.QuestionSubmitService;
import com.ks.fengoj.service.UserService;
import com.ks.fengoj.utils.SqlUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author 28356
 * @description 针对表【question_submit(题目提交)】的数据库操作Service实现
 * @createDate 2023-09-10 10:30:33
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {
    private final static Gson GSON = new Gson();
    @Resource
    private QuestionService questionService;
    @Resource
    private UserService userService;
    @Resource
    @Lazy
    private JudgeService judgeService;
    @Resource
    private QuestionSubmitMapper questionSubmitMapper;
    @Resource
    private MyMessageProducer myMessageProducer;


    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest 添加提交代码请求
     * @param loginUser                登录用户
     * @return questionSubmitId
     */
    @Override
    public Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        // 校验编程语言是否合法
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }

        // 判断代码是否为空
        String code = questionSubmitAddRequest.getCode();
        if (StringUtils.isBlank(code)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "代码为空");
        }

        // 判断实体是否存在，根据类别获取实体
        Long questionId = questionSubmitAddRequest.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        Long loginUserId = loginUser.getId();
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setLanguage(language);
        questionSubmit.setCode(code);
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo(GSON.toJson(judgeInfo));

        // todo 事务，status
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITTING.getValue());
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setUserId(loginUserId);
        boolean result = this.save(questionSubmit);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }
        Long questionSubmitId = questionSubmit.getId();
        Integer submitNum = question.getSubmitNum();
        Question updateQuestion = new Question();
        updateQuestion.setSubmitNum(submitNum + 1);
        updateQuestion.setId(questionId);
        boolean isUpdate = questionService.updateById(updateQuestion);
        if (!isUpdate) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "提交数更新失败");
        }

        // 改为 RabbitMQ
        myMessageProducer.sendMessage("code_exchange", "my_routingKey", String.valueOf(questionSubmitId));
        // CompletableFuture.runAsync(() -> {
        //     judgeService.doJudge(questionSubmitId);
        // });

        return questionSubmitId;
    }

    /**
     * 获取查询包装类
     *
     * @param questionQuerySubmitRequest questionQuerySubmitRequest
     * @return QueryWrapper<QuestionSubmit>
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionQuerySubmitRequest questionQuerySubmitRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionQuerySubmitRequest == null) {
            return queryWrapper;
        }
        String language = questionQuerySubmitRequest.getLanguage();
        Integer status = questionQuerySubmitRequest.getStatus();
        Long questionId = questionQuerySubmitRequest.getQuestionId();
        Long userId = questionQuerySubmitRequest.getUserId();
        String sortField = questionQuerySubmitRequest.getSortField();
        String sortOrder = questionQuerySubmitRequest.getSortOrder();


        // 拼接查询条件
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        // 脱敏：仅本人和管理员能看见自己（提交 userId 和登录用户 id 不同）提交的代码
        long userId = loginUser.getId();

        // 未登录用户和登录用户看不见别人的代码
        User user = userService.getById(questionSubmit.getUserId());
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        questionSubmitVO.setUserVO(userVO);

        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        questionSubmitVO.setTitle(question.getTitle());

        // 处理脱敏
        if (userId != questionSubmit.getUserId() && !userService.isAdmin(loginUser)) {
            questionSubmitVO.setCode(null);
        }
        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        List<QuestionSubmitVO> collect = questionSubmitList.stream()
                .map(questionSubmit -> getQuestionSubmitVO(questionSubmit, loginUser))
                .collect(Collectors.toList());
        questionSubmitVOPage.setRecords(collect);
        return questionSubmitVOPage;
    }

    @Override
    public Boolean deleteSubmitsByQuestionId(Long questionId) {
        QueryWrapper<QuestionSubmit> questionSubmitQueryWrapper = new QueryWrapper<>();
        questionSubmitQueryWrapper.eq("questionId", questionId);
        int delete = questionSubmitMapper.delete(questionSubmitQueryWrapper);
        return delete >= 0;
    }

}




