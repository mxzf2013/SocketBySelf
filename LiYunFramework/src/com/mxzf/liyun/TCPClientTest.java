package com.mxzf.liyun;

import com.mxzf.liyun.client.tcp.TCPClient;
import com.mxzf.liyun.domain.User;

public class TCPClientTest {
    public static void main(String[] args) {
	TCPClient tcp = new TCPClient();
	tcp.setIoHandler(new TcpJsonHandler());
	tcp.connect();
	try {
	    tcp.sendToServer("login", new User("A", "123"));
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
