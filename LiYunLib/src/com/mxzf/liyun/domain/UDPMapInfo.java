package com.mxzf.liyun.domain;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * 
 * @Title: MapInfo
 * @Dscription: 客户端网关信息
 * @author Deleter
 * @date 2017年4月27日 上午1:42:41
 * @version 1.0
 */
public class UDPMapInfo implements Serializable
{
    
    private String name;
    
    private InetAddress address;
    
    private int port;
    
    public InetAddress getAddress()
    {
        return address;
    }
    
    public void setAddress(InetAddress address)
    {
        this.address = address;
    }
    
    public int getPort()
    {
        return port;
    }
    
    public void setPort(int port)
    {
        this.port = port;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + port;
        return result;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UDPMapInfo other = (UDPMapInfo)obj;
        if (address == null)
        {
            if (other.address != null)
                return false;
        }
        else if (!address.equals(other.address))
            return false;
        if (name == null)
        {
            if (other.name != null)
                return false;
        }
        else if (!name.equals(other.name))
            return false;
        if (port != other.port)
            return false;
        return true;
    }
    
    @Override
    public String toString()
    {
        return "UDPMapInfo [name=" + name + ", address=" + address + ", port=" + port + "]";
    }
    
    public UDPMapInfo(String name, InetAddress address, int port)
    {
        this.name = name;
        this.address = address;
        this.port = port;
    }
    
    public UDPMapInfo()
    {
    }
}
