//package com.cxgz.activity.cxim.ui.utils;
//
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.ListAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.base.SumbitBaseFragment;
//import com.bean.MineSoHoCCdataEntity;
//import com.chaoxiang.base.utils.MyToast;
//import com.cxgz.activity.db.dao.SDUserEntity;
//import com.cxgz.activity.home.adapter.CommonAdapter;
//import com.cxgz.activity.home.adapter.ViewHolder;
//import com.entity.SDFileListEntity;
//import com.entity.VoiceEntity;
//import com.entity.administrative.employee.Annexdata;
//import com.entity.appro.ApproDataEntity;
//import com.http.FileUpload;
//import com.http.HttpURLUtil;
//import com.http.SDResponseInfo;
//import com.http.callback.SDRequestCallBack;
//import com.http.config.BusinessType;
//import com.injoy.ddx.R;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.ui.activity.research.employee.bean.Detaildata;
//import com.ui.activity.research.employee.bean.FirstChildProjectDetailBean;
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
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
///**
// * User: Selson
// * Date: 2016-06-07
// * FIXME 一级子项目详情
// */
//public class TestDetailFragment extends SumbitBaseFragment
//{
//    private List<File> files;
//    private List<String> imgPaths;
//    private File voice;
//
//    private ProgressDialog progresDialog;
//    private FileUpload upload;
//
//    private JSONArray cc;
//
//    private SDUserEntity userInfo;
//
//    private List<Annexdata> annexdata;//附件
//    private List<SDUserEntity> mRange = new ArrayList<>();
//
//    private List<ApproDataEntity> approData;//审批信息
//    private String approvalStatus;
//    private String approvalId;
//    private View file_btn_include;
//
//    private String id;
//
//    private FirstChildProjectDetailBean info;
//
//    private String status;
//
//    private List<MineSoHoCCdataEntity> list;
//
//    //私有的都定义在这里.
//    private TextView project_number, project_name, user_name;
//
//    private EditText log_time, log_content;
//
//    private ListView lv_ls_project;
//    private CommonAdapter<Detaildata> adapter;
//    private List<Detaildata> items = new ArrayList<>();
//
//    private String jobRole = "";
//
//
//    public static Fragment newInstance(String id) {
//        Bundle args = new Bundle();
//        if(!TextUtils.isEmpty(id)) {
//            args.putString("id", id);
//        }
//        TestDetailFragment fragment = new TestDetailFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    protected int getContentLayout()
//    {
//        return R.layout.research_progress_first_detail_fragment;
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
//        getData();
//    }
//
//    /**
//     * 员工界面审批
//     */
//    private List<View> selBtnViewArr = new ArrayList<>();
//    private int[] selBtnIdArr = {R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5};
//    private EditText approver_content_add;//内容
//
//    private void initBase()
//    {
//        id = getArguments().getString("id");
//    }
//
//    private void initView(View view)
//    {
//        jobRole = (String) SPUtils.get(getActivity(), SPUtils.JOBROLE, "normal_user");
//        //公用开始
//        file_btn_include = (View) view.findViewById(R.id.file_btn_include);
//        file_btn_include.setVisibility(View.VISIBLE);
//
//        //属于公用结束
//        //项目编号
////        project_number = (TextView) view.findViewById(R.id.project_number);
//
//        project_name = (TextView) view.findViewById(R.id.project_name);
//
//        log_time = (EditText) view.findViewById(R.id.log_time);
//
//        log_content = (EditText) view.findViewById(R.id.log_content);
//
//        lv_ls_project = (ListView) view.findViewById(R.id.lv_ls_project);
//        /**
//         * 申请人
//         */
//        user_name = (TextView) view.findViewById(R.id.user_name);
//        user_name.setText(SPUtils.get(getActivity(), SPUtils.USER_NAME, "").toString());
//
//        //审批
//        selBtnViewArr.clear();
//        for (int i = 0; i < selBtnIdArr.length; i++)
//        {
//            View selBtn = view.findViewById(selBtnIdArr[i]);
//            selBtnViewArr.add(selBtn);
//        }
//
//        //审批流程
//        approver_content_add = (EditText) view.findViewById(R.id.approver_content);
//
//////        //审批金额的
////        total_money_view_app = (TotalMoneyView) view.findViewById(R.id.total_money_view_app);
//
//        approver_text = (TextView) view.findViewById(R.id.approver_text);
//        approver_text.setText(SPUtils.get(getActivity(), SPUtils.USER_NAME, "") + "");//设置自己为审批人
//
//
//        setAgreeTypes();//设置是否同意
//        setPlus_file_btn();//审批的时候
//        setApproMic_btnOnclick();//审批的时候
//        setApproCamera_btnOnclick();//审批的时候
//        setApproClickBtnView();//审批的时候，选择附件查看的三个按钮
//        sumbitAppro.setOnClickListener(this);//审批提交
//        addNextApproMan("15");//下一个审批人
////        insertMoney();
//        //点击就可以展开了
//        setOnClickCost();
//
//
//    }
//
//    private void setDetailInfo()
//    {
//        setListAdatper();
//
////        project_number.setText(info.getIcode());
//
//        project_name.setText(info.getName());
//
//        log_time.setText(info.getCreateTime());
//        log_time.setEnabled(false);
//
//        log_content.setText(info.getRemark());
//        log_content.setEnabled(false);
//
////        设计界面没有该VIEW
////        addSendRangeInfo(list);//抄送
//        //显示附件
//        if (annexdata.size() > 0)
//        {   //如果有附件
//            //file_btn_include.setVisibility(View.VISIBLE);
//            //显示附件
//            setOnClickBtnView(annexdata);//跳转到附件界面
//        } else
//        {
//            file_btn_include.setVisibility(View.GONE);
//        }
//        approvalStatus = approData.get(0).getApprovalStatus();//获取状态
//        approvalId = info.getApprovalId() + "";
//        setApproCheckInfo(approvalStatus, approvalId, approData);
//
//    }
//
//    private void setListAdatper()
//    {
//        if (adapter == null)
//        {
//            adapter = new CommonAdapter<Detaildata>(getActivity(), items, R.layout.research_first_project_submit_list_item)
//            {
//                @Override
//                public void convert(ViewHolder helper, Detaildata item, final int position)
//                {
//                    helper.setText(R.id.product_name, item.getName());
//                    helper.setText(R.id.specifications, item.getStartTime());
//                    helper.setText(R.id.number, item.getEndTime());
//                    helper.setText(R.id.number, item.getEndTime());//申请人
//                }
//            };
//        }
//        lv_ls_project.setAdapter(adapter);
//        setListViewHeightBasedOnChildren(lv_ls_project);
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
//                if (jobRole.equals("normal_user")) {//员工的走这里
//                    ((FirstChildProjectListActivity) getActivity()).replaceSelect(0);
//                    ((FirstChildProjectListActivity) getActivity()).replaceFragment(FirstChildProjectListFragment.newInstance( null));
//                } else {
//                    ((ManaReacherFragment) getParentFragment()).replaceSelect(0);
//                    ((ManaReacherFragment) getParentFragment()).replaceFragment(FirstChildProjectListFragment.newInstance(null));
//                }
//            }
//        });
//        //公用结束
//    }
//
//    //获取参数
//    private void getData()
//    {
//        String url = HttpURLUtil.newInstance().append("pro_yfJdOne").append(id).toString();
//
//        mHttpHelper.get(url, null, true, new SDRequestCallBack(FirstChildProjectDetailBean.class)
//        {
//            @Override
//            public void onRequestFailure(HttpException error, String msg)
//            {
//                MyToast.showToast(getActivity(), msg);
//            }
//
//            @Override
//            public void onRequestSuccess(SDResponseInfo responseInfo)
//            {
//                info = (FirstChildProjectDetailBean) responseInfo.result;
////                status = info.getStatus() + "";//状态
//
//                list = info.getCcdata();//抄送人
//                annexdata = info.getAnnexdata();
//                approData = info.getApproData();//审批
//                items=info.getDetaildata();
//
//                setDetailInfo();
//            }
//        });
//    }
//
//
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
//        ((FirstChildProjectListActivity)getActivity()).replaceSelect(0);
//        ((FirstChildProjectListActivity)getActivity()).replaceFragment(FirstChildProjectListFragment.newInstance(date));
//
//    }
//
//
//    /**
//     * 审批
//     * @param v
//     */
//    @Override
//    public void onClick(View v)
//    {
//        super.onClick(v);
//        switch (v.getId())
//        {
//            case R.id.send_bottom_app_rl:
//                try
//                {
////                    onClickTitleRightBtn();
//                    if (info == null)
//                    {
//                        return;
//                    }
//                    if (info.getApproData() == null || info.getApproData().isEmpty())
//                    {
//                        return;
//                    }
//                    postAppro();
//                } catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//                break;
//            case R.id.send_bottom_rl:
//                try
//                {
////                    onClickTitleRightBtn();
//                } catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//                break;
//        }
//    }
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
//
//            }
//        });
//        progresDialog.show();
//    }
//
//
//
//    //提交审批
//    private void postAppro()
//    {
//
//        String approvalContent = approver_content_add.getText().toString().trim();
//        if (TextUtils.isEmpty(approvalContent))
//        {
//            SDToast.showShort(R.string.please_input_all_info);
//            return;
//        }
//
////        if (ZTUtils.checkSelectApprovalPerson(isAgree, selectedPersonData))
////            return;
//
//        List<NameValuePair> pairs = new ArrayList<>();
//
//        pairs.clear();
//
//        if (selectedPersonData != null && !selectedPersonData.isEmpty())
//        {
//            pairs.add(new BasicNameValuePair("approvalId", selectedPersonData.get(0).getUserId() + ""));
//            pairs.add(new BasicNameValuePair("approvalName", selectedPersonData.get(0).getName()));
//        }
//        //审批内容
//        pairs.add(new BasicNameValuePair("remark", approvalContent));
//        //是否同意
//        pairs.add(new BasicNameValuePair("isAgree", isAgree + ""));
//        //业务类型
//        pairs.add(new BasicNameValuePair("btype", "50"));
//        //业务id
//        pairs.add(new BasicNameValuePair("bid", String.valueOf(info.getApproData().get(0).getbId())));
//        //批准金额
////        pairs.add(new BasicNameValuePair("money", total_money_view_app.getTotalMoney() + ""));
//
//        List<SDFileListEntity> fileListEntities = new ArrayList<SDFileListEntity>();
//        if (files != null && !files.isEmpty())
//        {
//            for (File file : files)
//            {
//                SDFileListEntity fileListEntity = new SDFileListEntity();
//                fileListEntity.setEntity(getActivity(), file, FileUploadPath.SHARE, SDFileListEntity.FILE);
//                fileListEntities.add(fileListEntity);
//            }
//        }
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
//        if (voice != null)
//        {
//            SDFileListEntity fileListEntity = new SDFileListEntity();
//            fileListEntity.setEntity(getActivity(), voice, FileUploadPath.SHARE, SDFileListEntity.VOICE);
//            fileListEntities.add(fileListEntity);
//        }
//
//        String url = HttpURLUtil.newInstance().append("pro_task/").toString();
//        RequestParams params = new RequestParams();
//        params.addBodyParameter(pairs);
//        showProgress();
//        upload = FileUploadUtil.resumableUpload(getActivity().getApplication(), fileListEntities, BusinessType.NO_KEY, null, url, params, new FileUpload.UploadListener()
//        {
//            @Override
//            public void onSuccess(SDResponseInfo responseInfo, HashMap<String, Object> result)
//            {
//                if (progresDialog != null)
//                {
//                    progresDialog.dismiss();
//                }
//
//                SDToast.showShort(R.string.approval_success);
//                approView.setVisibility(View.GONE);
//
//                ((FirstChildProjectListActivity)getActivity()).replaceSelect(0);
//                ((FirstChildProjectListActivity)getActivity()).replaceFragment(FirstChildProjectListFragment.newInstance(null));
//                //findByTypeAndDate("");
//            }
//
//            @Override
//            public void onProgress(int byteCount, int totalSize)
//            {
//                if (progresDialog != null)
//                {
//                    progresDialog.dismiss();
//                }
//                SDToast.showShort(R.string.approval_fail);
//            }
//
//            @Override
//            public void onFailure(Exception ossException)
//            {
//                if (progresDialog != null)
//                {
//                    progresDialog.dismiss();
//                }
//                SDToast.showShort(R.string.approval_fail);
//            }
//        });
//    }
//
//
//
//
//
//    @Override
//    public void onSelectedImg(List<String> imgPaths) {
//        this.imgPaths = imgPaths;
//    }
//
//    @Override
//    public void onClickLocationFunction(double latitude, double longitude, String address) {
//
//    }
//
//    @Override
//    public void onClickSendRange(List<SDUserEntity> userEntities) {
//        mRange = userEntities;
//    }
//
//    @Override
//    public void onClickSendPerson(List<SDUserEntity> userEntities) {
//
//    }
//
//    @Override
//    public void onClickAttach(List<File> pickerFile) {
//        files = pickerFile;
//    }
//
//    @Override
//    public void onDelAttachItem(View v) {
//
//    }
//
//    @Override
//    public void onClickVoice(VoiceEntity voiceEntity) {
//        if (voiceEntity != null)
//        {
//            voice = new File(voiceEntity.getFilePath());
//        }
//    }
//
//    @Override
//    public int getDraftDataType() {
//        return 0;
//    }
//}