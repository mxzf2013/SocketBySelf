package com.mxzf.liyun.domain;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 
 * @Title: RequestMessage
 * @Dscription: 请求信息
 * @author Deleter
 * @date 2017年5月21日 下午6:40:56
 * @version 1.0
 */
public class RequestMessage implements Serializable {
	/**
	 * 动作
	 */
	private String action;

	/**
	 * 数据
	 */
	private HashMap<String, String> params = new HashMap<>();

	public RequestMessage() {
	}

	/**
	 * 设置动作指向
	 * 
	 * @param action
	 * @return RequestMessage
	 */
	public RequestMessage setAction(String action) {
		this.action = action;
		return this;
	}

	public String getAction() {
		return action;
	}

	/**
	 * 添加参数
	 * 
	 * @param name
	 *            键
	 * @param data
	 *            值
	 * @return RequestMessage
	 */
	public RequestMessage addParams(String name, String data) {
		params.put(name, data);
		return this;
	}

	public HashMap<String, String> getParams() {
		return params;
	}
}
