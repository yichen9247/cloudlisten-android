package com.cloudlisten.music.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
public class DeviceUtils {

    /**
     * 获取可用内存和总内存
     *
     * @param context
     * @return
     */
	public static String getAvailMeTotalMemoryInfo() {
        // 获取活动管理器
        ActivityManager am = (ActivityManager)(application.context.getSystemService(Context.ACTIVITY_SERVICE));
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        // 参1 内存信息
        am.getMemoryInfo(outInfo);
        return getPrintSize(outInfo.availMem) + " 可用 | " + getPrintSize(outInfo.totalMem);
    }

    public static String getPrintSize(long size) {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if(size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1000;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if(size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if(size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "GB";
        }
    }

    public static String getPhone() {
        String phone = Build.MODEL;
        return phone;
    }

}

