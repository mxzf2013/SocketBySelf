package com.mxzf.liyun.commend;

import java.util.ArrayList;
import java.util.Hashtable;

import com.mxzf.liyun.domain.LYFile;
import com.mxzf.liyun.domain.LYMessage;
import com.mxzf.liyun.domain.UDPMapInfo;
import com.mxzf.liyun.server.tcp.TCPServerThread;

public class LYCloudDataBase {
	/**
	 * 客户端网关信息集合
	 */
	public static Hashtable<String, UDPMapInfo> netMapInfos = new Hashtable<>();// 服务器

	/**
	 * TCP连接池
	 */
	public static Hashtable<String, TCPServerThread> tcpServerThread = new Hashtable<>();// 客户端心跳

	/**
	 * 本地客户端网关信息集合
	 */
	public static Hashtable<String, UDPMapInfo> localMapInfos = new Hashtable<>();// 我XX打通了连接
	/**
	 * 消息队列
	 */
	public static Hashtable<String, ArrayList<LYMessage>> msgQueue = new Hashtable<>();
	/**
	 * 文件队列
	 */
	public static Hashtable<String, ArrayList<LYFile>> fileQueue = new Hashtable<>();
}
