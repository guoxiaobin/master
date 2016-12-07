/**
 * 
 */
package com.nd.share.demo.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nd.share.demo.service.IndexService;
import com.nd.share.demo.service.WordsInfoService;
import com.nd.share.demo.util.StringUtil;

/**
 * 
 * @author 黄梦飞(920225)
 * @version created on 2016年6月28日下午3:50:14.
 */
@Component
public class IndexServiceImpl implements IndexService {
    public enum IndexType{ 
        //枚举索引类型 PINYIN 拼音;BIHUA 笔画;XUCI 虚词;FULU 附录;OTHER 其他
        PINYIN("$RA0505"),BIHUA("$RA0507"),XUCI("$RA0506"),FULU("$RA0508"),OTHER("$RA0510");
        
        private final String value;
        
        IndexType(String value){
            this.value = value;
        }
        
        public String getValue(){
            return value;
        }
    }
    
    @Resource
    private WordsInfoService wordsInfoService;

    @Value(value = "${ref.path}")
    private String refPath;


    @Override
    public JSONArray getIndex(IndexType type) {
        List<String> indexUrlList = wordsInfoService.getIndexSrc(type.getValue(), 0, 50);
        if(indexUrlList.size() == 0){
            return null;
        }
        String indexUrl = indexUrlList.get(0);
        String src = indexUrl.replace("${ref_path}/",refPath);
        
        //开始对可能出现的中文文件名进行urlencode转义
        /*int splitIndex = src.lastIndexOf('/');
        String srcfront = src.substring(0, splitIndex+1);
        String srcbefore = src.substring(splitIndex+1);
        
        String srcback = null;
        try {
            srcback = URLEncoder.encode(srcbefore,"utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        String content = StringUtil.getContent(src);
        JSONArray res = (JSONArray)JSONArray.parse(content);
        return res;
    }

}
