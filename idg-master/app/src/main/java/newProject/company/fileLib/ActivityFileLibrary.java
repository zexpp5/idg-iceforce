package newProject.company.fileLib;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseSubmitActivity;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.entity.VoiceEntity;
import com.http.FileUpload;
import com.http.SDResponseInfo;
import com.injoy.idg.R;
import com.utils.DialogImUtils;
import com.utils.DialogMeetingUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.invested_project.bean.BeanIceProject;
import newProject.view.DialogTextFilter;
import yunjing.utils.BaseDialogUtils;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.DialogFragmentProject;

//事项
public class ActivityFileLibrary extends BaseSubmitActivity
{
    @Bind(R.id.tabs)
    TabLayout mTabLayout;
    @Bind(R.id.vp_view)
    ViewPager mViewPager;
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    private String[] mTitleList = {"项目", "个人"};//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合

    MyPagerAdapter mAdapter;

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_file_library_main;
    }

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        //标题
        mNavigatorBar.setMidText(this.getString(R.string.iceforce_image_text_8));
        mNavigatorBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        //搜索
        mNavigatorBar.setRightImageVisible(true);
        mNavigatorBar.setRightImageResouce(R.mipmap.icon_public_search);
        mNavigatorBar.setRightImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DialogMeetingUtils.getInstance().showEditSomeThingDialog(ActivityFileLibrary.this, "搜索",
                        "请输入搜索关键字", "搜索", "取消", "请输入搜索关键字", new DialogMeetingUtils.onTitleClickListener()
                        {
                            @Override
                            public void setTitle(String s)
                            {
                                reFreshFragment(s);
                            }
                        });

            }
        });

        mNavigatorBar.setRightSecondImageVisible(true);
        mNavigatorBar.setRightSecondImageResouce(R.mipmap.icon_public_add);
        mNavigatorBar.setRightSecondImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showFileUpLoad();
            }
        });

        mViewList.clear();
        mViewList.add(new FragmentFileLib());
        mViewList.add(new FragmentPersonalFileLib());
        mAdapter = new ActivityFileLibrary.MyPagerAdapter(getSupportFragmentManager(),
                mViewList, mTitleList);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onSelectedImg(List<String> imgPaths)
    {
        this.imgPaths = imgPaths;
        showDoing("picture", "图片");
    }

    @Override
    public void onClickLocationFunction(double latitude, double longitude, String address)
    {

    }

    @Override
    public void onClickSendRange(List<SDUserEntity> userEntities)
    {

    }

    @Override
    public void onClickSendPerson(List<SDUserEntity> userEntities)
    {

    }

    @Override
    public void onClickAttach(List<File> pickerFile)
    {
        this.files = pickerFile;
        showDoing("file", "文件");

    }

    @Override
    public void onDelAttachItem(View v)
    {

    }

    @Override
    public void onClickVoice(VoiceEntity voiceEntity)
    {

    }

    @Override
    public int getDraftDataType()
    {
        return 0;
    }

    @Override
    public void onClickMoneyFile(String moneyString)
    {

    }

    //ViewPager适配器
    class MyPagerAdapter extends FragmentPagerAdapter
    {
        private List<Fragment> mViewList = new ArrayList<>();
        private String[] mTitle;

        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, String[] title)
        {
            super(fm);
            this.mViewList = fragmentList;
            this.mTitle = title;
        }

        @Override
        public int getCount()
        {
            return mViewList.size();//页卡数
        }

        @Override
        public Fragment getItem(int position)
        {
            return mViewList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return mTitle[position];//页卡标题
        }

    }

    List<BeanIceProject> beanIceProjectList = new ArrayList<>();

    private void showFileUpLoad()
    {
        beanIceProjectList.clear();
        beanIceProjectList.add(new BeanIceProject(0, "0", "拍照"));
        beanIceProjectList.add(new BeanIceProject(1, "1", "相册"));
        beanIceProjectList.add(new BeanIceProject(2, "2", "文件"));

        BaseDialogUtils.showDialogProject(ActivityFileLibrary.this, false, false, false, "", beanIceProjectList, new
                DialogFragmentProject.InputListener()
                {
                    @Override
                    public void onData(BeanIceProject content)
                    {
                        if (content.getKey().equals("0"))
                        {
                            takePhoto();
                        } else if (content.getKey().equals("1"))
                        {
                            takePicture();
                        } else if (content.getKey().equals("2"))
                        {
                            takeFile();
                        }
                    }
                });
    }

    private void showDoing(final String fileType, final String fileTip)
    {
        DialogTextFilter dialogTextFilter = new DialogTextFilter();
        dialogTextFilter.setTitleString("提 示");
        dialogTextFilter.setContentString("确定提交" + fileTip + "?");

        DialogImUtils.getInstance().showCommonDialog(this, dialogTextFilter, new DialogImUtils.OnYesOrNoListener()
        {
            @Override
            public void onYes()
            {
                List<File> tmpFiles = new ArrayList<File>();
                if (StringUtils.notEmpty(files) && files.size() > 0)
                {
                    for (int i = 0; i < files.size(); i++)
                    {
                        tmpFiles.clear();
                        tmpFiles.add(files.get(i));
                        postFile(fileType, fileTip, tmpFiles);
                    }
                }
            }

            @Override
            public void onNo()
            {

            }
        });
    }

    //
    private void postFile(String fileType, final String fileTip, List<File> tmpFiles)
    {
        ListHttpHelper.postFileLib(getApplication(), loginUserAccount, fileType, false, tmpFiles, imgPaths, null, new FileUpload
                .UploadListener()
        {
            @Override
            public void onSuccess(SDResponseInfo responseInfo, HashMap<String, Object> result)
            {
                if (responseInfo.getStatus() == 200)
                {
                    MyToast.showToast(ActivityFileLibrary.this, "上传" + fileTip + "成功");
                    if (StringUtils.notEmpty(files))
                        files.clear();
                    if (StringUtils.notEmpty(imgPaths))
                        imgPaths.clear();

                    if (StringUtils.notEmpty(mAdapter))
                    {
                        if (mAdapter.getItem(mViewPager.getCurrentItem()) instanceof FragmentPersonalFileLib)
                        {
                            FragmentPersonalFileLib fragmentFileLib = (FragmentPersonalFileLib) mAdapter.getItem(mViewPager
                                    .getCurrentItem());
                            fragmentFileLib.setQueryString("");
                        }
                    }
                }
            }

            @Override
            public void onProgress(int byteCount, int totalSize)
            {

            }

            @Override
            public void onFailure(Exception ossException)
            {

            }
        });
    }

    private void reFreshFragment(String queryStr)
    {
        if (StringUtils.notEmpty(mAdapter))
        {
            if (mAdapter.getItem(mViewPager.getCurrentItem()) instanceof FragmentPersonalFileLib)
            {
                FragmentPersonalFileLib fragmentPersonalFileLib = (FragmentPersonalFileLib) mAdapter.getItem(mViewPager
                        .getCurrentItem());
                fragmentPersonalFileLib.setQueryString(queryStr);
            }
            if (mAdapter.getItem(mViewPager.getCurrentItem()) instanceof FragmentFileLib)
            {
                FragmentFileLib fragmentFileLib = (FragmentFileLib) mAdapter.getItem(mViewPager
                        .getCurrentItem());
                fragmentFileLib.setQueryString(queryStr);
            }
        }
    }
}
