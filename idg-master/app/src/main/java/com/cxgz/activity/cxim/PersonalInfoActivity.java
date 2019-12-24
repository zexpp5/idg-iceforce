package com.cxgz.activity.cxim;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.cxim.bean.PersonInfoBean;
import com.cxgz.activity.cxim.bean.PersonalListBean;
import com.cxgz.activity.cxim.http.ImHttpHelper;
import com.cxgz.activity.cxim.person.SDPersonalAlterActivity;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.superdata.im.constants.CxIMMessageType;
import com.utils.FileDownloadUtil;
import com.utils.SDImgHelper;
import com.utils.SDToast;
import com.utils.SPUtils;


/**
 * 进入个人通讯信息
 */
public class PersonalInfoActivity extends BaseActivity implements View.OnClickListener
{
    private SimpleDraweeView ivContactsHead;//头像
    private TextView info_userName;//用户名
    private String userId;
    private Button send_chat; //发企信
    private Button send_voice;//发语音
    private Button send_phone;//打电话
    private Button send_msm;//发短信
    private SDUserEntity userInfo;
    private ImageView webview_carousel;

    /**
     * 头像路径
     */
    private String imgPath = "", Path = "", oldicon = "";

    PersonInfoBean personInfoBean;

    @Override
    protected void init()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
        {
            finish();
            return;
        }

        setTitle("详细资料");
        setLeftBack(R.drawable.folder_back);

        ivContactsHead = (SimpleDraweeView) findViewById(R.id.info_headImg);
        info_userName = (TextView) findViewById(R.id.info_userName);
        send_chat = (Button) findViewById(R.id.send_chat);
        send_voice = (Button) findViewById(R.id.send_voice);
        send_phone = (Button) findViewById(R.id.send_phone);
        send_msm = (Button) findViewById(R.id.send_msm);

        webview_carousel = (ImageView) findViewById(R.id.webview_carousel);
//        Glide.with(PersonalInfoActivity.this)
//                .load(R.mipmap.pic_im_bg)
//                .centerCrop()
//                .crossFade()
//                .into(webview_carousel);

        send_chat.setOnClickListener(this);
        send_voice.setOnClickListener(this);
        send_phone.setOnClickListener(this);
        send_msm.setOnClickListener(this);
        //这个ImAccount
        userId = bundle.getString(SPUtils.USER_ID, "");
//        if (StringUtils.isEmpty(userDao))
//            userDao = new SDUserDao(this);
//        userInfo = userDao.findUserByUserID(userId);
        getPersonalInfo(userId);
//        PersonInfoBean personInfoBean = ImFriendUtils.searchPersonalInfoByImAccount(PersonalInfoActivity.this, userId);
    }

    private void setPersonalDetailInfo(PersonInfoBean personInfoBean)
    {
        userInfo = new SDUserEntity();
        userInfo.setIcon(personInfoBean.getIcon());
        userInfo.setName(personInfoBean.getName());
        userInfo.setImAccount(personInfoBean.getImAccount());
        if (personInfoBean.getSex() == 1)
            userInfo.setSex(1);
        else
            userInfo.setSex(2);
        userInfo.setAccount(personInfoBean.getAccount());
        userInfo.setUserType(1);
        userInfo.setTelephone(personInfoBean.getTelephone());
        userInfo.setPhone(personInfoBean.getPhone());

        if (userInfo != null)
        {
            showHeadImg(userInfo);
            info_userName.setText(userInfo.getName());
//            if (StringUtils.notEmpty(userInfo.getSex()) && userInfo.getSex() == 1)
//            {
//                info_sex.setBackgroundResource(R.mipmap.male);
//            } else
//            {
//                info_sex.setBackgroundResource(R.mipmap.female);
//            }
        } else
        {
            info_userName.setText(userId);
//            info_dept_value.setText("客户");
        }
    }

    private void getPersonalInfo(String imAccount)
    {
        ImHttpHelper.findPersonInfoByImAccount(PersonalInfoActivity.this, true, mHttpHelper, imAccount, new SDRequestCallBack
                (PersonalListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(PersonalInfoActivity.this, R.string.request_fail_data);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                PersonalListBean personalInfoList = (PersonalListBean) responseInfo.getResult();
                personInfoBean = personalInfoList.getData();
                if (personInfoBean != null)
                {
                    setPersonalDetailInfo(personInfoBean);
                }
            }
        });
    }

    @Override
    public void onClick(View view)
    {
        Intent intent = new Intent();

        if (StringUtils.notEmpty(userInfo))
        {
            String number = userInfo.getTelephone();
//            String number = userInfo.getPhone();
            String imUser = userInfo.getImAccount();
            String myImAccount = (String) SPUtils.get(PersonalInfoActivity.this, SPUtils.IM_NAME, "0");
            switch (view.getId())
            {
                case R.id.send_chat:

                    if (!imUser.equals(myImAccount))
                    {
                        String account = imUser;
                        Intent singleChatIntent = new Intent(PersonalInfoActivity.this, ChatActivity.class);
                        singleChatIntent.putExtra(ChatActivity.EXTR_CHAT_TYPE, CxIMMessageType.SINGLE_CHAT.getValue());
                        singleChatIntent.putExtra(ChatActivity.EXTR_CHAT_ACCOUNT, account);
                        startActivity(singleChatIntent);
                    }

                    break;
                case R.id.send_voice:
                    if (!imUser.equals(myImAccount))
                    {
                        String accounts = imUser;
                        Intent voiceIntent = new Intent(this, VoiceActivity.class);
                        voiceIntent.setAction(Intent.ACTION_VIEW);
                        voiceIntent.putExtra(VoiceActivity.IM_ACCOUND, accounts);
                        voiceIntent.putExtra(VoiceActivity.IS_CALL, true);
                        startActivity(voiceIntent);
                    }
                    break;
                case R.id.send_phone:

                    //打电话
                    if (number != null && !number.equals("null") && !number.equals(""))
                    {
                        intent.setAction(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + number));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    } else
                    {
                        SDToast.showShort("对方未录入电话");
                    }
                    break;

                case R.id.send_msm:
                    //发短信
                    if (number != null && !number.equals("null") && !number.equals(""))
                    {
                        intent.setAction(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("smsto:" + number));
                    } else
                    {
                        SDToast.showShort("对方未录入电话");
                    }
                    break;
            }
            if (intent.getComponent() != null || intent.getAction() != null)
            {
                startActivity(intent);
            }
        }
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_personal_info2;
    }

    /**
     * 显示头像
     *
     * @param icoUrl
     */
    protected void showHeadImg(final String icoUrl)
    {
        oldicon = FileDownloadUtil.getDownloadIP(0, icoUrl);
        SDImgHelper.getInstance(this).loadSmallImg(oldicon,
                R.mipmap.temp_user_head, ivContactsHead);
        ivContactsHead.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (StringUtils.notEmpty(userInfo.getUserId()))
                {
                    Intent intent = new Intent(PersonalInfoActivity.this, SDPersonalAlterActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(com.utils.SPUtils.USER_ID, userInfo.getImAccount() + "");
                    intent.putExtras(bundle);
                    PersonalInfoActivity.this.startActivity(intent);
                }
            }
        });
    }

    /**
     * 显示头像
     *
     * @param userEntity
     * @param userEntity
     */
    protected void showHeadImg(SDUserEntity userEntity)
    {
        if (userEntity != null)
        {
            showHeadImg(userEntity.getIcon());
        }
    }
}
