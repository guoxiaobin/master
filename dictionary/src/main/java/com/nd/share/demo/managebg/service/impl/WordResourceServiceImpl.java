package com.nd.share.demo.managebg.service.impl;

import com.nd.share.demo.managebg.cs.Session;
import com.nd.share.demo.managebg.lc.LcClient;
import com.nd.share.demo.managebg.service.WordResourceService;
import com.nd.share.demo.managebg.util.ContentUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 郭晓斌(121017)
 * @version created on 20160801.
 */
@Service
public class WordResourceServiceImpl implements WordResourceService {

    @Resource
    private LcClient lcClient;
    @Resource
    private ContentUtils contentUtils;

    /**
     * 文件更新
     *
     * @param jsonUri    上传文件全路径
     * @param lcfilePath CS路径
     * @return
     */
    public Map<String, Object> reUploadFile(String jsonUri, String lcfilePath) {
        Map<String, Object> uriMap = new HashMap<>();
        String csUri = null;
        //获取session
        Session session = lcClient.getSessionFromLc("");
        File file = new File(jsonUri);

        try {
            csUri = contentUtils.reUpload(file.getName(), lcfilePath, jsonUri, session.getSessionId());
            uriMap.put("code",0);
            uriMap.put("csUri",csUri);
            uriMap.put("msg","更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            uriMap.put("code",1);
            uriMap.put("csUri",csUri);
            uriMap.put("msg","更新失败<br>失败原因:" +e.getMessage());
        }
        return uriMap;
    }
}
