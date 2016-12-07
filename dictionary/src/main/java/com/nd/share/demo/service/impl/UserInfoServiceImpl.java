package com.nd.share.demo.service.impl;

import com.nd.share.demo.common.response.ErrorCode;
import com.nd.share.demo.common.response.RestfulException;
import com.nd.share.demo.constants.Messages;
import com.nd.share.demo.domain.UserInfo;
import com.nd.share.demo.repository.UserInfoRepository;
import com.nd.share.demo.service.UserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 收藏词语
 *
 * @author 郭晓斌(121017)
 * @version created on 20160601.
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private UserInfoRepository userInfoRepository;

    /**
     * 保存用户信息
     *
     * @param userInfo
     */
    @Override
    public void save(UserInfo userInfo) {
        try {
            userInfoRepository.save(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestfulException(ErrorCode.SERVICE_ERROR_SYSTEM, Messages.INTERNAL_SERVER_ERROR, String.format("UserInfoServiceImpl, msg:%s", e.getMessage()));
        }
    }
}
