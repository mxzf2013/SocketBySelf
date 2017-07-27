package com.mxzf.liyun.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JsonUtils
{
    /**
     * 封装数据
     * 
     * @param action 标志
     * @param t 数据
     * @return
     */
    public static String encode(String action, Object object)
    {
        JSON json = new JSONObject();
        ((JSONObject)json).put("action", DesUtils.encrypt(action));
        ((JSONObject)json).put("obj", DesUtils.encrypt(JSON.toJSONString(object)));
        return json.toString();
    }
    
    /**
     * 解码标志
     * 
     * @param encodeData 被编码数据
     * @return
     */
    public static String decodeAction(String encodeData)
    {
        return DesUtils.decrypt(JSON.parseObject(encodeData).getString("action"));
    }
    
    /**
     * 解码对象
     * 
     * @param encodeData 被编码数据
     * @param t 类型
     * @return
     */
    public static <T> T decodeObj(String encodeData, Class<T> clazz)
    {
        String encodeLine = JSON.parseObject(encodeData).getString("obj");// 解密字串
        String decodeObj = DesUtils.decrypt(encodeLine);
        return JSON.parseObject(decodeObj, clazz);
    }
}
