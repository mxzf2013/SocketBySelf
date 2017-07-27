package com.mxzf.liyun.server.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import com.mxzf.liyun.commend.LYCloudDataBase;
import com.mxzf.liyun.domain.LYFile;

public class TCPServerThread extends Thread {

	private Socket socket;

	private DataInputStream inputStream;

	private DataOutputStream outputStream;

	private boolean isDone = true;

	public TCPServerThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		String toChatUserName;
		String fileName;
		int len;
		byte[] buff = new byte[1024 * 1024];
		try {
			this.outputStream = new DataOutputStream(socket.getOutputStream());
			this.inputStream = new DataInputStream(socket.getInputStream());
			while (isDone) {
				while ((toChatUserName = inputStream.readUTF()) != null) {
					fileName = inputStream.readUTF();
					ArrayList<LYFile> list = LYCloudDataBase.fileQueue
							.get(toChatUserName);
					for (int i = 0; i < list.size(); i++) {// 查找文件
						if (list.get(i).getFileName().equals(fileName)) {// 找到文件
							FileInputStream fileInputStream = new FileInputStream(
									list.get(i).getFilePath());
							outputStream.writeUTF("find");
							while ((len = inputStream.read(buff)) != -1) {
								outputStream.write(buff, 0, len);
							}
							outputStream.flush();
							outputStream.close();
							fileInputStream.close();
							System.out.println("文件下载完毕");
						}
					}
				}
			}
		} catch (IOException e) {
			isDone = false;
			System.out.println("TCP服务器:客户端断开连接");
		}
	}
}
