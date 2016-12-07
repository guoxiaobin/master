package com.nd.share.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.nd.gaea.rest.testconfig.MockUtil;
import com.nd.share.demo.service.QuestionsService;
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

import static org.junit.Assert.assertEquals;

/**
 * @author guoxiaobin
 * @date : 2016年8月30日
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/applicationContext.xml"})
public class QuestionsControllerTest {
    @Resource
    private QuestionsService questionsService;
    @Resource
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        // 初始化Mock
        MockitoAnnotations.initMocks(this);
        // 获取上下文容器中的controller
        QuestionsController questionsController = (QuestionsController) wac.getBean("questionsController");
        // 用mock的Service替换原有的service
        ReflectionTestUtils.setField(questionsController, "questionsService", questionsService);
        mockMvc = MockMvcBuilders.standaloneSetup(questionsController).build();
    }

    @Test
    public void getSingleQuestions() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("DictionarySource", "ID=1");
        String resStr = mockMvc.perform(MockMvcRequestBuilders.get("/v0.1/question?type=choice&category=sy", "json")
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.valueOf(MockUtil.APPLICATION_JSON)).headers(httpHeaders)).andReturn().getResponse().getContentAsString();
        JSONObject jsonobject = JSONObject.parseObject(resStr);
        assertEquals(true, jsonobject.containsKey("items"));
    }
}
