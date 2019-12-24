package newProject.company.project;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2017/11/2.
 * 项目协同的聊天详情
 */
public class ProjectChatBean implements Serializable
{
    private int status;
    private List<ProjectDetailDataBean> data;

    public List<ProjectDetailDataBean> getData()
    {
        return data;
    }

    public void setData(List<ProjectDetailDataBean> data)
    {
        this.data = data;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

}
