package com.bairuitech.blackboard.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

public class HttpUtil {
	public static final String URL = "http://120.24.76.197/asksvr/interface.php";
	public static List<Map<String, Object>> getList(String uriAPI,
			Map<String, String> p) throws ClientProtocolException, IOException,
			JSONException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// String uriAPI = URLEnum.login.getURL(); // 声明网址字符串
		HttpPost httpRequest = new HttpPost(uriAPI); // 建立HTTP POST联机
		List<NameValuePair> params = new ArrayList<NameValuePair>(); // Post运作传送变量必须用NameValuePair[]数组储存
		for (Map.Entry<String, String> e : p.entrySet()) {
			params.add(new BasicNameValuePair(e.getKey(), e.getValue()));
		}
		httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); // 发出http请求
		HttpResponse httpResponse = new DefaultHttpClient()
				.execute(httpRequest); // 取得http响应
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			String strResult = EntityUtils.toString(httpResponse.getEntity());
			list = JsonUtil.str2JsonList(strResult);

		}
		return list;
	}
	public static String getString(String uriAPI,
			Map<String, String> p) throws ClientProtocolException, IOException,
			JSONException {
		// String uriAPI = URLEnum.login.getURL(); // 声明网址字符串
		HttpPost httpRequest = new HttpPost(uriAPI); // 建立HTTP POST联机
		List<NameValuePair> params = new ArrayList<NameValuePair>(); // Post运作传送变量必须用NameValuePair[]数组储存
		for (Map.Entry<String, String> e : p.entrySet()) {
			params.add(new BasicNameValuePair(e.getKey(), e.getValue()));
		}
		httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); // 发出http请求
		HttpResponse httpResponse = new DefaultHttpClient()
				.execute(httpRequest); // 取得http响应
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			return  EntityUtils.toString(httpResponse.getEntity());
			

		}
		return null;
	}
	
}
