package yunjing.ui.share;

import android.app.Activity;

import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.injoy.idg.R;
import com.utils.SPUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import yunjing.utils.ShareUMUtils;


/**
 * Created by selson on 2017/9/22.
 * 公告文件 分享类
 */

public class ShareAnnounceFile
{
    private ShareUMUtils.OnSelectShareToListener mOnSelectShareToListener;
    private ShareAnnounceFile()
    {
    }

    public static synchronized ShareAnnounceFile getInstance()
    {
        return ShareAnnounceFileHelper.shareAnnounceFile;
    }

    private static class ShareAnnounceFileHelper
    {
        private static final ShareAnnounceFile shareAnnounceFile = new ShareAnnounceFile();
    }

    /* which
     * 0, 公告列表
     * 1，部门列表
     * 2，文件列表
     * 3，审批列表
     */
    public String getShareAnnounceFileList(Activity activity, String page, int which)
    {

        String shareUrl = "";
        String typeUrl="";
        if (which==0){
            typeUrl="/comNotice/share/list/api";
        }else if (which==1){
            typeUrl="/deptNotice/share/list/api";
        }else if (which==2){
            typeUrl="/fileCenter/share/list/api";
        }else if (which==3){
            typeUrl="/approvalset/share/list/api/1";
        }
        int companyIdInt = (int) SPUtils.get(activity, SPUtils.COMPANY_ID, 0);
        String companyId=companyIdInt+"";
        String userId = (String) SPUtils.get(activity, SPUtils.USER_ID, "");

        if (StringUtils.notEmpty(companyId) && StringUtils.notEmpty(userId))
        {
            if (which==3){
                shareUrl = typeUrl
                        + StringUtils.append(userId)
                        + StringUtils.append(page);
            }else{
                shareUrl = typeUrl
                        + StringUtils.append(companyId)
                        + StringUtils.append(userId)
                        + StringUtils.append(page);
            }

        } else
        {
            MyToast.showToast(activity, activity.getResources().getString(R.string.hint_share));
        }
        return shareUrl;
    }

    public void setShareAnnounceListInfo(Activity activity, String searchValue, String searchParams,
                                         String pageNumber, int which, String shareTitle, String shareValue){
        //搜索内容
        String mSearch = searchValue;
        String searchString = searchParams+"=";
        String url = ShareAnnounceFile.getInstance().getShareAnnounceFileList(activity,pageNumber,which);
        if (StringUtils.notEmpty(mSearch))
        {   //后台要求必须转两次。
            try {
                searchString +=  URLEncoder.encode(URLEncoder.encode(mSearch,"utf-8"),"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else
        {
            searchString = "";
        }
        ShareUMUtils.showShareDialog(url, searchString,shareTitle, shareValue, activity, mOnSelectShareToListener);

    }

    public void setShareAnnounceDetailInfo(Activity activity, String eid, int which, String shareTitle, String shareValue){

        String url = ShareAnnounceFile.getInstance().getShareAnnounFileDetail(activity,eid,which);

        ShareUMUtils.showShareDialog(url, "",shareTitle, shareValue, activity, mOnSelectShareToListener);

    }

    /* which
     * 0, 公告详情
     * 1，部门详情
     * 2，文件详情
     * 3，审批详情 没有
     */
    public String getShareAnnounFileDetail(Activity activity, String eid,int which)
    {

        String shareUrl = "";
        String typeUrl="";
        if (which==0){
            typeUrl="/comNotice/share/detail";
        }else if (which==1){
            typeUrl="/deptNotice/share/detail";
        }else if (which==2){
            typeUrl="/fileCenter/share/detail";
        }else if (which==3){
            //typeUrl="/oa/comNotice/share/list/api/";
        }

        if (StringUtils.notEmpty(eid))
        {
            shareUrl = typeUrl
                    + StringUtils.append(eid);
        } else
        {
            MyToast.showToast(activity, activity.getResources().getString(R.string.hint_share));
        }
        return shareUrl;
    }


}
