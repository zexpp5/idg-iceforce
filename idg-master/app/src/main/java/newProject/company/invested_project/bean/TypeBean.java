package newProject.company.invested_project.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2019/5/13.
 */

public class TypeBean implements Serializable
{
    private DataBeanX data;
    private int status;

    public DataBeanX getData()
    {
        return data;
    }

    public void setData(DataBeanX data)
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

    public static class DataBeanX
    {
        private String code;
        private Integer total;
        private List<DataBean> data;

        public String getCode()
        {
            return code;
        }

        public void setCode(String code)
        {
            this.code = code;
        }

        public Integer getTotal()
        {
            return total;
        }

        public void setTotal(Integer total)
        {
            this.total = total;
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
             * children : [{"children":null,"codeKey":"110100","codeNameEnUs":"北京","codeNameZhCn":"北京","codeNameZhTw":"北京","hasChild":0,"typeKey":"officeCity"}]
             * codeKey : 11
             * codeNameEnUs : 北京市
             * codeNameZhCn : 北京市
             * codeNameZhTw : 北京市
             * hasChild : 1
             * typeKey : officeCity
             */

            private String codeKey;
            private String codeNameEnUs;
            private String codeNameZhCn;
            private String codeNameZhTw;
            private Integer hasChild;
            private String typeKey;
            private List<ChildrenBean> children;

            public String getCodeKey()
            {
                return codeKey;
            }

            public void setCodeKey(String codeKey)
            {
                this.codeKey = codeKey;
            }

            public String getCodeNameEnUs()
            {
                return codeNameEnUs;
            }

            public void setCodeNameEnUs(String codeNameEnUs)
            {
                this.codeNameEnUs = codeNameEnUs;
            }

            public String getCodeNameZhCn()
            {
                return codeNameZhCn;
            }

            public void setCodeNameZhCn(String codeNameZhCn)
            {
                this.codeNameZhCn = codeNameZhCn;
            }

            public String getCodeNameZhTw()
            {
                return codeNameZhTw;
            }

            public void setCodeNameZhTw(String codeNameZhTw)
            {
                this.codeNameZhTw = codeNameZhTw;
            }

            public Integer getHasChild()
            {
                return hasChild;
            }

            public void setHasChild(Integer hasChild)
            {
                this.hasChild = hasChild;
            }

            public String getTypeKey()
            {
                return typeKey;
            }

            public void setTypeKey(String typeKey)
            {
                this.typeKey = typeKey;
            }

            public List<ChildrenBean> getChildren()
            {
                return children;
            }

            public void setChildren(List<ChildrenBean> children)
            {
                this.children = children;
            }

            public static class ChildrenBean
            {
                /**
                 * children : null
                 * codeKey : 110100
                 * codeNameEnUs : 北京
                 * codeNameZhCn : 北京
                 * codeNameZhTw : 北京
                 * hasChild : 0
                 * typeKey : officeCity
                 */

                private String children;
                private String codeKey;
                private String codeNameEnUs;
                private String codeNameZhCn;
                private String codeNameZhTw;
                private Integer hasChild;
                private String typeKey;

                public String getChildren()
                {
                    return children;
                }

                public void setChildren(String children)
                {
                    this.children = children;
                }

                public String getCodeKey()
                {
                    return codeKey;
                }

                public void setCodeKey(String codeKey)
                {
                    this.codeKey = codeKey;
                }

                public String getCodeNameEnUs()
                {
                    return codeNameEnUs;
                }

                public void setCodeNameEnUs(String codeNameEnUs)
                {
                    this.codeNameEnUs = codeNameEnUs;
                }

                public String getCodeNameZhCn()
                {
                    return codeNameZhCn;
                }

                public void setCodeNameZhCn(String codeNameZhCn)
                {
                    this.codeNameZhCn = codeNameZhCn;
                }

                public String getCodeNameZhTw()
                {
                    return codeNameZhTw;
                }

                public void setCodeNameZhTw(String codeNameZhTw)
                {
                    this.codeNameZhTw = codeNameZhTw;
                }

                public Integer getHasChild()
                {
                    return hasChild;
                }

                public void setHasChild(Integer hasChild)
                {
                    this.hasChild = hasChild;
                }

                public String getTypeKey()
                {
                    return typeKey;
                }

                public void setTypeKey(String typeKey)
                {
                    this.typeKey = typeKey;
                }
            }
        }
    }
}
