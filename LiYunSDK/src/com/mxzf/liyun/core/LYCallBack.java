package com.mxzf.liyun.core;

/**
 * 
 * @Title: LYCallBack
 * @Dscription: 回调接口
 * @author Deleter
 * @date 2017年5月21日 上午1:46:43
 * @version 1.0
 */
public interface LYCallBack {
    public void onSuccess();

    public void onProgress(int progress, String status);

    public void onError(int code, String message);
}
