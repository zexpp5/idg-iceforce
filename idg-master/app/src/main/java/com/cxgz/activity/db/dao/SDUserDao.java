package com.cxgz.activity.db.dao;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.base.BaseApplication;
import com.bean_erp.LoginListBean;
import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cx.utils.HanziToPinyin;
import com.cxgz.activity.cxim.http.ImHttpHelper;
import com.cxgz.activity.db.entity.SDDictionaryEntity;
import com.cxgz.activity.superqq.SuperMainActivity;
import com.entity.LoginDataBean;
import com.entity.SDDepartmentEntity;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.SqlInfo;
import com.lidroid.xutils.exception.DbException;
import com.utils.SPUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;


/**
 * Amy 20150513 增加根据用户ID查找用户信息
 *
 * @author Administrator
 */
public class SDUserDao extends BaseDao
{
    private DbUtils mDbUtils;
    private String userId;

    private SDUserDao()
    {
    }

    public static synchronized SDUserDao getInstance()
    {
        return SDUserDaoHelper.sdUserDao;
    }

    public static class SDUserDaoHelper
    {
        private static final SDUserDao sdUserDao = new SDUserDao();
    }

    public enum SearchType implements Serializable
    {
        ALL,   //所有用户
        ONJOB, //在职用户
        QUIT,   //离职用户
        INSIDE, //内部人员
        OUTSIDE, //外部人员
        REMOVE_OWN, //移除自己
    }

    public enum OfficeType implements Serializable
    {
        ALL,   //所有用户
        ONJOB, //在职用户
        QUIT,   //离职用户
        SXJOB, //实习
        SYJOB, //试用
    }

    public SDUserDao(Context context)
    {
        super(context);
        mDbUtils = BaseApplication.mDbUtils;
        userId = (String) SPUtils.get(context, SPUtils.USER_ID, "-1");
    }

    public void saveUser(SDUserEntity entity)
    {
        try
        {
            db.saveOrUpdate(entity);
        } catch (DbException e)
        {
            e.printStackTrace();
        }
    }

    //查询所有人
    public synchronized List<SDUserEntity> findAllUser()
    {
        List<SDUserEntity> userEntities = new ArrayList<SDUserEntity>();
        try
        {
            userEntities = db.findAll(SDUserEntity.class);
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        return userEntities;
    }

    //查询所有人
    public synchronized List<SDAllConstactsEntity> findAllConstacts()
    {
        List<SDAllConstactsEntity> sdAllConstactsEntityList = new ArrayList<SDAllConstactsEntity>();
        try
        {
            sdAllConstactsEntityList = db.findAll(SDAllConstactsEntity.class);
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        return sdAllConstactsEntityList;
    }

    //查询所有人-不包含客服-不包含自己
    public synchronized List<SDUserEntity> findAllUserNot()
    {
        List<SDUserEntity> userEntities = new ArrayList<SDUserEntity>();
        try
        {
            userEntities = db.findAll(Selector.from(SDUserEntity.class).where("userType", "!=", Constants.USER_TYPE_KEFU));
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        return userEntities;
    }

    //查询所有人-但是不包括我自己(或者是除了这个ImAccount的)
    public synchronized List<SDUserEntity> findAllUserOutMe(String myImAccount)
    {
        List<SDUserEntity> userEntities = new ArrayList<SDUserEntity>();
        try
        {
            userEntities = db.findAll(Selector.from(SDUserEntity.class).where("imAccount", "!=", myImAccount));
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        return userEntities;
    }

    /**
     * 根据IMAccount查询通讯录
     */
    public synchronized SDUserEntity findUserByImAccount(String account)
    {
        List<String> list = new ArrayList<String>();
        list.add(account);
        List<SDUserEntity> userEntities = findUserByImAccount(list);
        if (userEntities == null || userEntities.isEmpty())
        {
            return null;
        }
        return userEntities.get(0);
    }

    /**
     * 多个查询
     * 根据IMAccount查询通讯录
     */
    public synchronized List<SDUserEntity> findUserByImAccount(List<String> imAccountList)
    {
        List<SDUserEntity> userEntities = new ArrayList<>();
        if (imAccountList == null)
        {
            return userEntities;
        }
        String[] conditionArray = imAccountList.toArray(new String[imAccountList.size()]);
        try
        {
            userEntities = BaseApplication.getmDbUtils().findAll(Selector.from(SDUserEntity.class).where("imAccount", "in",
                    conditionArray));
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        return userEntities;
    }


    /**
     * 根据account查询通讯录
     */
    public synchronized SDUserEntity findUserByAccount(String account)
    {
        List<String> list = new ArrayList<String>();
        list.add(account);
        List<SDUserEntity> userEntities = findUserByAccount(list);
        if (userEntities == null || userEntities.isEmpty())
        {
            return null;
        }
        return userEntities.get(0);
    }

    /**
     * 多个查询
     * 根据Account查询通讯录
     */
    public synchronized List<SDUserEntity> findUserByAccount(List<String> accountList)
    {
        List<SDUserEntity> userEntities = new ArrayList<>();
        if (accountList == null)
        {
            return userEntities;
        }
        String[] conditionArray = accountList.toArray(new String[accountList.size()]);
        try
        {
            userEntities = BaseApplication.getmDbUtils().findAll(Selector.from(SDUserEntity.class).where("account", "in",
                    conditionArray));
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        return userEntities;
    }


    /**
     * 单个查询
     * 根据用户的EID（也就是userId）查询通讯录
     */
    public synchronized SDUserEntity findUserByUserId(String userId)
    {
        List<String> list = new ArrayList<String>();
        list.add(userId);
        List<SDUserEntity> userEntities = findUserByUserId(list);
        if (userEntities == null || userEntities.isEmpty())
        {
            return null;
        }
        return userEntities.get(0);
    }

    /**
     * 多个查询
     * 根据用户的EID（也就是userId）查询通讯录
     */
    public synchronized List<SDUserEntity> findUserByUserId(List<String> userIdList)
    {
        List<SDUserEntity> userEntities = new ArrayList<>();
        if (userIdList == null)
        {
            return userEntities;
        }
        String[] conditionArray = userIdList.toArray(new String[userIdList.size()]);
        try
        {
            userEntities = BaseApplication.getmDbUtils().findAll(Selector.from(SDUserEntity.class).where("eid", "in",
                    conditionArray));
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        return userEntities;
    }

    //查询某个任职状态下的的所有员工
    public synchronized List<SDUserEntity> findAllUsersByOfficeType(OfficeType... searchTypes)
    {
        List<SDUserEntity> userEntities = new ArrayList<SDUserEntity>();
        OfficeType searchType = searchTypes[0];
        int type = 0;
        switch (searchType)
        {
            //所有
            case ALL:
                type = 0;
                break;
            //实习
            case SXJOB:
                type = 1;
                break;
            //试用
            case SYJOB:
                type = 2;
                break;
            //在职
            case ONJOB:
                type = 3;
                break;
            //离职
            case QUIT:
                type = 4;
                break;

        }
        try

        {
            if (type == 0)
                userEntities = db.findAll(SDUserEntity.class);
            else
                userEntities = db.findAll(Selector.from(SDUserEntity.class)
                        .where("jobStatus", "=", type));
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        return userEntities;
    }

    //查询某部门（通过部门ID）下的所有员工
    public synchronized List<SDUserEntity> findAllUsersBydpId(String dpId)
    {
        List<SDUserEntity> userEntities = new ArrayList<SDUserEntity>();
        if (TextUtils.isEmpty(dpId))
        {
            return userEntities;
        }
        try
        {
            userEntities = db.findAll(Selector.from(SDUserEntity.class).where("deptId", "=", dpId));
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        return userEntities;
    }

    /**
     * 查找离职用户
     *
     * @return
     */
    public synchronized List<SDUserEntity> findQuitUserById()
    {
        return findAllUsersByOfficeType(OfficeType.QUIT);
    }

    /**
     * 查找在职用户
     *
     * @return
     */
    public synchronized List<SDUserEntity> findJobUser()
    {
        return findAllUsersByOfficeType(OfficeType.ONJOB);
    }

    //旧的兼容。
    // 根据单个用户ID查找用户信息，Amy20150518
    public synchronized SDUserEntity findUserByUserID(String userId)
    {
        return findUserByUserId(userId);
    }


    public synchronized void saveConstact(Application application, LoginListBean sdContact,
                                          LoginDataBean.LoginEntity loginEntity)
    {
        try
        {
            if (mDbUtils.tableIsExist(SDUserEntity.class))
            {
                //删除表
                mDbUtils.dropTable(SDUserEntity.class);
            }
            mDbUtils.createTableIfNotExist(SDUserEntity.class);
        } catch (DbException e)
        {
            e.printStackTrace();
        }

        List<SDUserEntity> userEntitys = sdContact.getData();

        for (SDUserEntity userEntity : userEntitys)
        {
            setUserHead(userEntity);
            try
            {
                mDbUtils.save(userEntity);
            } catch (DbException e)
            {
                e.printStackTrace();
            }
        }

        List<SDAllConstactsEntity> allContactsEntitys = sdContact.getAllContacts();
        try
        {
            if (mDbUtils.tableIsExist(SDAllConstactsEntity.class))
            {
                //删除表
                mDbUtils.dropTable(SDAllConstactsEntity.class);
            }
            mDbUtils.createTableIfNotExist(SDAllConstactsEntity.class);
        } catch (DbException e)
        {
            e.printStackTrace();
        }

        if (allContactsEntitys != null)
        {
            for (SDAllConstactsEntity sdAllConstactsEntity : allContactsEntitys)
            {
//            setUserHead(sdAllConstactsEntity);
                try
                {
                    mDbUtils.save(sdAllConstactsEntity);
                } catch (DbException e)
                {
                    e.printStackTrace();
                }
            }
        }

//        ImHttpHelper.postKefuListSave(application);

        //静态数据
        if (loginEntity != null)
            if (StringUtils.notEmpty(loginEntity.getStaticList()))
                SDDictCodeDao.saveDictionary(application, loginEntity.getStaticList());

        // 权限功能菜单表
        if (loginEntity != null)
            if (StringUtils.notEmpty(loginEntity.getMenuList()))
                SDMenusDao.saveMenus(application, loginEntity.getMenuList());

        SDUnreadDao.saveUnRead(application);

    }

//    /*
//     *保存表 。在表中保存一个user对象。最初执行保存动作时，也会创建User表
//     */
//    public synchronized void saveConstact(Application application, SDContactInfo sdContact)
//    {
//        List<SDUserEntity> oldUserEntitys = null;
//        try
//        {
//            oldUserEntitys = mDbUtils.findAll(SDUserEntity.class);
//        } catch (DbException e)
//        {
//            e.printStackTrace();
//        }
//
//        try
//        {
//            if (mDbUtils.tableIsExist(SDUserEntity.class))
//            {
//                //删除表
//                mDbUtils.dropTable(SDUserEntity.class);
//            }
//        } catch (DbException e)
//        {
//            e.printStackTrace();
//        }
//
//        try
//        {
//            if (mDbUtils.tableIsExist(SDDepartmentEntity.class))
//            {
//                mDbUtils.dropTable(SDDepartmentEntity.class);
//            }
//        } catch (DbException e)
//        {
//            e.printStackTrace();
//        }
//
//        try
//        {
//            if (mDbUtils.tableIsExist(SDUserDeptRelEntity.class))
//            {
//                mDbUtils.dropTable(SDUserDeptRelEntity.class);
//            }
//        } catch (DbException e)
//        {
//            e.printStackTrace();
//        }
//
//
//        try
//        {
//            if (mDbUtils.tableIsExist(SDUserCusEntity.class))
//            {
//                mDbUtils.dropTable(SDUserCusEntity.class);
//            }
//        } catch (DbException e)
//        {
//            e.printStackTrace();
//        }
//
//
//        try
//        {
//            mDbUtils.createTableIfNotExist(SDUserEntity.class);
//        } catch (DbException e)
//        {
//            e.printStackTrace();
//        }
//
//        try
//        {
//            mDbUtils.createTableIfNotExist(SDDepartmentEntity.class);
//        } catch (DbException e)
//        {
//            e.printStackTrace();
//        }
//
//        try
//        {
//            mDbUtils.createTableIfNotExist(SDUserDeptRelEntity.class);
//        } catch (DbException e)
//        {
//            e.printStackTrace();
//        }
//
//        List<SDUserEntity> userEntitys = sdContact.getUsers();
//        if (oldUserEntitys != null)
//        {
//            SDLogUtil.syso("开始更新头像");
//        }
//
//        List<SDDepartmentEntity> departments = sdContact.getDps();
//        List<SDUserDeptRelEntity> RelEntities = sdContact.getUser_dp_rels();
//
//        for (SDUserEntity userEntity : userEntitys)
//        {
//            setUserHead(userEntity);
//            try
//            {
//                mDbUtils.save(userEntity);
//            } catch (DbException e)
//            {
//                e.printStackTrace();
//            }
//        }
//
//        if (departments != null)
//        {
//            for (SDDepartmentEntity departmentEntity : departments)
//            {
//                setDpHead(departmentEntity);
//                try
//                {
//                    mDbUtils.save(departmentEntity);
//                } catch (DbException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//
//        if (RelEntities != null)
//        {
//            for (SDUserDeptRelEntity relEntitie : RelEntities)
//            {
//                try
//                {
//                    mDbUtils.save(relEntitie);
//                } catch (DbException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    /**
     * 通过部门id获取该部门下的人数
     *
     * @param departmentEntity
     * @return
     */
    public synchronized int getDepartmentUserCount(SDDepartmentEntity departmentEntity)
    {
        int count = 0;
        Cursor cursor = null;
        try
        {
            cursor = mDbUtils.execQuery(new SqlInfo("SELECT count(*) FROM SYS_USER WHERE USER_ID IN " +
                    "(SELECT USER_ID FROM USER_DEPT_REL WHERE dpId=?)", departmentEntity.getDpId()));
            cursor.moveToFirst();
            count = cursor.getInt(0);
        } catch (DbException e)
        {
            e.printStackTrace();
        } finally
        {
            if (cursor != null)
            {
                cursor.close();
                cursor = null;
            }
        }
        SDLogUtil.debug("department_user_count==" + count);
        return count;
    }

    /**
     * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
     */
    @SuppressLint("DefaultLocale")
    public SDUserEntity setUserHead(SDUserEntity user)
    {
        String headerName = user.getName();
        if (headerName != null && !headerName.isEmpty())
        {
            if (Character.isDigit(headerName.charAt(0)))
            {
                user.setHeader("#");
            } else
            {
                headerName = headerName.trim();
                user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1)
                        .toUpperCase());
                user.setCnName(user.getName());
                char header = user.getHeader().toLowerCase().charAt(0);
                if (header < 'a' || header > 'z')
                {
                    user.setHeader("#");
                    user.setCnName("#");
                }
            }
        }
        return user;
    }

    /**
     * 设置hearder属性，方便通讯中对部门按header分类显示，以及通过右侧ABCD...字母栏快速定位部门
     */
    @SuppressLint("DefaultLocale")
    public synchronized SDDepartmentEntity setDpHead(SDDepartmentEntity departmentEntity)
    {
        if (StringUtils.notEmpty(departmentEntity.getDpName()))
        {
            String headerName = departmentEntity.getDpName();
            if (Character.isDigit(headerName.charAt(0)))
            {
                departmentEntity.setHeader("#");
            } else
            {
                departmentEntity.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring
                        (0, 1).toUpperCase());
                char header = departmentEntity.getHeader().toLowerCase().charAt(0);
                if (header < 'a' || header > 'z')
                {
                    departmentEntity.setHeader("#");
                }
            }
        }
        return departmentEntity;
    }

    // 通过角色jobRole
    public synchronized List<SDUserEntity> findAllUserInSecretaryByRole(int no, int size, String jobRole, SearchType...
            searchTypes)
    {
        return findUsersOnLimitByRole(no, size, jobRole, searchTypes);
    }

    public long findAllCount()
    {
        long count = 0;
        try
        {
            count = db.count(Selector.from(SDUserEntity.class));
        } catch (DbException e)
        {
            e.printStackTrace();
        }

        return count;
    }

    /**
     * 通过searchTpye类型搜出对应的用户
     *
     * @param searchTypes
     * @return
     * @desc OUTSIDE, INSIDE_REL_OUTSIDE 不能同时存在
     */
    public synchronized List<SDUserEntity> findUsersOnLimitByRole(int no, int size, String jobRole, SearchType... searchTypes)
    {
        List<SDUserEntity> userEntities = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        // sb.append("SELECT * FROM SYS_USER");
        sb.append("SELECT u.USER_ID,u.REAL_NAME,u.JOB,u.SEX,d.dpName" + ", u.TELEPHONE,u.ACCOUNT,u.EMAIL,u.IM_ACCOUNT,u" +
                ".MANAGER_ID,u.UPDA_TETIME,u.STATUS,u.USER_TYPE " +
                " FROM SYS_USER u ,SYS_DEPARTMENT d ,USER_DEPT_REL ud " +
                " WHERE u.USER_ID=ud.USER_ID AND ud.dpId= d.dpId AND ");
        Arrays.sort(searchTypes);
        if (searchTypes != null && searchTypes.length > 0)
        {
            //sb.append(" WHERE");
            for (int i = 0; i < searchTypes.length; i++)
            {
                SearchType searchType = searchTypes[i];
                switch (searchType)
                {
                    case ALL:
                        sb.append(" 0 = 0");
                        break;
                    case ONJOB:
                        if (i == 0)
                        {
                            sb.append(" STATUS = 1");
                        } else
                        {
                            sb.append(" AND STATUS = 1");
                        }
                        break;
                    case QUIT:
                        if (i == 0)
                        {
                            sb.append(" STATUS = 0");
                        } else
                        {
                            sb.append(" AND STATUS = 0");
                        }
                        break;
                    case INSIDE:
                        if (i == 0)
                        {
                            sb.append(" USER_TYPE = 1");
                        } else
                        {
                            sb.append(" AND USER_TYPE = 1");
                        }
                        break;
                    case REMOVE_OWN:
                        if (i == 0)
                        {
                            sb.append(" USER_ID != " + userId);
                        } else
                        {
                            sb.append(" AND USER_ID != " + userId);
                        }
                        break;
                }
            }
            SDLogUtil.debug("sql==" + sb.toString());
        }
        sb.append(" AND u.jobRole = ");
        sb.append("'" + jobRole + "'");
        sb.append(" ORDER BY u.USER_ID LIMIT " + size * no + ", " + size);
        Cursor cursor = null;
        try
        {
            cursor = db.execQuery(sb.toString());
            while (cursor.moveToNext())
            {
                SDUserEntity userEntity = new SDUserEntity();
                int userIdIndex = cursor.getColumnIndex("USER_ID");
                int nameIndex = cursor.getColumnIndex("REAL_NAME");
                int telephoneIndex = cursor.getColumnIndex("TELEPHONE");
                int accountIndex = cursor.getColumnIndex("ACCOUNT");
                int EmailIndex = cursor.getColumnIndex("EMAIL");
                int jobIndex = cursor.getColumnIndex("JOB");
                int sexIndex = cursor.getColumnIndex("SEX");
                int imAccountIndex = cursor.getColumnIndex("IM_ACCOUNT");
                int dpName = cursor.getColumnIndex("dpName");
                int managerIdIndex = cursor.getColumnIndex("MANAGER_ID");
                int statusIndex = cursor.getColumnIndex("STATUS");
                int userType = cursor.getColumnIndex("USER_TYPE");


                userEntity.setEid(Integer.parseInt(cursor
                        .getString(userIdIndex)));
                userEntity.setName(cursor.getString(nameIndex));
                userEntity.setTelephone(cursor.getString(telephoneIndex));
                userEntity.setAccount(cursor.getString(accountIndex));
                userEntity.setEmail(cursor.getString(EmailIndex));
                userEntity.setJob(cursor.getString(jobIndex));
                userEntity.setSex(cursor.getInt(sexIndex));
                userEntity.setImAccount(cursor.getString(imAccountIndex));
                userEntity.setDeptName(cursor.getString(dpName));
//                userEntity.setIcon(cursor.getString(iconIndex));
                userEntity.setManagerId(cursor.getLong(managerIdIndex));
                userEntity.setStatus(cursor.getInt(statusIndex));
                userEntity.setUserType(cursor.getInt(userType));

                userEntities.add(userEntity);
            }

            return userEntities;
        } catch (DbException e)
        {
            e.printStackTrace();
            return userEntities;
        } finally
        {
            closeCusor(cursor);
        }
    }

}
