package com.mxzf.liyun.domain;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * 
 * @Title: Message
 * @Dscription: 信息载体
 * @author Deleter
 * @date 2017年5月1日 下午1:29:46
 * @version 1.0
 */
public class Message implements Serializable
{
    
    private String startClient;
    
    private String targetClient;
    
    private String type;
    
    private String msg;
    
    private ArrayList<File> files;
    
    private File voice;
    
    private Date createTime;
    
    public String getStartClient()
    {
        return startClient;
    }
    
    public void setStartClient(String startClient)
    {
        this.startClient = startClient;
    }
    
    public String getTargetClient()
    {
        return targetClient;
    }
    
    public void setTargetClient(String targetClient)
    {
        this.targetClient = targetClient;
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
    
    public String getMsg()
    {
        return msg;
    }
    
    public void setMsg(String msg)
    {
        this.msg = msg;
    }
    
    public ArrayList<File> getFiles()
    {
        return files;
    }
    
    public void setFiles(ArrayList<File> files)
    {
        this.files = files;
    }
    
    public File getVoice()
    {
        return voice;
    }
    
    public void setVoice(File voice)
    {
        this.voice = voice;
    }
    
    public Date getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }
    
    public Message()
    {
    }
    
    public Message(String startClient, String targetClient, String type, String msg, Date createTime)
    {
        this.startClient = startClient;
        this.targetClient = targetClient;
        this.type = type;
        this.msg = msg;
        this.createTime = createTime;
    }
    
    public Message(String startClient, String targetClient, String type, ArrayList<File> files, Date createTime)
    {
        this.startClient = startClient;
        this.targetClient = targetClient;
        this.type = type;
        this.files = files;
        this.createTime = createTime;
    }
    
    public Message(String startClient, String targetClient, String type, File voice, Date createTime)
    {
        super();
        this.startClient = startClient;
        this.targetClient = targetClient;
        this.type = type;
        this.voice = voice;
        this.createTime = createTime;
    }
}
