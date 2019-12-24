package yunjing.processor.eventbus;

import java.util.Map;

/**
 * Created by selson on 2016/3/28.
 * 视频发送事件类
 */
public class UnReadMessage
{
    public boolean isShow;

    public int menuId;

    public int count;

    public Map<String, String> map;

    public UnReadMessage(boolean isShow, int menuId, int count)
    {
        this.isShow = isShow;
        this.count = count;
        this.menuId = menuId;
    }

    public UnReadMessage(boolean isShow, int menuId, int count, Map<String, String> map)
    {
        this.isShow = isShow;
        this.count = count;
        this.menuId = menuId;
        this.map = map;
    }
}

