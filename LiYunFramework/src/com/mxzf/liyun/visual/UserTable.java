package com.mxzf.liyun.visual;

import java.util.Hashtable;

import com.mxzf.liyun.util.StringUtil;

/**
 * 
 * @Title: UserTable
 * @Dscription: 用户表
 * @author Deleter
 * @date 2017年5月22日 下午3:01:44
 * @version 1.0
 */
public class UserTable {

	private static Hashtable<String, String> list = new Hashtable<>();

	static {
		list.put("1943815081", "zz,520.");
		list.put("1943815082", "zz,520.");
		list.put("1943815083", "zz,520.");
	}

	/**
	 * 获取密码
	 * 
	 * @param username
	 *            用户名
	 * @return String
	 */
	public static String getPassword(String username) {
		return list.get(username);
	}

	/**
	 * 用户名是否存在
	 * 
	 * @param username
	 *            用户名
	 * @return boolean
	 */
	public static boolean isExist(String username) {
		return list.containsKey(username) ;
	}

	/**
	 * 添加用户
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return boolean
	 */
	public static boolean addUser(String username, String password) {
		if (username != null && password != null) {
			list.put(username, password);
			return true;
		}
		return false;
	}

	/**
	 * 更换密码
	 * 
	 * @return
	 */
	public static boolean updatePassword(String username, String password,
			String newPassword) {
		if (!StringUtil.isEmpty(username, password, newPassword)) {
			String pwd = list.get(username);
			if (pwd != null) {
				if (password.equals(pwd)) {// 密码一致
					list.put(username, newPassword);
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		return false;
	}
}
