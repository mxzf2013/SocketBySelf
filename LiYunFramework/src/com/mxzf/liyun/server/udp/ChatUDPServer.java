package com.mxzf.liyun.server.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import com.alibaba.fastjson.JSON;
import com.mxzf.liyun.commend.LYCloudDataBase;
import com.mxzf.liyun.commend.LYCode;
import com.mxzf.liyun.commend.LYConfigs;
import com.mxzf.liyun.domain.LYMessage;
import com.mxzf.liyun.domain.RequestMessage;
import com.mxzf.liyun.domain.ResponseMessage;
import com.mxzf.liyun.domain.UDPMapInfo;
import com.mxzf.liyun.server.tcp.FileUploadServer;
import com.mxzf.liyun.visual.OnlineTable;
import com.mxzf.liyun.visual.UserTable;

public class ChatUDPServer {

	private static DatagramSocket server = null;
	private static FileUploadServer fileUploadServer = null;

	private ChatUDPServer() {

	}

	public static void start() {
		boolean isDone = true;
		try {

			fileUploadServer = new FileUploadServer();
			fileUploadServer.start();

			System.out.println("UDP服务器开启！监听端口：" + LYConfigs.UDP_SERVER_PORT);
			server = new DatagramSocket(LYConfigs.UDP_SERVER_PORT);
			byte[] recvBuf = new byte[LYConfigs.UDP_PACKAGE_SIZE];
			DatagramPacket recvPacket = new DatagramPacket(recvBuf,
					recvBuf.length);
			while (isDone) {
				server.receive(recvPacket);// 阻塞
				delayRequest(recvPacket);
			}
		} catch (Exception e) {
			System.err.println("服务器异常关闭");
			e.printStackTrace();
			isDone = false;
			server.close();
		}
	}

	private static void delayRequest(DatagramPacket recvPacket)
			throws IOException {
		String encodeData = new String(recvPacket.getData(), 0,
				recvPacket.getLength());
		System.out.println("UDP服务器：收到数据：" + encodeData);
		System.out.println("UDP服务器：正在处理数据，并反馈");
		// 网关的端口
		int port = recvPacket.getPort();
		// 网关的IP地址
		InetAddress address = recvPacket.getAddress();
		// 处理
		RequestMessage rm = JSON.parseObject(encodeData, RequestMessage.class);
		String action = rm.getAction();

		if (LYCode.ACTION_CONN.equals(action)) {// ----------------连接
			System.out.println("UDP服务器：有客户端请求打洞!");
			byte[] sendBuff = "1".getBytes();
			response(sendBuff, address, port);
		} else if (LYCode.ACTION_REG.equals(action)) {// ----------------注册
			String username = rm.getParams().get("username");
			if (!UserTable.isExist(username)) {// ----------------用户名不存在
				ResponseMessage responseMessage = new ResponseMessage();
				responseMessage.setResult(LYCode.RESULT_ERROR);
				responseMessage.setDesc(LYCode.DESC_USERNAME_EXIST);
				byte[] sendBuff = JSON.toJSONString(responseMessage).getBytes();
				response(sendBuff, address, port);
			} else {
				String password = rm.getParams().get("password");
				UserTable.addUser(username, password);
				ResponseMessage responseMessage = new ResponseMessage();
				responseMessage.setResult(LYCode.RESULT_OK);
				responseMessage.setDesc(LYCode.DESC_REG_OK);
				byte[] sendBuff = JSON.toJSONString(responseMessage).getBytes();
				response(sendBuff, address, port);
			}
		} else if (LYCode.ACTION_LOGIN.equals(action)) {// ----------------登录
			String username = rm.getParams().get("username");
			String password = rm.getParams().get("password");

			if (UserTable.isExist(username)) { // ----------------有效用户名

				if (OnlineTable.getUserType(username)) {// ----------------在线
					ResponseMessage responseMessage = new ResponseMessage();
					responseMessage.setResult(LYCode.RESULT_OK);
					responseMessage.setDesc(LYCode.DESC_LOGIN_OK);
					byte[] sendBuff = JSON.toJSONString(responseMessage)
							.getBytes();
					response(sendBuff, address, port);
				} else {// ----------------不在线
					if (UserTable.getPassword(username).equals(password)) {
						LYCloudDataBase.netMapInfos.put(username,
								new UDPMapInfo(username, address, port));
						OnlineTable.setUserType(username, true);
						ResponseMessage responseMessage = new ResponseMessage();
						responseMessage.setResult(LYCode.RESULT_OK);
						responseMessage.setDesc(LYCode.DESC_LOGIN_OK);
						byte[] sendBuff = JSON.toJSONString(responseMessage)
								.getBytes();
						response(sendBuff, address, port);
					} else {
						ResponseMessage responseMessage = new ResponseMessage();
						responseMessage.setResult(LYCode.RESULT_ERROR);
						responseMessage.setDesc(LYCode.DESC_PASSWORD_ERROR);
						byte[] sendBuff = JSON.toJSONString(responseMessage)
								.getBytes();
						response(sendBuff, address, port);
					}
				}
			} else {
				ResponseMessage responseMessage = new ResponseMessage();
				responseMessage.setResult(LYCode.RESULT_ERROR);
				responseMessage.setDesc(LYCode.DESC_USERNAME_UNEXIST);
				byte[] sendBuff = JSON.toJSONString(responseMessage).getBytes();
				response(sendBuff, address, port);
			}

		} else if (LYCode.ACTION_LOGOUT.equals(action)) {// ----------------登出
			String username = rm.getParams().get("username");
			String password = rm.getParams().get("password");
			if (OnlineTable.getUserType(username)) {// ----------------在线
				if (UserTable.getPassword(username).equals(password)) {// ----------------通知客户端下线
					OnlineTable.setUserType(username, false);
					ResponseMessage responseMessage = new ResponseMessage();
					responseMessage.setResult(LYCode.RESULT_OK);
					responseMessage.setDesc(LYCode.DESC_ACTION_OK);
					byte[] sendBuff = JSON.toJSONString(responseMessage)
							.getBytes();
					response(sendBuff, address, port);
				} else {// ----------------无权操作
					ResponseMessage responseMessage = new ResponseMessage();
					responseMessage.setResult(LYCode.RESULT_ERROR);
					responseMessage.setDesc(LYCode.DESC_ACTION_ERROR);
					byte[] sendBuff = JSON.toJSONString(responseMessage)
							.getBytes();
					response(sendBuff, address, port);
				}
			} else {
				ResponseMessage responseMessage = new ResponseMessage();
				responseMessage.setResult(LYCode.RESULT_ERROR);
				responseMessage.setDesc(LYCode.DESC_ACTION_ERROR);
				byte[] sendBuff = JSON.toJSONString(responseMessage).getBytes();
				response(sendBuff, address, port);
			}
		} else if (LYCode.ACTION_SEND_LYMESSAGE.equals(action)) {// ----------------发送LYMESSAGE
			LYMessage lyMessage = JSON.parseObject(
					rm.getParams().get(LYCode.PARAM_LYMESSAGE_DATA),
					LYMessage.class);

			String toChatUsername = lyMessage.getToChatUsername();
			UDPMapInfo mapInfo = LYCloudDataBase.netMapInfos
					.get(toChatUsername);
			if (mapInfo != null) {// ----------------在线
				response(JSON.toJSONString(rm).getBytes(),
						mapInfo.getAddress(), mapInfo.getPort());
			} else {
				if (LYCloudDataBase.msgQueue.get(toChatUsername) != null) {// ----------------对应Username的消息队列是否已创建
					LYCloudDataBase.msgQueue.get(toChatUsername).add(lyMessage);
				} else {
					ArrayList<LYMessage> lyQueue = new ArrayList<>();
					lyQueue.add(lyMessage);
					LYCloudDataBase.msgQueue.put(toChatUsername, lyQueue);// ----------------保存到消息队列
				}
				System.out.println("保存数据到" + toChatUsername + "的消息队列");
				ResponseMessage responseMessage = new ResponseMessage();
				responseMessage.setResult(LYCode.RESULT_ERROR);
				responseMessage.setDesc(LYCode.DESC_USER_UNLINE);
				byte[] sendBuff = JSON.toJSONString(responseMessage).getBytes();
				response(sendBuff, address, port);
			}

		} else if (LYCode.ACTION_GET_ALL_MSG.equals(action)) {// ----------------获取所有离线消息
			String username = rm.getParams().get("username");
			if (LYCloudDataBase.msgQueue.get(username) != null) {
				ResponseMessage responseMessage = new ResponseMessage();
				responseMessage.setResult(LYCode.RESULT_OK);
				responseMessage.setDesc(LYCode.DESC_ACTION_OK);
				responseMessage.setData(JSON
						.toJSONString(LYCloudDataBase.msgQueue.get(username)));
				byte[] sendBuff = JSON.toJSONString(responseMessage).getBytes();
				response(sendBuff, address, port);
			} else {
				ResponseMessage responseMessage = new ResponseMessage();
				responseMessage.setResult(LYCode.RESULT_ERROR);
				responseMessage.setDesc(LYCode.DESC_ACTION_ERROR);
				byte[] sendBuff = JSON.toJSONString(responseMessage).getBytes();
				response(sendBuff, address, port);
			}
		} else if (LYCode.ACTION_GET_ALL_MSG_SIZE.equals(action)) {// ----------------获取离线消息数量
			String username = rm.getParams().get("username");
			if (LYCloudDataBase.msgQueue.get(username) != null) {
				ResponseMessage responseMessage = new ResponseMessage();
				responseMessage.setResult(LYCode.RESULT_OK);
				responseMessage.setDesc(LYCode.DESC_ACTION_OK);
				responseMessage.setData(String.valueOf(LYCloudDataBase.msgQueue
						.get(username).size()));
				byte[] sendBuff = JSON.toJSONString(responseMessage).getBytes();
				response(sendBuff, address, port);
			} else {
				ResponseMessage responseMessage = new ResponseMessage();
				responseMessage.setResult(LYCode.RESULT_ERROR);
				responseMessage.setDesc(LYCode.DESC_ACTION_ERROR);
				byte[] sendBuff = JSON.toJSONString(responseMessage).getBytes();
				response(sendBuff, address, port);
			}
		} else if (LYCode.ACTION_GET_MSG_ID.equals(action)) {// ----------------根据ID获取离线消息

		} else if (LYCode.ACTION_GET_ALL_GROUP.equals(action)) {// ----------------获取所有分组

		} else if (LYCode.ACTION_GET_GROUP_ID.equals(action)) {// ----------------根据ID获取分组详情

		} else if (LYCode.ACTION_GET_PERSON_INFO.equals(action)) {// ----------------获取个人信息

		} else if (LYCode.ACTION_GET_PERSON_INFO_ID.equals(action)) {// ----------------根据ID获取个人信息

		}
	}

	public static void response(byte[] sendBuff, InetAddress address, int port)
			throws IOException {
		// 设定Client的网关IP和端口
		DatagramPacket sendPacket = new DatagramPacket(sendBuff,
				sendBuff.length, address, port);
		// 通知Client
		server.send(sendPacket);
	}

	public static void main(String[] args) {
		ChatUDPServer.start();
	}
}
