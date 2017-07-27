package com.mxzf.liyun.dao.impl;

import com.mxzf.liyun.dao.BaseModule;

/**
 * 
 * @Title: RosterModule
 * @Dscription: 花名册模块（路由模块）
 * @author Deleter
 * @date 2017年5月18日 下午4:01:53
 * @version 1.0
 */
public class RosterModule implements BaseModule {
    public RosterModule() {
	System.out.println("花名册模块（路由模块）");
    }

    @Override
    public boolean canHandleElement() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public void handleELement() {
	// TODO Auto-generated method stub
	System.out.println("获取路由");
    }

}
