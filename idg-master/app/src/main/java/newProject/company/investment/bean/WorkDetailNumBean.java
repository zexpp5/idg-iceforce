package newProject.company.investment.bean;

/**
 * Created by zsz on 2019/9/2.
 */

public class WorkDetailNumBean {

    /**
     * data : {"code":"success","data":{"newNum":0,"readNum":0,"toLookNum":0,"trackNum":1},"returnMessage":"SUCCESS","total":null}
     * status : 200
     */

    private DataBeanX data;
    private int status;

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataBeanX {
        /**
         * code : success
         * data : {"newNum":0,"readNum":0,"toLookNum":0,"trackNum":1}
         * returnMessage : SUCCESS
         * total : null
         */

        private String code;
        private DataBean data;
        private String returnMessage;
        private String total;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public String getReturnMessage() {
            return returnMessage;
        }

        public void setReturnMessage(String returnMessage) {
            this.returnMessage = returnMessage;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public static class DataBean {
            /**
             * newNum : 0
             * readNum : 0
             * toLookNum : 0
             * trackNum : 1
             */

            private int newNum;
            private int readNum;
            private int toLookNum;
            private int trackNum;

            public int getNewNum() {
                return newNum;
            }

            public void setNewNum(int newNum) {
                this.newNum = newNum;
            }

            public int getReadNum() {
                return readNum;
            }

            public void setReadNum(int readNum) {
                this.readNum = readNum;
            }

            public int getToLookNum() {
                return toLookNum;
            }

            public void setToLookNum(int toLookNum) {
                this.toLookNum = toLookNum;
            }

            public int getTrackNum() {
                return trackNum;
            }

            public void setTrackNum(int trackNum) {
                this.trackNum = trackNum;
            }
        }
    }
}
