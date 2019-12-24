package com.bean.std;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cx on 2016/8/17.
 */
public class SuperMeettingBean implements Serializable{
    /**
     * total : 0
     * status : 200
     * currentPage : 1
     */

    private int total;
    /**
     * pageSize : 20
     * currentPage : 1
     * count : 0
     * start : 0
     * pages : 0
     */

    private DatasBean datas;
    private int status;
    private int currentPage;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public DatasBean getDatas() {
        return datas;
    }

    public void setDatas(DatasBean datas) {
        this.datas = datas;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public static class DatasBean {
        private int pageSize;
        private int currentPage;
        private int count;
        private int start;
        /**
         * limit : 20
         * skip : 0
         * restrictedTypes : []
         * meta : {"snapshot":false}
         */

        private QueryBean query;
        private int pages;
        /**
         * groupType : 2
         * groupId : 20160817105031164357642200004
         * createTime : 2016-08-17 10:50:31
         * updateTime : 2016-08-17 10:50:31
         * updateTimeMillisecond : 1471402231816
         * owner : 17099999901
         * companyId : 179
         * members : [{"joinTime":"2016-08-17 10:50:31","joinTimeMillisecond":1471402231816,"userId":"17099999902"},{"joinTime":"2016-08-17 10:50:31","joinTimeMillisecond":1471402231816,"userId":"17099999908"},{"joinTime":"2016-08-17 10:50:31","joinTimeMillisecond":1471402231816,"userId":"17099999904"}]
         * createTimeMillisecond : 1471402231816
         */

        private List<DataListBean> dataList;

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public QueryBean getQuery() {
            return query;
        }

        public void setQuery(QueryBean query) {
            this.query = query;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public List<DataListBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<DataListBean> dataList) {
            this.dataList = dataList;
        }

        public static class QueryBean {
            private int limit;
            private int skip;
            /**
             * $or : [{"owner":"17099999901"},{"members.userId":"17099999901"}]
             * groupType : 2
             */

            private QueryObjectBean queryObject;
            /**
             * snapshot : false
             */

            private MetaBean meta;
            private List<?> restrictedTypes;

            public int getLimit() {
                return limit;
            }

            public void setLimit(int limit) {
                this.limit = limit;
            }

            public int getSkip() {
                return skip;
            }

            public void setSkip(int skip) {
                this.skip = skip;
            }

            public QueryObjectBean getQueryObject() {
                return queryObject;
            }

            public void setQueryObject(QueryObjectBean queryObject) {
                this.queryObject = queryObject;
            }

            public MetaBean getMeta() {
                return meta;
            }

            public void setMeta(MetaBean meta) {
                this.meta = meta;
            }

            public List<?> getRestrictedTypes() {
                return restrictedTypes;
            }

            public void setRestrictedTypes(List<?> restrictedTypes) {
                this.restrictedTypes = restrictedTypes;
            }

            public static class QueryObjectBean {
                private int groupType;
                private String groupName;
                /**
                 * owner : 17099999901
                 */

                private List<$orBean> $or;

                public int getGroupType() {
                    return groupType;
                }

                public void setGroupType(int groupType) {
                    this.groupType = groupType;
                }

                public String getGroupName() {
                    return groupName;
                }

                public void setGroupName(String groupName) {
                    this.groupName = groupName;
                }

                public List<$orBean> get$or() {
                    return $or;
                }

                public void set$or(List<$orBean> $or) {
                    this.$or = $or;
                }

                public static class $orBean {
                    private String owner;

                    public String getOwner() {
                        return owner;
                    }

                    public void setOwner(String owner) {
                        this.owner = owner;
                    }
                }
            }

            public static class MetaBean {
                private boolean snapshot;

                public boolean isSnapshot() {
                    return snapshot;
                }

                public void setSnapshot(boolean snapshot) {
                    this.snapshot = snapshot;
                }
            }
        }

        public static class DataListBean {
            private int groupType;
            private String groupId;
            private String createTime;
            private String groupName;
            private String updateTime;
            private long updateTimeMillisecond;
            private String owner;
            private String companyId;
            private long createTimeMillisecond;
            /**
             * joinTime : 2016-08-17 10:50:31
             * joinTimeMillisecond : 1471402231816
             * userId : 17099999902
             */

            private List<MembersBean> members;

            public int getGroupType() {
                return groupType;
            }

            public void setGroupType(int groupType) {
                this.groupType = groupType;
            }

            public String getGroupId() {
                return groupId;
            }

            public void setGroupId(String groupId) {
                this.groupId = groupId;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getGroupName() {
                return groupName;
            }

            public void setGroupName(String groupName) {
                this.groupName = groupName;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public long getUpdateTimeMillisecond() {
                return updateTimeMillisecond;
            }

            public void setUpdateTimeMillisecond(long updateTimeMillisecond) {
                this.updateTimeMillisecond = updateTimeMillisecond;
            }

            public String getOwner() {
                return owner;
            }

            public void setOwner(String owner) {
                this.owner = owner;
            }

            public String getCompanyId() {
                return companyId;
            }

            public void setCompanyId(String companyId) {
                this.companyId = companyId;
            }

            public long getCreateTimeMillisecond() {
                return createTimeMillisecond;
            }

            public void setCreateTimeMillisecond(long createTimeMillisecond) {
                this.createTimeMillisecond = createTimeMillisecond;
            }

            public List<MembersBean> getMembers() {
                return members;
            }

            public void setMembers(List<MembersBean> members) {
                this.members = members;
            }

            public static class MembersBean {
                private String joinTime;
                private long joinTimeMillisecond;
                private String userId;

                public String getJoinTime() {
                    return joinTime;
                }

                public void setJoinTime(String joinTime) {
                    this.joinTime = joinTime;
                }

                public long getJoinTimeMillisecond() {
                    return joinTimeMillisecond;
                }

                public void setJoinTimeMillisecond(long joinTimeMillisecond) {
                    this.joinTimeMillisecond = joinTimeMillisecond;
                }

                public String getUserId() {
                    return userId;
                }

                public void setUserId(String userId) {
                    this.userId = userId;
                }
            }
        }
    }
}
