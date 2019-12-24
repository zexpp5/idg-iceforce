package newProject.ui.annual_meeting;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;

import com.cxgz.activity.cxim.base.BaseFragmentActivity;
import com.cc.emojilibrary.EmojiconEditText;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cx.dao.MyMenuItem;
import com.cxgz.activity.cxim.utils.MainTopMenuUtils;
import com.cxgz.activity.cxim.view.ResizeLayout;
import com.cxgz.activity.cxim.workCircle.utils.CommonUtils;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.ui.http.BasicDataHttpHelper;
import com.utils.DialogUtilsIm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import master.flame.danmaku.ui.widget.DanmakuView;
import newProject.company.vacation.WebVacationActivity;
import newProject.ui.annual_meeting.danmu.Danmu;
import newProject.ui.annual_meeting.danmu.DanmuControl;
import tablayout.view.BottomMenu;
import yunjing.processor.eventbus.UnReadDanmu;
import yunjing.view.CustomNavigatorBar;

import static com.injoy.idg.R.id.ll_main_item;

/**
 * Created by selson on 2018/1/8
 */
public class AnnualMeetingMainActivity extends BaseFragmentActivity
{
    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
    @Bind(R.id.btn_set_mode_keyboard)
    Button btnSetModeKeyboard;
    @Bind(R.id.et_sendmessage)
    EmojiconEditText etSendmessage;
    @Bind(R.id.btn_send)
    Button btnSend;
    @Bind(R.id.ll_edit_content)
    LinearLayout llEditContent;
    @Bind(R.id.ll_register)
    LinearLayout llRegister;
    @Bind(R.id.ll_interaction)
    LinearLayout llInteraction;
    @Bind(R.id.ll_annual_meeting)
    LinearLayout llAnnualMeeting;
    @Bind(ll_main_item)
    LinearLayout llMainItem;
    @Bind(R.id.danmaku_view)
    DanmakuView mDanmakuView;

    @Bind(R.id.ll_main)
    ResizeLayout ll_main; //主布局

    @Bind(R.id.bar_bottom)
    LinearLayout bar_bottom;

    @Bind(R.id.ll_test_01)
    LinearLayout ll_test_01;

    //默认
    public int height = 0;
    public int height1 = 0;
    public int height2 = 0;


//    @Bind(R.id.sv)
//    NoScroollView sv;

    //年会ID
    String mEid = "";
    //年会信息
    AnnualMeetingDetailBean annualMeetingDetailBean = null;

    private DanmuControl mDanmuControl;

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initTitle();

        initLintener();

        mDanmuControl = new DanmuControl(this);
        mDanmuControl.setDanmakuView(mDanmakuView);

        AnnualMeetingInfoFragment annualMeetingInfoFragment = new AnnualMeetingInfoFragment();
        replaceFragment(annualMeetingInfoFragment);

        postAnnualMeetingInfo();
    }

    private void initTitle()
    {
        titleBar.setMidText("公司年会");
        titleBar.setLeftImageVisible(true);
        titleBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    private void showAndHide()
    {
        if (llEditContent.getVisibility() == View.GONE && llMainItem.getVisibility() == View.VISIBLE)
        {
            llEditContent.setVisibility(View.VISIBLE);
            llMainItem.setVisibility(View.GONE);
        } else
        {
            //隐藏键盘
            CommonUtils.hideSoftInput(AnnualMeetingMainActivity.this, etSendmessage);

            llEditContent.setVisibility(View.GONE);
            llMainItem.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_work_annual_meeting_main;
    }

    @OnClick({R.id.btn_set_mode_keyboard, R.id.btn_send, R.id.ll_edit_content, R.id.ll_register, R.id
            .ll_interaction, R.id.ll_annual_meeting, ll_main_item})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_set_mode_keyboard:
                showAndHide();
                break;

            case R.id.btn_send:

                if (StringUtils.notEmpty(etSendmessage.getText().toString().trim()))
                {
//                    MyToast.showToast(AnnualMeetingMainActivity.this, msg);
                    sendDanmu(etSendmessage.getText().toString());
                }
                break;
            case R.id.ll_edit_content:

                break;
            case R.id.ll_register:
                firstTab();
                break;
            case R.id.ll_interaction: //互动
                hudong();
                break;
            case R.id.ll_annual_meeting:
                nianhui();
                break;
            case ll_main_item:


                break;
        }
    }

    //年会信息
    private void postAnnualMeetingInfo()
    {
        BasicDataHttpHelper.postNianInfo(AnnualMeetingMainActivity.this, new SDRequestCallBack(AnnualMeetingDetailBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                DialogUtilsIm.dialogPayFinish(AnnualMeetingMainActivity.this, "提 示", msg, "确定", "", new DialogUtilsIm
                        .OnYesOrNoListener2()

                {
                    @Override
                    public void onYes()
                    {
                        AnnualMeetingMainActivity.this.finish();
                    }
                });
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                annualMeetingDetailBean = (AnnualMeetingDetailBean) responseInfo.getResult();
                if (annualMeetingDetailBean.getStatus() == 200)
                {
                    getInfo();
                } else if (annualMeetingDetailBean.getStatus() == 400)
                {
                    DialogUtilsIm.dialogPayFinish(AnnualMeetingMainActivity.this, "提 示", annualMeetingDetailBean.getMsg(),
                            "确定", "", new DialogUtilsIm
                                    .OnYesOrNoListener2()

                            {
                                @Override
                                public void onYes()
                                {
                                    AnnualMeetingMainActivity.this.finish();
                                }
                            });
                }
            }
        });
    }

    private void getInfo()
    {
        if (annualMeetingDetailBean != null)
        {
            mEid = annualMeetingDetailBean.getData().getDetail().getEid() + "";
        }
    }

    private void sendDanmu(String danmuString)
    {
        if (StringUtils.notEmpty(mEid))
        {
            BasicDataHttpHelper.postNianDanmu(AnnualMeetingMainActivity.this, mEid, danmuString,
                    new SDRequestCallBack()
                    {
                        @Override
                        public void onRequestFailure(HttpException error, String msg)
                        {
                            MyToast.showToast(AnnualMeetingMainActivity.this, msg);
                        }

                        @Override
                        public void onRequestSuccess(SDResponseInfo responseInfo)
                        {
                            etSendmessage.setText("");
                            MyToast.showToast(AnnualMeetingMainActivity.this, R.string.request_succeed2);
                        }
                    });
        } else
        {
            postAnnualMeetingInfo();
        }
    }

    //年会信息
    private void firstTab()
    {
        List<MyMenuItem> menuItem = new ArrayList<>();
        menuItem.add(new MyMenuItem(this.getResources().getString(R.string.im_work_annual_m_first_01), R.mipmap.icon_group_chat));
        menuItem.add(new MyMenuItem(this.getResources().getString(R.string.im_work_annual_m_first_02), R.mipmap.icon_group_chat));

        MainTopMenuUtils.getInstance(AnnualMeetingMainActivity.this).showAnnualMeetingMenu(llRegister, menuItem, new
                BottomMenu.MenuItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        Intent intent = new Intent();
                        switch (position)
                        {
                            case 0:
                                //年会信息
                                AnnualMeetingInfoFragment annualMeetingInfoFragment = new AnnualMeetingInfoFragment();
                                replaceFragment(annualMeetingInfoFragment);
//                                        intent = new Intent(AnnualMeetingMainActivity.this, AnnualMeetingInfoActivity.class);
//                                        startActivity(intent);
                                break;

                            case 1:
                                //节目单
                                if (StringUtils.notEmpty(mEid))
                                {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("EID", Integer.parseInt(mEid));
                                    AnnualProgramFragment annualProgramFragment = new AnnualProgramFragment();
                                    annualProgramFragment.setArguments(bundle);
                                    replaceFragment(annualProgramFragment);
//                                            sv.smoothScrollTo(0, 0);
                                } else
                                {
                                    MyToast.showToast(AnnualMeetingMainActivity.this, "网络繁忙,请稍候再试");
                                }
                                break;

                        }
                    }
                });
    }


    //互动
    private void hudong()
    {
        List<MyMenuItem> menuItem = new ArrayList<>();
        menuItem.add(new MyMenuItem(this.getResources().getString(R.string.im_work_annual_m_second_01), R.mipmap
                .icon_group_chat));
        menuItem.add(new MyMenuItem(this.getResources().getString(R.string.im_work_annual_m_second_02), R.mipmap
                .icon_group_chat));

        MainTopMenuUtils.getInstance(AnnualMeetingMainActivity.this).showAnnualMeetingMenu(llInteraction, menuItem, new
                BottomMenu.MenuItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        Intent intent = new Intent();
                        switch (position)
                        {
                            case 0:
                                if (StringUtils.notEmpty(mEid))
                                {
                                    ProgramVotingFragment programVotingFragment = new ProgramVotingFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("EID", Integer.parseInt(mEid));
                                    programVotingFragment.setArguments(bundle);
                                    replaceFragment(programVotingFragment);

                                } else
                                {
                                    MyToast.showToast(AnnualMeetingMainActivity.this, "网络繁忙,请稍候再试");
                                }
                                break;

                            case 1:
                                //显示弹幕
//                                showAndHide();

                                if (StringUtils.notEmpty(mEid))
                                {
                                    IStarFragment iStarFragment = new IStarFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("EID", Integer.parseInt(mEid));
                                    iStarFragment.setArguments(bundle);
                                    replaceFragment(iStarFragment);

                                } else
                                {
                                    MyToast.showToast(AnnualMeetingMainActivity.this, "网络繁忙,请稍候再试");
                                }

//                                DialogUtilsIm.dialogPayFinish(AnnualMeetingMainActivity.this, "提 示", "XX-PK", "确定", "", new DialogUtilsIm
//                                        .OnYesOrNoListener2()
//
//                                {
//                                    @Override
//                                    public void onYes()
//                                    {
//                                        AnnualMeetingMainActivity.this.finish();
//                                    }
//                                });
                                break;
                        }
                    }
                });
    }

    //
    private void nianhui()
    {
        List<MyMenuItem> menuItem = new ArrayList<>();
        menuItem.add(new MyMenuItem(this.getResources().getString(R.string.im_work_annual_m_third_01), R.mipmap.icon_group_chat));

        MainTopMenuUtils.getInstance(AnnualMeetingMainActivity.this).showAnnualMeetingMenu(llAnnualMeeting,
                menuItem, new
                        BottomMenu.MenuItemClickListener()
                        {
                            @Override
                            public void onItemClick(View view, int position)
                            {
                                Intent intent = new Intent();
                                switch (position)
                                {
                                    case 0:
                                        if (StringUtils.notEmpty(annualMeetingDetailBean.getData().getDetail().getPicUrl()))
                                        {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("TITLE", "相册");
                                            bundle.putString("URL", annualMeetingDetailBean.getData().getDetail().getPicUrl() +
                                                    "");
                                            intent = new Intent(AnnualMeetingMainActivity.this, WebVacationActivity.class);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        } else
                                        {
                                            MyToast.showToast(AnnualMeetingMainActivity.this, "网络繁忙,请稍候再试");
                                        }
//                                        //中奖信息
//                                        if (StringUtils.notEmpty(mEid))
//                                        {
//                                            replaceFragment(WinningMeetingFragment.newInstance(Integer.parseInt(mEid)));
//                                        } else
//                                        {
//                                            MyToast.showToast(AnnualMeetingMainActivity.this, "网络繁忙,请稍候再试");
//                                        }
                                        break;

                                }
                            }
                        });
    }

    private void setData(UnReadDanmu info)
    {
        List<Danmu> danmus = new ArrayList<>();
//        Danmu danmu5 = new Danmu(0, 2, "Like", R.mipmap.temp_user_head, "");
        Danmu danmu1 = new Danmu(0, 1, "Comment", info.getIcon(), info.getMsg());
//        Danmu danmu3 = new Danmu(0, 3, "Comment", "", "这还是一条弹幕啦啦啦");
        danmus.add(danmu1);
        Collections.shuffle(danmus);
        mDanmuControl.addDanmuList(danmus);
    }

    /**
     * 推送接收
     *
     * @param
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onDanmuEvent(UnReadDanmu info)
    {
        if (info.isShow)
        {
            if (info.meetId.equals(mEid))
                setData(info);
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mDanmuControl.pause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mDanmuControl.resume();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mDanmuControl.destroy();
    }

    private boolean isKeyboardShown(View rootView)
    {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }

    private void initLintener()
    {
        ll_main.getRootView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                if (isKeyboardShown(ll_main.getRootView()))
                {
//                    MyToast.showToast(AnnualMeetingMainActivity.this, "弹出键盘:" + System.currentTimeMillis());
                }
            }
        });

        bar_bottom.post(new Runnable()
        {
            @Override
            public void run()
            {
                height = bar_bottom.getHeight();
            }
        });

        ll_test_01.post(new Runnable()
        {
            @Override
            public void run()
            {
                height1 = ll_test_01.getHeight();
            }
        });

        llMainItem.post(new Runnable()
        {
            @Override
            public void run()
            {
                height2 = llMainItem.getHeight();
            }
        });
    }
}
