package newProject.company.vacation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cx.base.BaseActivity;
import com.cxgz.activity.cxim.utils.UnReadUtils;
import com.cxgz.activity.db.dao.SDUnreadDao;
import com.chaoxiang.base.config.Constants;
import com.injoy.idg.R;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import newProject.ui.news.NewsListActivity;
import tablayout.view.textview.FontTextView;
import yunjing.processor.eventbus.UnReadMessage;
import yunjing.utils.DisplayUtil;
import yunjing.utils.FragmentCallBackInterface;
import yunjing.view.CustomNavigatorBar;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class VacationActivity extends BaseActivity implements FragmentCallBackInterface
{
    private FragmentManager mFragmentManager;
    private Fragment mSelectFragment;
    private CustomNavigatorBar mTitleBar;
    private View mBottomLine;
    private TextView mTitle;
    private ImageView mHeadImage;
    private RelativeLayout mOneLayout, mTwoLayout, mThreeLayout, mFourLayout, mFiveLayout;
    private ImageView mOneImage, mTwoImage, mThreeImage, mFourImage, mFiveImage;
    private TextView mOneText, mTwoText, mThreeText, mFourText, mFiveText;
    private FontTextView mOneMSGText, mTwoMSGText, mThreeMSGText, mFourMSGText, mFiveMSGText;

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //休假
//        geta1();
        setMsgTextVisible(5, SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_PUSH_HOLIDAY) + "", true);
    }

    @Override
    protected void init()
    {
        EventBus.getDefault().register(this);
        initViews();
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_vacation;
    }


    private void initViews()
    {
        mTitleBar = (CustomNavigatorBar) findViewById(R.id.title_bar);
        mTitleBar.setMidText("人事");
        mTitleBar.setLeftImageVisible(true);
        mTitleBar.getLeftImageView().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        mTitleBar.setRightTextVisible(false);
        mTitleBar.setRightImageVisible(false);
        mTitleBar.setRightSecondImageVisible(false);
        mHeadImage = (ImageView) findViewById(R.id.head_image);
        mTitle = (TextView) findViewById(R.id.item_title);
        mOneLayout = (RelativeLayout) findViewById(R.id.item_one);
        mTwoLayout = (RelativeLayout) findViewById(R.id.item_two);
        mThreeLayout = (RelativeLayout) findViewById(R.id.item_three);
        mFourLayout = (RelativeLayout) findViewById(R.id.item_four);
        mFiveLayout = (RelativeLayout) findViewById(R.id.item_five);

        mOneImage = (ImageView) findViewById(R.id.item_one_image);
        mTwoImage = (ImageView) findViewById(R.id.item_two_image);
        mThreeImage = (ImageView) findViewById(R.id.item_three_image);
        mFourImage = (ImageView) findViewById(R.id.item_four_image);
        mFiveImage = (ImageView) findViewById(R.id.item_five_image);

        mOneText = (TextView) findViewById(R.id.item_one_text);
        mTwoText = (TextView) findViewById(R.id.item_two_text);
        mThreeText = (TextView) findViewById(R.id.item_three_text);
        mFourText = (TextView) findViewById(R.id.item_four_text);
        mFiveText = (TextView) findViewById(R.id.item_five_text);

        mOneMSGText = (FontTextView) findViewById(R.id.unread_msg_number_work_one);
        mTwoMSGText = (FontTextView) findViewById(R.id.unread_msg_number_work_two);
        mThreeMSGText = (FontTextView) findViewById(R.id.unread_msg_number_work_three);
        mFourMSGText = (FontTextView) findViewById(R.id.unread_msg_number_work_four);
        mFiveMSGText = (FontTextView) findViewById(R.id.unread_msg_number_work_five);

        mBottomLine = findViewById(R.id.bottom_line);
        initVars();
    }


    private void initVars()
    {
        for (int i = 1; i < 6; i++)
        {
            getLayout(i).setOnClickListener(mOnClickListener);
        }

    }

    //替换fragment，使用replace节省内存
    public void replaceFragment(Fragment fragment)
    {
        if (fragment == null)
        {
            return;
        }
        if (mFragmentManager == null)
        {
            mFragmentManager = getSupportFragmentManager();
        }
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.replace(R.id.apply_fragment_container, fragment);
        ft.commit();
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v == mOneLayout)
            {
                UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_QJ);
                startActivity(new Intent(VacationActivity.this, VacationListActivity.class));
                setMsgTextVisible(1, "0", false);

            } else if (v == mTwoLayout)
            {
                VacationFragment fragment = new VacationFragment();
                replaceFragment(fragment);
            } else if (v == mThreeLayout)
            {
                Bundle bundle = new Bundle();
                bundle.putString("TITLE", "休假制度");
                bundle.putString("URL", Constants.API_SERVER_URL + "/rule/holiday.htm");
                Intent intent = new Intent(VacationActivity.this, WebVacationActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            } else if (v == mFourLayout)
            {
                Bundle bundle = new Bundle();
                bundle.putString("TITLE", "差旅制度");
                bundle.putString("URL", Constants.API_SERVER_URL + "/rule/travel.htm");
                Intent intent = new Intent(VacationActivity.this, WebVacationActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            } else if (v == mFiveLayout)
            {
                UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_PUSH_HOLIDAY);
                Intent intent = new Intent(VacationActivity.this, NewsListActivity.class);
                startActivity(intent);
                setMsgTextVisible(5, "0", false);
            } else if (v == mTitleBar.getLeftImageView() || v == mTitleBar.getLeftText())
            {
                finish();
            }
        }
    };


    /**
     * 设置 item 可见隐藏
     *
     * @param which
     * @param visibility
     */
    public void setLayoutVisible(int which, int visibility)
    {
        if (which == 1)
        {
            mOneLayout.setVisibility(visibility);
        } else if (which == 2)
        {
            mTwoLayout.setVisibility(visibility);
        } else if (which == 3)
        {
            mThreeLayout.setVisibility(visibility);
        } else if (which == 4)
        {
            mFourLayout.setVisibility(visibility);
        } else if (which == 5)
        {
            mFiveLayout.setVisibility(visibility);
        }
    }

    public void setTitleText(String title)
    {
        mTitle.setText(title);
    }

    /**
     * 设置 item 文字图标
     *
     * @param which
     * @param text
     */
    public void setItemTextAndImage(int which, String text, int resource)
    {
        if (which == 1)
        {
            mOneText.setText(text);
            mOneImage.setBackgroundResource(resource);
        } else if (which == 2)
        {
            mTwoText.setText(text);
            mTwoImage.setBackgroundResource(resource);
        } else if (which == 3)
        {
            mThreeText.setText(text);
            mThreeImage.setBackgroundResource(resource);
        } else if (which == 4)
        {
            mFourText.setText(text);
            mFourImage.setBackgroundResource(resource);
        } else if (which == 5)
        {
            mFiveText.setText(text);
            mFiveImage.setBackgroundResource(resource);
        }
    }

    /**
     * 设置 item 颜色
     *
     * @param which
     * @param color
     */
    public void setItemTextColor(int which, int color)
    {
        if (which == 1)
        {
            mOneText.setTextColor(color);
        } else if (which == 2)
        {
            mTwoText.setTextColor(color);
        } else if (which == 3)
        {
            mThreeText.setTextColor(color);
        } else if (which == 4)
        {
            mFourText.setTextColor(color);
        } else if (which == 5)
        {
            mFiveText.setTextColor(color);
        }
    }

    /**
     * 设置 item 图片
     *
     * @param which
     * @param resource
     */
    public void setItemImage(int which, int resource)
    {
        if (which == 1)
        {
            mOneImage.setBackgroundResource(resource);
        } else if (which == 2)
        {
            mTwoImage.setBackgroundResource(resource);
        } else if (which == 3)
        {
            mThreeImage.setBackgroundResource(resource);
        } else if (which == 4)
        {
            mFourImage.setBackgroundResource(resource);
        } else if (which == 5)
        {
            mFiveImage.setBackgroundResource(resource);
        }
    }

    public RelativeLayout getLayout(int which)
    {
        if (which == 1)
        {
            return mOneLayout;
        } else if (which == 2)
        {
            return mTwoLayout;
        } else if (which == 3)
        {
            return mThreeLayout;
        } else if (which == 4)
        {
            return mFourLayout;
        } else if (which == 5)
        {
            return mFiveLayout;
        } else
        {
            return null;
        }
    }

    public FontTextView getMSGText(int which)
    {
        if (which == 1)
        {
            return mOneMSGText;
        } else if (which == 2)
        {
            return mTwoMSGText;
        } else if (which == 3)
        {
            return mThreeMSGText;
        } else if (which == 4)
        {
            return mFourMSGText;
        } else if (which == 5)
        {
            return mFiveMSGText;
        } else
        {
            return null;
        }
    }

    public void setHideView(boolean isHide)
    {
        if (isHide)
        {
            mBottomLine.setVisibility(INVISIBLE);
        } else
        {
            mBottomLine.setVisibility(VISIBLE);
        }
    }

    public void setHeadImage(int resource)
    {
        mHeadImage.setBackgroundResource(resource);
    }

    /**
     * 推送接收
     *
     * @param
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onUnReadEvent(UnReadMessage info)
    {
        if (info.isShow)
        {
            //显示小红点
            switch (info.menuId)
            {
                case Constants.IM_PUSH_QJ: //请假
                    setMsgTextVisible(1, info.count + "", true);
                    break;
                //消息
                case Constants.IM_PUSH_PUSH_HOLIDAY:
                    setMsgTextVisible(5, info.count + "", true);
                    break;
            }
        } else
        {
            //隐藏小红点
            switch (info.menuId)
            {
                case Constants.IM_PUSH_QJ: //请假
                    setMsgTextVisible(1, info.count + "", false);
                    break;
                //消息
                case Constants.IM_PUSH_PUSH_HOLIDAY:
                    setMsgTextVisible(5, info.count + "", false);
                    break;
            }
        }
    }

    /**
     * @param which 第几个 0-3
     * @param count 数字
     */
    public void setMsgTextVisible(int which, String count, boolean isShow)
    {
        if (StringUtils.notEmpty(count))
        {
            if (Integer.parseInt(count) > 0)
            {
                getMSGText(which).setText(String.valueOf(count));
                if (isShow)
                {
                    getMSGText(which).setVisibility(View.VISIBLE);
                } else
                {
                    getMSGText(which).setVisibility(View.GONE);
                }
            } else
            {
                getMSGText(which).setVisibility(View.GONE);
            }
        } else
        {
            getMSGText(which).setVisibility(View.GONE);
        }
    }

    @Override
    public void setSelectedFragment(Fragment fragment)
    {
        mSelectFragment = fragment;
    }

    @Override
    public void refreshList()
    {

    }

    @Override
    public void callBackObject(Object object)
    {

    }

    @Override
    public void onBackPressed()
    {
        //直接返回
        if (mSelectFragment == null)
        {
            super.onBackPressed();
        } else
        {
            //进入fragment 返回
            mFragmentManager.beginTransaction().remove(mSelectFragment).commit();
            mSelectFragment.onDestroyView();
            mSelectFragment = null;
            DisplayUtil.hideInputSoft(this);

        }
    }
}
