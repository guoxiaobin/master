package com.nd.share.demo.service;

import com.nd.share.demo.domain.Feedback;

/**
 * 意见反馈
 *
 * @author 郭晓斌(121017)
 * @version created on 20160531.
 */
public interface FeekbackService {

    /**
     * 保存意见反馈
     * @param feedback
     */
    void save(Feedback feedback);
}
