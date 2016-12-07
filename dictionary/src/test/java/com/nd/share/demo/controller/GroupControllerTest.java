package com.nd.share.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.nd.gaea.rest.testconfig.MockUtil;
import com.nd.share.demo.service.AppendixService;
import com.nd.share.demo.service.GroupService;
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
 * @date : 2016年6月20日
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/applicationContext.xml"})
public class GroupControllerTest {

    @Resource
    private GroupService groupService;
    @Resource
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        // 初始化Mock
        MockitoAnnotations.initMocks(this);
        // 获取上下文容器中的controller
        GroupController groupController = (GroupController) wac.getBean("groupController");
        // 用mock的Service替换原有的service
        ReflectionTestUtils.setField(groupController, "groupService", groupService);
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
    }

    @Test
    public void getGroup() throws Exception {
        JSONObject testJson = JSONObject.parseObject("{\"items\":[{\"content\":\"远古人死后不用官村，仅将..\"}," +
                "{\"content\":\"远古人死后不用官村，仅将..\"}]}");
        String resStr = mockMvc.perform(MockMvcRequestBuilders.get("/v0.1/basicdata/group/knowledge", "json")
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.valueOf(MockUtil.APPLICATION_JSON))).andReturn().getResponse().getContentAsString();
        JSONObject jsonobject = JSONObject.parseObject(resStr);
        assertEquals(true, jsonobject.containsKey("items"));
    }
}
