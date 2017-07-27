package com.mxzf.liyun.core;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

import com.mxzf.liyun.commend.LYConfigs;

public class FileUploadClient extends Thread {

	private Socket socket;

	private DataOutputStream dos;

	private boolean isConnected = false;

	@Override
	public void run() {
		try {
			socket = new Socket(LYConfigs.TCP_SERVER_ADDR,
					LYConfigs.TCP_UPLOAD_SERVER_PORT);
			dos = new DataOutputStream(socket.getOutputStream());
			isConnected = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送文件到服务器
	 * 
	 * @param file
	 */
	public void sendFile(File file, String chatName, String toChatName) {
		boolean isFinish = false;
		boolean isDone = true;
		while (isDone) {
			while (isConnected) {
				try {
					dos.writeUTF(chatName);
					dos.writeUTF(toChatName);
					dos.writeUTF(file.getName());
					dos.writeLong(file.length());
					int len;
					byte[] buff = new byte[1024 * 1024];
					FileInputStream fileInputStream = new FileInputStream(file);
					while ((len = fileInputStream.read(buff)) != -1) {
						dos.write(buff, 0, len);
					}
					fileInputStream.close();
					dos.flush();
					dos.close();
					isFinish = true;
					isDone = false;
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			if (isFinish) {
				try {
					socket.close();
					socket = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}
}
