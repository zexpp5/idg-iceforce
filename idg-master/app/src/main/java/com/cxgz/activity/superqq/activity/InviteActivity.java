package com.cxgz.activity.superqq.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.injoy.idg.R;

import yunjing.utils.DisplayUtil;


/**
 * 邀请加入，复制邀请。
 */
public class InviteActivity extends BaseActivity
{
    private String goUrl = "";

    @Override
    protected void init()
    {
        setLeftBack(R.drawable.folder_back);
        setTitle("邀请加入");
        goUrl = DisplayUtil.getInviteUrl(InviteActivity.this);

    }


    private void getParams(final String host)
    {
//        String url = HttpURLUtil.newInstance().append("invite").append("save").toString();
//        RequestParams params = new RequestParams();
//        params.addBodyParameter("url", "companyId=" + companyId
//                + "&companyName=" + companyName +
//                "&inviteName=" + userName +
//                "&companyAccount=" + companyAccount);
//        mHttpHelper.post(url, params, true, new SDRequestCallBack() {
//            @Override
//            public void onRequestFailure(HttpException error, String msg) {
//
//            }
//
//            @Override
//            public void onRequestSuccess(SDResponseInfo responseInfo) {
//                try {
//                    JSONObject object = new JSONObject(responseInfo.result.toString());
//                    long id = object.getJSONObject("data").getLong("id");
//                    goUrl = host + "?inviteSysuserId=" + id;
//                    inviteText = userName + "邀请您加入【"
//                            + companyName +
//                            "】，请点击链接【"
//                            + goUrl +
//                            "】完成注册。【超享企业工作平台】";
//                    if (onClickPos == 0) {
//                        copyItem();
//                    } else {
//                        gotoErweima();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    public void copy(View view)
    {
        copyItem();
    }

    public void copyItem()
    {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", goUrl);
        cm.setPrimaryClip(mClipData);
        MyToast.showToast(InviteActivity.this, "邀请内容已经复制到剪切板");
    }

    public void erweima(View view)
    {
        gotoErweima();
    }

    public void gotoErweima()
    {
        if (StringUtils.notEmpty(goUrl))
        {
            Intent intent = new Intent(this, ErweimaActivity.class);
            intent.putExtra("text", goUrl);
            startActivity(intent);
        }
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_invite2;
    }


}
