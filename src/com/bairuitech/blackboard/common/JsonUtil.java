package com.bairuitech.blackboard.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {
	public static Map<String, Object> str2Json(String info) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (info != null) {
			try {
				JSONObject json = new JSONObject(info);
				@SuppressWarnings("unchecked")
				Iterator<String> it = json.keys();
				while (it.hasNext()) {
					String key = it.next();
					if(json.isNull(key)){
						map.put(key, null);
					}else{
						map.put(key, json.get(key));
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	public static Object str2Object(String info)  {
		if (info.startsWith("[")) {
			return str2JsonList(info);
		} else {
			return str2Json(info);
		}

	}

	public static String Json2Str(Map<String, Object> info) {
		String result = "";
		if (info != null) {
			try {
				JSONObject json = new JSONObject();
				for (Map.Entry<String, Object> e : info.entrySet()) {
					String key = e.getKey();
					if(json.isNull(key)){
						json.put(key, null);
					}else{
						json.put(key, json.get(key));
					}
				}
				result = json.toString();
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return result;
	}

	public static List<Map<String, Object>> str2JsonList(String info) {

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try {
			JSONArray json = new JSONArray(info);
			for (int i = 0; i < json.length(); i++) {

				Map<String, Object> map = new HashMap<String, Object>();

				JSONObject jsonObj = json.getJSONObject(i);
				@SuppressWarnings("unchecked")
				Iterator<String> it = jsonObj.keys();
				while (it.hasNext()) {
					String key = it.next();
					map.put(key, jsonObj.get(key));
				}
				result.add(map);
			}
		} catch (Exception e) {

		}
		return result;
	}

}
