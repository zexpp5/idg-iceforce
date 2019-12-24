//package com.cxgz.activity.cxim.ui.utils;
//
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.AdapterView;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.ListAdapter;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.base.SumbitBaseFragment;
//import com.bean.MBSubmitInfo;
//import com.chaoxiang.base.utils.MyToast;
//import com.chaoxiang.base.utils.StringUtils;
//import com.cxgz.activity.db.dao.SDUserEntity;
//import com.cxgz.activity.home.adapter.CommonAdapter;
//import com.cxgz.activity.home.adapter.ViewHolder;
//import com.entity.SDFileListEntity;
//import com.entity.VoiceEntity;
//import com.entity.gztutils.ZTUtils;
//import com.entity.office.ListEntity;
//import com.entity.staticvalues.StaticValues;
//import com.http.FileUpload;
//import com.http.HttpURLUtil;
//import com.http.SDResponseInfo;
//import com.http.callback.SDRequestCallBack;
//import com.http.config.BusinessType;
//import com.injoy.ddx.R;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.ui.activity.research.employee.bean.FirstProjectDetailBean;
//import com.ui.activity.research.employee.bean.ProjectinfoList;
//import com.ui.activity.research.employee.establish.submit.EstablishSumbitActivity;
//import com.ui.activity.research.employee.progress.table.FirstChildProjectListActivity;
//import com.ui.activity.research.employee.progress.table.FirstChildProjectListFragment;
//import com.ui.fragment.std.ManaReacherFragment;
//import com.utils.FileUploadPath;
//import com.utils.FileUploadUtil;
//import com.utils.SDToast;
//import com.utils.SPUtils;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.json.JSONArray;
//
//import java.io.File;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//
//import tablayout.widget.CustomSpinner;
//import tablayout.widget.MBCustomSpinner;
//
///**
// * User: Selson
// */
//public class TestSumbitFragment extends SumbitBaseFragment
//{
//    private View titleView;
//    private int pageNumber = 0;
//    private List<ListEntity> datas;
//    private String total;
//    private String dateStr = "";
//    private List<File> files;
//    private List<String> imgPaths;
//    private File voice;
//    private List<SDUserEntity> mRange = new ArrayList<>();
//    private List<NameValuePair> pairs = new ArrayList<>();
//
//    private List<StaticValues> data;
//    private List<FirstProjectDetailBean> submitStaticDatas = new ArrayList<>();
//
//    private JSONArray cc;
//    private int selectIndex = 0;
//    MBSubmitInfo info;
//
//    //公用
//    private LinearLayout sumbit_add;
//    private View file_btn_include;//图片的btn
//    private RelativeLayout send_bottom_rl, delete_bottom_rl;
//    private TextView read_name, copy_to;
//    //公用结束
//
//    private ListView mListView;
//    private LinearLayout add_item_ll;
//    private TextView user_name;
//
//    private CommonAdapter<FirstProjectDetailBean> adapter;
//
//    //项目名称
//    private String projectName;
//    //
//    private EditText log_time, log_content;
//    //静态project数据
//    List<String> projectList;
//    //list的序列
//    private int projectListIndex = 0;
//    //数据列表
//    private CustomSpinner inputProjectList;
//
//    private TextView tv_production_submit_add_product;
//    private String jobRole = "";
//
//
//    public static Fragment newInstance(int index, String date)
//    {
//        Bundle args = new Bundle();
//        args.putInt("index", index);
//        if (!TextUtils.isEmpty(date))
//        {
//            args.putString("date", date);
//        }
//        TestSumbitFragment fragment = new TestSumbitFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    protected int getContentLayout()
//    {
//        return R.layout.research_progress_first_submit_fragment;
//    }
//
//    @Override
//    protected void init(View view)
//    {
//        //基础配置-包括监听和各种初始化
//        initBase();
//
//        //view的绑定
//        initView(view);
//
//        //各种监听
//        initListener();
//
//        //以下是公用模块-开始！
//        setCamera_btnOnclick();//拍照
//        setMic_btnOnclick();//录音
//        setPlus_file_btn();
//
//        //申请时间
//        addDateView(log_time);
//
//        setCopyName();//选择抄送人
//        setApprolName();//选择送阅人
//        setOnClickBtnView();//这个是提交,那三个btn
//
//        //公用模块-结束！
//        setListAdatper();
//
//        getEstablishProjectData(0, "");
//
//        inputProjectList.changeTransparencyColor();
//    }
//
//    /**
//     * 获取服务端获取到的数据。
//     */
//    private void getEstablishProjectData(int page, String dateStr)
//    {
//        String url = HttpURLUtil.newInstance()
//                .append("pro_yfJdOne")
//                .append("project")
//                .toString();
//
//        RequestParams params = new RequestParams();
//        //
//        params.addBodyParameter("scope", "oneself");
//
//        mHttpHelper.get(url, params, true, new SDRequestCallBack(ProjectinfoList.class)
//        {
//            @Override
//            public void onRequestFailure(HttpException error, String msg)
//            {
////                MyToast.showToast(getActivity(), msg);
//            }
//
//            @Override
//            public void onRequestSuccess(SDResponseInfo responseInfo)
//            {
//                ProjectinfoList info = (ProjectinfoList) responseInfo.getResult();
//
//                List<ProjectinfoList.ProjectInfo> mData = new ArrayList();
//
//                if (info.getDatas() == null)
//                {
//                    mData = new ArrayList<ProjectinfoList.ProjectInfo>();
//                } else
//                {
//                    mData = info.getDatas();
//                }
//
//                if (mData.size() > 0)
//                {
//                    List<String> tmpList = new ArrayList<String>();
//                    for (int i = 0; i < mData.size(); i++)
//                    {
//                        tmpList.add(mData.get(i).getName());
//                    }
//                    setTypes(tmpList);
//                } else
//                {
//                    MyToast.showToast(getActivity(), R.string.research_submit_add_product);
//                    openActivity(EstablishSumbitActivity.class);
//                }
//            }
//        });
//    }
//
//
//    private void initView(View view)
//    {
//
//        jobRole = (String) SPUtils.get(getActivity(), SPUtils.JOBROLE, "normal_user");
//        tv_production_submit_add_product = (TextView) view.findViewById(R.id.tv_production_submit_add_product);
//        tv_production_submit_add_product.setText("添加一级子项目");
//
//        //公用开始
//        //添加提交的add //提交界面，让其显示
//        sumbit_add = (LinearLayout) view.findViewById(R.id.submit_add);
//        sumbit_add.setVisibility(View.VISIBLE);
//        //提交的父View--可见
//        file_btn_include = (View) view.findViewById(R.id.file_btn_include);
//        file_btn_include.setVisibility(View.VISIBLE);
//        //提交
//        send_bottom_rl = (RelativeLayout) view.findViewById(R.id.send_bottom_rl);
//
//        //审核
//        read_name = (TextView) view.findViewById(R.id.log_read_name);
//        //抄送
//        copy_to = (TextView) view.findViewById(R.id.log_copy_to);
//
//        delete_bottom_rl = (RelativeLayout) view.findViewById(R.id.delete_bottom_rl);
//
//        //属于公用结束
//
//        llRight.setVisibility(View.GONE);
//        mListView = (ListView) view.findViewById(R.id.lv_ls_project);
//        //申请时间
//        log_time = (EditText) view.findViewById(R.id.log_time);
//        log_time.setEnabled(false);
//        log_time.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
//
//        log_content = (EditText) view.findViewById(R.id.log_content);
//
//        inputProjectList = (CustomSpinner) view.findViewById(R.id.inputDate1);
//
//        add_item_ll = (LinearLayout) view.findViewById(R.id.add_item_ll);
//        add_item_ll.setOnClickListener(this);
//
//        user_name = (TextView) view.findViewById(R.id.user_name);
//
//        user_name.setText(SPUtils.get(getActivity(), SPUtils.USER_NAME, "").toString());
//
//    }
//
//    private void initBase()
//    {
//        //获取定位信息
//        getCurrPosition();
//
//
//    }
//
//    /**
//     * 空间监听等
//     */
//    private void initListener()
//    {
//        //公用开始
//        //左下方的列表文字按钮-显示列表
//        setBottomLeftListVisible(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                //添加
//            }
//        });
//
//        send_bottom_rl.setOnClickListener(this); //提交
//        delete_bottom_rl.setOnClickListener(this);//编辑
//
//        //公用结束
//    }
//
//    //设置从网络获取到的数据
//    private void setTypes(List<String> listData)
//    {
//        projectList = new ArrayList<String>();
//
//        for (int i = 0; i < listData.size(); i++)
//        {
//            projectList.add(listData.get(i).toString());
//        }
//
//        inputProjectList.setListStr(projectList);
//        inputProjectList.setSelectIndex(0);
//        inputProjectList.setOnSpinnerItemClickListener(new CustomSpinner.OnSpinnerItemClickListener()
//        {
//            @Override
//            public void onItemClick(String condition, int position)
//            {
//                projectListIndex = position;
//            }
//        });
//    }
//
//    private void showProgress()
//    {
//        progresDialog = new ProgressDialog(getActivity());
//        progresDialog.setCanceledOnTouchOutside(false);
//        progresDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
//        {
//
//            @Override
//            public void onCancel(DialogInterface dialog)
//            {
//                upload.cancel();
//            }
//        });
//        progresDialog.setMessage(getString(R.string.Is_post));
//        progresDialog.show();
//        progresDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
//        {
//            @Override
//            public void onCancel(DialogInterface dialogInterface)
//            {
////                if (upload != null ) {
////                    upload.cancel();
////                    ProgressBar pb = (ProgressBar) findViewById(R.id.top_pb);
////                    if(pb != null) {
////                        pb.setProgress(0);
////                    }
////                }
//            }
//        });
//        progresDialog.show();
//    }
//
//    @Override
//    public void onClick(View v)
//    {
//        super.onClick(v);
//        switch (v.getId())
//        {
//            case R.id.send_bottom_rl:
//                try
//                {
//                    onClickTitleRightBtn();
//                } catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//                break;
//
//            case R.id.add_item_ll:
//                showDialog();
//
//                break;
//            case R.id.delete_bottom_rl:
//                if (submitStaticDatas.size() == 0)
//                {
//                    MyToast.showToast(getActivity(), R.string.research_submit_add_product);
//                    return;
//                }
//                showAddDialog(0, 3);
//                break;
//        }
//    }
//
//    /**
//     * 显示详情的添加窗口！
//     */
//    private void showDialog()
//    {
//        FirstProjectDetailBean pwd = new FirstProjectDetailBean();
//        submitStaticDatas.add(pwd);
//        showAddDialog(submitStaticDatas.size() - 1, 1);
//    }
//
//    //1新建 2点击item 3点击编辑
//    private void showAddDialog(final int position, final int inputType)
//    {
//        selectIndex = position;
//        final Dialog dialog = new Dialog(getActivity());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.research_first_project_sumbit_staff_dialog, null);
//        dialog.setContentView(contentView);
//        dialog.setCanceledOnTouchOutside(true);
//        //用于让popupWindow现实在该view下。
//        final RelativeLayout rlBottomView = (RelativeLayout) contentView.findViewById(R.id.rl_bottom_view);
//
//        final EditText product_name = (EditText) contentView.findViewById(R.id.product_name);
//        final TextView specifications = (TextView) contentView.findViewById(R.id.specifications);
//
////        addDateView(specifications, rlBottomView);
//        addDateView(specifications);
//
//        final TextView number = (TextView) contentView.findViewById(R.id.number);
//
////        addDateView(number, rlBottomView);
//        addDateView(number);
//
//        //这个是在点击了编辑的时候
//        final MBCustomSpinner typeCustomSpinner = (MBCustomSpinner) contentView.findViewById(R.id.spinner_listview_id);//类型
//        List<String> list = new ArrayList<String>();
//        for (FirstProjectDetailBean item : submitStaticDatas)
//        {
//            list.add(item.getName());
//        }
//        typeCustomSpinner.setListStr(list);
//
//        typeCustomSpinner.setOnSpinnerItemClickListener(new MBCustomSpinner.OnSpinnerItemClickListener()
//        {
//            @Override
//            public void onItemClick(String condition, int position)
//            {
//                submitStaticDatas.get(selectIndex).setStartTime(specifications.getText().toString());
//                submitStaticDatas.get(selectIndex).setEndTime(number.getText().toString());
//                selectIndex = position;
//                initDialogEditText(product_name, specifications, number, typeCustomSpinner, position, inputType);
//            }
//        });
//
//        TextView tv_accomplish = (TextView) contentView.findViewById(R.id.tv_accomplish);
//        TextView tv_clean = (TextView) contentView.findViewById(R.id.tv_clean);
//
//        initDialogEditText(product_name, specifications, number, typeCustomSpinner, position, inputType);
//
//        tv_accomplish.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {//完成
//                if (inputType != 3)
//                {//编辑不判断名称
//                    if (TextUtils.isEmpty(product_name.getText().toString()))
//                    {
//                        MyToast.showToast(getActivity(), R.string.first_project_submit_input_product_err_tips);
//                        return;
//                    }
//                }
//                if (TextUtils.isEmpty(specifications.getText().toString()) || TextUtils.isEmpty(number.getText().toString()))
//                {
//                    MyToast.showToast(getActivity(), R.string.first_project_submit_input_product_err_tips);
//                    return;
//                }
//                if (ZTUtils.checkStartTime2(specifications.getText().toString(), number.getText().toString()))
//                {
//                    return;
//                }
//
//                switch (inputType)
//                {
//                    case 1://新建
//                        submitStaticDatas.get(position).setName(product_name.getText().toString());
//                        submitStaticDatas.get(position).setStartTime(specifications.getText().toString());
//                        submitStaticDatas.get(position).setEndTime(number.getText().toString());
//                        break;
//                    case 2://点击item
//                        submitStaticDatas.get(position).setName(product_name.getText().toString());
//                        submitStaticDatas.get(position).setStartTime(specifications.getText().toString());
//                        submitStaticDatas.get(position).setEndTime(number.getText().toString());
//                        break;
//                    case 3://点击编辑
//                        submitStaticDatas.get(selectIndex).setStartTime(specifications.getText().toString());
//                        submitStaticDatas.get(selectIndex).setEndTime(number.getText().toString());
//                        break;
//                }
//                for (int i = submitStaticDatas.size(); i > 0; i--)
//                {
//                    if (TextUtils.isEmpty(submitStaticDatas.get(i - 1).getName()) && TextUtils.isEmpty(submitStaticDatas.get(i - 1).getStartTime()) && TextUtils.isEmpty(submitStaticDatas.get(i - 1).getEndTime()))
//                    {
//                        submitStaticDatas.remove(i - 1);
//                    }
//                }
//                setListAdatper();
//                dialog.dismiss();
//            }
//        });
//        tv_clean.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {//清除
//                submitStaticDatas.remove(selectIndex);
//                setListAdatper();
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//    }
//
//    private void initDialogEditText(EditText n, TextView s, TextView q, MBCustomSpinner typeCustomSpinner, int position, int inputType)
//    {
//        if (submitStaticDatas.size() == 0)
//            return;
//        if (3 == inputType)
//        {
//            n.setVisibility(View.GONE);
//            typeCustomSpinner.setVisibility(View.VISIBLE);
//        } else
//        {
//            if (!TextUtils.isEmpty(submitStaticDatas.get(position).getName()))
//            {
//                n.setText(submitStaticDatas.get(position).getName());
//            } else
//            {
//                n.setText("");
//            }
//        }
//        if (!TextUtils.isEmpty(submitStaticDatas.get(position).getStartTime()))
//        {
//            s.setText(submitStaticDatas.get(position).getStartTime());
//        } else
//        {
//            s.setText("");
//        }
//        if (!TextUtils.isEmpty(submitStaticDatas.get(position).getEndTime()))
//        {
//            q.setText(submitStaticDatas.get(position).getEndTime());
//        } else
//        {
//            q.setText("");
//        }
//    }
//
//    private void onClickTitleRightBtn() throws Exception
//    {
//
//
//        if (selectedPersonData.size() == 0)
//        {
//            SDToast.showShort("请选择审批人！");
//            return;
//        }
//
//        projectName = inputProjectList.getListStr().get(projectListIndex);
//
//        if (StringUtils.isEmpty(projectName))
//        {
//            MyToast.showToast(getActivity(), R.string.research_submit_choose_project_name);
//            return;
//        }
//
//        if (submitStaticDatas.size() < 1)
//        {
//            MyToast.showToast(getActivity(), R.string.research_submit_null_with_first_project);
//            showDialog();
//            return;
//        }
//
//        pairs.clear();
//
//        pairs.add(new BasicNameValuePair("approvalId", selectedPersonData.get(0).getUserId() + ""));
//        pairs.add(new BasicNameValuePair("approvalName", selectedPersonData.get(0).getName()));
//        pairs.add(new BasicNameValuePair("name", projectName));
//        pairs.add(new BasicNameValuePair("createTime", log_time.getText().toString().trim()));
//
//        pairs.add(new BasicNameValuePair("remark", log_content.getText().toString().toString()));
//        //子项目，就是该项目下的listview上的每一行数据
//        pairs.add(new BasicNameValuePair("detail", mGson.toJson(submitStaticDatas)));
//
//        if (mRange != null && !mRange.isEmpty())
//        {
//            cc = userListToStringArray(mRange);
//            pairs.add(new BasicNameValuePair("cc", cc.toString()));
//        }
//
//        List<SDFileListEntity> fileListEntities = new ArrayList<SDFileListEntity>();
//        if (files != null && !files.isEmpty())
//        {
//            for (File file : files)
//            {
//                SDFileListEntity fileListEntity = new SDFileListEntity();
//                fileListEntity.setEntity(getActivity(), file, FileUploadPath.SHARE, SDFileListEntity.ECO_RETURN_GOODS);
//                fileListEntities.add(fileListEntity);
//            }
//        }
//
//        if (imgPaths != null)
//        {
//            for (String imgPath : imgPaths)
//            {
//                SDFileListEntity fileListEntity = new SDFileListEntity();
//                fileListEntity.setEntity(getActivity(), new File(imgPath), FileUploadPath.SHARE, SDFileListEntity.IMAGE);
//                fileListEntity.setIsOriginalImg(isOriginalImg);
//                fileListEntities.add(fileListEntity);
//            }
//        }
//
//        if (voice != null)
//        {
//            SDFileListEntity fileListEntity = new SDFileListEntity();
//            fileListEntity.setEntity(getActivity(), voice, FileUploadPath.SHARE, SDFileListEntity.VOICE);
//            fileListEntities.add(fileListEntity);
//        }
//
//        String url = HttpURLUtil.newInstance().append("pro_yfJdOne/").toString();
//        RequestParams params = new RequestParams();
//        params.addBodyParameter(pairs);
//        showProgress();
//        upload = FileUploadUtil.resumableUpload(getActivity().getApplication(), fileListEntities, BusinessType.NO_KEY, null, url, params, new FileUpload.UploadListener()
//        {
//
//            @Override
//            public void onSuccess(SDResponseInfo responseInfo, HashMap<String, Object> result)
//            {
//                if (progresDialog != null)
//                    progresDialog.dismiss();
//                MyToast.showToast(getActivity(), R.string.submit_success);
//
//                //添加
//                if (jobRole.equals("normal_user"))
//                {//员工的走这里
//                    ((FirstChildProjectListActivity) getActivity()).replaceSelect(0);
//                    ((FirstChildProjectListActivity) getActivity()).replaceFragment(FirstChildProjectListFragment.newInstance(null));
//                } else
//                {
//                    ((ManaReacherFragment) getParentFragment()).replaceSelect(0);
//                    ((ManaReacherFragment) getParentFragment()).replaceFragment(FirstChildProjectListFragment.newInstance(null));
//                }
//                // findByTypeAndDate("");
//            }
//
//            @Override
//            public void onProgress(int byteCount, int totalSize)
//            {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void onFailure(Exception ossException)
//            {
//                //sendMsgFail(null);
//                if (progresDialog != null)
//                    progresDialog.dismiss();
//                MyToast.showToast(getActivity(), R.string.submit_fail);
//                //finish();
//            }
//        });
//    }
//
//    private void setListAdatper()
//    {
//        if (adapter == null)
//        {
//            adapter = new CommonAdapter<FirstProjectDetailBean>(getActivity(), submitStaticDatas, R.layout.research_first_project_submit_list_item)
//            {
//                @Override
//                public void convert(ViewHolder helper, FirstProjectDetailBean item, final int position)
//                {
//                    helper.setText(R.id.product_name, item.getName());
//                    helper.setText(R.id.specifications, item.getStartTime());
//                    helper.setText(R.id.number, item.getEndTime());
//                }
//            };
//        }
//
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//            {
//                showAddDialog(position, 2);//点击item
//            }
//        });
//        mListView.setAdapter(adapter);
//        setListViewHeightBasedOnChildren(mListView);
//    }
//
//    public static void checkEtDecimal(final EditText editText)
//    {
//        editText.addTextChangedListener(new TextWatcher()
//        {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count)
//            {
//                if (s.toString().contains("."))
//                {
//                    if (s.length() - 1 - s.toString().indexOf(".") > 2)
//                    {
//                        s = s.toString().subSequence(0,
//                                s.toString().indexOf(".") + 3);
//                        editText.setText(s);
//                        editText.setSelection(s.length());
//                    }
//                }
//                if (s.toString().trim().substring(0).equals("."))
//                {
//                    s = "0" + s;
//                    editText.setText(s);
//                    editText.setSelection(2);
//                }
//
//                if (s.toString().startsWith("0")
//                        && s.toString().trim().length() > 1)
//                {
//                    if (!s.toString().substring(1, 2).equals("."))
//                    {
//                        editText.setText(s.subSequence(0, 1));
//                        editText.setSelection(1);
//                        return;
//                    }
//                }
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after)
//            {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s)
//            {
//                // TODO Auto-generated method stub
//            }
//        });
//    }
//
//    @Override
//    public void onSelectedImg(List<String> imgPaths)
//    {
//        this.imgPaths = imgPaths;
//    }
//
//    @Override
//    public void onClickLocationFunction(double latitude, double longitude, String address)
//    {
//
//    }
//
//    @Override
//    public void onClickSendRange(List<SDUserEntity> userEntities)
//    {
//        mRange = userEntities;
//    }
//
//    @Override
//    public void onClickSendPerson(List<SDUserEntity> userEntities)
//    {
//
//    }
//
//    @Override
//    public void onClickAttach(List<File> pickerFile)
//    {
//        files = pickerFile;
//    }
//
//    @Override
//    public void onDelAttachItem(View v)
//    {
//
//    }
//
//    @Override
//    public void onClickVoice(VoiceEntity voiceEntity)
//    {
//        if (voiceEntity != null)
//        {
//            voice = new File(voiceEntity.getFilePath());
//        }
//    }
//
//    @Override
//    public int getDraftDataType()
//    {
//        return 0;
//    }
//
//    @Override
//    public void findByTypeAndDate(String date)
//    {
//        super.findByTypeAndDate(date);
////        Bundle bundle = new Bundle();
////        bundle.putInt("index", 0);//选中哪几个
////        bundle.putString("date", date);
////        startActivity(bundle, FirstChildProjectListActivity.class);  //列表
////        BaseApplication.getInstance().getTopActivity().finish();
//        ((FirstChildProjectListActivity) getActivity()).replaceSelect(0);
//        ((FirstChildProjectListActivity) getActivity()).replaceFragment(FirstChildProjectListFragment.newInstance(date));
//
//    }
//
//    //scroll list展示冲突
//    public static void setListViewHeightBasedOnChildren(ListView listView)
//    {
//        ListAdapter listAdapter = listView.getAdapter();
//        if (listAdapter == null)
//        {
//            return;
//        }
//        int totalHeight = 0;
//        for (int i = 0, len = listAdapter.getCount(); i < len; i++)
//        {
//            View listItem = listAdapter.getView(i, null, listView);
//            listItem.measure(0, 0);
//            totalHeight += listItem.getMeasuredHeight();
//        }
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        listView.setLayoutParams(params);
//    }
//}