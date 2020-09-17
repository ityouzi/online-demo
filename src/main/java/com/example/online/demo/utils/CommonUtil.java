package com.example.online.demo.utils;

import org.apache.commons.lang.StringUtils;

import java.util.*;

public class CommonUtil {

	/**
	 * map 转成 string 
	 * @param map
	 * @return
	 */
	public static String mapToString(Map<String, String> map) {
		SortedMap<String, String> sortedMap = new TreeMap<String, String>(map);

		StringBuilder sb = new StringBuilder();

		for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
			if (StringUtils.isBlank(entry.getValue())) {
				continue;
			}
			sb.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
		}
		sb.deleteCharAt(sb.length() - 1);

		return sb.length() == 0 ? "" : sb.toString();
	}
	
	/**
	 * 除去数组中的空值和签名参数
	 * 
	 * @param sArray
	 *            签名参数组
	 * @return 去掉空值与签名参数后的新签名参数组
	 */
	public static Map<String, String> paraFilter(Map<String, String> sArray) {

		Map<String, String> result = new HashMap<String, String>();

		if (sArray == null || sArray.size() <= 0) {
			return result;
		}

		for (String key : sArray.keySet()) {
			String value = sArray.get(key);
			if (value == null || StringUtils.isEmpty(value) || key.equalsIgnoreCase("sign")) {
				continue;
			}
			result.put(key, value);
		}

		return result;
	}
	
	/**
	 * 遍历以及根据重新排序
	 * 
	 * @param sortedParams
	 * @return
	 */
	public static String getSignContent(Map<String, String> sortedParams) {
		StringBuffer content = new StringBuffer();
		List<String> keys = new ArrayList<String>(sortedParams.keySet());
		Collections.sort(keys);
		int index = 0;
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = sortedParams.get(key);
			if (StringUtil.areNotEmpty(key, value)) {
				content.append((index == 0 ? "" : "&") + key + "=" + value);
				index++;
			}
		}
		return content.toString();
	}
}
