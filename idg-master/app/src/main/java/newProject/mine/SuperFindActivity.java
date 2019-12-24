package newProject.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.cxgz.activity.cx.base.BaseActivity;
import com.cxgz.activity.cxim.ui.voice.list.VoiceMeetingListActivity;
import com.chaoxiang.base.config.Constants;
import com.injoy.idg.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.ApplyListActivity;
import newProject.bean.SearchFirstBean;
import tablayout.view.textview.FontTextView;
import yunjing.utils.BaseDialogUtils;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.DialogSuperUserFragment;


/**
 * Created by selson on 2017/7/21.
 * 超级用户搜索
 */
public class SuperFindActivity extends BaseActivity
{

    //一级弹框
    private List<SearchFirstBean> firstDirectoryList = new ArrayList<SearchFirstBean>();

    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
    @Bind(R.id.tv_first_directory)
    FontTextView tvFirstDirectory;
    @Bind(R.id.rl_first_directory_btn)
    RelativeLayout rlFirstDirectoryBtn;
    @Bind(R.id.tv_second_directory)
    FontTextView tvSecondDirectory;
    @Bind(R.id.rl_second_directory_btn)
    RelativeLayout rlSecondDirectoryBtn;
    @Bind(R.id.rl_search_btn)
    RelativeLayout rlSearchBtn;
    @Bind(R.id.edt_search_content)
    EditText edtSearchContent;

    List<String> firstList = new ArrayList<>();

    //搜索标识
    private int searchFristPosition = 1;
    //是否是单独没个列表（搜索）进来的判断
    private boolean isMoreSearch = false;

    private String searchString = "";

    @Override
    protected void init()
    {
        ButterKnife.bind(this);

        initFristList();
        //初始化数据
        setFirstList();

        initData();
        initHeadBar();
    }

    private void initFristList()
    {
        firstList.add(getResources().getString(R.string.super_find_first_01));
        firstList.add(getResources().getString(R.string.super_find_first_02));
       // firstList.add(getResources().getString(R.string.super_find_first_03));
      /*  firstList.add(getResources().getString(R.string.super_find_first_04));
        firstList.add(getResources().getString(R.string.super_find_first_05));
        firstList.add(getResources().getString(R.string.super_find_first_06));
        firstList.add(getResources().getString(R.string.super_find_first_07));
        firstList.add(getResources().getString(R.string.super_find_first_08));*/
    }

    private void initData()
    {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null)
        {
            searchFristPosition = bundle.getInt(Constants.SEARCH_FIRST_DICT);

            //默认的数据去初始化
            tvFirstDirectory.setText(firstDirectoryList.get(searchFristPosition - 1).getTitle());
            isMoreSearch = true;
        }
    }

    /**
     * 数据，一级列表的。
     */
    private void setFirstList()
    {
        //默认第一个
        tvFirstDirectory.setText(getResources().getString(R.string.super_find_first_01));
        searchFristPosition = 1;
        //shuju
        SearchFirstBean searchFirstBean;
        for (int i = 0; i < firstList.size(); i++)
        {
            searchFirstBean = new SearchFirstBean();
            searchFirstBean.setPosition(i + 1);
            searchFirstBean.setTitle(firstList.get(i));
            firstDirectoryList.add(searchFirstBean);
        }
    }

    /**
     * 第一个目录的dialog
     */
    private void showFirstDialog()
    {
        if (firstDirectoryList.size() > 0)
            BaseDialogUtils.showSuperUserDialog(SuperFindActivity.this, "主模块", false, firstDirectoryList, new
                    DialogSuperUserFragment.InputListener()
                    {
                        @Override
                        public void onData(SearchFirstBean content)
                        {
                            tvFirstDirectory.setText(content.getTitle());
                            searchFristPosition = content.getPosition();
                        }
                    });
    }

    /**
     * 初始化头布局
     */
    private void initHeadBar()
    {
        titleBar.setMidText("超级搜索");
        titleBar.setLeftImageVisible(true);
        titleBar.setLeftImageOnClickListener(mOnClickListener);
        titleBar.setTitleBarBackground(DisplayUtil.mTitleColor);
        if (isMoreSearch)
        {
            rlFirstDirectoryBtn.setVisibility(View.GONE);
        } else
        {
            rlFirstDirectoryBtn.setVisibility(View.VISIBLE);
        }


    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v == titleBar.getLeftImageView())
            {
                finish();
            }
        }
    };

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_super_admin_main;
    }

    @OnClick({R.id.rl_first_directory_btn, R.id.rl_second_directory_btn, R.id.rl_search_btn})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.rl_first_directory_btn:
                showFirstDialog();
                break;
            case R.id.rl_search_btn:
                searchString = edtSearchContent.getText().toString().trim();
                searchContent(searchString);
                /*if (!TextUtils.isEmpty(searchString)){

                }else{
                    Toast.makeText(SuperFindActivity.this,"请输入搜索关键词",Toast.LENGTH_SHORT).show();
                }
                break;*/
        }
    }


    /**
     * 搜索内容
     */
    private void searchContent(String searchString)
    {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putBoolean("HIDE",true);
        switch (searchFristPosition)
        {
            //公司通知
            case 1:
                intent.setClass(this, ApplyListActivity.class);
                bundle.putInt("CHOOSE", 4);
                break;
            //日常会议
            case 2:
                intent.setClass(this, ApplyListActivity.class);
                bundle.putInt("CHOOSE", 5);
                break;
            //语音会议
            case 3:
                intent.setClass(this, VoiceMeetingListActivity.class);
                bundle.putInt("CHOOSE", 1);
                break;
            //语音会议
            case 4:
                intent.setClass(this, VoiceMeetingListActivity.class);
                break;
            //外出工作
            case 5:
                intent.setClass(this, ApplyListActivity.class);
                bundle.putInt("CHOOSE", 2);
                break;
            //借支申请
            case 6:
                intent.setClass(this, ApplyListActivity.class);
                bundle.putInt("CHOOSE", 0);
                break;
            //请假申请
            case 7:
                intent.setClass(this, ApplyListActivity.class);
                bundle.putInt("CHOOSE", 1);
                break;
            //工作日志
            case 8:
                intent.setClass(this, ApplyListActivity.class);
                bundle.putInt("CHOOSE", 3);
                break;
        }

        bundle.putString(Constants.SEARCH, searchString);
        //if (!isMoreSearch)
        bundle.putString(Constants.IS_SEARCH, Constants.IS_SEARCH);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }


}
