package com.chaoxiang.imrestful.callback;

import com.chaoxiang.base.utils.SDGson;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @auth lwj
 * @date 2016-01-04
 * @desc
 */
public abstract class EntityCallback<T> extends Callback<T> {


    @Override
    public T parseNetworkResponse(Response response) throws IOException {
        Type genType = this.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        SDGson mGson = new SDGson();
        T result = mGson.fromJson(response.body().string(), params[0]);
        return result;
    }
}
