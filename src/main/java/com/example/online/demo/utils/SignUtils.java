package com.example.online.demo.utils;

import java.util.*;
import java.util.Map.Entry;

public class SignUtils {

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

		for (Entry<String, String> entry : sArray.entrySet()) {
			String value = entry.getValue();
			String key = entry.getKey();
			if (value == null || isEmpty(value)
					|| key.equalsIgnoreCase("sign")) {
				continue;
			}
			result.put(key, value);
		}

		return result;
	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createLinkString(Map<String, String> params) {

		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		String prestr = "";

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);

			// 拼接时，不包括最后一个&字符
			if (i == keys.size() - 1) {
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}

		return prestr;
	}

	public static boolean isEmpty(String value) {
		int strLen;
		if (value == null) {
			return true;
		}

		strLen = value.trim().length();
		if (strLen == 0) {
			return true;
		}

		for (int i = 0; i < strLen; ++i) {
			if (!(Character.isWhitespace(value.charAt(i)))) {
				return false;
			}
		}
		return true;
	}
}
