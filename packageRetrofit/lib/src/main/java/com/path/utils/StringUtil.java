package com.path.utils;

/**
 * Created by win10 on 2016/6/19.
 */
public class StringUtil {
    /**
     * 字符串拼接  线程安全
     * @param
     * @return
     */
    public static String buffer(String... array){
        StringBuffer s = new StringBuffer();
        for (String str:array) {
            s.append(str);
        }
        return s.toString();
    }
    /**
     * 字符串拼接 线程不安全 效率高
     * @param
     * @return
     */
    public static String builder(String... array){
        StringBuilder s = new StringBuilder();
        for (String str: array) {
            s.append(str);
        }
        return s.toString();
    }

}
