package com.mxzf.liyun.dao.impl;

import com.mxzf.liyun.dao.BaseModule;

/**
 * 
 * @Title: GroupModule
 * @Dscription: 群组模块
 * @author Deleter
 * @date 2017年5月18日 下午4:02:06
 * @version 1.0
 */
public class GroupModule implements BaseModule {
    public GroupModule() {
	System.out.println("群组模块");
    }

    @Override
    public boolean canHandleElement() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public void handleELement() {
	// TODO Auto-generated method stub
	System.out.println("获取群组信息");
    }

}
