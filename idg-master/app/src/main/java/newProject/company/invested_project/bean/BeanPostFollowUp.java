package newProject.company.invested_project.bean;

import java.io.Serializable;

/**
 * Created by selson on 2019/5/23.
 */

public class BeanPostFollowUp implements Serializable
{
    private String indexId;
    private String indexValue;

    public String getIndexId()
    {
        return indexId;
    }

    public void setIndexId(String indexId)
    {
        this.indexId = indexId;
    }

    public String getIndexValue()
    {
        return indexValue;
    }

    public void setIndexValue(String indexValue)
    {
        this.indexValue = indexValue;
    }
}
