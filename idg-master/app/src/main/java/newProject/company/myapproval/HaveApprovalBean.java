package newProject.company.myapproval;

import java.io.Serializable;

/**
 * Created by selson on 2018/6/14.
 */

public class HaveApprovalBean implements Serializable
{

    /**
     * data : {"holidatApprove":true,"costApprove":true,"travelApprove":false}
     * status : 200
     */
    private DataBean data;
    private int status;

    public DataBean getData()
    {
        return data;
    }

    public void setData(DataBean data)

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

    public static class DataBean
    {
        /**
         * holidatApprove : true
         * costApprove : true
         * travelApprove : false
         */
        private boolean holidatApprove;
        private boolean costApprove;
        private boolean travelApprove;

        private boolean  hrApprove;

        public boolean isHrApprove()
        {
            return hrApprove;
        }

        public void setHrApprove(boolean hrApprove)
        {
            this.hrApprove = hrApprove;
        }

        public boolean isHolidatApprove()
        {
            return holidatApprove;
        }

        public void setHolidatApprove(boolean holidatApprove)
        {
            this.holidatApprove = holidatApprove;
        }

        public boolean isCostApprove()
        {
            return costApprove;
        }

        public void setCostApprove(boolean costApprove)
        {
            this.costApprove = costApprove;
        }

        public boolean isTravelApprove()
        {
            return travelApprove;
        }

        public void setTravelApprove(boolean travelApprove)
        {
            this.travelApprove = travelApprove;
        }
    }
}
