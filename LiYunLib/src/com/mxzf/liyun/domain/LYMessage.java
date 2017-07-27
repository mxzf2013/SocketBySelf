package com.mxzf.liyun.domain;

import java.io.Serializable;

import com.mxzf.liyun.commend.LYCode;
import com.mxzf.liyun.commend.LYLocalDataBase;
import com.mxzf.liyun.util.Base64Util;
import com.mxzf.liyun.util.ImageUtil;

public class LYMessage implements Serializable {

	private String content;
	private String filePath;
	private String videoThumbContent;
	private String chatUsername;
	private String toChatUsername;
	private int length;
	private int chatType = 0;// 0、单聊 1、群聊
	private int msgType;// 消息类型 0、文本 1、语音 2、图片 3、地理位置 4、文件消息

	// 地理位置
	private double latitude;
	private double longitude;
	private String locationAddress;

	public LYMessage() {
	}

	/**
	 * 创建一条文本信息(默认单聊)
	 * 
	 * @param content
	 *            消息文字内容
	 * @param toChatUsername
	 *            对方用户或者群聊ID
	 * @return LYMessage
	 */
	public static LYMessage createTxtSendMessage(String content,
			String toChatUsername) {
		LYMessage message = new LYMessage();
		message.setContent(content);
		message.setChatType(LYCode.CHAT_TYPE_SINGLE);
		message.setMsgType(LYCode.MESSAGE_TYPE_TXT);
		message.setChatUsername(LYLocalDataBase.USERNAME);
		message.setToChatUsername(toChatUsername);
		return message;
	}

	/**
	 * 发送语音消息
	 * 
	 * @param filePath
	 *            语音文件路径
	 * @param length
	 *            录音时间（秒）
	 * @param toChatUsername
	 *            对方用户或者群聊ID
	 * @return LYMessage
	 */
	public static LYMessage createVoiceSendMessage(String filePath, int length,
			String toChatUsername) {
		LYMessage message = new LYMessage();
		message.setContent(Base64Util.getContent(filePath));
		message.setLength(length);
		message.setChatType(LYCode.CHAT_TYPE_SINGLE);
		message.setMsgType(LYCode.MESSAGE_TYPE_VOICE);
		message.setChatUsername(LYLocalDataBase.USERNAME);
		message.setToChatUsername(toChatUsername);
		return message;
	}

	/**
	 * 发送视频消息
	 * 
	 * @param videoPath
	 *            视频本地路径
	 * @param thumbPath
	 *            视频预览图路径
	 * @param videoLength
	 *            视频时间长度（秒）
	 * @param toChatUsername
	 *            对方用户或者群聊ID
	 * @return LYMessage
	 */
	public static LYMessage createVideoSendMessage(String videoPath,
			String thumbPath, int videoLength, String toChatUsername) {
		LYMessage message = new LYMessage();
		message.setContent(Base64Util.getContent(videoPath));
		message.setVideoThumbContent(Base64Util.getContent(thumbPath));
		message.setLength(videoLength);
		message.setChatType(LYCode.CHAT_TYPE_SINGLE);
		message.setMsgType(LYCode.MESSAGE_TYPE_VIDEO);
		message.setChatUsername(LYLocalDataBase.USERNAME);
		message.setToChatUsername(toChatUsername);
		return message;
	}

	/**
	 * 
	 * @param imagePath
	 *            图片本地路径
	 * @param isOrign
	 *            false为不发送原图（默认超过100k的图片会压缩后发给对方）
	 * @param toChatUsername
	 *            对方用户或者群聊ID
	 * @return LYMessage
	 */
	public static LYMessage createImageSendMessage(String imagePath,
			boolean needOrign, String toChatUsername) {
		LYMessage message = new LYMessage();
		if (needOrign) {
			message.setContent(Base64Util.getContent(imagePath));
		} else {
			message.setContent(Base64Util.getContent(ImageUtil
					.toSmall(imagePath)));
		}
		message.setChatType(LYCode.CHAT_TYPE_SINGLE);
		message.setMsgType(LYCode.MESSAGE_TYPE_PIC);
		message.setChatUsername(LYLocalDataBase.USERNAME);
		message.setToChatUsername(toChatUsername);
		return message;
	}

	/**
	 * 发送地理位置
	 * 
	 * @param latitude
	 *            经度
	 * @param longitude
	 *            纬度
	 * @param locationAddress
	 *            具体位置内容
	 * @param toChatUsername
	 *            对方用户或者群聊ID
	 * @return LYMessage
	 */
	public static LYMessage createLocationSendMessage(double latitude,
			double longitude, String locationAddress, String toChatUsername) {
		LYMessage message = new LYMessage();
		message.setLatitude(latitude);
		message.setLongitude(longitude);
		message.setLocationAddress(locationAddress);
		message.setChatType(LYCode.CHAT_TYPE_SINGLE);
		message.setMsgType(LYCode.MESSAGE_TYPE_PIC);
		message.setChatUsername(LYLocalDataBase.USERNAME);
		message.setToChatUsername(toChatUsername);
		return message;
	}

	/**
	 * 发送文件消息
	 * 
	 * @param filePath
	 *            文件所在的路径
	 * @param toChatUsername
	 *            对方用户或者群聊ID
	 * @return LYMessage
	 */
	public static LYMessage createFileSendMessage(String filePath,
			String toChatUsername) {
		LYMessage message = new LYMessage();
		message.setFilePath(filePath);
		message.setChatUsername(LYLocalDataBase.USERNAME);
		message.setToChatUsername(toChatUsername);
		message.setChatType(LYCode.CHAT_TYPE_SINGLE);
		message.setMsgType(LYCode.MESSAGE_TYPE_FILE);
		return message;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getToChatUsername() {
		return toChatUsername;
	}

	public void setToChatUsername(String toChatUsername) {
		this.toChatUsername = toChatUsername;
	}

	public int getChatType() {
		return chatType;
	}

	public void setChatType(int chatType) {
		this.chatType = chatType;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public String getVideoThumbContent() {
		return videoThumbContent;
	}

	public void setVideoThumbContent(String videoThumbContent) {
		this.videoThumbContent = videoThumbContent;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getLocationAddress() {
		return locationAddress;
	}

	public void setLocationAddress(String locationAddress) {
		this.locationAddress = locationAddress;
	}

	public String getChatUsername() {
		return chatUsername;
	}

	public void setChatUsername(String chatUsername) {
		this.chatUsername = chatUsername;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
