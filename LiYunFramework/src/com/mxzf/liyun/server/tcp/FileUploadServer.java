package com.mxzf.liyun.server.tcp;

import java.io.IOException;
import java.net.ServerSocket;

import com.mxzf.liyun.commend.LYConfigs;

public class FileUploadServer extends Thread {

	@Override
	public void run() {
		ServerSocket listen;
		boolean isDone = true;
		try {
			System.out.println("TCP上传服务器启动！监听端口："
					+ LYConfigs.TCP_UPLOAD_SERVER_PORT);
			listen = new ServerSocket(LYConfigs.TCP_UPLOAD_SERVER_PORT);
			while (isDone) {
				// 无限监听
				new TCPServerThread(listen.accept()).start();
				System.out.println("TCP上传服务器：一位用户连接上此服务器");
			}
		} catch (IOException e) {
			e.printStackTrace();
			isDone = false;
		}
	}
}
