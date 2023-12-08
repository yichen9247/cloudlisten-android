package com.cloudlisten.music.utils.okhttp3;

import android.os.Handler;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;


public class OKHttpManager {
    //实例化OKHttpManager
    private static OKHttpManager mInstance;

    private OkHttpClient okHttpClient;

    private Handler mHandler;

    private Gson gson;

    /**
     * 初始化OKHttp , handler , gson
     */
    private OKHttpManager() {
        initOKHttp();
        mHandler = new Handler();
        gson = new Gson();
    }

    /**
     * 单例
     *
     * @return
     */
    public static synchronized OKHttpManager getmInstance() {
        if (mInstance == null) {
            mInstance = new OKHttpManager();
        }
        return mInstance;
    }

    /**
     * 初始化OKHttp
     */
    private void initOKHttp() {

        okHttpClient = new OkHttpClient().newBuilder()
                .readTimeout(30000, TimeUnit.SECONDS)//读数据，以秒为单位
                .connectTimeout(30000, TimeUnit.SECONDS)//连接超时时间，以秒为单位
//                .cache()
                .build();
    }

    /**
     * 处理okhttp网络请求
     *
     * @param okHttpUtils    自己封装的OKHttpUtils
     * @param okHttpCallBack 自己封装的OKHttpCallBack
     */
    public void request(OKHttpUtils okHttpUtils, final OKHttpCallBack okHttpCallBack) {
        //如果为空抛出空指针异常
        if (okHttpCallBack == null) {
            throw new NullPointerException("okHttpCallBack is null !");
        }
        //okhttp访问网络及返回方法
        okHttpClient.newCall(okHttpUtils.buildRequest()).enqueue(new Callback() {
            /**
             * 获取失败
             * @param call
             * @param e
             */
            @Override
            public void onFailure(Call call, IOException e) {
                //返回失败时的回调
                sendonFailureMessage(okHttpCallBack, call, e);
            }

            /**
             * 获取成功
             * @param call
             * @param response
             * @throws IOException
             */
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    if (okHttpCallBack.mType == null || okHttpCallBack.mType == String.class) {
                        sendonSuccessMessage(okHttpCallBack, result);
                    } else {
                        try {
                            sendonSuccessMessage(okHttpCallBack, gson.fromJson(result,okHttpCallBack.mType));
                        } catch (Exception e) {
                            //此处的0占位
                            sendonErrorMessage(okHttpCallBack,0);
                        }
                    }

                    if (response.body() != null) {
                        response.body().close();
                    }
                } else {
                    sendonErrorMessage(okHttpCallBack, response.code());
                }
            }
        });
    }

    /**
     * 获取信息失败时的回调信息
     *
     * @param okHttpCallBack
     * @param call
     * @param e
     */
    private void sendonFailureMessage(final OKHttpCallBack okHttpCallBack, final Call call, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                okHttpCallBack.onFailure(call, e);
            }
        });
    }

    /**
     * 连接服务器出错时的回调信息
     *
     * @param okHttpCallBack 回调的callback
     * @param code           错误码
     */
    private void sendonErrorMessage(final OKHttpCallBack okHttpCallBack, final int code) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                okHttpCallBack.onError(code);
            }
        });
    }

    /**
     * 成功获取数据时的回调
     *
     * @param okHttpCallBack 回调的callback
     * @param object         获取的数据
     */
    private void sendonSuccessMessage(final OKHttpCallBack okHttpCallBack, final Object object) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                okHttpCallBack.onSuccess(object);
            }
        });
    }
}
