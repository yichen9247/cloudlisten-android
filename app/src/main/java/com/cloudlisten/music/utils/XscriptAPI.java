package com.cloudlisten.music.utils;

import android.icu.text.SimpleDateFormat;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class XscriptAPI {
    
    public static String GetTimes(String msg)
    {
        SimpleDateFormat formatter= new SimpleDateFormat(msg);
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }
    
    private final static String ENCODE = "Utf-8"; 
    /**
     * URL 解码
     */
    public static String Decoder(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * URL 转码
     */
    public static String Encoder(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
