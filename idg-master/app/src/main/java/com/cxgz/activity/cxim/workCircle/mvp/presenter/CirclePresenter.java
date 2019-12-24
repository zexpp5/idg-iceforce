package com.cxgz.activity.cxim.workCircle.mvp.presenter;

import android.content.Context;
import android.view.View;

import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.bean.CircleModeBean;
import com.cxgz.activity.cxim.bean.CircleModelListBean;
import com.cxgz.activity.cxim.bean.TuiGuangBean;
import com.cxgz.activity.cxim.http.WorkRecordFilter;
import com.cxgz.activity.cxim.workCircle.bean.CircleItem;
import com.cxgz.activity.cxim.workCircle.bean.CommentConfig;
import com.cxgz.activity.cxim.workCircle.bean.CommentItem;
import com.cxgz.activity.cxim.workCircle.bean.RecordListBean;
import com.cxgz.activity.cxim.workCircle.bean.User;
import com.cxgz.activity.cxim.workCircle.bean.WorkCilcleDetailBean;
import com.cxgz.activity.cxim.workCircle.bean.WorkCilcleDetailListBean;
import com.cxgz.activity.cxim.workCircle.listener.IDataRequestListener;
import com.cxgz.activity.cxim.workCircle.mvp.contract.CircleContract;
import com.cxgz.activity.cxim.workCircle.mvp.modle.CircleModel;
import com.utils.FileUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * @author yiw
 * @ClassName: CirclePresenter
 * @Description: 通知model请求服务器和通知view更新
 * @date 2015-12-28 下午4:06:03
 */
public class CirclePresenter implements CircleContract.Presenter
{
    private CircleModel circleModel;
    private CircleContract.View view;

    public CirclePresenter(CircleContract.View view)
    {
        circleModel = new CircleModel();
        this.view = view;
    }

    public void loadData(int loadType)
    {
//        List<CircleItem> datas = DatasUtil.createCircleDatas();
//        if (view != null)
//        {
//            view.update2loadData(loadType, datas);
//        }
    }

    //加载详情数据
    public void loadDetailData(Context context, final int loadType, String s_btype, String l_eid)
    {
        circleModel.loadDetailData(context, s_btype, l_eid, new IDataRequestListener()
        {
            @Override
            public void loadSuccess(Object object)
            {
                WorkCilcleDetailListBean workCilcleDetailListBean = ((WorkCilcleDetailListBean) object);
                WorkCilcleDetailBean workCilcleDetailBean = workCilcleDetailListBean.getData();
                List<CircleItem> circleItemList = new ArrayList<CircleItem>();
                CircleItem tmpCilcleItem = new CircleItem();

                tmpCilcleItem.setContentTitle(workCilcleDetailBean.getName());
                tmpCilcleItem.setContent(workCilcleDetailBean.getRemark());

                tmpCilcleItem.setType("2");
                tmpCilcleItem.setCreateTime(workCilcleDetailBean.getCreateTime());
                tmpCilcleItem.setId(String.valueOf(workCilcleDetailBean.getEid()));
                //各种类型
                tmpCilcleItem.setLinkImg(String.valueOf(workCilcleDetailBean.getBtype()));
                tmpCilcleItem.setLinkTitle(workCilcleDetailBean.getName());

                //请假什么的
                if (StringUtils.notEmpty(workCilcleDetailBean.getStartDate()))
                    tmpCilcleItem.setStartDate(workCilcleDetailBean.getStartDate());
                if (StringUtils.notEmpty(workCilcleDetailBean.getEndDate()))
                    tmpCilcleItem.setEndDate(workCilcleDetailBean.getEndDate());
                //金额
                if (StringUtils.notEmpty(workCilcleDetailBean.getMoney()))
                    tmpCilcleItem.setMoney(workCilcleDetailBean.getMoney());

                List<CommentItem> commentItemList = new ArrayList<CommentItem>();
                List<RecordListBean> recordListBeanList = workCilcleDetailListBean.getData().getRecordList();
                if (recordListBeanList.size() > 0 && recordListBeanList != null)
                    for (int i = recordListBeanList.size(); i > 0; i--)
                    {
                        CommentItem commentItem = new CommentItem();
                        RecordListBean recordListBean = new RecordListBean();
                        recordListBean = recordListBeanList.get(i - 1);
//                        commentItem.setUser(new User(recordListBean.getImAccount(),
//                                recordListBean.getUserName(), recordListBean.getIcon()));

                        commentItem.setUser(new User(String.valueOf(recordListBean.getUserId()),
                                "", ""));

                        commentItem.setId(String.valueOf(recordListBean.getEid()));
                        commentItem.setContent(recordListBean.getRemark());
                        commentItem.setCreateTime(recordListBean.getCreateTime());
                        commentItem.setBtype(String.valueOf(recordListBean.getBtype()));
                        commentItemList.add(0, commentItem);
                    }
                tmpCilcleItem.setComments(commentItemList);

                List<String> photos = new ArrayList<String>();

                if (workCilcleDetailBean.getAnnexList() != null && workCilcleDetailBean.getAnnexList().size() > 0)
                {
                    for (int i = 0; i < workCilcleDetailBean.getAnnexList().size(); i++)
                    {
                        if (FileUtil.getNewFileType(workCilcleDetailBean.getAnnexList().get(i)) == 1)
                        {
                            String photo = workCilcleDetailBean.getAnnexList().get(i).getPath();
                            if (!photos.contains(photo))
                            {
                                photos.add(photo);
                            } else
                            {
                                i--;
                            }
                        }
                    }
                    tmpCilcleItem.setPhotos(photos);
                    //附件
                    tmpCilcleItem.setAnnexList(workCilcleDetailBean.getAnnexList());
                }

                //用户资料
//                tmpCilcleItem.setUser(new User(workCilcleDetailBean.getUserImAccount(),
//                        workCilcleDetailBean.getUname(), workCilcleDetailBean.getIcon()));

                tmpCilcleItem.setUser(new User(String.valueOf
                        (workCilcleDetailListBean.getData().getUserId()), "", ""));

                circleItemList.add(tmpCilcleItem);
                //转换数据
                if (view != null)
                {
                    view.update2loadDetailData(loadType, circleItemList);
                }
            }
        });
    }

    //加载数据
    public void loadPageData(Context context, int tagInt, final int loadType, int page)
    {
        circleModel.loadData(context, tagInt, page, new IDataRequestListener()
        {
            @Override
            public void loadSuccess(Object object)
            {
//                List<CircleItem> datas = DatasUtil.createCircleDatas();
                CircleModelListBean circleModelListBean = ((CircleModelListBean) object);
                List<CircleModeBean> circleModeBeanList = circleModelListBean.getData();
                List<CircleItem> circleDatas = new ArrayList<CircleItem>();
                for (int i = 0; i < circleModeBeanList.size(); i++)
                {
                    CircleItem circleItem = new CircleItem();
                    circleItem.setType("1");
                    circleItem.setCreateTime(circleModeBeanList.get(i).getCreateTime());
                    circleItem.setId(String.valueOf(circleModeBeanList.get(i).getEid()));
                    //各种类型
                    circleItem.setLinkImg(String.valueOf(circleModeBeanList.get(i).getBtype()));
                    circleItem.setLinkTitle(circleModeBeanList.get(i).getName());
                    //内容
                    circleItem.setContent(circleModeBeanList.get(i).getRemark());
                    circleItem.setComments(new ArrayList<CommentItem>());
                    //用户资料
                    circleItem.setUser(new User(circleModeBeanList.get(i).getSend_imAccount(),
                            circleModeBeanList.get(i).getSend_name(), circleModeBeanList.get(i).getSend_icon()));
                    //条数
                    circleItem.setTotal(circleModelListBean.getTotal());

                    circleDatas.add(circleItem);
                }

                //转换数据
                if (view != null)
                {
                    view.update2loadData2(loadType, circleDatas, circleModelListBean);
                }
            }
        });
    }

    /**
     * @param circleId
     * @return void    返回类型
     * @throws
     * @Title: deleteCircle
     * @Description: 删除动态
     */
    public void deleteCircle(final String circleId)
    {
        circleModel.deleteCircle(new IDataRequestListener()
        {
            @Override
            public void loadSuccess(Object object)
            {
                if (view != null)
                {
                    view.update2DeleteCircle(circleId);
                }
            }
        });
    }

    /**
     * @param circlePosition
     * @return void    返回类型
     * @throws
     * @Title: addFavort
     * @Description: 点赞
     */
    public void addFavort(final int circlePosition)
    {
        circleModel.addFavort(new IDataRequestListener()
        {

            @Override
            public void loadSuccess(Object object)
            {
//                FavortItem item = DatasUtil.createCurUserFavortItem();
//                if (view != null)
//                {
//                    view.update2AddFavorite(circlePosition, item);
//                }

            }
        });
    }

    /**
     * @param @param circlePosition
     * @param @param favortId
     * @return void    返回类型
     * @throws
     * @Title: deleteFavort
     * @Description: 取消点赞
     */
    public void deleteFavort(final int circlePosition, final String favortId)
    {
        circleModel.deleteFavort(new IDataRequestListener()
        {

            @Override
            public void loadSuccess(Object object)
            {
                if (view != null)
                {
                    view.update2DeleteFavort(circlePosition, favortId);
                }
            }
        });
    }

    /**
     * @param content
     * @param config  CommentConfig
     * @return void    返回类型
     * @throws
     * @Title: addComment
     * @Description: 增加评论
     */
    public void addComment(Context context, final User user,
                           final WorkRecordFilter content,
                           final CommentConfig config)
    {
        if (config == null)
        {
            return;
        }

        circleModel.addComment(context, content, new IDataRequestListener()
        {
            @Override
            public void loadSuccess(Object object)
            {
                CommentItem newItem = null;
                if (config.commentType == CommentConfig.Type.PUBLIC)
                {
                    newItem = new CommentItem();
                    newItem.setContent(content.getS_remark());
                    newItem.setUser(user);
                } else if (config.commentType == CommentConfig.Type.REPLY)
                {
//                    newItem = DatasUtil.createReplyComment(config.replyUser, content.getS_remark());
                }
                if (view != null)
                {
                    view.update2AddComment(config.circlePosition, newItem);
                }
            }

        });
    }

    /**
     * @param @param circlePosition
     * @param @param commentId
     * @return void    返回类型
     * @throws
     * @Title: deleteComment
     * @Description: 删除评论
     */
    public void deleteComment(final int circlePosition, final String commentId)
    {
        circleModel.deleteComment(new IDataRequestListener()
        {

            @Override
            public void loadSuccess(Object object)
            {
                if (view != null)
                {
                    view.update2DeleteComment(circlePosition, commentId);
                }
            }

        });
    }

    /**
     * @param commentConfig
     */
    public void showEditTextBody(CommentConfig commentConfig)
    {
        if (view != null)
        {
            view.updateEditTextBodyVisible(View.VISIBLE, commentConfig);
        }
    }

    public void showEditTextBodyDisVisable(CommentConfig commentConfig)
    {
        if (view != null)
        {
            view.updateEditTextBodyVisible(View.GONE, commentConfig);
        }
    }

    //用于在adapter中回到到Activity专用
    public void setIsOrDisAgreeComment(int circlePosition, WorkRecordFilter workRecordFilter)
    {
        if (view != null)
        {
            view.update2AddComment(circlePosition, workRecordFilter);
        }
    }


    /**
     * 清除对外部对象的引用，反正内存泄露。
     */
    public void recycle()
    {
        this.view = null;
    }
}
