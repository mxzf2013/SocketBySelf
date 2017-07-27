package com.mxzf.liyun.client.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import com.mxzf.liyun.IoHandler;
import com.mxzf.liyun.commend.LYConfigs;
import com.mxzf.liyun.util.JsonUtils;

public class TCPClient {
	private Socket socket;

	private ReadThread readThread;

	private DataOutputStream outputStream;

	private IoHandler proxyIo;

	private boolean isDone = true;

	/**
	 * 设置适配器
	 * 
	 * @param proxyIo
	 */
	public void setIoHandler(IoHandler proxyIo) {
		this.proxyIo = proxyIo;
	}

	/**
	 * 连接服务器
	 * 
	 * @throws IOException
	 */
	private void reconnect() throws IOException {
		if (socket != null) {
			socket.close();
			socket = null;
		}
		socket = new Socket(LYConfigs.TCP_SERVER_ADDR,
				LYConfigs.TCP_UPLOAD_SERVER_PORT);
		readThread = new ReadThread(socket.getInputStream()); // 读
		readThread.start();
		outputStream = new DataOutputStream(socket.getOutputStream());
	}

	public int connect() {
		int tcpServerResp = 404;
		try {
			reconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tcpServerResp;
	}

	/**
	 * 发送到服务器
	 * 
	 * @param action
	 * @param obj
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void sendToServer(String action, Object obj) throws IOException,
			InterruptedException {
		boolean isSend = false;
		int retryCount = 0;
		while (retryCount < 3)// 最多重试三次
		{
			if (socket.isConnected() && !socket.isOutputShutdown()) {
				outputStream.writeUTF(JsonUtils.encode(action, obj));
				outputStream.flush();
				isSend = true;
				System.out.println("send!");
			}
			if (isSend)
				break;
			System.out.println("TCP客户端：正在重试!");
			Thread.sleep(500);
			retryCount++;
		}
	}

	public void close() throws IOException {
		if (socket != null)
			socket.close();
	}

	public ReadThread getReadThread() {
		return readThread;
	}

	public void setReadThread(ReadThread readThread) {
		this.readThread = readThread;
	}

	/**
	 * 
	 * @Title: ReadThread
	 * @Dscription: 读线程
	 * @author Deleter
	 * @date 2017年4月30日 上午1:30:08
	 * @version 1.0
	 */
	public class ReadThread extends Thread {
		DataInputStream inputStream;

		public ReadThread(InputStream is) {
			this.inputStream = new DataInputStream(is);
		}

		@Override
		public void run() {

			String line;
			while (isDone) {
				try {
					while ((line = inputStream.readUTF()) != null) {
						proxyIo.onMessage(line);
					}
				} catch (IOException e) {
					isDone = false;
					System.out.println("TCP客户端：服务器无响应");
				}
			}
		}
	}
}
