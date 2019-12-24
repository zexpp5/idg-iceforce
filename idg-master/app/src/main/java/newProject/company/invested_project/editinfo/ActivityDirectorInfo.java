package newProject.company.invested_project.editinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.ImageUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.http.HttpURLUtil;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.DialogImUtils;
import com.utils.slidedatetimepicker.SlideDateTimeListener;
import com.utils.slidedatetimepicker.SlideDateTimePicker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import newProject.company.invested_project.InVestedProjectInfoActivity;
import newProject.company.invested_project.bean.BeanDirector;
import newProject.company.invested_project.bean.BeanIceProject;
import newProject.company.invested_project.bean.BeanSearchFile;
import newProject.company.invested_project.bean.PostDirectorParameter;
import newProject.company.vacation.WebVacationActivity;
import newProject.utils.HttpHelperUtils;
import newProject.view.DialogTextFilter;
import tablayout.view.textview.FontEditext;
import tablayout.view.textview.FontTextView;
import yunjing.utils.BaseDialogUtils;
import yunjing.utils.DisplayUtil;
import yunjing.utils.DividerItemDecoration2;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.DialogFragmentProject;

import static android.media.CamcorderProfile.get;
import static com.injoy.idg.R.id.edt_program_content;
import static com.injoy.idg.R.id.edt_program_director_info;
import static com.injoy.idg.R.id.fanatic_approval;
import static newProject.company.invested_project.InVestedProjectInfoActivity.mRequestCode7;

/**
 * Created by selson on 2019/5/7.
 * 新增董事会信息
 */
public class ActivityDirectorInfo extends BaseActivity
{
    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.edt_program_project_name)
    FontEditext edtProgramProjectName;
    @Bind(R.id.edt_program_time)
    FontTextView edtProgramTime;
    @Bind(edt_program_director_info)
    FontEditext edtProgramDirectorInfo;
    @Bind(R.id.edt_program_flag_type)
    FontTextView edtProgramFlagType;
    @Bind(edt_program_content)
    FontEditext edtProgramContent;
    @Bind(R.id.tv_save)
    TextView tvSave;
    @Bind(R.id.ll_save)
    LinearLayout llSave;
    @Bind(R.id.radioButton1)
    RadioButton radioButton1;
    @Bind(R.id.radioButton2)
    RadioButton radioButton2;
    @Bind(R.id.radio_group)
    RadioGroup radioGroup;

    @Bind(R.id.ll_view_01)
    LinearLayout ll_view_01;
    @Bind(R.id.ll_view_02)
    LinearLayout ll_view_02;

    @Bind(R.id.ll_file)
    RelativeLayout ll_file;

    @Bind(R.id.tv_choose_file)
    FontTextView tv_choose_file;

    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;

    private String tmpProjectName = "";

    private String tmpprojId = "";
    private String tmpUserName = "";
    private String tmpImportantFlag = "0";
    private String tmpImportantFlagType = "";

    BeanDirector.DataBeanX.DataBean dataBeanEdit = null;
    private boolean isEdit = false;

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_invested_add_director;
    }

    @Override
    protected void init()
    {
        tmpUserName = loginUserAccount;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            tmpprojId = bundle.getString(Constants.PROJECT_EID);
            tmpProjectName = bundle.getString(Constants.PROJECT_NAME);
            isEdit = bundle.getBoolean(Constants.TYPE_EDIT, false);
            if (isEdit)
            {
                String jsonString = bundle.getString(Constants.COMMON_INFO);
                dataBeanEdit = SDGson.toObject(jsonString, BeanDirector.DataBeanX.DataBean.class);
            }
        }

        ButterKnife.bind(this);
        titleBar.setLeftImageVisible(true);
        titleBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isEdit)
                {
                    DialogTextFilter dialogTextFilter = new DialogTextFilter();
                    dialogTextFilter.setTitleString("提 示");
                    dialogTextFilter.setYesString("确定");
                    dialogTextFilter.setNoString("取消");
                    dialogTextFilter.setContentString("放弃当前的修改?");

                    DialogImUtils.getInstance().showCommonDialog(ActivityDirectorInfo.this, dialogTextFilter, new
                            DialogImUtils.OnYesOrNoListener()
                            {
                                @Override
                                public void onYes()
                                {
                                    finish();
                                }

                                @Override
                                public void onNo()
                                {

                                }
                            });
                } else
                {
                    finish();
                }
                finish();
            }
        });

        edtProgramProjectName.setText(tmpProjectName);
        radioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    tmpImportantFlag = "1";
                    ll_view_01.setVisibility(View.VISIBLE);
                    ll_view_02.setVisibility(View.VISIBLE);
                }
            }
        });

        radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    tmpImportantFlag = "0";
                    ll_view_01.setVisibility(View.GONE);
                    ll_view_02.setVisibility(View.GONE);
                }
            }
        });

        if (isEdit)
        {
            titleBar.setMidText("编辑董事会信息");
            if (StringUtils.notEmpty(dataBeanEdit.getProjName()))

                edtProgramProjectName.setText(dataBeanEdit.getProjName());

            edtProgramTime.setEnabled(false);
            if (StringUtils.notEmpty(dataBeanEdit.getMeetingDateStr()))
                edtProgramTime.setText(DateUtils.getDate("yyyy-MM-dd", dataBeanEdit.getMeetingDateStr()));
            if (StringUtils.notEmpty(dataBeanEdit.getContent()))
                edtProgramDirectorInfo.setText(dataBeanEdit.getContent());
            if (StringUtils.notEmpty(dataBeanEdit.getDecisionTypeStr()))
                edtProgramFlagType.setText(dataBeanEdit.getDecisionTypeStr());
            if (StringUtils.notEmpty(dataBeanEdit.getMeetingDesc()))
                edtProgramContent.setText(dataBeanEdit.getMeetingDesc());

            tmpImportantFlagType = dataBeanEdit.getDecisionType();

            if (StringUtils.notEmpty(dataBeanEdit.getImportantFlag()) && dataBeanEdit.getImportantFlag() == 1)
            {
                radioButton1.setChecked(true);
//                tmpImportantFlag = "1";
//                ll_view_01.setVisibility(View.VISIBLE);
//                ll_view_02.setVisibility(View.VISIBLE);
            } else
            {
                radioButton2.setChecked(true);
//                tmpImportantFlag = "0";
//                ll_view_01.setVisibility(View.GONE);
//                ll_view_02.setVisibility(View.GONE);
            }

            //相关文件的
            if (StringUtils.notEmpty(dataBeanEdit.getFileInfoList()) && dataBeanEdit.getFileInfoList().size() > 0)
            {
                tv_choose_file.setVisibility(View.GONE);
                mRecyclerview.setVisibility(View.VISIBLE);
                fileList.clear();

                for (int i = 0; i < dataBeanEdit.getFileInfoList().size(); i++)
                {
                    fileList.add(new BeanSearchFile(dataBeanEdit.getFileInfoList().get(i).getFileId(), dataBeanEdit
                            .getFileInfoList().get(i).getFileName()));
                }
            } else
            {
                tv_choose_file.setVisibility(View.VISIBLE);
                mRecyclerview.setVisibility(View.GONE);
            }

            initAdapter();

        } else
        {
            titleBar.setMidText("新增董事会信息");
            tv_choose_file.setVisibility(View.VISIBLE);
            initAdapter();
        }


    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

    }

    private void postProgram()
    {
        if (StringUtils.empty(edtProgramTime.getText().toString().trim()))
        {
            MyToast.showToast(ActivityDirectorInfo.this, getResources().getString(R.string.hint_director_program2));
            return;
        }

        if (StringUtils.empty(edtProgramDirectorInfo.getText().toString().trim()))
        {
            MyToast.showToast(ActivityDirectorInfo.this, getResources().getString(R.string.hint_director_program3));
            return;
        }

        if (tmpImportantFlag.equals("1"))
        {
            if (StringUtils.empty(edtProgramFlagType.getText().toString().trim()))
            {
                MyToast.showToast(ActivityDirectorInfo.this, getResources().getString(R.string.hint_director_program5));
                return;
            }
            if (StringUtils.empty(edtProgramContent.getText().toString().trim()))
            {
                MyToast.showToast(ActivityDirectorInfo.this, getResources().getString(R.string.hint_director_program6));
                return;
            }
        }


        PostDirectorParameter postDirectorParameter = new PostDirectorParameter();
        postDirectorParameter.setProjId(tmpprojId);
        postDirectorParameter.setUsername(tmpUserName);
        postDirectorParameter.setMeetingDateStr(edtProgramTime.getText().toString().trim());
        postDirectorParameter.setContent(edtProgramDirectorInfo.getText().toString().trim());
        postDirectorParameter.setImportantFlag(tmpImportantFlag);
        if (fileList.size() > 0)
        {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < fileList.size(); i++)
            {
                if (i == 0)
                {
                    stringBuilder.append(fileList.get(i).getEid());
                } else
                {
                    stringBuilder.append(",").append(fileList.get(i).getEid());
                }
            }
            postDirectorParameter.setFileId(stringBuilder.toString());
        }

        if (tmpImportantFlag.equals("1"))
        {
            postDirectorParameter.setDecisionType(tmpImportantFlagType);
            postDirectorParameter.setMeetingDesc(edtProgramContent.getText().toString().trim());
        }

        if (isEdit)
        {
            if (StringUtils.notEmpty(dataBeanEdit.getMeetingId()))
            {
                postDirectorParameter.setMeetingId(dataBeanEdit.getMeetingId());
            }

            //同时删掉被减去的文件
            if (StringUtils.notEmpty(dataBeanEdit.getFileInfoList()) && dataBeanEdit.getFileInfoList().size() > 0)
            {
                List<BeanDirector.DataBeanX.DataBean.FileInfoListBean> fileInfoListBeen = dataBeanEdit.getFileInfoList();
                List<String> tmpFileList = new ArrayList<>();
                if (StringUtils.notEmpty(fileList) && fileList.size() > 0)
                {
                    for (int i = 0; i < fileInfoListBeen.size(); i++)
                    {
                        Iterator<BeanSearchFile> fileIterator = fileList.iterator();
                        boolean isEXist = false;
                        while (fileIterator.hasNext())
                        {
                            BeanSearchFile beanSearchFile = fileIterator.next();
                            if (beanSearchFile.getEid().equals(fileInfoListBeen.get(i).getFileId()))
                            {
                                isEXist = true;
                            }
                        }

                        if (!isEXist)
                        {
                            tmpFileList.add(fileInfoListBeen.get(i).getFileId());
                        }
                    }

                    //删掉带过来的。
                    StringBuilder stringBuilder = new StringBuilder();
                    if (StringUtils.notEmpty(tmpFileList) && tmpFileList.size() > 0)
                    {
                        for (int i = 0; i < tmpFileList.size(); i++)
                        {
                            if (i == 0)
                            {
                                stringBuilder.append(tmpFileList.get(i));
                            } else
                            {
                                stringBuilder.append(",").append(tmpFileList.get(i));
                            }
                        }
                        deleteFileById(stringBuilder.toString());
                    }

                } else
                {
                    //删掉带过来的。
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < fileInfoListBeen.size(); i++)
                    {
                        if (i == 0)
                        {
                            stringBuilder.append(fileInfoListBeen.get(i).getFileId());
                        } else
                        {
                            stringBuilder.append(",").append(fileInfoListBeen.get(i).getFileId());
                        }
                    }
                    deleteFileById(stringBuilder.toString());
                }
            }
        }

        if (isEdit)
        {
            ListHttpHelper.postEditDirector(ActivityDirectorInfo.this, SDGson.toJson(postDirectorParameter), new
                    SDRequestCallBack()
                    {
                        @Override
                        public void onRequestFailure(HttpException error, String msg)
                        {
                            MyToast.showToast(ActivityDirectorInfo.this, "编辑失败");
                        }

                        @Override
                        public void onRequestSuccess(SDResponseInfo responseInfo)
                        {
                            MyToast.showToast(ActivityDirectorInfo.this, "编辑成功");
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
        } else
        {
            ListHttpHelper.postAddDirector(ActivityDirectorInfo.this, SDGson.toJson(postDirectorParameter), new
                    SDRequestCallBack()

                    {
                        @Override
                        public void onRequestFailure(HttpException error, String msg)
                        {
                            MyToast.showToast(ActivityDirectorInfo.this, "新增失败");
                        }

                        @Override
                        public void onRequestSuccess(SDResponseInfo responseInfo)
                        {
                            MyToast.showToast(ActivityDirectorInfo.this, "新增成功");
//                Intent intent = new Intent();
//                Bundle bundle = new Bundle();
//                bundle.putBoolean(Constants.ACTIVITY_RESULT_STRING, true);
//                intent.putExtras(bundle);
                            setResult(RESULT_OK);

                            finish();
                        }
                    });
        }
    }

    private void deleteFileById(String fileId)
    {
        ListHttpHelper.getDeleteFileById(ActivityDirectorInfo.this, "", fileId, new SDRequestCallBack()
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(ActivityDirectorInfo.this, "删除失败");
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                MyToast.showToast(ActivityDirectorInfo.this, "删除成功");
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private SlideDateTimeListener startTimeListener = new SlideDateTimeListener()
    {
        @Override
        public void onDateTimeSet(Date date)
        {
            edtProgramTime.setText(DisplayUtil.mFormatter.format(date));
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel()
        {

        }
    };

    @OnClick({R.id.edt_program_time, R.id.edt_program_flag_type, R.id.tv_save, R.id.ll_file})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.edt_program_time:

                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(startTimeListener)
                        .setInitialDate(new Date())
                        //.setMinDate(minDate)
                        //.setMaxDate(maxDate)
                        .setIs24HourTime(true)
                        //.setTheme(SlideDateTimePicker.HOLO_DARK)
                        //.setIndicatorColor(Color.parseColor("#990000"))
                        .build()
                        .show();
                break;
            case R.id.edt_program_flag_type:
                ShowTypeDialog();
                break;
            case R.id.tv_save:
                postProgram();
                break;

            case R.id.ll_file:
                Intent intent = new Intent(ActivityDirectorInfo.this, ActivityPersonalFileLib.class);
                Bundle bundle = new Bundle();
                if (fileList.size() > 0)
                {
                    bundle.putSerializable(Constants.TYPE_LIST, (Serializable) fileList);
                    intent.putExtras(bundle);
                }
                startActivityForResult(intent, 1000);
                break;
        }
    }

    //决策类型
    private void ShowTypeDialog()
    {
        HttpHelperUtils.getInstance().getType(ActivityDirectorInfo.this, true, Constants.TYPE_DECISION_TYPE, new HttpHelperUtils
                .InputListener()
        {
            @Override
            public void onData(List<BeanIceProject> icApprovedList)
            {
                if (icApprovedList.size() > 0)
                {
                    BaseDialogUtils.showDialogProject(ActivityDirectorInfo.this, false, false, false, "决策类型", icApprovedList, new
                            DialogFragmentProject.InputListener()
                            {
                                @Override
                                public void onData(BeanIceProject content)
                                {
                                    tmpImportantFlagType = content.getKey();
                                    edtProgramFlagType.setText(content.getValue());
                                }
                            });
                } else
                {
                    MyToast.showToast(ActivityDirectorInfo.this, "请重新获取决策类型");
                }
            }
        });
    }

    List<BeanSearchFile> fileList = new ArrayList<>();
    TmpAdapter tmpAdapter = null;

    private void initAdapter()
    {
        tmpAdapter = new TmpAdapter(R.layout.item_director_file_choose, fileList);
//        DividerItemDecoration2 dividerItemDecoration2 = new DividerItemDecoration2(ActivityDirectorInfo.this,
//                DividerItemDecoration2.VERTICAL_LIST, R.drawable.recyclerview_divider2, 0);
//        mRecyclerview.addItemDecoration(dividerItemDecoration2);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(ActivityDirectorInfo.this));
        mRecyclerview.setAdapter(tmpAdapter);
        tmpAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                Intent intent = new Intent(ActivityDirectorInfo.this, ActivityPersonalFileLib.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.TYPE_LIST, (Serializable) fileList);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1000);
            }
        });

        tmpAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
        {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position)
            {
                if (view.getId() == R.id.tv_detele_file)
                {
                    BeanSearchFile tmpBean = fileList.get(position);
                    if (fileList.size() > 0)
                    {
                        Iterator<BeanSearchFile> beanSearchFileIterator = fileList.iterator();
                        while (beanSearchFileIterator.hasNext())
                        {
                            BeanSearchFile beanSearchFile = beanSearchFileIterator.next();
                            if (beanSearchFile.getEid().equals(tmpBean.getEid()))
                            {
                                beanSearchFileIterator.remove();
                            }
                        }
                        tmpAdapter.notifyDataSetChanged();
                    }
                    if (fileList.size() > 0)
                    {
                        mRecyclerview.setVisibility(View.VISIBLE);
                        tv_choose_file.setVisibility(View.GONE);
                    } else
                    {
                        tv_choose_file.setVisibility(View.VISIBLE);
                        mRecyclerview.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private class TmpAdapter extends BaseQuickAdapter<BeanSearchFile, BaseViewHolder>
    {
        public TmpAdapter(@LayoutRes int layoutResId, @Nullable List<BeanSearchFile> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, BeanSearchFile item)
        {
            holder.addOnClickListener(R.id.tv_detele_file);
            holder.setText(R.id.tv_content, item.getFileName());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK)
        {
            if (data != null)
            {
                Bundle bundle = data.getExtras();
                fileList.clear();
                if (bundle != null)
                {
                    fileList = (List<BeanSearchFile>) bundle.getSerializable(Constants.TYPE_LIST);
                    if (fileList == null)
                        fileList = new ArrayList<>();
                }

                initAdapter();
                if (fileList.size() > 0)
                {
                    mRecyclerview.setVisibility(View.VISIBLE);
                    tv_choose_file.setVisibility(View.GONE);
                } else
                {
                    tv_choose_file.setVisibility(View.VISIBLE);
                    mRecyclerview.setVisibility(View.GONE);
                }
            }
        }
    }
}
