package com.mxzf.liyun;

import com.mxzf.liyun.domain.Message;
import com.mxzf.liyun.util.JsonUtils;

public class UdpJsonHandler extends IoHandler
{
    @Override
    public void onMessage(String str)
    {
        if (str != null)
        {
            String action = JsonUtils.decodeAction(str);
            Message obj = JsonUtils.decodeObj(str, Message.class);
            System.out.println("TCP客户端：适配器：Action：" + action);
            System.out.println("TCP客户端：适配器：Obj：" + obj.toString());
        }
        else
        {
            System.out.println("TCP客户端：适配器：null");
        }
    }
}
