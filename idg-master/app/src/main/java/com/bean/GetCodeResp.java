package com.bean;

import java.io.Serializable;

/**
 * Created by cx007 on 2016/6/4.
 */
public class GetCodeResp implements Serializable
{
    private CodeBean data;

    private int status;

    public void setData(CodeBean data)
    {
        this.data = data;
    }

    public CodeBean getData()
    {
        return this.data;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getStatus()
    {
        return this.status;
    }


    public class CodeBean
    {
        public String code;

        public String codeKey;

        public void setCode(String code)
        {
            this.code = code;
        }

        public String getCode()
        {
            return this.code;
        }

        public void setCodeKey(String codeKey)
        {
            this.codeKey = codeKey;
        }

        public String getCodeKey()
        {
            return this.codeKey;
        }


    }
}
