package com.example.online.demo.common;



/**
 * 
 * @Description 
 * 公共返回bean
 * @author 北辰不落雪
 * @date 2019年3月8日
 */
public class ResultCodeBean<T> implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public String code;

	public String msg;

	public T data;

	public ResultCodeBean() {
		super();
	}


	
	public ResultCodeBean(String code, String msg) {
		this(code,msg,null);
	}
	
	public ResultCodeBean(String code, String msg, T data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	
	public String getCode() {

		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {

		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {

		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
