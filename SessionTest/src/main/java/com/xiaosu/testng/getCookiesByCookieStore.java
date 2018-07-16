package com.xiaosu.testng;

import java.io.IOException;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

public class getCookiesByCookieStore {
	// 用来存储cookies信息变量
	private CookieStore cookieStore;

	// get请求
	@Test
	public void getCookies() throws ClientProtocolException, IOException {
		// cookieStore
		this.cookieStore = new BasicCookieStore();
		// 创建httpClient 对象
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		// 创建httpGet对象，传入url
		HttpGet httpGet = new HttpGet("http://127.0.0.1:8080/SessionServlet001");
		// 发送请求，并且接收响应
		CloseableHttpResponse response = httpClient.execute(httpGet);
		// 提取返回响应头内容
		Header[] headers = response.getAllHeaders();
		// 获取cookie 信息
		List<Cookie> cookies = cookieStore.getCookies();
		for (Cookie cookie : cookies) {
			System.out.println("cookie信息" + cookie);
		}
		// 获取响应实体内容并编码转换
		HttpEntity entity = response.getEntity();
		String entityString = EntityUtils.toString(entity, "utf-8");
		System.out.println(entityString);
		// 关闭连接
		httpClient.close();
	}

	@Test(dependsOnMethods = { "getCookies" })
	public void testGetWithCookies() throws ClientProtocolException, IOException {
		// 创建httpClient 对象包含cookie
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		// CloseableHttpClient httpClient = HttpClients.createDefault();
		// 创建httpGet对象，传入url
		HttpGet httpGet = new HttpGet("http://127.0.0.1:9999/getWithCookies");
		// 设置cookie信息
		// 发送请求，并且接收响应
		CloseableHttpResponse response = httpClient.execute(httpGet);
		// 获取响应实体内容并编码转换
		if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			String entityString = EntityUtils.toString(entity, "utf-8");
			System.out.println(entityString);
		}
		// 关闭连接
		httpClient.close();
	}
}
