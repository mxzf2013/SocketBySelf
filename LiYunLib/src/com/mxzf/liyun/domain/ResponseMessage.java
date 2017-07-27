package com.mxzf.liyun.domain;

import java.io.Serializable;

public class ResponseMessage implements Serializable {

    private int result;
    private String desc;
    private String data;

    public int getResult() {
	return result;
    }

    public void setResult(int result) {
	this.result = result;
    }

    public String getDesc() {
	return desc;
    }

    public void setDesc(String desc) {
	this.desc = desc;
    }

    public String getData() {
	return data;
    }

    public void setData(String data) {
	this.data = data;
    }

    @Override
    public String toString() {
	return "ResponseMessage [result=" + result + ", desc=" + desc
		+ ", data=" + data + "]";
    }
}
