package com.chaoxiang.imrestful.builder;

import com.chaoxiang.imrestful.bean.FileInput;
import com.chaoxiang.imrestful.request.PutRequest;
import com.chaoxiang.imrestful.request.RequestCall;

import java.io.File;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * @auth lwj
 * @date 2016-01-04
 * @desc
 */
public class PutBuilder extends OkHttpRequestBuilder {
    @Override
    public RequestCall build()
    {
        return new PutRequest(url, tag, params, headers, files).build();
    }

    private List<FileInput> files = new ArrayList<>();

    public PutBuilder addFile(String name, String filename, File file)
    {
        files.add(new FileInput(name, filename, file));
        return this;
    }

    //
    @Override
    public PutBuilder url(String url)

    {
        this.url = url;
        return this;
    }

    @Override
    public PutBuilder tag(Object tag)
    {
        this.tag = tag;
        return this;
    }

    @Override
    public PutBuilder params(Map<String, String> params)
    {
        this.params = params;
        return this;
    }

    @Override
    public PutBuilder addParams(String key, String val)
    {
        if (this.params == null)
        {
            params = new IdentityHashMap<>();
        }
        params.put(key, val);
        return this;
    }

    @Override
    public PutBuilder headers(Map<String, String> headers)
    {
        this.headers = headers;
        return this;
    }

    @Override
    public PutBuilder addHeader(String key, String val)
    {
        if (this.headers == null)
        {
            headers = new IdentityHashMap<>();
        }
        headers.put(key, val);
        return this;
    }
}
