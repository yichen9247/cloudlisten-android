package com.cloudlisten.music.utils.okhttp3;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by 孟鹏 on 2016-11-1-0001.
 * 作用：
 */

public class OKHttpUtils {

    public OKHttpUtils() {
    }

    /**
     * 网络请求队列
     *
     * @param callBack 处理事件的callBack
     */
    public void enqueue(OKHttpCallBack callBack) {
        OKHttpManager.getmInstance().request(this, callBack);
    }

    private Builder mBuilder;

    /**
     * 为了下文获取url
     *
     * @param mBuilder
     */
    public OKHttpUtils(Builder mBuilder) {
        this.mBuilder = mBuilder;
    }

    /**
     * 创建Request请求对象
     *
     * @return Request
     */
    public Request buildRequest() {

        Request.Builder builder = new Request.Builder();
        //get请求
        if (mBuilder.method == "GET") {

            builder.url(buildGetRequestParam());
            builder.get();

        }
        //post请求
        else if (mBuilder.method == "POST") {

            builder.url(mBuilder.url);

            try {
                builder.post(buildRequestBody());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return builder.build();
    }

    /**
     * 构建get请求时传的参数拼接成url
     *
     * @return 拼接的url字符串
     */
    private String buildGetRequestParam() {

        if (mBuilder.params.size() <= 0) {
            return this.mBuilder.url;
        }

        Uri.Builder builder = Uri.parse(mBuilder.url).buildUpon();

        for (RequestParam p : mBuilder.params) {
            builder.appendQueryParameter(p.getKey(), p.getValue() == null ? "" : p.getValue().toString());
        }

        String url = builder.build().toString();

        return url;
    }

    /**
     * okhttp中post请求需要RequestBody,所以这个方法就是为了
     * 构建RequestBody.
     *
     * @return RequestBody 用于post请求的RequestBody
     */
    private RequestBody buildRequestBody() throws JSONException {

        if (mBuilder.isJsonParams) {

            JSONObject jsonObject = new JSONObject();
            //遍历params的list
            for (RequestParam p : mBuilder.params) {
                jsonObject.put(p.getKey(), p.getValue());
            }
            //转化成String
            String json = jsonObject.toString();
            //创建json字符串
            return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        }

        FormBody.Builder builder = new FormBody.Builder();

        for (RequestParam p : mBuilder.params) {

            builder.add(p.getKey(), p.getValue() == null ? "" : p.getValue().toString());
        }

        return builder.build();
    }

    /**
     * 初始化[创建]一个Builder
     *
     * @return
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private String url;
        private String method;
        private List<RequestParam> params;
        private boolean isJsonParams;

        private Builder() {
            //默认是get方法
            method = "GET";
            params = new ArrayList<>();
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public OKHttpUtils build() {
            return new OKHttpUtils(this);
        }

        /**
         * get请求
         */
        public Builder get() {
            method = "GET";
            return this;
        }

        /**
         * post请求
         */
        public Builder post() {
            method = "POST";
            return this;
        }

        /**
         * post中的Json请求
         */
        public Builder postJson() {
            isJsonParams = true;
            return post();
        }

        /**
         * 构建传参用的键值对
         *
         * @param key   传参key值
         * @param value 传参value值
         * @return 构建的参数的list
         */
        public Builder addParam(String key, Object value) {
            params.add(new RequestParam(key, value));
            return this;
        }
    }
}
