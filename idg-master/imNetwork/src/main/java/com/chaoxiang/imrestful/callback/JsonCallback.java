package com.chaoxiang.imrestful.callback;

import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * @auth lwj
 * @date 2016-01-05
 * @desc
 */
public abstract class JsonCallback extends Callback<JSONObject> {


    @Override
    public JSONObject parseNetworkResponse(Response response) throws IOException {
        try {
            JSONObject object = new JSONObject(response.body().string());
            return object;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
