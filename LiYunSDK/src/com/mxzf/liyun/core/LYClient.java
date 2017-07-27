package com.mxzf.liyun.core;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.mxzf.liyun.commend.LYCode;
import com.mxzf.liyun.commend.LYConfigs;
import com.mxzf.liyun.commend.LYLocalDataBase;
import com.mxzf.liyun.domain.LYMessage;
import com.mxzf.liyun.domain.RequestMessage;
import com.mxzf.liyun.domain.ResponseMessage;

public class LYClient {
	private static DatagramSocket client;
	private boolean isConnected = false;

	private LYMessageListener msgListener;

	private LYClient() {
	}

	private static class StaticClass {
		private static LYClient ly = new LYClient();
	}

	public synchronized static LYClient getInstance() throws SocketException {
		if (client == null) {
			client = new DatagramSocket();
			RequestMessage rm = new RequestMessage()
					.setAction(LYCode.ACTION_CONN);
			if (sendToServer(rm) != null) {
				System.out.println("拟连接成功!");
			} else {
				System.out.println("拟连接失败!");
			}
		}
		return StaticClass.ly;
	}

	/**
	 * 发送数据到服务器
	 * 
	 * @param rm
	 * @return
	 */
	private static String sendToServer(RequestMessage rm) {
		String result = null;
		try {
			byte[] data = JSON.toJSONString(rm).getBytes();
			InetAddress address = InetAddress
					.getByName(LYConfigs.TCP_SERVER_ADDR);
			DatagramPacket sendPacket = new DatagramPacket(data, data.length,
					address, LYConfigs.UDP_SERVER_PORT);
			client.send(sendPacket);
			byte[] recvBuf = new byte[LYConfigs.UDP_PACKAGE_SIZE];
			DatagramPacket recvPacket = new DatagramPacket(recvBuf,
					recvBuf.length);
			client.receive(recvPacket);
			result = new String(recvPacket.getData(), 0, recvPacket.getLength());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 用户注册<br>
	 * 注册失败会抛出NullException
	 * 
	 * @param username
	 *            用户名
	 * @param pwd
	 *            密码
	 * @return User
	 */
	public synchronized ResponseMessage createAccount(String username,
			String password) {
		RequestMessage rm = new RequestMessage();
		rm.setAction(LYCode.ACTION_REG);
		rm.addParams("username", username);
		rm.addParams("password", password);
		String response = sendToServer(rm);
		return JSON.parseObject(response, ResponseMessage.class);
		/*
		 * String result = HttpRequest.sendPost(
		 * "http://127.0.0.1:8080/liyun/user/reg", "username=" + username +
		 * "&password=" + password); ResponseMessage rm =
		 * JSON.parseObject(result, ResponseMessage.class); if (rm.getResult()
		 * == 0) { throw new NullPointerException(rm.getDesc()); } else { return
		 * JSON.parseObject(rm.getData(), User.class); }
		 */
	}

	/**
	 * 登录
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param callBack
	 *            登录回调
	 * @throws SocketException
	 */
	public synchronized void login(String username, String password,
			LYCallBack callBack) throws SocketException {
		callBack.onProgress(0, "login");
		/*
		 * String result = HttpRequest.sendPost(
		 * "http://127.0.0.1:8080/liyun/user/login", "username=" + username +
		 * "&password=" + password);
		 */
		RequestMessage requestMessage = new RequestMessage();
		requestMessage.setAction(LYCode.ACTION_LOGIN);
		requestMessage.addParams("username", username);
		requestMessage.addParams("password", password);
		String result = sendToServer(requestMessage);
		ResponseMessage rm = JSON.parseObject(result, ResponseMessage.class);
		callBack.onProgress(rm.getResult(), rm.getDesc());
		if (rm.getResult() == LYCode.RESULT_OK) {
			// 保存
			LYLocalDataBase.USERNAME = username;
			LYLocalDataBase.PASSWORD = password;
			callBack.onSuccess();
			// 在success方法中调用
			// LYClient.getInstance().groupManager().loadAllGroups();
			// LYClient.getInstance().chatManager().loadAllConversations();
		} else {
			callBack.onError(rm.getResult(), rm.getDesc());
		}
	}

	/**
	 * 退出登录
	 * 
	 * @param isLogout
	 *            是否立即退出
	 */
	public synchronized void logout(boolean isLogout) {
		if (isLogout) {
			// 退出登录逻辑
			RequestMessage requestMessage = new RequestMessage();
			requestMessage.setAction(LYCode.ACTION_LOGOUT);
			requestMessage.addParams("username", LYLocalDataBase.USERNAME);
			requestMessage.addParams("password", LYLocalDataBase.PASSWORD);
			ResponseMessage rm = JSON.parseObject(sendToServer(requestMessage),
					ResponseMessage.class);
			if (rm.getResult() == LYCode.RESULT_OK) {
				System.out.println("下线：" + rm.getDesc());
				// 本地处理
				LYLocalDataBase.USERNAME = null;
				LYLocalDataBase.PASSWORD = null;
				// 关闭连接
				if (client != null) {
					client.close();
					client = null;
				}
				msgListener = null;
			} else {
				System.out.println("下线：" + rm.getDesc());
			}
		}
	}

	/**
	 * 退出登录（异步）
	 * 
	 * @param isLogout
	 *            是否立即退出
	 * @param callBack
	 *            退出登录回调
	 */
	public void logout(boolean isLogout, LYCallBack callBack) {
		if (isLogout) {
			callBack.onProgress(0, "logout");
			// 退出登录逻辑
			RequestMessage requestMessage = new RequestMessage();
			requestMessage.setAction(LYCode.ACTION_LOGOUT);
			requestMessage.addParams("username", LYLocalDataBase.USERNAME);
			requestMessage.addParams("password", LYLocalDataBase.PASSWORD);
			ResponseMessage rm = JSON.parseObject(sendToServer(requestMessage),
					ResponseMessage.class);
			if (rm.getResult() == LYCode.RESULT_OK) {// 成功
				callBack.onSuccess();
			} else {// 失败
				callBack.onError(0, rm.getDesc());
			}
		}
	}

	/**
	 * 执行此方法之前，必须要先调用chatManager方法
	 * 
	 * @param message
	 *            LYMessage
	 * @throws IOException
	 */
	public synchronized void sendMessage(LYMessage message) throws IOException {
		// 检查监听器
		if (msgListener == null)
			throw new NullPointerException("没有设置消息监听");
		// 发送消息逻辑
		if (message.getMsgType() == LYCode.MESSAGE_TYPE_FILE) {
			final String filePath = message.getFilePath();
			final String chatName = message.getChatUsername();
			final String toChatUsername = message.getToChatUsername();
			new Thread(new Runnable() {

				@Override
				public void run() {
					// 开启文件传输服务
					FileUploadClient fileUploadClient = new FileUploadClient();
					fileUploadClient.start();
					fileUploadClient.sendFile(new File(filePath), chatName,
							toChatUsername);
				}
			}).start();
		} else {
			RequestMessage rm = new RequestMessage();
			rm.setAction(LYCode.ACTION_SEND_LYMESSAGE);
			rm.addParams(LYCode.PARAM_LYMESSAGE_DATA,
					JSON.toJSONString(message));
			ResponseMessage responseMessage = JSON.parseObject(
					sendToServer(rm), ResponseMessage.class);
			if (responseMessage.getResult() == LYCode.RESULT_OK) {
				msgListener.onMessageDelivered(null);// 收到已送达回执
			} else {
				msgListener.onMessageError(responseMessage.getDesc());
			}
		}
	}

	/**
	 * 添加消息接收监听
	 * 
	 * @param msgListener
	 *            LYMessageListener
	 */
	public synchronized void addMessageListener(LYMessageListener msgListener) {
		// 新开一个线程，不断读取服务器反馈的数据
		this.msgListener = msgListener;
		new Thread(new Runnable() {// 逻辑上有问题

					@Override
					public void run() {
						byte[] recvBuf = new byte[LYConfigs.UDP_PACKAGE_SIZE];
						DatagramPacket recvPacket = new DatagramPacket(recvBuf,
								recvBuf.length);
						String result;
						while (true) {
							try {
								client.receive(recvPacket);
								result = new String(recvPacket.getData(), 0,
										recvPacket.getLength());
								RequestMessage rm = JSON.parseObject(result,
										RequestMessage.class);
								if (LYCode.ACTION_SEND_LYMESSAGE.equals(rm
										.getAction())) {// 收到LYMESSAGE
									ArrayList<LYMessage> lists = new ArrayList<>();
									lists.add(JSON.parseObject(rm.getParams()
											.get(LYCode.PARAM_LYMESSAGE_DATA),
											LYMessage.class));
									LYClient.this.msgListener
											.onMessageReceived(lists);
								} else if (LYCode.ACTION_NEW_FILE.equals(rm
										.getAction())) {// 收到File
									String toChatUsername = rm.getParams().get(
											"toChatUsername");
									String fileName = rm.getParams().get(
											"fileName");
									FileDownloadClient fileDownloadClient = new FileDownloadClient();
									fileDownloadClient.start();
									DataInputStream dis = fileDownloadClient
											.getDataInputStream(toChatUsername,
													fileName);
									LYClient.this.msgListener.onFileReceived(
											fileName, dis);
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}).start();
	}

	/**
	 * 初始化本地会话管理器
	 * 
	 * @throws SocketException
	 */
	public synchronized LYClient chatManager() throws SocketException {
		// 初始化逻辑
		if (LYLocalDataBase.USERNAME == null)
			throw new NullPointerException("请先登录");
		return StaticClass.ly;
	}

	/**
	 * 下载本地会话
	 */
	public synchronized void loadAllConversations() {
		RequestMessage rm = new RequestMessage();
		rm.setAction(LYCode.ACTION_GET_ALL_MSG);
		rm.addParams("username", LYLocalDataBase.USERNAME);
		ResponseMessage responseMessage = JSON.parseObject(sendToServer(rm),
				ResponseMessage.class);
		if (responseMessage.getResult() == LYCode.RESULT_OK) {
			System.out.println("获取所有离线消息成功");
			ArrayList array = JSON.parseObject(responseMessage.getData(),
					ArrayList.class);
			for (int i = 0; i < array.size(); i++) {
				System.out.println(array.get(i).toString());
			}
		} else {
			System.out.println("下载本地会话：" + responseMessage.getDesc());
		}
	}

	/**
	 * 初始化群组管理器
	 */
	public synchronized LYClient groupManager() {
		// 初始化逻辑
		return StaticClass.ly;
	}

	/**
	 * 下载所有群组
	 */
	public synchronized void loadAllGroups() {

	}

	//测试方法
	public static void main(String[] args) {
		try {
			LYClient.getInstance().login("1943815081", "zz,520.",
					new LYCallBack() {

						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							System.out.println("登录成功");

						}

						@Override
						public void onProgress(int progress, String status) {
							// TODO Auto-generated method stub
							System.out.println("progress：" + progress);
							System.out.println("status：" + status);
						}

						@Override
						public void onError(int code, String message) {
							// TODO Auto-generated method stub
							System.out.println("code：" + code);
							System.out.println("message：" + message);
						}
					});
			Thread.sleep(3000);
			LYClient.getInstance().addMessageListener(new LYMessageListener() {

				@Override
				public void onMessageReceived(List<LYMessage> messages) {
					// TODO Auto-generated method stub
					System.out.println("onMessageReceived");
				}

				@Override
				public void onMessageRead(List<LYMessage> messages) {
					// TODO Auto-generated method stub
					System.out.println("onMessageRead");
				}

				@Override
				public void onMessageError(String desc) {
					// TODO Auto-generated method stub
					System.out.println("onMessageError：" + desc);
				}

				@Override
				public void onMessageDelivered(List<LYMessage> message) {
					// TODO Auto-generated method stub
					System.out.println("onMessageDelivered");
				}

				@Override
				public void onMessageChanged(LYMessage message, Object change) {
					// TODO Auto-generated method stub
					System.out.println("onMessageChanged");
				}

				@Override
				public void onFileReceived(String fileName, DataInputStream bis) {
					// TODO Auto-generated method stub

				}

			});
			LYMessage msg = LYMessage.createFileSendMessage(
					"c:/深入分析Java  Web技术内幕.pdf", "123456");
			LYClient.getInstance().chatManager().sendMessage(msg);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
