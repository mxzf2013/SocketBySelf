package com.mxzf.liyun.core;

import java.io.DataInputStream;
import java.util.List;

import com.mxzf.liyun.domain.LYMessage;

/**
 * 
 * @Title: LYMessageListener
 * @Dscription: 消息接收监听
 * @author Deleter
 * @date 2017年5月21日 上午3:17:38
 * @version 1.0
 */
public interface LYMessageListener {

	/**
	 * 收到消息
	 * 
	 * @param messages
	 */
	public void onMessageReceived(List<LYMessage> messages);

	/**
	 * 收到已读回执
	 * 
	 * @param messages
	 */
	public void onMessageRead(List<LYMessage> messages);

	/**
	 * 收到已送达回执
	 * 
	 * @param message
	 */
	public void onMessageDelivered(List<LYMessage> message);

	/**
	 * 消息状态变动
	 * 
	 * @param message
	 * @param change
	 */
	public void onMessageChanged(LYMessage message, Object change);

	/**
	 * 消息没有送达
	 * 
	 * @param desc
	 */
	public void onMessageError(String desc);

	/**
	 * 当新的文件送达
	 * 
	 * @param bis
	 */
	public void onFileReceived(String fileName, DataInputStream bis);
}
