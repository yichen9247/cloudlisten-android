package com.cloudlisten.music.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * 一个集合各种加密方式的算法类
 
 * @author Mr.Hua 花落人断肠
 */

public class EnCode {
    
    //MD5加密算法
    public static String MD5(String text) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(text.getBytes());
            return new BigInteger(1, md5.digest()).toString(16);
        } catch (Exception e) {
            return "MD5加密算法出现异常！" + "\n" + e;
        }
    }
    
    //SHA加密算法
    public static String SHA(String text) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            sha.update(text.getBytes());
            return new BigInteger(1, sha.digest()).toString(256);
        } catch (Exception e) {
            return "SHA加密算法出现异常！" + "\n" + e;
        }
    }
    
    //Base64加密算法
    public static String Base64Jia(String text) {
        try {         
            final Base64.Encoder encoder = Base64.getEncoder();
            final byte[] textbyte = text.getBytes("UTF-8");
            final String encoderText = encoder.encodeToString(textbyte);
            return encoderText;
        } catch (Exception e) {
            return "Base64加密算法出现异常！" + "\n" + e;
        }
    }
    
    //Base64解密算法
    public static String Base64Jie(String text) {
        try {
            final Base64.Decoder decoder = Base64.getDecoder();           
            return new String(decoder.decode(text),"UTF-8");
        } catch (Exception e) {
            return "Base64解密算法出现异常！" + "\n" + e;
        }
    }
}
