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

public class getCookiesByCookieStore2 {
	// �����洢cookies��Ϣ����
	// private CookieStore cookieStore;
	// get����
	@Test(threadPoolSize = 3, invocationCount = 3)
	public void getCookies() throws ClientProtocolException, IOException {
		// cookieStore
		CookieStore cookieStore = new BasicCookieStore();
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
			System.out.println("cookie��Ϣ" + cookie.getName() + "=" + cookie.getValue());
		}
		// ��ȡ��Ӧʵ�����ݲ�����ת��
		HttpEntity entity = response.getEntity();
		String entityString = EntityUtils.toString(entity, "utf-8");
		System.out.println("����������Ӧ����" + entityString);
		// �ر�����
		httpClient.close();
	}
}
