package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by zsz on 2019/4/20.
 */

public class VoiceBaseBean {

    /**
     * data : [{"createTime":"2019-04-20 20:19:50","fileSize":2214,"name":"3fc9d08f43ca45a8b64ad2f61850fc9e.ogg","path":"https://erp.obs.cn-south-1.myhuaweicloud.com:443/3fc9d08f43ca45a8b64ad2f61850fc9e.ogg","showType":2,"srcName":"6e6317128d4b4e23990830d3110651ee1555762766087.ogg","type":"ogg","userId":1096}]
     * status : 200
     */

    private int status;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * createTime : 2019-04-20 20:19:50
         * fileSize : 2214
         * name : 3fc9d08f43ca45a8b64ad2f61850fc9e.ogg
         * path : https://erp.obs.cn-south-1.myhuaweicloud.com:443/3fc9d08f43ca45a8b64ad2f61850fc9e.ogg
         * showType : 2
         * srcName : 6e6317128d4b4e23990830d3110651ee1555762766087.ogg
         * type : ogg
         * userId : 1096
         */

        private String createTime;
        private int fileSize;
        private String name;
        private String path;
        private int showType;
        private String srcName;
        private String type;
        private int userId;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getFileSize() {
            return fileSize;
        }

        public void setFileSize(int fileSize) {
            this.fileSize = fileSize;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getShowType() {
            return showType;
        }

        public void setShowType(int showType) {
            this.showType = showType;
        }

        public String getSrcName() {
            return srcName;
        }

        public void setSrcName(String srcName) {
            this.srcName = srcName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
