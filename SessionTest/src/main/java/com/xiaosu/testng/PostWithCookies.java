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
		// �����ͻ���
		this.cookieStore = new BasicCookieStore();
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		// ����get����
		String uri = "http://127.0.0.1:9999/getCookies";
		HttpGet httpGet = new HttpGet(uri);
		// ִ������
		CloseableHttpResponse response = httpClient.execute(httpGet);
		// ��ȡcookie��Ϣ
		List<Cookie> cookies = cookieStore.getCookies();
		for (Cookie cookie : cookies) {
			System.out.println("cookie ��Ϣ��" + cookie);
		}
		// ��ȡ��Ӧ���
		HttpEntity entity = response.getEntity();
		String responseContent = EntityUtils.toString(entity, "utf-8");
		System.out.println("��Ӧ�����" + responseContent);
		httpClient.close();
	}

	@Test(dependsOnMethods = { "getCookies" })
	public void postWithCookies() throws ClientProtocolException, IOException {
		// �����ͻ���
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		// ����post����
		String uri = "http://127.0.0.1:9999/postWithCookies";
		HttpPost httpPost = new HttpPost(uri);
		// �������ͷ��Ϣ
		httpPost.setHeader("content-type", "application/json");
		// ��Ӳ���
		String jsonString = "{\"name\":\"xixi\",\"age\":\"20\"}";
		StringEntity stringEntity = new StringEntity(jsonString, "utf-8");
		httpPost.setEntity(stringEntity);
		// ��������
		CloseableHttpResponse response = httpClient.execute(httpPost);
		// �����ȡ,
		HttpEntity responseEntity = response.getEntity();
		String result = EntityUtils.toString(responseEntity);
		System.out.println("��Ӧ���:" + result);
		// json����ж�ת��
		JSONObject resultJson = JSONObject.parseObject(result);
		String resultOfResponse = resultJson.getString("result");
		String statusOfResponse = resultJson.getString("status");
		Assert.assertEquals(resultOfResponse, "success");
		Assert.assertEquals(statusOfResponse, "1");
		// �ر�����
		httpClient.close();
	}
}
