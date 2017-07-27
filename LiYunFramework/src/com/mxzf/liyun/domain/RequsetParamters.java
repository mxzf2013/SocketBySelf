package com.mxzf.liyun.domain;

import java.io.Serializable;

/**
 * 
 * @Title: RequsetParamters
 * @Dscription: 请求参数
 * @author Deleter
 * @date 2017年5月5日 下午1:48:00
 * @version 1.0
 */
public class RequsetParamters implements Serializable
{
    private String action;
    
    private String result;
    
    private String data;
    
    public String getAction()
    {
        return action;
    }
    
    public void setAction(String action)
    {
        this.action = action;
    }
    
    public String getResult()
    {
        return result;
    }
    
    public void setResult(String result)
    {
        this.result = result;
    }
    
    public String getData()
    {
        return data;
    }
    
    public void setData(String data)
    {
        this.data = data;
    }
    
    @Override
    public String toString()
    {
        return "RequsetParamters [action=" + action + ", result=" + result + ", data=" + data + "]";
    }
    
    public RequsetParamters(String action, String result, String data)
    {
        this.action = action;
        this.result = result;
        this.data = data;
    }
    
    public RequsetParamters()
    {
    }
}
