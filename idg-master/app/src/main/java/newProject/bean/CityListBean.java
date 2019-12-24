package newProject.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2018/6/8.
 */

public class CityListBean implements Serializable
{

    /**
     * data : [{"id":113,"name":"万州区"},{"id":114,"name":"涪陵区"}]
     * status : 200
     */
    private int status;
    private List<DataBean> data;

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public List<DataBean> getData()
    {
        return data;
    }

    public void setData(List<DataBean> data)
    {
        this.data = data;
    }

    public static class DataBean
    {
        /**
         * id : 113
         * name : 万州区
         */

        private int id;
        private String name;

        public int getId()
        {
            return id;
        }

        public void setId(int id)
        {
            this.id = id;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }
    }
}
