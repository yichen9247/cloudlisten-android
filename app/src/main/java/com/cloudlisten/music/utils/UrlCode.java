package com.cloudlisten.music.utils;

import java.io.UnsupportedEncodingException;

public class UrlCode {
    private final static String ENCODE = "Utf-8"; 
    /**
     * URL 解码
     */
    public static String Decoder(String str) {
        String result = "";
        if (null == str) {
            return "请输入正确的内容！";
        }
        try {
            result = java.net.URLDecoder.decode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            return "URL转码算法出现异常！";
        }
        return result;
    }
    /**
     * URL 转码
     */
    public static String Encoder(String str) {
        String result = "";
        if (null == str) {
            return "请输入正确的内容！";
        }
        try {
            result = java.net.URLEncoder.encode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            return "URL转码算法出现异常！";
        }
        return result;
    }
}

