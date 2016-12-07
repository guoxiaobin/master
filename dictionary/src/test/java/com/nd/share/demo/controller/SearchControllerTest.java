package com.nd.share.demo.controller;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nd.gaea.rest.testconfig.MockUtil;

/**
 * @author jinmj
 * @date : 2016年6月20日 上午11:27:14
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring/applicationContext.xml" })
public class SearchControllerTest {

//	@Resource
//	private SearchService searchService;

	@Resource
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		// 初始化Mock
		MockitoAnnotations.initMocks(this);
		// 获取上下文容器中的controller
		SearchController searchController = (SearchController) wac.getBean("searchController");
		// 用mock的Service替换原有的service
//		ReflectionTestUtils.setField(searchController, "appService", appService);
		mockMvc = MockMvcBuilders.standaloneSetup(searchController).build();
	}
	
	@Test
	public void getSearch() throws Exception {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("DictionarySource", "ID=1");
		String resStr = mockMvc.perform(MockMvcRequestBuilders.get("/v0.1/search?words=为", "json").characterEncoding("UTF-8")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.valueOf(MockUtil.APPLICATION_JSON)).headers(httpHeaders)).andReturn().getResponse().getContentAsString();
		JSONObject jsonobject = JSONObject.parseObject(resStr);		
		JSONArray jsonarray = JSONArray.parseArray(jsonobject.get("items").toString()); 
		JSONObject obj = JSONObject.parseObject(jsonarray.get(0).toString());	
		assertEquals("为", obj.get("title"));		
	}

}
