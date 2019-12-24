package yunjing.processor.eventbus;

/**
 */
public class UnReadCommon
{
    public boolean isShow;

    public String msg;

    public UnReadCommon()
    {
    }

    public UnReadCommon(boolean isShow, String msg)
    {
        this.isShow = isShow;
        this.msg = msg;
    }

    public boolean isShow()
    {
        return isShow;
    }

    public void setShow(boolean show)
    {
        isShow = show;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }
}

