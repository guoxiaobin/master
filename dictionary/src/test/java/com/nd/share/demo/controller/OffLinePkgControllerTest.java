package com.nd.share.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.nd.gaea.rest.testconfig.MockUtil;
import com.nd.share.demo.service.OfflinePkgService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * @author guoxiaobin
 * @date : 2016年8月30日
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/applicationContext.xml"})
public class OffLinePkgControllerTest {

    @Resource
    private OfflinePkgService offlinePkgService;
    @Resource
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        // 初始化Mock
        MockitoAnnotations.initMocks(this);
        // 获取上下文容器中的controller
        OffLinePkgController offLinePkgController = (OffLinePkgController) wac.getBean("offLinePkgController");
        // 用mock的Service替换原有的service
        ReflectionTestUtils.setField(offLinePkgController, "offlinePkgService", offlinePkgService);
        mockMvc = MockMvcBuilders.standaloneSetup(offLinePkgController).build();
    }

    @Test
    public void getUpdListInfoFull() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("DictionarySource", "ID=1");
        String json = "{}";
        String resStr = mockMvc.perform(post("/v0.1/offlinepkg/updlist_full", "json").characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.valueOf(MockUtil.APPLICATION_JSON)).headers(httpHeaders)
                .content(json.getBytes())).andReturn().getResponse().getContentAsString();
        Map<String,Object> map = JSONObject.parseObject(resStr,Map.class);
        assertEquals(true, map.containsKey("items"));
    }

    @Test
    public void getOfflinePkgsAdding() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("DictionarySource", "ID=1");
        String resStr = mockMvc.perform(MockMvcRequestBuilders.get("/v0.1/offlinepkg/urllist?typeId=base&version=5", "json")
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.valueOf(MockUtil.APPLICATION_JSON)).headers(httpHeaders)).andReturn().getResponse().getContentAsString();
        Map<String, Object> map = JSONObject.parseObject(resStr,Map.class);
        assertEquals(true, map.containsKey("items"));
    }

    @Test
    public void getUpdListInfo() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("DictionarySource", "ID=1");
        String json = "{}";
        String resStr = mockMvc.perform(post("/v0.1/offlinepkg/updlistinfo", "json").characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.valueOf(MockUtil.APPLICATION_JSON)).headers(httpHeaders)
                .content(json.getBytes())).andReturn().getResponse().getContentAsString();
        Map<String,Object> map = JSONObject.parseObject(resStr,Map.class);
        assertEquals(true, map.containsKey("items"));
    }
}
