package com.nd.share.demo.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import javax.annotation.Resource;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.nd.gaea.rest.testconfig.MockUtil;
import com.nd.share.demo.service.WordsInfoService;

/**
 * @author jinmj
 * @date : 2016年6月20日 上午11:27:14
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring/applicationContext.xml" })
public class WordsInfoControllerTest {

	@Resource
	private WordsInfoService wordsInfoService;

	@Resource
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		// 初始化Mock
		MockitoAnnotations.initMocks(this);
		// 获取上下文容器中的controller
		WordsInfoController wordsInfoController = (WordsInfoController) wac.getBean("wordsInfoController");
		// 用mock的Service替换原有的service
		ReflectionTestUtils.setField(wordsInfoController, "wordsInfoService", wordsInfoService);
		mockMvc = MockMvcBuilders.standaloneSetup(wordsInfoController).build();
	}
	
	@Test
	public void getWordsInfo() throws Exception {
		String json = "{\"items\":[\"111\",\"222\"]}";		
		String resStr = mockMvc.perform(post("/v0.1/update/wordsinfo", "json").characterEncoding("UTF-8")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.valueOf(MockUtil.APPLICATION_JSON))
				.content(json.getBytes())).andReturn().getResponse().getContentAsString();
		JSONObject jsonobject = JSONObject.parseObject(resStr);		
		assertEquals("1", jsonobject.get("status"));		
	}

}
