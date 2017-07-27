package com.mxzf.liyun.dao.impl;

import com.mxzf.liyun.dao.BaseModule;

/**
 * 
 * @Title: ChatModule
 * @Dscription: 聊天模块
 * @author Deleter
 * @date 2017年5月18日 下午3:59:14
 * @version 1.0
 */
public class ChatModule implements BaseModule {
    public ChatModule() {
	System.out.println("聊天模块");
    }

    @Override
    public boolean canHandleElement() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public void handleELement() {
	// TODO Auto-generated method stub
	System.out.println("发送聊天信息");
    }

}
