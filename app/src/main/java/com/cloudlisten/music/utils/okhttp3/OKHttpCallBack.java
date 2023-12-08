package com.cloudlisten.music.utils.okhttp3;

import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;


public abstract class OKHttpCallBack<T> {
    /**
     * 静态代码块，获取T的泛型
     *
     * @param subclass 泛型
     * @return T的泛型
     */
    static Type getSuperclassTypeParameter(Class<?> subclass) {

        Type superclass = subclass.getGenericSuperclass();

        if (superclass instanceof Class) {

            return null;
        }
        ParameterizedType parameterizedType = (ParameterizedType) superclass;

        return $Gson$Types.canonicalize(parameterizedType.getActualTypeArguments()[0]);

    }

    public Type mType;

    /**
     * okhttp的callback
     */
    public OKHttpCallBack() {
        mType = getSuperclassTypeParameter(this.getClass());
    }

    /**
     * 获取数据成功，配合Gson解析成javabean
     * 前提是必须要先用gson解析成javabean，然后把javabean的泛型代替T，
     * 例如：OKHttpCallBack<User>,User就是解析出来的，可以直接拿来用
     *
     * @param t 解析后得到的数据
     */
    public void onSuccess(T t) {
    }

    /**
     * 获取数据时链接服务器失败
     *
     * @param code 链接服务器失败的状态码
     */
    public void onError(int code) {
    }

    /**
     * 获取数据失败
     *
     * @param call 失败时的call
     * @param e    抛出的异常
     */
    public void onFailure(Call call, IOException e) {
    }
}
