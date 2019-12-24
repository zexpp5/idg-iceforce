package newProject.ui.work;

import com.bean.std.SuperSearchChatBean;
import com.chaoxiang.base.utils.SDGson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2017/11/28.
 */

public class GMDataList implements Serializable
{
    private int status;

    private List<GMeetingDataBean> data;

    public static GMDataList parse(String jsonString)
    {
        SDGson gson = new SDGson();
        GMDataList bean = gson.fromJson(jsonString, new TypeToken<GMDataList>()
        {
        }.getType());

        return bean;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public List<GMeetingDataBean> getData()
    {
        return data;
    }

    public void setData(List<GMeetingDataBean> data)
    {
        this.data = data;
    }
}
