package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by zsz on 2019/4/12.
 */

public class FollowProjectListBean {


    /**
     * data : {"code":"success","data":[{"contentType":"TEXT","createBy":"1388","createTime":"2019-04-12 11:39:12","followId":"4ccfde23cb324a298917b1fbaad3043f","followName":"罗智慧","followType":"NEW_INFO","projId":"827e38fbb1c44c8994b14d091804420e","projName":"齐天大圣要上天续","projState":null,"projType":null,"showDesc":"罗智慧新建项目齐天大圣要上天续 项目业务:null","url":null,"userId":"1388","validFlag":1},{"contentType":"TEXT","createBy":"1388","createTime":"2019-04-12 11:37:21","followId":"69f01040f49b46449f0a665e645b3cbb","followName":"罗智慧","followType":"NEW_INFO","projId":"2b85f89627184fdc921fd619e56d7f7f","projName":"齐天大圣要上天续","projState":null,"projType":null,"showDesc":"罗智慧新建项目齐天大圣要上天续 项目业务:null","url":null,"userId":"1388","validFlag":1},{"contentType":"TEXT","createBy":"1388","createTime":"2019-04-12 11:23:55","followId":"24787155411b488c83a536d8b13308b3","followName":"罗智慧","followType":"NEW_INFO","projId":"5e4ddec105ce4575bb40d90db191738c","projName":"齐天大圣要上天","projState":null,"projType":null,"showDesc":"罗智慧新建项目齐天大圣要上天 项目业务:null","url":null,"userId":"1388","validFlag":1},{"contentType":"TEXT","createBy":"9999","createTime":"2019-04-11 16:50:21","followId":"d800bae96d544013a7a775bf7e0bdeda","followName":"黄建爽","followType":"NEW_INFO","projId":"f8ff9a15e16b4ccf8be5b85416d6a979","projName":"411测试2","projState":null,"projType":null,"showDesc":"黄建爽新建项目411测试2 项目业务:中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述","url":null,"userId":"9999","validFlag":1},{"contentType":"TEXT","createBy":"9999","createTime":"2019-04-11 16:41:47","followId":"ce6c1e87f25f4b9480aef0dc71b43850","followName":"黄建爽","followType":"NEW_INFO","projId":"d7306407c747499bbe5a54df52b7a233","projName":"411测试","projState":null,"projType":null,"showDesc":"黄建爽新建项目411测试 项目业务:中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述","url":null,"userId":"9999","validFlag":1}]}
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
         * data : [{"contentType":"TEXT","createBy":"1388","createTime":"2019-04-12 11:39:12","followId":"4ccfde23cb324a298917b1fbaad3043f","followName":"罗智慧","followType":"NEW_INFO","projId":"827e38fbb1c44c8994b14d091804420e","projName":"齐天大圣要上天续","projState":null,"projType":null,"showDesc":"罗智慧新建项目齐天大圣要上天续 项目业务:null","url":null,"userId":"1388","validFlag":1},{"contentType":"TEXT","createBy":"1388","createTime":"2019-04-12 11:37:21","followId":"69f01040f49b46449f0a665e645b3cbb","followName":"罗智慧","followType":"NEW_INFO","projId":"2b85f89627184fdc921fd619e56d7f7f","projName":"齐天大圣要上天续","projState":null,"projType":null,"showDesc":"罗智慧新建项目齐天大圣要上天续 项目业务:null","url":null,"userId":"1388","validFlag":1},{"contentType":"TEXT","createBy":"1388","createTime":"2019-04-12 11:23:55","followId":"24787155411b488c83a536d8b13308b3","followName":"罗智慧","followType":"NEW_INFO","projId":"5e4ddec105ce4575bb40d90db191738c","projName":"齐天大圣要上天","projState":null,"projType":null,"showDesc":"罗智慧新建项目齐天大圣要上天 项目业务:null","url":null,"userId":"1388","validFlag":1},{"contentType":"TEXT","createBy":"9999","createTime":"2019-04-11 16:50:21","followId":"d800bae96d544013a7a775bf7e0bdeda","followName":"黄建爽","followType":"NEW_INFO","projId":"f8ff9a15e16b4ccf8be5b85416d6a979","projName":"411测试2","projState":null,"projType":null,"showDesc":"黄建爽新建项目411测试2 项目业务:中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述","url":null,"userId":"9999","validFlag":1},{"contentType":"TEXT","createBy":"9999","createTime":"2019-04-11 16:41:47","followId":"ce6c1e87f25f4b9480aef0dc71b43850","followName":"黄建爽","followType":"NEW_INFO","projId":"d7306407c747499bbe5a54df52b7a233","projName":"411测试","projState":null,"projType":null,"showDesc":"黄建爽新建项目411测试 项目业务:中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述中文的描述","url":null,"userId":"9999","validFlag":1}]
         */

        private String code;
        private List<FollowProjectBaseBean> data;
        private Integer total;
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public List<FollowProjectBaseBean> getData() {
            return data;
        }

        public void setData(List<FollowProjectBaseBean> data) {
            this.data = data;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }
    }
}
