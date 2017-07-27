package com.mxzf.liyun.util;

public class StringUtil {
    /**
     * 判断是否空串
     * 
     * @param strings
     * @return
     */
    public static boolean isEmpty(String... strings) {
	boolean result = false;
	for (int index = 0; index < strings.length; index++) {
	    if (strings[index] != null && !strings[index].trim().equals(""))
		result = true;
	    else {
		result = false;
	    }
	}
	return result;
    }
}
