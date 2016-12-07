package com.nd.share.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.nd.share.demo.constants.Constants;
import com.nd.share.demo.domain.Feedback;
import com.nd.share.demo.service.FeekbackService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author 郭晓斌(121017)
 * @version created on 20160531.
 */
@RestController
@RequestMapping(value = Constants.VERSION_CURRENT)
public class FeekbackController {

    @Resource
    private FeekbackService feekbackService;

    /**
     * 意见反馈
     *
     * @param type 类型（1 意见反馈；2是纠错）
     * @param feedback  反馈信息
     * @return
     */
    @RequestMapping(value = "/feedback", method = RequestMethod.POST)
    private JSONObject saveFeedback(@RequestParam(value = "type", required = true) int type,
                                    @RequestBody Feedback feedback) {
        JSONObject jsonObject = new JSONObject();
        if(feedback != null){
            feedback.setType(type);
            feedback.setCreateTime(new Date());
            feekbackService.save(feedback);
        }

        // 响应体
        jsonObject.put("message","成功");
        jsonObject.put("code","DICT/SUCCESS");

        return jsonObject;
    }
}
