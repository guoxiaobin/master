package com.nd.share.demo.service;

import com.nd.share.demo.domain.UserInfo;

/**
 * 意见反馈
 *
 * @author 郭晓斌(121017)
 * @version created on 20160601.
 */
public interface UserInfoService {

    /**
     * 保存用户信息
     *
     * @param userInfo
     */
    void save(UserInfo userInfo);
}
