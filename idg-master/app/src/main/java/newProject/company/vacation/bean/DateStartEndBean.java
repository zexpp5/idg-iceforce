package newProject.company.vacation.bean;

/**
 * Created by selson on 2019/1/22.
 */

public class DateStartEndBean
{
    /**
     * data : {"isConflict":true,"msg":"请假期间有月会安排，请确认是否要提交请假。"}
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
         * isConflict : true
         * msg : 请假期间有月会安排，请确认是否要提交请假。
         */

        private boolean isConflict;
        private String msg;

        public boolean isIsConflict()
        {
            return isConflict;
        }

        public void setIsConflict(boolean isConflict)
        {
            this.isConflict = isConflict;
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
}
