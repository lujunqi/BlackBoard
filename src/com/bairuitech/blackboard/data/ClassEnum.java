package com.bairuitech.blackboard.data;

import java.util.HashMap;
import java.util.Map;

public class ClassEnum {
	public static Map<String, String> json = new HashMap<String, String>();
	public static Map<String, Integer> classKey = new HashMap<String, Integer>();
	public static Map<String, Integer> subjectKey = new HashMap<String, Integer>();

	static {
		// 小学
		String json1 = "[{\"title\":\"语文\",\"icon\":\"yuwen\"},"
				+ "{\"title\":\"数学\",\"icon\":\"math\"},"
				+ "{\"title\":\"英语\",\"icon\":\"english\"},"
				+ "{\"title\":\"科学\",\"icon\":\"kexue\"},"
				+ "{\"title\":\"吐心事\",\"icon\":\"xinshi\"}]";
		// 初中
		String json2 = "[{\"title\":\"语文\",\"icon\":\"yuwen\"},"
				+ "{\"title\":\"数学\",\"icon\":\"math\"},"
				+ "{\"title\":\"英语\",\"icon\":\"english\"},"
				+ "{\"title\":\"科学\",\"icon\":\"kexue\"},"
				+ "{\"title\":\"历史\",\"icon\":\"lishi\"},"
				+ "{\"title\":\"化学\",\"icon\":\"huaxue\"},"
				+ "{\"title\":\"地理\",\"icon\":\"dili\"},"
				+ "{\"title\":\"生物\",\"icon\":\"shengwu\"},"
				+ "{\"title\":\"物理\",\"icon\":\"wuli\"},"
				+ "{\"title\":\"吐心事\",\"icon\":\"xinshi\"}]";
		// 高中
		String json3 = "[{\"title\":\"语文\",\"icon\":\"yuwen\"},"
				+ "{\"title\":\"数学\",\"icon\":\"math\"},"
				+ "{\"title\":\"英语\",\"icon\":\"english\"},"
				+ "{\"title\":\"科学\",\"icon\":\"kexue\"},"
				+ "{\"title\":\"历史\",\"icon\":\"lishi\"},"
				+ "{\"title\":\"政治\",\"icon\":\"zhengzhi\"},"
				+ "{\"title\":\"化学\",\"icon\":\"huaxue\"},"
				+ "{\"title\":\"地理\",\"icon\":\"dili\"},"
				+ "{\"title\":\"生物\",\"icon\":\"shengwu\"},"
				+ "{\"title\":\"物理\",\"icon\":\"wuli\"},"
				+ "{\"title\":\"吐心事\",\"icon\":\"xinshi\"}]";

		json.put("小学四年级", json1);
		json.put("小学五年级", json1);
		json.put("小学六年级", json1);

		json.put("七年级", json2);
		json.put("八年级", json2);
		json.put("九年级", json2);

		json.put("高中一年级", json3);
		json.put("高中二年级", json3);
		json.put("高中三年级", json3);

		classKey.put("小学一年级", 1);
		classKey.put("小学四年级", 4);
		classKey.put("小学五年级", 5);
		classKey.put("小学六年级", 6);

		classKey.put("七年级", 7);
		classKey.put("八年级", 8);
		classKey.put("九年级", 9);

		classKey.put("高中一年级", 10);
		classKey.put("高中二年级", 11);
		classKey.put("高中三年级", 12);

		subjectKey.put("语文", 1);
		subjectKey.put("数学", 2);
		subjectKey.put("英语", 3);
		subjectKey.put("科学", 4);
		subjectKey.put("吐心事", 5);
		subjectKey.put("历史", 6);
		subjectKey.put("化学", 7);
		subjectKey.put("地理", 8);
		subjectKey.put("生物", 9);
		subjectKey.put("物理", 10);
		subjectKey.put("政治", 11);

	}

	public static String getClassName(String key) {
		for (Map.Entry<String, Integer> e : classKey.entrySet()) {
			if (key.equals("" + e.getValue())) {
				return e.getKey();
			}
		}
		return "";
	}

	public static String getSubjectName(String key) {
		for (Map.Entry<String, Integer> e : subjectKey.entrySet()) {
			if (key.equals("" + e.getValue())) {
				return e.getKey();
			}
		}
		return "";
	}

}
