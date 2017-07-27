package com.mxzf.liyun;



public class TcpJsonHandler extends IoHandler
{
    @Override
    public void onMessage(String str)
    {
        System.out.println("TCP客户端：适配器：" + str);
    }
}
