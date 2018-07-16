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
	// �����洢cookies��Ϣ����
	private CookieStore cookieStore;

	// get����
	@Test
	public void getCookies() throws ClientProtocolException, IOException {
		// cookieStore
		this.cookieStore = new BasicCookieStore();
		// ����httpClient ����
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		// ����httpGet���󣬴���url
		HttpGet httpGet = new HttpGet("http://127.0.0.1:8080/SessionServlet001");
		// �������󣬲��ҽ�����Ӧ
		CloseableHttpResponse response = httpClient.execute(httpGet);
		// ��ȡ������Ӧͷ����
		Header[] headers = response.getAllHeaders();
		// ��ȡcookie ��Ϣ
		List<Cookie> cookies = cookieStore.getCookies();
		for (Cookie cookie : cookies) {
			System.out.println("cookie��Ϣ" + cookie);
		}
		// ��ȡ��Ӧʵ�����ݲ�����ת��
		HttpEntity entity = response.getEntity();
		String entityString = EntityUtils.toString(entity, "utf-8");
		System.out.println(entityString);
		// �ر�����
		httpClient.close();
	}

	@Test(dependsOnMethods = { "getCookies" })
	public void testGetWithCookies() throws ClientProtocolException, IOException {
		// ����httpClient �������cookie
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		// CloseableHttpClient httpClient = HttpClients.createDefault();
		// ����httpGet���󣬴���url
		HttpGet httpGet = new HttpGet("http://127.0.0.1:9999/getWithCookies");
		// ����cookie��Ϣ
		// �������󣬲��ҽ�����Ӧ
		CloseableHttpResponse response = httpClient.execute(httpGet);
		// ��ȡ��Ӧʵ�����ݲ�����ת��
		if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			String entityString = EntityUtils.toString(entity, "utf-8");
			System.out.println(entityString);
		}
		// �ر�����
		httpClient.close();
	}
}
