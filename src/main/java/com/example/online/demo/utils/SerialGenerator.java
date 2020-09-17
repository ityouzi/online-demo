package com.example.online.demo.utils;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 随机序列生成器
 * 
 */
public class SerialGenerator {

	/**
	 * 以随机种子初始化random对象
	 */
	private static final Random RANDOM = new SecureRandom();
	
	/**
	 * 内部交易码长度
	 */
	private static int INTER_TRANS_ID_LENGTH = 32;

	/**
	 * 日期格式化模板
	 */
	private static ThreadLocal<SimpleDateFormat> dateFormatFactory = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyyMMddHHmmssSSS");
		}
	};

	private static final String DEFAULT_TANSID_TYPE = "10";

	private static final char[] mm = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };


	/**
	 * 生成规则 17位日期毫秒数 + 消息类型（10) + 随机数补齐32位
	 * 
	 */
	public static String getOrder() {
		StringBuilder sb = new StringBuilder();
		sb.append(dateFormatFactory.get().format(new Date())).append(DEFAULT_TANSID_TYPE)
				.append(generateRandomSerial(INTER_TRANS_ID_LENGTH - sb.length()));
		return sb.toString();
	}

	
	/**
	 * 生成len长度的随机数序列
	 * @param len
	 * @return String randomString
	 */
	public static String generateRandomSerial(int len) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append(mm[RANDOM.nextInt(mm.length)]);
		}
		return sb.toString();
	}

}