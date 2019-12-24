package com.utils;

import android.app.Activity;
import android.content.Context;

import com.bean.UnReadNumBean;
import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.db.dao.SDUnreadDao;
import com.cxgz.activity.db.entity.SDUnreadEntity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.lidroid.xutils.exception.HttpException;
import com.ui.http.BasicDataHttpHelper;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import yunjing.processor.eventbus.UnReadMessage;

import static com.base.BaseApplication.map;


/**
 * Created by selson on 2018/9/19.
 */

public class ToolMainUtils
{
    //    trave（出差）
//    holiday（请假）
//    resumption（销假）
//    cost（报销）
    public static final String TYPE_TRAVE = "trave";
    public static final String TYPE_HOLIDAY = "holiday";
    public static final String TYPE_RESUMPTION = "resumption";
    public static final String TYPE_COST = "cost";

    private ToolMainUtils()
    {

    }

    public static synchronized ToolMainUtils getInstance()
    {
        return ToolMainUtilsHelper.toolMainUtils;
    }

    private static class ToolMainUtilsHelper
    {
        private static final ToolMainUtils toolMainUtils = new ToolMainUtils();
    }

    public void addCalendar()
    {


    }

    public void getUnreadNum(final Activity mContext, final String typeString)
    {
        BasicDataHttpHelper.getPushUnRead(mContext, typeString, new SDRequestCallBack(UnReadNumBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
//                MyToast.showToast(mContext, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                UnReadNumBean unReadNumBean = (UnReadNumBean) responseInfo.getResult();
                SDUnreadEntity sdUnreadEntity = null;
                switch (typeString)
                {
                    case TYPE_TRAVE:
                        sdUnreadEntity = SDUnreadDao.getInstance().findByMenuId(Constants.IM_PUSH_CLSP2);
                        break;
                    case TYPE_HOLIDAY:
                        sdUnreadEntity = SDUnreadDao.getInstance().findByMenuId(Constants.IM_PUSH_CK);
                        break;
                    case TYPE_RESUMPTION:
                        sdUnreadEntity = SDUnreadDao.getInstance().findByMenuId(Constants.IM_PUSH_XIAO2);
                        break;
                    case TYPE_COST:
                        sdUnreadEntity = SDUnreadDao.getInstance().findByMenuId(Constants.IM_PUSH_BSPS);
                        break;
                }
                if (sdUnreadEntity != null)
                {
                    if (unReadNumBean.getData() > 0)
                    {
                        sdUnreadEntity.setUnreadCount(unReadNumBean.getData());
                        SDUnreadDao.getInstance().update(sdUnreadEntity);
                    } else
                    {
                        sdUnreadEntity.setUnreadCount(0);
                        SDUnreadDao.getInstance().update(sdUnreadEntity);
                    }
                    if (StringUtils.notEmpty(unReadNumBean.getData()))
                        EventBus.getDefault().post(new UnReadMessage(true,
                                Integer.parseInt(typeString), unReadNumBean.getData()));
                }
            }
        });
    }

    /**
     * @param mContext
     * @param typeString
     * @param isNeedNotice 设置未读
     */
    public void setUnreadNum(final Activity mContext, final String typeString, boolean isNeedNotice)
    {
        BasicDataHttpHelper.getPushUnRead(mContext, typeString, new SDRequestCallBack(UnReadNumBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
//                MyToast.showToast(mContext, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                UnReadNumBean unReadNumBean = (UnReadNumBean) responseInfo.getResult();

                Map<String, String> map = new HashMap<String, String>();
                switch (typeString)
                {
                    case TYPE_TRAVE:
                        map.put("btype", Constants.IM_PUSH_CLSP2 + "");
                        map.put("msg", "您有【出差申请】待批审");
                        break;
                    case TYPE_HOLIDAY:
                        map.put("btype", Constants.IM_PUSH_CK + "");
                        map.put("msg", "您有【请假申请】待批审");
                        break;
                    case TYPE_RESUMPTION:
                        map.put("btype", Constants.IM_PUSH_XIAO2 + "");
                        map.put("msg", "您有【销假申请】待批审");
                        break;
                    case TYPE_COST:
                        map.put("btype", Constants.IM_PUSH_BSPS + "");
                        map.put("msg", "您有【报销申请】待批审");
                        break;
                }

                map.put("type", Constants.APPROVE_FOR_ME + "");
                map.put("from", "pushAdmin101");
                SDLogUtil.debug("push-type-主main的推送:" + map.get(SDUnreadDao.BTYPE) + " --数量：" + unReadNumBean.getData());
                unReaderChange(mContext, unReadNumBean.getData(), map);
            }
        });
    }

    public static void unReaderChange(Context context, int total, Map<String, String> map)
    {
        if (total > 0)
        {
            SDUnreadEntity sdUnreadEntity = SDUnreadDao.getInstance().findByMenuId(Integer.parseInt(map.get(SDUnreadDao.BTYPE)));
            if (sdUnreadEntity == null)
            {
                sdUnreadEntity = new SDUnreadEntity();
                sdUnreadEntity.setMenuId(Integer.parseInt(map.get(SDUnreadDao.BTYPE)));
                sdUnreadEntity.setBtype(Integer.parseInt(map.get("btype")));
                sdUnreadEntity.setRead(false);
                sdUnreadEntity.setUnreadCount(total);
                sdUnreadEntity.setType(Integer.parseInt(map.get("type")));
                sdUnreadEntity.setFromAccount(map.get("from"));
                sdUnreadEntity.setMsg(map.get("msg"));
                //二级目录
                SDUnreadDao.getInstance().save(sdUnreadEntity);
            } else
            {
//                sdUnreadEntity.setMenuId(Integer.parseInt(map.get(SDUnreadDao.BTYPE)));
                sdUnreadEntity.setUnreadCount(total);
                sdUnreadEntity.setRead(false);
                sdUnreadEntity.setMsg(map.get("msg"));
                SDUnreadDao.getInstance().update(sdUnreadEntity);
            }

            EventBus.getDefault().post(new UnReadMessage(true,
                    Integer.parseInt(map.get(SDUnreadDao.BTYPE)), total));
        } else
        {
            SDUnreadEntity sdUnreadEntity = SDUnreadDao.getInstance().findByMenuId(Integer.parseInt(map.get(SDUnreadDao.BTYPE)));
            if (sdUnreadEntity == null)
            {
                sdUnreadEntity = new SDUnreadEntity();
                sdUnreadEntity.setMenuId(Integer.parseInt(map.get(SDUnreadDao.BTYPE)));
                sdUnreadEntity.setBtype(Integer.parseInt(map.get("btype")));
                sdUnreadEntity.setRead(true);
                sdUnreadEntity.setUnreadCount(0);
                sdUnreadEntity.setType(Integer.parseInt(map.get("type")));
                sdUnreadEntity.setFromAccount(map.get("from"));
                sdUnreadEntity.setMsg(map.get("msg"));
                //二级目录
                SDUnreadDao.getInstance().save(sdUnreadEntity);
            } else
            {
//                sdUnreadEntity.setMenuId(Integer.parseInt(map.get(SDUnreadDao.BTYPE)));
                sdUnreadEntity.setUnreadCount(0);
                sdUnreadEntity.setRead(true);
                sdUnreadEntity.setMsg(map.get("msg"));
                SDUnreadDao.getInstance().update(sdUnreadEntity);
            }
        }
    }

    public void setPSUnRead(Activity mContext)
    {
        setUnreadNum(mContext, ToolMainUtils.TYPE_TRAVE, true);
        setUnreadNum(mContext, ToolMainUtils.TYPE_HOLIDAY, true);
        setUnreadNum(mContext, ToolMainUtils.TYPE_RESUMPTION, true);
        setUnreadNum(mContext, ToolMainUtils.TYPE_COST, true);
    }


}
