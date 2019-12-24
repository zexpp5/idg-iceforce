package newProject.company.project_manager.investment_project;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chaoxiang.base.config.Constants;
import com.cxgz.activity.cx.base.BaseActivity;
import com.injoy.idg.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import newProject.company.project_manager.investment_project.bean.MenuBean;
import newProject.company.tools.ToolsListActivity;
import newProject.company.vacation.WebVacationActivity;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;



public class EmployeeGuideActivity extends BaseActivity
{
    private CustomNavigatorBar mTitleBar;
    private LinkedList<MenuItemView> mItemViewList = new LinkedList<>();
    private List<MenuBean.DataBean> mList = new ArrayList<>();
    private LinearLayout mMenuContainerLayout;
    private int mColumn = 3;
    private LinearLayout.LayoutParams mParams;
    private LinearLayout mLayout;
    private LinearLayout mLayout2;

    @Override
    protected void init()
    {
        initViews();
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_project_2;
    }

    private void initViews()
    {
        mTitleBar = (CustomNavigatorBar) findViewById(R.id.title_bar);
        mTitleBar.setMidText("员工指南");
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
        mMenuContainerLayout = (LinearLayout) findViewById(R.id.menu_container_layout);
        setMenu();
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {

            for (int i = 0; i < mItemViewList.size(); i++)
            {
                if (v == mItemViewList.get(i))
                {
                    toJump(i);
                    break;
                }
            }
        }
    };

    private void toJump(int i)
    {
        if (mItemViewList.get(i).getMenuInfo().equals("0"))
        {
            //1=规章制度，2=行政办公
            startActivity(new Intent(EmployeeGuideActivity.this, ToolsListActivity.class).putExtra("i_kind", 1));
        } else if (mItemViewList.get(i).getMenuInfo().equals("1"))
        {
            startActivity(new Intent(EmployeeGuideActivity.this, ToolsListActivity.class).putExtra("i_kind", 2));
        }
        else if (mItemViewList.get(i).getMenuInfo().equals("2"))
        {
            startActivity(new Intent(EmployeeGuideActivity.this, ToolsListActivity.class).putExtra("i_kind", 3));
        }
//        else if (mItemViewList.get(i).getMenuInfo().equals("2"))
//        {
//
//            Bundle bundle = new Bundle();
//            bundle.putString("TITLE", "差旅制度");
//            bundle.putString("URL", Constants.API_SERVER_URL + "/rule/travel.htm");
//            Intent intent = new Intent(EmployeeGuideActivity.this, WebVacationActivity.class);
//            intent.putExtras(bundle);
//            startActivity(intent);
//
//        } else if (mItemViewList.get(i).getMenuInfo().equals("3"))
//        {
//            Bundle bundle = new Bundle();
//            bundle.putString("TITLE", "年假制度");
//            bundle.putString("URL", Constants.API_SERVER_URL + "/rule/holiday.htm");
//            Intent intent = new Intent(EmployeeGuideActivity.this, WebVacationActivity.class);
//            intent.putExtras(bundle);
//            startActivity(intent);
//        }
    }

    private String[] mListString = {"规章制度", "行政办公", "常用模板", "年假制度"};

    private void setMenu()
    {
        mItemViewList.clear();
        if (mMenuContainerLayout.getChildCount() > 0)
        {
            mMenuContainerLayout.removeAllViews();
        }

        mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams
                .WRAP_CONTENT);

        //判断是否是最后一行,不等于0不是最后一行
        mLayout = new LinearLayout(EmployeeGuideActivity.this);
        mLayout.setPadding(0, DisplayUtil.getRealPixel2(20), 0, 0);
        mLayout.setWeightSum(3);
        mLayout.setOrientation(LinearLayout.HORIZONTAL);
        mMenuContainerLayout.addView(mLayout, mParams);

        for (int i = 0; i < mColumn; i++)
        {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            MenuItemView itemView = new MenuItemView(EmployeeGuideActivity.this);
            itemView.setGravity(Gravity.CENTER);
            if (i == 0)
            {
                itemView.setMenuImage(R.mipmap.icon_guizhang);
            } else if (i == 1)
            {
                itemView.setMenuImage(R.mipmap.icon_xingzheng);
            }
            else if (i == 2)
            {
                itemView.setMenuImage(R.mipmap.icon_usual);
            }
            itemView.setMenuInfo(i + "");
            itemView.setMenuText(mListString[i]);
            mLayout.addView(itemView, params);
            mItemViewList.add(itemView);
        }
//        mLayout2 = new LinearLayout(EmployeeGuideActivity.this);
//        mLayout2.setPadding(0, DisplayUtil.getRealPixel2(20), 0, 0);
//        mLayout2.setWeightSum(3);
//        mLayout2.setOrientation(HORIZONTAL);
//        mMenuContainerLayout.addView(mLayout2, mParams);
//
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
//        MenuItemView itemView = new MenuItemView(EmployeeGuideActivity.this);
//        itemView.setGravity(Gravity.CENTER);
//        itemView.setMenuImage(R.mipmap.travel_icon);
//        itemView.setMenuInfo(mColumn + "");
//        itemView.setMenuText(mListString[mColumn]);
//        mLayout2.addView(itemView, params);
//        mItemViewList.add(itemView);

        for (int k = 0; k < mItemViewList.size(); k++)
        {
            mItemViewList.get(k).setOnClickListener(mOnClickListener);
        }
    }
}
