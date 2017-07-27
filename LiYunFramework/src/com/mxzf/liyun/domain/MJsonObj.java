package com.mxzf.liyun.domain;

import java.io.Serializable;

/**
 * 
 * @Title: MJsonObj
 * @Dscription: 我的jsonobj
 * @author Deleter
 * @date 2017年5月5日 下午2:22:20
 * @version 1.0
 */
public class MJsonObj implements Serializable
{
    private String action;
    
    private String obj;
    
    public String getAction()
    {
        return action;
    }
    
    public void setAction(String action)
    {
        this.action = action;
    }
    
    public String getObj()
    {
        return obj;
    }
    
    public void setObj(String obj)
    {
        this.obj = obj;
    }
    
    @Override
    public String toString()
    {
        return "MJsonObj [action=" + action + ", obj=" + obj + "]";
    }
    
    public MJsonObj(String action, String obj)
    {
        this.action = action;
        this.obj = obj;
    }
    
    public MJsonObj()
    {
    }
}
