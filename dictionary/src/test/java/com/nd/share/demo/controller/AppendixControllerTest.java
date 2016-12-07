package com.nd.share.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.util.JSON;
import com.nd.gaea.rest.testconfig.MockUtil;
import com.nd.share.demo.service.AppendixService;
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
public class AppendixControllerTest {

    @Resource
    private AppendixService appendixService;
    @Resource
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        // 初始化Mock
        MockitoAnnotations.initMocks(this);
        // 获取上下文容器中的controller
        AppendixController appendixController = (AppendixController) wac.getBean("appendixController");
        // 用mock的Service替换原有的service
        ReflectionTestUtils.setField(appendixController, "appendixService", appendixService);
        mockMvc = MockMvcBuilders.standaloneSetup(appendixController).build();
    }

    @Test
    public void getAppendix() throws Exception {
        JSONObject testJson = JSONObject.parseObject("{\"appendixs\":[{\"title\":\"附录一 阅读古书必备的文言语法常识\"," +
                "\"items\":[{\"title\":\"一   文言文中的宾语前置句\"," +
                "\"descirption\":\"在文言文里，名词可以直接放在动词前作状语。做状语的名词所表示的意义主要有以下\"},{\"title\":\"二   文言文中的被动句\"," +
                "\"descirption\":\"在文言文里，名词可以直接放在动词前作状语。做状语的名词所表示的意义主要有以下\"}]},{\"title\":\"附录二 文言文常见通假字例释\"," +
                "\"items\":[{\"title\":\"文言文常见通假字例释\"," +
                "\"descirption\":\"文言文中常见同音或近音字的通用和假借，这是阅读古书在文字上的一个障碍。熟悉文言文中常见的通假字，避免望文生义，有助于提高文言文阅读水平。为此，我们整理了中学文言文学习中常见的一百多组通假字（包括古今字），供读者参考。各组按通假字的汉语拼音顺序排列，先列通假字，后用破折号引出本字（如“蚤—早”表示“蚤”是通假字,“早”是本字），然后再释义并举例。\"},{\"title\":\"罢—疲 疲劳，困乏。\",\"descirption\":\"《史记·项羽本纪》（巨鹿之战）：今秦攻赵，战胜则兵罢，我承其敝。\"}]}],\"illustrate\":{\"title\":\"凡例\",\"description\":\"在文言文里，名词可以直接放在动词前作\"}}");
        String resStr = mockMvc.perform(MockMvcRequestBuilders.get("/v0.1/basicdata/appendix", "json")
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.valueOf(MockUtil.APPLICATION_JSON))).andReturn().getResponse().getContentAsString();
        JSONObject jsonobject = JSONObject.parseObject(resStr);        
        assertEquals(true, jsonobject.containsKey("fl1"));
    }

}
