package newProject.company.project;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2017/11/2.
 * 项目协同的聊天详情
 */
public class ProjectNewChatBean implements Serializable
{   
    private int status;
    private Data data;

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public Data getData()
    {
        return data;
    }

    public void setData(Data data)
    {
        this.data = data;
    }

    public class Data
    {
        private List<ProjectDetailDataBean> data;

        private int forward_limitId;

        public void setData(List<ProjectDetailDataBean> data)
        {
            this.data = data;
        }

        public List<ProjectDetailDataBean> getData()
        {
            return this.data;
        }

        public void setForward_limitId(int forward_limitId)
        {
            this.forward_limitId = forward_limitId;
        }

        public int getForward_limitId()
        {
            return this.forward_limitId;
        }

    }
}
