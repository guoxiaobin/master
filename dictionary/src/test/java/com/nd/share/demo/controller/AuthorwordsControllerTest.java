package com.nd.share.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.nd.gaea.rest.testconfig.MockUtil;
import com.nd.share.demo.service.AppendixService;
import com.nd.share.demo.service.AuthorwordsService;
import com.nd.share.demo.util.SpringUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
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

import static org.junit.Assert.assertEquals;

/**
 * @author guoxiaobin
 * @date : 2016年6月21日
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/applicationContext.xml"})
public class AuthorwordsControllerTest {

    @Resource
    private AuthorwordsService authorwordsService;
    @Resource
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        // 初始化Mock
        MockitoAnnotations.initMocks(this);
        // 获取上下文容器中的controller
        AuthorwordsController authorwordsController = (AuthorwordsController) wac.getBean("authorwordsController");
        // 用mock的Service替换原有的service
        ReflectionTestUtils.setField(authorwordsController, "authorwordsService", authorwordsService);
        mockMvc = MockMvcBuilders.standaloneSetup(authorwordsController).build();
    }

    @Test
    public void getAuthorwords() throws Exception {
        JSONObject testJson = JSONObject.parseObject("{\"items\":[{\"title\":\"1：编者的话\",\"descirption\":\"1：《 文言文学习字典" +
                " 》 是以中学生为主要读\"},{\"title\":\"2：编者的话\",\"descirption\":\"2：《 文言文学习字典 》 是以中学生为主要读\"}]}");
        String resStr = mockMvc.perform(MockMvcRequestBuilders.get("/v0.1/basicdata/authorwords", "json")
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.valueOf(MockUtil.APPLICATION_JSON))).andReturn().getResponse().getContentAsString();
        JSONObject jsonobject = JSONObject.parseObject(resStr);
        assertEquals(true, jsonobject.containsKey("author"));
    }
}
