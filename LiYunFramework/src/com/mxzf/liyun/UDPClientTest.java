package com.mxzf.liyun;

import java.io.IOException;

import com.mxzf.liyun.client.udp.UDPClient;
import com.mxzf.liyun.domain.User;

public class UDPClientTest {
    public static void main(String[] args) throws Exception {
	final UDPClient udpA = new UDPClient();
	final UDPClient udpB = new UDPClient();

	udpA.setIoHandler(new UdpJsonHandler());
	udpB.setIoHandler(new UdpJsonHandler());

	try {
	    udpA.connect();
	    udpB.connect();

	    udpA.login(new User("A", "123"));
	    udpB.login(new User("B", "123"));
	    new Thread(new Runnable() {
		@Override
		public void run() {
		    try {
			udpA.getClientUdpMapInfo("B");
		    } catch (IOException | InterruptedException e) {
			e.printStackTrace();
		    }
		}
	    }).start();
	    new Thread(new Runnable() {

		@Override
		public void run() {
		    try {
			udpB.getClientUdpMapInfo("A");
		    } catch (IOException | InterruptedException e) {
			e.printStackTrace();
		    }
		}
	    }).start();
	} catch (IOException e1) {
	    e1.printStackTrace();
	} catch (InterruptedException e1) {
	    e1.printStackTrace();
	}

	Thread.sleep(2000);

	new Thread(new Runnable() {

	    @Override
	    public void run() {
		try {
		    udpA.sendMessageToClient("B", "Hello World! I'm A Client!");
		} catch (IOException | InterruptedException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	}).start();
	new Thread(new Runnable() {

	    @Override
	    public void run() {
		try {
		    udpB.sendMessageToClient("A", "Hello World! I'm B Client!");
		} catch (IOException | InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	}).start();
    }
}
