package com.nd.share.demo.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import javax.annotation.Resource;

import com.nd.share.demo.service.CollectWordService;
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

import com.alibaba.fastjson.JSONObject;
import com.nd.gaea.rest.testconfig.MockUtil;
import com.nd.share.demo.service.WordDetailService;

/**
 * @author jinmj
 * @date : 2016年6月20日 上午11:27:14
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring/applicationContext.xml" })
public class WordsControllerTest {

	@Resource
	private CollectWordService collectWordService;
	@Resource
	private WordDetailService wordDetailService;

	@Resource
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		// 初始化Mock
		MockitoAnnotations.initMocks(this);
		// 获取上下文容器中的controller
		WordsController wordsController = (WordsController) wac.getBean("wordsController");
		// 用mock的Service替换原有的service
		ReflectionTestUtils.setField(wordsController, "wordDetailService", wordDetailService);
		ReflectionTestUtils.setField(wordsController, "collectWordService", collectWordService);
		mockMvc = MockMvcBuilders.standaloneSetup(wordsController).build();
	}
	
	@Test
	public void getWordsInfo() throws Exception {		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("DictionarySource", "ID=1");
		String resStr = mockMvc.perform(MockMvcRequestBuilders.get("/v0.1/words/5799891c-94cd-47fa-9b93-1f1a06ef2b95/details", "json").characterEncoding("UTF-8")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.valueOf(MockUtil.APPLICATION_JSON)).headers(httpHeaders)).andReturn().getResponse().getContentAsString();
		JSONObject jsonobject = JSONObject.parseObject(resStr);		
		assertEquals(true, jsonobject.containsKey("tags"));		
	}

	@Test
	public void getWorkCollects() throws Exception{
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("DictionarySource", "ID=1");
		httpHeaders.add("userId", "121017");
		String json = "{}";
		String resStr = mockMvc.perform(post("/v0.1/words/collections?timestamp=0", "json").characterEncoding("UTF-8")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.valueOf(MockUtil.APPLICATION_JSON)).headers(httpHeaders)
				.content(json.getBytes())).andReturn().getResponse().getContentAsString();
		JSONObject jsonobject = JSONObject.parseObject(resStr);
		assertEquals(true, jsonobject.containsKey("items"));
	}

	@Test
	public void wordExist() throws Exception{
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("DictionarySource", "ID=1");
		httpHeaders.add("userId", "121017");
		String json = "{}";
		String resStr = mockMvc.perform(post("/v0.1/words/000123/is_exist", "json").characterEncoding("UTF-8")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.valueOf(MockUtil.APPLICATION_JSON)).headers(httpHeaders)
				.content(json.getBytes())).andReturn().getResponse().getContentAsString();
		JSONObject jsonobject = JSONObject.parseObject(resStr);
		assertEquals(0, jsonobject.get("is_exist"));
	}

	@Test
	public void cancelCollection() throws Exception{
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("DictionarySource", "ID=1");
		httpHeaders.add("userId", "121017");
		String json = "{}";
		String resStr = mockMvc.perform(delete("/v0.1/words/cancel/collection/000123", "json").characterEncoding("UTF-8")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.valueOf(MockUtil.APPLICATION_JSON)).headers(httpHeaders)).andReturn().getResponse().getContentAsString();
		JSONObject jsonobject = JSONObject.parseObject(resStr);
		int num = (int)jsonobject.get("status");
		assertEquals(true, (num != 0 && num != 1));
	}
}
