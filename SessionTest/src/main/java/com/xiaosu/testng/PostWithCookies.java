package com.xiaosu.testng;

import java.io.IOException;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSONObject;

public class PostWithCookies {
	private CookieStore cookieStore;

	@Test
	public void getCookies() throws ClientProtocolException, IOException {
		// 创建客户端
		this.cookieStore = new BasicCookieStore();
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		// 创建get请求
		String uri = "http://127.0.0.1:9999/getCookies";
		HttpGet httpGet = new HttpGet(uri);
		// 执行请求
		CloseableHttpResponse response = httpClient.execute(httpGet);
		// 获取cookie信息
		List<Cookie> cookies = cookieStore.getCookies();
		for (Cookie cookie : cookies) {
			System.out.println("cookie 信息：" + cookie);
		}
		// 获取响应结果
		HttpEntity entity = response.getEntity();
		String responseContent = EntityUtils.toString(entity, "utf-8");
		System.out.println("响应结果：" + responseContent);
		httpClient.close();
	}

	@Test(dependsOnMethods = { "getCookies" })
	public void postWithCookies() throws ClientProtocolException, IOException {
		// 创建客户端
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		// 创建post请求
		String uri = "http://127.0.0.1:9999/postWithCookies";
		HttpPost httpPost = new HttpPost(uri);
		// 添加请求头信息
		httpPost.setHeader("content-type", "application/json");
		// 添加参数
		String jsonString = "{\"name\":\"xixi\",\"age\":\"20\"}";
		StringEntity stringEntity = new StringEntity(jsonString, "utf-8");
		httpPost.setEntity(stringEntity);
		// 发送请求
		CloseableHttpResponse response = httpClient.execute(httpPost);
		// 结果获取,
		HttpEntity responseEntity = response.getEntity();
		String result = EntityUtils.toString(responseEntity);
		System.out.println("响应结果:" + result);
		// json结果判断转换
		JSONObject resultJson = JSONObject.parseObject(result);
		String resultOfResponse = resultJson.getString("result");
		String statusOfResponse = resultJson.getString("status");
		Assert.assertEquals(resultOfResponse, "success");
		Assert.assertEquals(statusOfResponse, "1");
		// 关闭连接
		httpClient.close();
	}
}
