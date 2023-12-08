package com.cloudlisten.music;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.cloudlisten.music.utils.AppConfig;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;

/**
 * 该类是工具类，利用其进行可如下功能.
 * 1.读写、移动、复制、删除文件等
 * 2.取得随机数、取得斐波那契数列某一项的值等
 * 3.画图Bitmap相关
 * 4.时间数字转为字符串
 */

class Toolutils {

    Toolutils() {
    }


    public static String 取中间(String text, String left, String right) {
        String result = "";
        int zLen;
        if (left == null || left.isEmpty()) {
            zLen = 0;
        } else {
            zLen = text.indexOf(left);
            if (zLen > -1) {
                zLen += left.length();
            } else {
                zLen = 0;
            }
        }
        int yLen = text.indexOf(right, zLen);
        if (yLen < 0 || right == null || right.isEmpty()) {
            yLen = text.length();
        }
        result = text.substring(zLen, yLen);
        return result;
    }

    /**
     * 判断SD卡是否存在.
     *
     * @return SD卡存在返回true，否则返回false
     */
    private static boolean sdCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 返回SD卡路径.
     *
     * @return SD卡路径
     */
    static String sdCardPath() {
        File sdDir = null;
        if (sdCardExist()) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        if (sdDir == null) {
            sdDir = new File("sdcard");
        }
        return sdDir.toString();
    }

    static final String rooter_path = new AppConfig().rooter_path;
    static final String ROOT = sdCardPath() + rooter_path;

    static final String DEFAULT_STRING = "null";
    static final int DEFAULT_INT = 0;
    static final long DEFAULT_LONG = 0L;
    static final double DEFAULT_DOUBLE = 0.0;

    /**
     * 读取一个字符串.
     *
     * @param path         路径
     * @param key          关键字
     * @param defaultValue 默认值
     * @return 读取内容存在则返回读到的字符串，否则返回空字符串
     */
    static String getString(String path, String key, String defaultValue) {
        Properties p = new Properties();
        try {
            p.load(new FileInputStream(new File(path)));
        } catch (IOException e) {
            // log exception
        }
        return p.getProperty(key, defaultValue);
    }

    static String getString(String path, String key) {
        return getString(path, key, DEFAULT_STRING);
    }

    static String getString(String path, int key, String defaultValue) {
        return getString(path, key + "", defaultValue);
    }

    static String getString(String path, int key) {
        return getString(path, key, DEFAULT_STRING);
    }

    static String getString(String path, long key, String defaultValue) {
        return getString(path, key + "", defaultValue);
    }

    static String getString(String path, long key) {
        return getString(path, key, DEFAULT_STRING);
    }

    static String getString(String path, double key, String defaultValue) {
        return getString(path, key + "", defaultValue);
    }

    static String getString(String path, double key) {
        return getString(path, key, DEFAULT_STRING);
    }

    static int getInt(String path, String key, int defaultValue) {
        return Integer.parseInt(getString(path, key, defaultValue + ""));
    }

    static int getInt(String path, String key) {
        return getInt(path, key, DEFAULT_INT);
    }

    static int getInt(String path, int key, int defaultValue) {
        return getInt(path, key + "", defaultValue);
    }

    static int getInt(String path, int key) {
        return getInt(path, key + "", DEFAULT_INT);
    }

    static int getInt(String path, long key, int defaultValue) {
        return getInt(path, key + "", defaultValue);
    }

    static int getInt(String path, long key) {
        return getInt(path, key + "", DEFAULT_INT);
    }

    static int getInt(String path, double key, int defaultValue) {
        return getInt(path, key + "", defaultValue);
    }

    static int getInt(String path, double key) {
        return getInt(path, key + "", DEFAULT_INT);
    }

    static long getLong(String path, String key, long defaultValue) {
        return Long.parseLong(getString(path, key, defaultValue + ""));
    }

    static long getLong(String path, String key) {
        return getLong(path, key, DEFAULT_INT);
    }

    static long getLong(String path, int key, int defaultValue) {
        return getLong(path, key + "", defaultValue);
    }

    static long getLong(String path, int key) {
        return getLong(path, key + "", DEFAULT_LONG);
    }

    static long getLong(String path, long key, int defaultValue) {
        return getLong(path, key + "", defaultValue);
    }

    static long getLong(String path, long key) {
        return getLong(path, key + "", DEFAULT_LONG);
    }

    static long getLong(String path, double key, int defaultValue) {
        return getLong(path, key + "", defaultValue);
    }

    static long getLong(String path, double key) {
        return getLong(path, key + "", DEFAULT_LONG);
    }

    static double getDouble(String path, String key, double defaultValue) {
        return Double.parseDouble(getString(path, key, defaultValue + ""));
    }

    static double getDouble(String path, String key) {
        return getDouble(path, key, DEFAULT_DOUBLE);
    }

    static double getDouble(String path, int key, int defaultValue) {
        return getDouble(path, key + "", defaultValue);
    }

    static double getDouble(String path, int key) {
        return getDouble(path, key + "", DEFAULT_DOUBLE);
    }

    static double getDouble(String path, long key, int defaultValue) {
        return getDouble(path, key + "", defaultValue);
    }

    static double getDouble(String path, long key) {
        return getDouble(path, key + "", DEFAULT_DOUBLE);
    }

    static double getDouble(String path, double key, int defaultValue) {
        return getDouble(path, key + "", defaultValue);
    }

    static double getDouble(String path, double key) {
        return getDouble(path, key + "", DEFAULT_DOUBLE);
    }

    /**
     * 写入一个字符串.
     *
     * @param path         路径
     * @param key          关键字
     * @param defaultValue 默认值
     * @return 写入新的字符串则返回true，否则返回false
     */
    static boolean set(String path, String key, String defaultValue) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                Objects.requireNonNull(f.getParentFile()).mkdirs();
                if (!f.createNewFile()) {
                    return false;
                }
            }
            if (f.isDirectory() || !f.canWrite()) {
                return false;
            }
            Properties p = new Properties();
            p.load(new FileInputStream(path));
            p.setProperty(key, defaultValue);
            p.store(new FileOutputStream(path), null);
            return true;
        } catch (IOException e) {
            // log exception
            return false;
        }
    }

    static boolean set(String path, String key, int defaultValue) {
        return set(path, key, defaultValue + "");
    }

    static boolean set(String path, String key, long defaultValue) {
        return set(path, key, defaultValue + "");
    }

    static boolean set(String path, String key, double defaultValue) {
        return set(path, key, defaultValue + "");
    }

    static boolean set(String path, int key, String defaultValue) {
        return set(path, key + "", defaultValue);
    }

    static boolean set(String path, int key, int defaultValue) {
        return set(path, key + "", defaultValue + "");
    }

    static boolean set(String path, int key, long defaultValue) {
        return set(path, key + "", defaultValue + "");
    }

    static boolean set(String path, int key, double defaultValue) {
        return set(path, key + "", defaultValue + "");
    }

    static boolean set(String path, long key, String defaultValue) {
        return set(path, key + "", defaultValue);
    }

    static boolean set(String path, long key, int defaultValue) {
        return set(path, key + "", defaultValue + "");
    }

    static boolean set(String path, long key, long defaultValue) {
        return set(path, key + "", defaultValue + "");
    }

    static boolean set(String path, long key, double defaultValue) {
        return set(path, key + "", defaultValue + "");
    }

    static boolean set(String path, double key, String defaultValue) {
        return set(path, key + "", defaultValue);
    }

    static boolean set(String path, double key, int defaultValue) {
        return set(path, key + "", defaultValue + "");
    }

    static boolean set(String path, double key, long defaultValue) {
        return set(path, key + "", defaultValue + "");
    }

    static boolean set(String path, double key, double defaultValue) {
        return set(path, key + "", defaultValue + "");
    }

    /*
    文件名去除后缀的函数
 */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }
    /**
     * 移动文件或文件夹.
     * oldPath对应的文件/文件夹必须存在
     * oldPath与newPath必须同为文件/同为文件夹
     * 同为文件，则移动原始文件
     * 同为文件夹，则移动原始文件夹内所有文件
     * 移动文件时，根据overlap决定是否覆盖
     *
     * @param oldPath 原文件/文件夹
     * @param newPath 新文件/文件夹
     * @param overlap 是否覆盖
     * @return 移动成功返回true，否则返回false
     */
    static boolean move(String oldPath, String newPath, boolean overlap) {
        File oldFile = new File(oldPath);
        File newFile = new File(newPath);
        if (!oldFile.exists()) {
            return false;
        }
        if (oldFile.isDirectory()) {
            if (newFile.isFile()) {
                return false;
            }
            newFile.mkdirs();
            String[] children = oldFile.list();// 文件名
            if (children != null) {
                for (String f : children) {
                    if (!move(oldPath + "/" + f, newPath + "/" + f, overlap)) {
                        return false;
                    }
                }
            }
            return oldFile.delete();
        } else {
            Objects.requireNonNull(newFile.getParentFile()).mkdirs();
            if (newFile.isDirectory()) {
                return false;
            }
            if (overlap) {
                newFile.delete();
            }
            if (newFile.exists()) {
                return false;
            }
            return oldFile.renameTo(newFile);
        }
    }

    /**
     * 重命名文件或文件夹.
     *
     * @param filePath    原文件/文件夹
     * @param newFileName 新文件名/文件夹名
     * @param overlap     是否覆盖
     * @return 重命名成功返回true，否则返回false
     */
    static boolean rename(String filePath, String newFileName, boolean overlap) {
        try {
            String canonicalPath = new File(filePath).getCanonicalPath();
            String newPath = canonicalPath.substring(0, canonicalPath.lastIndexOf(File.separator))
                    + File.separator + newFileName;
            return move(filePath, newPath, overlap);
        } catch (IOException e) {
            // log exception
            return false;
        }
    }

    /**
     * 复制文件或文件夹.
     *
     * @param oldPath 原文件/文件夹
     * @param newPath 新文件/文件夹
     * @param overlap 是否覆盖
     * @return 复制成功返回true，否则返回false
     */
    static boolean copy(String oldPath, String newPath, boolean overlap) {
        File oldFile = new File(oldPath);
        File newFile = new File(newPath);
        if (!oldFile.exists()) {
            return false;
        }
        if (oldFile.isDirectory()) {
            if (newFile.isFile()) {
                return false;
            }
            newFile.mkdirs();
            String[] children = oldFile.list();// 文件名
            if (children != null) {
                for (String f : children) {
                    if (!copy(oldPath + "/" + f, newPath + "/" + f, overlap)) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            Objects.requireNonNull(newFile.getParentFile()).mkdirs();
            if (newFile.isDirectory()) {
                return false;
            }
            if (overlap) {
                newFile.delete();
            }
            if (newFile.exists()) {
                return false;
            }

            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(oldFile));
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newFile));
                byte[] buff = new byte[1024];
                int len;
                while ((len = bis.read(buff)) != -1) {
                    bos.write(buff, 0, len);
                }
                bis.close();
                bos.close();
            } catch (IOException e) {
                // log exception
                return false;
            }
            return true;
        }
    }

    /**
     * 删除文件或文件夹.
     *
     * @param filePath 想删除的文件/文件夹
     * @return 删除成功返回true，否则返回false
     */
    static boolean delete(String filePath) {
        File file = new File(filePath);
        if (file.isDirectory()) {// 如果是目录，先删除里面所有的东西
            String[] children = file.list();// 获取文件夹内所有文件路径
            if (children != null) {// 判断文件夹里面有没有东西，有就删除
                for (String f : children) {
                    delete(filePath + File.separator + f);
                }
            }
        }
        return file.delete();// 文件或空目录可以直接删除
    }

    /**
     * 获取文件夹里面的所有文件名
     *
     * @param directoryPath 文件夹
     * @return 如果传入文件，或者不存在的路径，返回null；
     * 如果传入空文件夹，返回空的String数组；
     * 如果传入非空文件夹，返回所有文件名
     */
    static String[] getAllFileName(String directoryPath) {
        return new File(directoryPath).list();
    }

    /**
     * 斐波那契数列的某一项的值
     *
     * @param index 下标，从1开始
     * @return 斐波那契数列对应下标的值
     */
    static int getFibonacci(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("传入的数字" + index + "低于1");
        }
        if (index == 1 || index == 2) {
            return 1;
        } else {
            return getFibonacci(index - 1) + getFibonacci(index - 2);
        }
    }

    static Random random = new Random();

    /**
     * 返回一个均匀分布的随机整数（包括上下限）.
     *
     * @param min 下限
     * @param max 上限
     * @return 随机整数
     */
    static int getRandomInt(int min, int max) {
        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }
        // random.nextInt(a)随机生成[0,a)的随机数
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * 返回一个均匀分布的随机小数（包括上下限）.
     *
     * @param min 下限
     * @param max 上限
     * @return 随机小数
     */
    static double getRandomDouble(double min, double max) {
        if (min > max) {
            double temp = min;
            min = max;
            max = temp;
        }
        // random.nextDouble()随机生成[0,1]的随机数
        return random.nextDouble() * (max - min) + min;
    }

    static final double TWENTY_PERCENT = 0.84;
    static final double TEN_PERCENT = 1.28;
    static final double FIVE_PERCENT = 1.64;
    static final double THREE_PERCENT = 1.88;
    static final double TWO_PERCENT = 2.05;
    static final double ONE_PERCENT = 2.32;
    static final double FIVE_PER_THOUSAND = 2.57;
    static final double THREE_PER_THOUSAND = 2.75;
    static final double TWO_PER_THOUSAND = 2.88;
    static final double ONE_PER_THOUSAND = 3.08;

    /**
     * 返回一个正态分布的随机小数（包括上下限）.
     *
     * @param min      下限
     * @param max      上限
     * @param coverage 以标准正态分布表为准，确定正态分布有效范围
     *                 传入Mx.ONE_PERCENT等数值，或者自定义数值（必须为正）
     *                 比如，查表得2.32为0.9898，2.33为0.9901，
     *                 则传入2.32代表取得上限概率约为1%，取得下限概率约为1%
     * @return 随机小数
     */
    static double getNormalDistributionDouble(double min, double max, double coverage) {
        if (coverage <= 0) {
            return (max + min) / 2;
        }
        double a = random.nextGaussian();
        if (a > coverage) {
            a = coverage;
        } else if (a < -coverage) {
            a = -coverage;
        }
        a = a / (coverage * 2) + 0.5;// [0,1]
        return (max - min) * a + min;
    }

    static double getNormalDistributionDouble(double min, double max) {
        return getNormalDistributionDouble(min, max, ONE_PERCENT);
    }

    /**
     * 返回一个正态分布的随机整数（包括上下限）.
     * 利用正态分布小数，扩充范围至[min,max+1)
     *
     * @param min      下限
     * @param max      上限
     * @param coverage 以标准正态分布表为准，确定正态分布有效范围
     *                 传入Mx.ONE_PERCENT等数值，或者自定义数值（必须为正）
     *                 比如，查表得2.32为0.9898，2.33为0.9901，
     *                 则传入2.32代表取得上限概率约为1%，取得下限概率约为1%
     * @return 随机小数
     */
    static int getNormalDistributionInt(int min, int max, double coverage) {
        double num = getNormalDistributionDouble(min, max + 1.0, coverage);
        return num >= max + 1.0 ? max : (int) num;
    }

    static int getNormalDistributionInt(int min, int max) {
        return getNormalDistributionInt(min, max, ONE_PERCENT);
    }

    static char getRandomChar() {
        String str = "";
        byte[] b = new byte[2];
        b[0] = (byte) (176 + Math.abs(random.nextInt(39)));// 高位
        b[1] = (byte) (161 + Math.abs(random.nextInt(93)));// 低位
        try {
            str = new String(b, "GBK");
        } catch (UnsupportedEncodingException e) {
            // log exception
        }
        return str.charAt(0);
    }

    /**
     * 将本地图片转成Bitmap.
     *
     * @param path 已有图片的路径
     * @return Bitmap
     */
    static Bitmap openImage(String path) {
        Bitmap bitmap;
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
        } catch (IOException e) {
            // log exception
            return null;
        }
        return bitmap;
    }

    /**
     * 从网址获取Bitmap
     *
     * @param src 网址
     * @return 解析完毕的Bitmap
     */
    static Bitmap returnBitMap(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            // log exception
            return null;
        }
    }

    /**
     * 将long类型的时间（以毫秒ms为单位）改成字符串.
     *
     * @param time 通常是System.currentTimeMillis()
     * @return 返回格式为"2020-01-01 00:00:00"的字符串
     * 当然，如果你想获取格式为"2020/01/01 00:00:00"的字符串，
     * 可以将"yyyy-MM-dd HH:mm:ss"改为"yyyy/MM/dd HH:mm:ss"
     * 除此之外，还能加上星期几，上午还是下午等等，
     * 请自行查阅SimpleDateFormat的format方法的用法
     */
    static String timeToStr(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.CHINA).format(new Date(time));
    }

    /**
     * 将long类型的时间差（以秒s为单位）改成字符串.
     *
     * @param timeDiff 时间差
     * @return 返回格式为"a天b小时c分d秒"的字符串
     * 如果数字为0，则不会显示该部分
     * 比如传入60，输出为"1分"
     */
    static String timeDiffToStr(long timeDiff) {
        if (timeDiff < 0) {
            throw new IndexOutOfBoundsException("时间差" + timeDiff + "必须为正");
        }
        String str = "";
        if (timeDiff >= 86400) {
            long day = timeDiff / 86400;
            timeDiff -= day * 86400;
            str = str + day + "天";
        }
        if (timeDiff >= 3600) {
            long hour = timeDiff / 3600;
            timeDiff -= hour * 3600;
            str = str + hour + "时";
        }
        if (timeDiff >= 60) {
            long minute = timeDiff / 60;
            timeDiff -= minute * 60;
            str = str + minute + "分";
        }
        if (timeDiff != 0) {
            str = str + timeDiff + "秒";
        }
        return str;
    }

}
