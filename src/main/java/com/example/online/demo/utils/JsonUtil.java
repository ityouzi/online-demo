package com.example.online.demo.utils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.StringWriter;

/**
 * {@code JsonUtil}主要区分普通类型和泛型两种。
 * <p>
 * 提供将类(包括泛型)与json字符串互转的方法；
 * <p>
 * 从json字符串内部提取出内部的bean的方法；
 * <p>
 * 更新json字符串内部bean字符串的方法。
 * 
 */
public class JsonUtil {

	private static ThreadLocal<ObjectMapper> local = new ThreadLocal<ObjectMapper>() {
		@Override
		protected ObjectMapper initialValue() {
			return new ObjectMapper();
		}
	};

	/**
	 * 将JavaBean将转换为Json字符串
	 * 
	 * @param obj
	 *            - java对象
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String object2JsonStr(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = local.get();
		String jsonStr;
		try {
			jsonStr = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw e;
		}
		return jsonStr;
	}

	/**
	 * 将JSON格式的字符串转为JavaBean
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 * @throws IOException
	 * @throws JsonParseException
	 *             - json格式出错
	 * @throws JsonMappingException
	 *             - 类型有问题
	 */
	public static <T> T jsonStr2Obj(String json, Class<T> clazz) throws IOException {
		ObjectMapper mapper = local.get();
		T obj;
		try {
			obj = mapper.readValue(json, clazz);
		} catch (JsonParseException | JsonMappingException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
		return obj;
	}

	/**
	 * 从 json里获取内部bean
	 * 
	 * @param json
	 *            json字符串
	 * @param fieldName
	 *            属性名
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	public static <T> T get(String json, String fieldName, Class<T> clazz) throws IOException {
		ObjectMapper mapper = local.get();
		T obj;
		try {
			ObjectNode objectNode = (ObjectNode) mapper.readTree(json);
			obj = mapper.readValue(objectNode.get(fieldName).toString(), clazz);
		} catch (JsonParseException | JsonMappingException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
		return obj;
	}

	/**
	 * 从 json里获取内部bean
	 * 
	 * @param json
	 *            json字符串
	 * @param fieldName
	 *            属性名
	 * @param valueTypeRef
	 *            类型，eg:new TypeReference&lt;List&lt;ChargeQueryBean&gt;&gt;(){}
	 * @param
	 * @return
	 * @throws IOException
	 */
	public static <T> T get(String json, String fieldName, TypeReference<?> valueTypeRef) throws IOException {
		ObjectMapper mapper = local.get();
		T obj;
		try {
			ObjectNode objectNode = (ObjectNode) mapper.readTree(json);
			obj = (T) mapper.readValue(objectNode.get(fieldName).toString(), valueTypeRef);
		} catch (JsonParseException | JsonMappingException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
		return obj;
	}

	/**
	 * 将新json字符串更新到json字符串中
	 * 
	 * @param json
	 *            原json字符串
	 * @param needToUpdate
	 *            新字符串
	 * @param fieldName
	 *            属性名
	 * @return
	 * @throws IOException
	 */
	public static String updateJsonStr(String json, String needToUpdate, String fieldName) throws IOException {
		ObjectMapper mapper = local.get();
		ObjectNode objectNode = null;
		try {
			JsonNode needToUpdateNode = null;
			objectNode = (ObjectNode) mapper.readTree(json);
			needToUpdateNode = mapper.readTree(needToUpdate);
			objectNode.set(fieldName, needToUpdateNode);
			json = objectNode.toString();
			return json;
		} catch (IOException e) {
			throw e;
		}
	}

	/**
	 * 将Bean转为带有泛型的对象<br>
	 * 使用示例 List&lt;ChrageQueryBean&gt; list = JsonUtil.jsonStr2GenericObj(jsonStr, new TypeReference&lt;List&lt;ChrageQueryBean&gt;&gt;()
	 * {});
	 * 
	 * @param json
	 *            - json字符串
	 * @param type
	 *            - jackson中定义的TypeReference对象
	 * @return
	 * @throws IOException
	 */
	public static <T> T jsonStr2GenericObj(String json, TypeReference<?> type) throws IOException {
		ObjectMapper mapper = local.get();
		T genericObj;
		try {
			genericObj = (T) mapper.readValue(json, type);
		} catch (JsonParseException | JsonMappingException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
		return genericObj;
	}

	/**
	 * 将泛型Bean转为Json字符串，并保留特殊设置<br>
	 * 使用示例 String jsonStr = JsonUtil.genericObj2JsonStr(list, new TypeReference&lt;List&lt;XPayBean&gt;&gt;() {});
	 * 
	 * @param obj
	 *            - java对象
	 * @param type
	 *            - jackson中定义的TypeReference对象
	 * @return
	 * @throws IOException
	 */
	public static <T> String genericObj2JsonStr(T obj, TypeReference<?> type) throws IOException {
		ObjectMapper mapper = local.get();
		ObjectWriter writer = mapper.writerFor(type);
		StringWriter w = new StringWriter();
		try {
			writer.writeValue(w, obj);
		} catch (JsonGenerationException | JsonMappingException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}

		return w.toString();
	}

	/**
	 * 从json字符串中获取value值（第一层）
	 * 
	 * @param json
	 * @param fieldName
	 * @return
	 * @throws IOException
	 */
	public static String getValue(String json, String fieldName) throws IOException {

		try {
			ObjectMapper mapper = new ObjectMapper();

			ObjectNode objectNode = (ObjectNode) mapper.readTree(json);
			String withStr = "\"";
			String valueStr = objectNode.get(fieldName).toString();
			if (valueStr.startsWith(withStr) && valueStr.endsWith(withStr)) {
				valueStr = valueStr.substring(1, valueStr.length() - 1);
			}
			return valueStr;

		} catch (NullPointerException e) {
			return null;
		} catch (JsonParseException | JsonMappingException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
	}
}