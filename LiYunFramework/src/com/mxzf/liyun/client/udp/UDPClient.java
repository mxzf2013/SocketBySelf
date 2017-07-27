package com.mxzf.liyun.client.udp;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;

import com.mxzf.liyun.IoHandler;
import com.mxzf.liyun.commend.LYCloudDataBase;
import com.mxzf.liyun.commend.LYConfigs;
import com.mxzf.liyun.domain.Message;
import com.mxzf.liyun.domain.MessageType;
import com.mxzf.liyun.domain.UDPMapInfo;
import com.mxzf.liyun.domain.User;
import com.mxzf.liyun.util.JsonUtils;

public class UDPClient {
    private String username;

    private String lastUsername;

    private DatagramSocket client;

    private IoHandler proxyHandler;

    private boolean isConnected = false;

    /**
     * 连接UDP服务器
     * 
     * @throws InterruptedException
     * @throws IOException
     */
    public void connect() throws IOException, InterruptedException {
	client = new DatagramSocket();
	if (sendToServer("conncet", "1") != null) {
	    System.out.println("UDP客户端：拟连接成功!");
	} else {
	    System.out.println("UDP客户端 ：拟连接失败!");
	}
    }

    public boolean isConnected() {
	return isConnected;
    }

    /**
     * 设置适配器
     * 
     * @param proxyHandler
     */
    public void setIoHandler(IoHandler proxyHandler) {
	this.proxyHandler = proxyHandler;
    }

    /**
     * 发送到服务器
     * 
     * @param action
     * @param obj
     * @throws IOException
     * @throws InterruptedException
     */
    public String sendToServer(String action, Object obj) throws IOException,
	    InterruptedException {
	String returnStr = null;
	byte[] data = JsonUtils.encode(action, obj).getBytes();
	InetAddress addr = InetAddress.getByName(LYConfigs.TCP_SERVER_ADDR);
	DatagramPacket sendPacket = new DatagramPacket(data, data.length, addr,
		LYConfigs.UDP_SERVER_PORT);
	System.out.println("UDP客户端：" + username + "：发送封包给服务器");
	client.send(sendPacket);
	byte[] recvBuf = new byte[500];
	DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
	client.receive(recvPacket);
	String encodeData = new String(recvPacket.getData(), 0,
		recvPacket.getLength());
	if (encodeData != null && encodeData.length() > 0) {
	    System.out.println("UDP客户端：" + username + "：收到加密数据：" + encodeData);
	    String recvAction = JsonUtils.decodeAction(encodeData);
	    System.out.println("UDP客户端：" + username + "：解析加密数据：Action："
		    + recvAction);
	    if ("connectResp".equals(recvAction))// 连接UDP服务器
	    {
		isConnected = true;
		returnStr = encodeData;
	    } else if ("loginResp".equals(recvAction)) {// 登录
		returnStr = encodeData;
		username = JsonUtils.decodeObj(encodeData, String.class);
		System.out.println("UDP客户端：" + username + "：解析加密数据：Obj："
			+ username);
	    } else if ("getClientUdpMapInfoResp".equals(recvAction))// 获取目标客户端外网地址
	    {
		returnStr = encodeData;
		// 连接客户端（内网穿透）
		UDPMapInfo mapInfo = JsonUtils.decodeObj(encodeData,
			UDPMapInfo.class);
		System.out.println("UDP客户端：" + username + "：解析加密数据：Obj："
			+ mapInfo.toString());
		if (mapInfo.getName() != null) {
		    System.out.println("UDP客户端：" + username + "：获取"
			    + lastUsername + "的地址和端口成功!");// 成功
		    LYCloudDataBase.localMapInfos.put(mapInfo.getName(),
			    mapInfo);// 添加本地映射规则
		    sendToClient(mapInfo, "connectClient", "1");
		} else {
		    System.out.println("UDP客户端：" + username + "：获取："
			    + lastUsername + "的地址和端口失败!");// 失败
		}
	    }
	} else {
	    System.out.println("UDP客户端：" + username + "：未收到数据");
	}
	return returnStr;
    }

    /**
     * 发送到客户端
     * 
     * @param action
     * @param obj
     * @throws IOException
     * @throws InterruptedException
     */
    private String sendToClient(UDPMapInfo mapInfo, String action, Object obj)
	    throws IOException, InterruptedException {
	System.out.println("UDP客户端：" + username + "：UDP内网穿透开始");
	String returnStr = null;
	byte[] data = JsonUtils.encode(action, obj).getBytes();
	DatagramPacket sendPacket = new DatagramPacket(data, data.length,
		mapInfo.getAddress(), mapInfo.getPort());
	System.out.println("UDP客户端：" + username + "：发送封包给" + mapInfo.getName()
		+ "客户端");
	client.send(sendPacket);
	byte[] recvBuf = new byte[500];
	DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
	client.receive(recvPacket);
	String encodeData = new String(recvPacket.getData(), 0,
		recvPacket.getLength());
	if (encodeData != null && encodeData.length() > 0) {
	    System.out.println("UDP客户端：" + username + "：收到数据：" + encodeData);
	    String recvAction = JsonUtils.decodeAction(encodeData);
	    if ("connectClientResp".equals(recvAction))// 连接上UDP客户端
	    {
		returnStr = encodeData;
		System.out.println("UDP客户端：" + username + "：对方客户端收到消息!");
		LYCloudDataBase.localMapInfos.put(username, mapInfo);// 添加有效的映射规则
		return returnStr;
	    } else {
		proxyHandler.onMessage(encodeData);
	    }
	} else {
	    System.out.println("UDP客户端：" + username + "：未收到对方客户端的响应数据，重新发送!");
	    sendToClient(mapInfo, action, obj);
	}
	return returnStr;
    }

    /**
     * 获取目标客户端外网地址
     * 
     * @param username
     *            目标客户端标识
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public UDPMapInfo getClientUdpMapInfo(String username) throws IOException,
	    InterruptedException {
	lastUsername = username;
	String encodeData = sendToServer("getClientUdpMapInfo", username);
	return JsonUtils.decodeObj(encodeData, UDPMapInfo.class);
    }

    /**
     * 登录
     * 
     * @param user
     * @return 用户名
     * @throws IOException
     * @throws InterruptedException
     */
    public String login(User user) throws IOException, InterruptedException {
	String encodeData = sendToServer("login", user);
	return JsonUtils.decodeObj(encodeData, String.class);
    }

    public void close() {
	if (client != null)
	    client.close();
    }

    /**
     * 发送消息
     * 
     * @param targetClientName
     * @param type
     * @param content
     * @throws IOException
     * @throws InterruptedException
     */
    public void sendMessageToClient(String targetClientName, String content)
	    throws IOException, InterruptedException {
	Message msg = new Message(username, targetClientName,
		MessageType.MSG.name(), content, new Date());
	sendToClient(LYCloudDataBase.localMapInfos.get(targetClientName),
		MessageType.MSG.name(), msg);
    }

    /**
     * 发送文件
     * 
     * @param targetClientName
     * @param type
     * @param content
     * @throws IOException
     * @throws InterruptedException
     */
    public void sendFileToClient(String targetClientName, ArrayList<File> files)
	    throws IOException, InterruptedException {
	Message msg = new Message(username, targetClientName,
		MessageType.VOICE.name(), files, new Date());
	sendToClient(LYCloudDataBase.localMapInfos.get(targetClientName),
		MessageType.VOICE.name(), msg);
    }

    /**
     * 发送语音
     * 
     * @param targetClientName
     * @param type
     * @param content
     * @throws IOException
     * @throws InterruptedException
     */
    public void sendVoiceToClient(String targetClientName, File voice)
	    throws IOException, InterruptedException {
	Message msg = new Message(username, targetClientName,
		MessageType.VOICE.name(), voice, new Date());
	sendToClient(LYCloudDataBase.localMapInfos.get(targetClientName),
		MessageType.VOICE.name(), msg);
    }
}
