package newProject.ui.annual_meeting;

import java.util.List;

/**
 * Created by Administrator on 2018/1/9.
 */

public class VotingListBean
{

    /**
     * data : {"isVote":1,"itemList":[{"eid":6,"itemIndex":1,"name":"舞蹈表演","voteCount":3},{"eid":8,"itemIndex":2,
     * "name":"张屹-唱歌","voteCount":1}]}
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
         * isVote : 1
         * itemList : [{"eid":6,"itemIndex":1,"name":"舞蹈表演","voteCount":3},{"eid":8,"itemIndex":2,"name":"张屹-唱歌","voteCount":1}]
         */

        private int isVote;
        private int startVote;
        private List<ItemListBean> itemList;
        private List<VoteItemList> voteItemList;

        public List<VoteItemList> getVoteItemList()
        {
            return voteItemList;
        }

        public void setVoteItemList(List<VoteItemList> voteItemList)
        {
            this.voteItemList = voteItemList;
        }

        public int getStartVote()
        {
            return startVote;
        }

        public void setStartVote(int startVote)
        {
            this.startVote = startVote;
        }

        public int getIsVote()
        {
            return isVote;
        }

        public void setIsVote(int isVote)
        {
            this.isVote = isVote;
        }

        public List<ItemListBean> getItemList()
        {
            return itemList;
        }

        public void setItemList(List<ItemListBean> itemList)
        {
            this.itemList = itemList;
        }

        public static class VoteItemList
        {
            /**
             * eid : 6
             * itemIndex : 1
             * name : 舞蹈表演
             * voteCount : 3
             */

            private int eid;
            private String name;


            public int getEid()
            {
                return eid;
            }

            public void setEid(int eid)
            {
                this.eid = eid;
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

        public static class ItemListBean
        {
            /**
             * eid : 6
             * itemIndex : 1
             * name : 舞蹈表演
             * voteCount : 3
             */

            private int eid;
            private int itemIndex;
            private String name;
            private int voteCount;
            private boolean isCheck;

            public boolean isCheck()
            {
                return isCheck;
            }

            public void setCheck(boolean check)
            {
                isCheck = check;
            }

            public int getEid()
            {
                return eid;
            }

            public void setEid(int eid)
            {
                this.eid = eid;
            }

            public int getItemIndex()
            {
                return itemIndex;
            }

            public void setItemIndex(int itemIndex)
            {
                this.itemIndex = itemIndex;
            }

            public String getName()
            {
                return name;
            }

            public void setName(String name)
            {
                this.name = name;
            }

            public int getVoteCount()
            {
                return voteCount;
            }

            public void setVoteCount(int voteCount)
            {
                this.voteCount = voteCount;
            }
        }
    }
}
