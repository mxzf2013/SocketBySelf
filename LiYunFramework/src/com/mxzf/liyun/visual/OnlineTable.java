package com.mxzf.liyun.visual;

import java.util.Hashtable;
/**
 * 
 * @Title: OnlineTable
 * @Dscription:  在线列表
 * @author Deleter
 * @date 2017年5月22日 下午3:01:30
 * @version 1.0
 */
public class OnlineTable {

	private static Hashtable<String, Boolean> list = new Hashtable<>();

	static {
		list.put("1943815081", false);
		list.put("1943815082", false);
		list.put("1943815083", false);
	}

	/**
	 * 获取在线状态
	 * 
	 * @param username
	 *            用户名
	 * @return String
	 */
	public static boolean getUserType(String username) {
		return list.get(username);
	}

	/**
	 * 添加用户在线状态
	 * 
	 * @param username
	 *            用户名
	 * @param type
	 *            状态
	 * @return boolean
	 */
	public static boolean setUserType(String username, Boolean type) {
		if (username != null) {
			list.put(username, type);
			return true;
		}
		return false;
	}
}
