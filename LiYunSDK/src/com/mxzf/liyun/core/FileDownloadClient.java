package com.mxzf.liyun.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.mxzf.liyun.commend.LYConfigs;

public class FileDownloadClient extends Thread {

	private Socket socket;

	private DataOutputStream dos;
	private DataInputStream dis;

	private boolean isConnected = false;

	@Override
	public void run() {
		try {
			socket = new Socket(LYConfigs.TCP_SERVER_ADDR,
					LYConfigs.TCP_DOWNLOAD_SERVER_PORT);
			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());
			isConnected = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public DataInputStream getDataInputStream(String toChatUsername,
			String fileName) {
		String line;
		try {
			dos.writeUTF(toChatUsername);
			dos.writeUTF(fileName);
			dos.flush();
			while ((line = dis.readUTF()) != null) {
				return dis;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
