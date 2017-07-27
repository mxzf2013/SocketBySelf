package com.mxzf.liyun.dao.impl;

import com.mxzf.liyun.dao.BaseModule;

/**
 * 
 * @Title: LoginModule
 * @Dscription: 登录模块
 * @author Deleter
 * @date 2017年5月18日 下午3:59:33
 * @version 1.0
 */
public class LoginModule implements BaseModule {

    public LoginModule() {
	System.out.println("登录模块");
    }

    @Override
    public boolean canHandleElement() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public void handleELement() {
	// TODO Auto-generated method stub
	System.out.println("用户登录");
    }

}
