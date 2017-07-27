package com.mxzf.liyun.commend;

/**
 * @Title: Configs
 * @Dscription: 常量池
 * @author Deleter
 * @date 2017年5月21日 上午2:16:08
 * @version 1.0
 */
public class LYCode {
	// paramName
	public static final String PARAM_LYMESSAGE_DATA = "msgData";

	// responseCode
	public static final int RESULT_OK = 1;
	public static final int RESULT_ERROR = 0;

	// responseDesc
	public static final String DESC_REG_OK = "注册成功";
	public static final String DESC_REG_ERROR = "注册失败";
	public static final String DESC_USERNAME_EXIST = "用户名已存在，请更换用户名重试";
	public static final String DESC_LOGIN_OK = "登录成功";
	public static final String DESC_LOGIN_ERROR = "登录失败";
	public static final String DESC_USERNAME_UNEXIST = "用户名不存在，请检查后重试";
	public static final String DESC_PASSWORD_ERROR = "密码错误，请重新输入";
	public static final String DESC_USER_UNLINE = "对方不在线，待对方上线后系统会自动通知";

	public static final String DESC_ACTION_OK = "操作成功";
	public static final String DESC_ACTION_ERROR = "操作失败";

	// chatType
	public static final int CHAT_TYPE_SINGLE = 0;
	public static final int CHAT_TYPE_ALL = 1;

	// msgType
	public static final int MESSAGE_TYPE_TXT = 0x00001;
	public static final int MESSAGE_TYPE_VOICE = 0x00002;
	public static final int MESSAGE_TYPE_VIDEO = 0x00003;
	public static final int MESSAGE_TYPE_PIC = 0x00004;
	public static final int MESSAGE_TYPE_LOCAL = 0x00005;
	public static final int MESSAGE_TYPE_FILE = 0x00006;

	// action
	public static final String ACTION_REQUEST = "0x00001";// 请求
	public static final String ACTION_SERVER_RESPONSE = "0x00002";// 服务器响应
	public static final String ACTION_CLIENT_RESPONSE = "0x00003";// 客户端响应

	// detailAction
	public static final String ACTION_CONN = "0x00004";// 连接
	public static final String ACTION_CONN_RESPONSE = "0x00005";
	public static final String ACTION_REG = "0x00006";// 注册
	public static final String ACTION_REG_RESPONSE = "0x00007";
	public static final String ACTION_LOGIN = "0x00008";// 登录
	public static final String ACTION_LOGIN_RESPONSE = "0x00009";
	public static final String ACTION_SEND_MSG = "0x00010";// 发送文本
	public static final String ACTION_SEND_MSG_RESPONSE = "0x00011";
	public static final String ACTION_SEND_VOICE = "0x00012";// 发送语音
	public static final String ACTION_SEND_VOICE_RESPONSE = "0x00013";
	public static final String ACTION_SEND_PIC = "0x00014";// 发送图片
	public static final String ACTION_SEND_PIC_RESPONSE = "0x00015";
	public static final String ACTION_SEND_LOCAL = "0x00016";// 发送地理位置
	public static final String ACTION_SEND_LOCAL_RESPONSE = "0x00017";
	public static final String ACTION_SEND_FILE = "0x00018";// 发送文件
	public static final String ACTION_SEND_FILE_RESPONSE = "0x00019";
	public static final String ACTION_SEND_LYMESSAGE = "0x00020";// 发送LYMESSAGE

	// 离线消息
	public static final String ACTION_GET_ALL_MSG = "0x00021"; // 获取所有离线消息
	public static final String ACTION_GET_ALL_MSG_SIZE = "0x00022";// 获取所有离线消息数量
	public static final String ACTION_GET_MSG_ID = "0x00023";// 根据ID获取离线消息
	// 分组
	public static final String ACTION_GET_ALL_GROUP = "0x00024";// 获取所有分组
	public static final String ACTION_GET_GROUP_ID = "0x00025";// 根据ID获取分组详情
	// 个人
	public static final String ACTION_GET_PERSON_INFO = "0x00026";// 获取个人信息
	public static final String ACTION_GET_PERSON_INFO_ID = "0x00027";// 根据ID获取个人信息

	// 补充
	public static final String ACTION_LOGOUT = "0x00028";// 登出
	public static final String ACTION_LOGOUT_RESPONSE = "0x00029";
	public static final String ACTION_SEND_CONFIG = "0x00030";// 发送透传信息
	public static final String ACTION_SEND_CONFIG_RESPONSE = "0x00031";

	public static final String ACTION_NEW_FILE = "0x00033";// 文件待接收

}
