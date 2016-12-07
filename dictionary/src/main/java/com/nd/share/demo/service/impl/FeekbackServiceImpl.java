package com.nd.share.demo.service.impl;

import com.nd.share.demo.common.response.ErrorCode;
import com.nd.share.demo.common.response.RestfulException;
import com.nd.share.demo.constants.Messages;
import com.nd.share.demo.domain.Feedback;
import com.nd.share.demo.repository.FeedbackRepository;
import com.nd.share.demo.service.FeekbackService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 反馈列表
 * @author 郭晓斌(121017)
 * @version created on 20160601.
 */
@Service
public class FeekbackServiceImpl implements FeekbackService {

    @Resource
    private FeedbackRepository feedbackRepository;

    /**
     * 保存意见反馈
     *
     * @param feedback
     */
    @Override
    public void save(Feedback feedback) {
        try {
            // 保存意见
            feedbackRepository.save(feedback);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestfulException(ErrorCode.SERVICE_ERROR_SYSTEM, Messages.INTERNAL_SERVER_ERROR, String.format("FeekbackServiceImpl, msg:%s", e.getMessage()));
        }
    }
}
