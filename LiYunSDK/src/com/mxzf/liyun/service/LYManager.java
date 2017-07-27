package com.mxzf.liyun.service;

import java.util.HashMap;

import com.mxzf.liyun.dao.BaseModule;
import com.mxzf.liyun.dao.impl.ChatModule;
import com.mxzf.liyun.dao.impl.GroupModule;
import com.mxzf.liyun.dao.impl.LoginModule;
import com.mxzf.liyun.dao.impl.RosterModule;

/**
 * 提供登录、消息发送和消息接收的接口
 * 
 * @Title: LYManager
 * @Dscription: 管理类
 * @author Deleter
 * @date 2017年5月18日 下午3:58:40
 * @version 1.0
 */
public class LYManager {

    private static HashMap<String, String> modules = new HashMap<>();
    private static LoginModule loginModule = null;
    private static RosterModule rosterModule = null;
    private static GroupModule groupModule = null;
    private static ChatModule chatModule = null;

    static {
	new Thread(new Runnable() {
	    @Override
	    public void run() {
		// 监听消息
		// 遍历上层模块
		// 调用canHandleElement进行判断
		// 如果为true,那么继续调用handleELement进行消息分发
	    }
	}).start();
    }

    /**
     * 通过className获取模块
     * 
     * @param className
     *            类名
     * @return BaseModule
     */
    public synchronized BaseModule moduleForClassName(String className) {
	BaseModule baseModule = null;
	if ("LoginModule".equals(className)) {
	    baseModule = loginModule = ((loginModule == null) ? new LoginModule()
		    : loginModule);
	} else if ("RosterModule".equals(className)) {
	    baseModule = rosterModule = ((rosterModule == null) ? new RosterModule()
		    : rosterModule);
	} else if ("GroupModule".equals(className)) {
	    baseModule = groupModule = ((groupModule == null) ? new GroupModule()
		    : groupModule);
	} else if ("ChatModule".equals(className)) {
	    baseModule = chatModule = ((chatModule == null) ? new ChatModule()
		    : chatModule);
	} else {
	    // 默认登录模块
	    baseModule = loginModule = ((loginModule == null) ? new LoginModule()
		    : loginModule);
	}
	return baseModule;
    }

    /**
     * 注册子模块
     * 
     * @param superModule
     *            顶级模块
     * @param subModule
     *            子模块
     */
    public synchronized void registerSubModuleWithClassName(String superModule,
	    String subModule) {
	modules.put(superModule, subModule);
    }

    public static void main(String[] args) {
	LYManager manager = new LYManager();
	manager.moduleForClassName("LoginModule").handleELement();
	manager.moduleForClassName("RosterModule").handleELement();
	manager.moduleForClassName("GroupModule").handleELement();
	manager.moduleForClassName("ChatModule").handleELement();
	manager.moduleForClassName("").handleELement();
    }
}
