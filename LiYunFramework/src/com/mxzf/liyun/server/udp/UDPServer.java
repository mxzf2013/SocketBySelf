package com.mxzf.liyun.server.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.mxzf.liyun.commend.LYCloudDataBase;
import com.mxzf.liyun.commend.LYConfigs;
import com.mxzf.liyun.domain.UDPMapInfo;
import com.mxzf.liyun.domain.User;
import com.mxzf.liyun.util.JsonUtils;

public class UDPServer {
    private UDPServer() {

    }

    public static void start() {
	boolean isDone = true;
	DatagramSocket server = null;
	try {
	    System.out.println("UDP服务器开启！监听端口：" + LYConfigs.UDP_SERVER_PORT);
	    server = new DatagramSocket(LYConfigs.UDP_SERVER_PORT);
	    byte[] recvBuf = new byte[400];
	    DatagramPacket recvPacket = new DatagramPacket(recvBuf,
		    recvBuf.length);
	    while (isDone) {
		server.receive(recvPacket);
		delayRequest(server, recvPacket);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    isDone = false;
	    server.close();
	}
    }

    private static void delayRequest(DatagramSocket server,
	    DatagramPacket recvPacket) throws IOException {
	String encodeData = new String(recvPacket.getData(), 0,
		recvPacket.getLength());
	System.out.println("UDP服务器：收到数据：" + encodeData);
	System.out.println("UDP服务器：正在处理数据，并反馈");
	// 网关的端口
	int port = recvPacket.getPort();
	// 网关的IP地址
	InetAddress addr = recvPacket.getAddress();
	// 处理
	String action = JsonUtils.decodeAction(encodeData);
	if ("conncet".equals(action))// ----------------------------------------------------
				     // 拟连接
	{
	    System.out.println("UDP服务器：有客户端请求打洞!");
	    String flag = JsonUtils.decodeObj(encodeData, String.class);
	    if ("1".equals(flag)) {
		byte[] sendBuff = JsonUtils.encode("connectResp", "1")
			.getBytes();
		// 设定Client的网关IP和端口
		DatagramPacket sendPacket = new DatagramPacket(sendBuff,
			sendBuff.length, addr, port);
		// 通知Client
		server.send(sendPacket);
	    }
	} else if ("login".equals(action))// ----------------------------------------------------
					  // 登录
	{
	    User user = JsonUtils.decodeObj(encodeData, User.class);
	    System.out.println("UDP服务器：客户" + user.getUsername() + "请求登录!");
	    // 存储ClientInfo
	    LYCloudDataBase.netMapInfos.put(user.getUsername(), new UDPMapInfo(
		    user.getUsername(), addr, port));
	    // 反馈
	    byte[] sendBuff = JsonUtils.encode("loginResp", user.getUsername())
		    .getBytes();
	    // 设定Client的网关IP和端口
	    DatagramPacket sendPacket = new DatagramPacket(sendBuff,
		    sendBuff.length, addr, port);
	    // 通知Client
	    server.send(sendPacket);
	} else if ("getClientUdpMapInfo".equals(action))// ----------------------------------------------------
							// 获取外网地址
	{
	    String username = JsonUtils.decodeObj(encodeData, String.class);
	    UDPMapInfo mapInfo = LYCloudDataBase.netMapInfos.get(username);
	    byte[] sendBuff;
	    if (mapInfo != null) {
		sendBuff = JsonUtils.encode("getClientUdpMapInfoResp", mapInfo)
			.getBytes();
	    } else {
		sendBuff = JsonUtils.encode("getClientUdpMapInfoResp",
			new UDPMapInfo(null, null, 0)).getBytes();
	    }
	    // 设定Client的网关IP和端口
	    DatagramPacket sendPacket = new DatagramPacket(sendBuff,
		    sendBuff.length, addr, port);
	    // 通知Client
	    server.send(sendPacket);
	}
    }
}
