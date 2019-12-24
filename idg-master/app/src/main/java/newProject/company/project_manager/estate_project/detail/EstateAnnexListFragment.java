package newProject.company.project_manager.estate_project.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.base_erp.ERPSumbitBaseFragment;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.config.Config;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.imrestful.OkHttpUtils;
import com.chaoxiang.imrestful.callback.FileCallBack;
import com.cxgz.activity.cxim.utils.FileDownLoadUtils;
import com.cxgz.activity.cxim.utils.FileUtill;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.entity.VoiceEntity;
import com.http.HttpURLUtil;
import com.injoy.idg.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.okhttp.Request;
import com.utils.SPUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.company.expense.bean.FileListBean;
import newProject.company.vacation.WebVacationActivity;
import yunjing.view.StatusTipsView;

/**
 * author: Created by aniu on 2018/6/15.
 */

public class EstateAnnexListFragment extends ERPSumbitBaseFragment {
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mLoadingView;
    private List<FileListBean> mDataLists = new ArrayList<>();
    private ApplyAdapter mApplyAdapter;

    public static Fragment newInstance(ArrayList<FileListBean> data) {
        Bundle args = new Bundle();
        args.putParcelableArrayList("data", data);
        EstateAnnexListFragment fragment = new EstateAnnexListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSelectedImg(List<String> imgPaths) {

    }

    @Override
    public void onClickLocationFunction(double latitude, double longitude, String address) {

    }

    @Override
    public void onClickSendRange(List<SDUserEntity> userEntities) {

    }

    @Override
    public void onClickSendPerson(List<SDUserEntity> userEntities) {

    }

    @Override
    public void onClickAttach(List<File> pickerFile) {

    }

    @Override
    public void onDelAttachItem(View v) {

    }

    @Override
    public void onClickVoice(VoiceEntity voiceEntity) {

    }

    @Override
    public int getDraftDataType() {
        return 0;
    }

    @Override
    public void onClickMoneyFile(String moneyString) {

    }

    @Override
    protected int getContentLayout() {
        return R.layout.annex_list_layout;
    }

    @Override
    protected void init(View view) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mDataLists = bundle.getParcelableArrayList("data");
        }
//        mEid = 16322;
        initRefresh();
        initRV();

        mLoadingView.hide();
    }


    public void initRefresh() {
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setEnableRefresh(false);


        mLoadingView.showLoading();
        mLoadingView.setOnVisibleChangeListener(new StatusTipsView.OnVisibleChangeListener() {
            @Override
            public void onVisibleChanged(boolean visible) {
                if (mRecyclerview != null) {
                    mRecyclerview.setVisibility(visible ? View.GONE : View.VISIBLE);
                }
            }
        });
        mLoadingView.hide();
    }


    private void initRV() {
        mApplyAdapter = new ApplyAdapter(R.layout.expense_annex_item_layout, mDataLists);

        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(mApplyAdapter);
        /*mApplyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                InvestWayListBean.DataBean departDataBean = (InvestWayListBean.DataBean) adapter.getData().get(position);
                if (adapter.getData().size() > 0 && departDataBean != null)
                {

                }
            }
        });*/
    }


    private class ApplyAdapter extends BaseQuickAdapter<FileListBean, BaseViewHolder> {
        public ApplyAdapter(@LayoutRes int layoutResId, @Nullable List<FileListBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(final BaseViewHolder holder, final FileListBean item) {

            View line_bottom = holder.getView(R.id.line_bottom);
            if (holder.getAdapterPosition() == mData.size() - 1)
                line_bottom.setVisibility(View.INVISIBLE);
            else
                line_bottom.setVisibility(View.VISIBLE);

            if (StringUtils.notEmpty(item.getFileName()))
                holder.setText(R.id.annex_item_title, item.getFileName());

            if (StringUtils.notEmpty(item.getType()) && StringUtils.notEmpty(holder.getView(R.id.annex_image))) {
                setICON(item.getType(), holder.getView(R.id.annex_image));
            } else {
                setICON("other", holder.getView(R.id.annex_image));
            }

            if (StringUtils.notEmpty(holder.getView(R.id.down_look_btn)) && StringUtils.notEmpty(item.getFileName())) {
                if (judgePDF(item.getType())) {
                    holder.getView(R.id.down_look_btn).setVisibility(View.INVISIBLE);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setOpenFile(item);
                        }
                    });
                } else {
                    if (StringUtils.notEmpty(FileDownLoadUtils.getInstance().getFilePath(Config.CACHE_FILE_DIR,
                            item.getFileName()))) {
                        holder.getView(R.id.down_look_btn).setVisibility(View.INVISIBLE);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setOpenFile(item);
                            }
                        });
                    } else {
                        holder.getView(R.id.down_look_btn).setVisibility(View.VISIBLE);
                        holder.getView(R.id.down_look_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.getView(R.id.down_look_btn).setEnabled(false);
                                String tokenString = (String) SPUtils.get(getActivity(), SPUtils.ACCESS_TOKEN, "");
                                String unCodeFileName = "";

                                if (StringUtils.notEmpty(item.getFileName())) {   //后台要求必须转码两次。
                                    try {
                                        unCodeFileName = URLEncoder.encode(URLEncoder.encode(item.getFileName(), "utf-8"),
                                                "utf-8");

                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    unCodeFileName = "";
                                }

                                String downLoadUrl = HttpURLUtil.newInstance()
                                        .append("/cost/detail/annex/download.json?token=")
                                        .append(tokenString)
                                        .toString()
                                        + "&fileName=" + unCodeFileName
                                        + "&fileUrl=" + item.getFileUrl()
                                        + "&type=" + item.getType()
                                        + "&eid=" + item.getIdX();

                                OkHttpUtils.get().url(downLoadUrl).build().execute(new FileCallBack(Config
                                        .CACHE_FILE_DIR, item.getFileName()) {
                                    @Override
                                    public void inProgress(float progress) {
                                        float b = (float) (Math.round(progress * 100)) / 100;
                                        DecimalFormat decimalFormat = new DecimalFormat(".00");
//                                        holder.setText(R.id.down_look_btn, decimalFormat.format(b) + "%");
                                    }

                                    @Override
                                    public void onError(Request request, Exception e) throws Exception {
                                        ImageView iv_down_look = holder.getView(R.id.down_look_btn);
                                        iv_down_look.setVisibility(View.VISIBLE);
                                        holder.getView(R.id.down_look_btn).setEnabled(true);
                                    }

                                    @Override
                                    public void onResponse(File response) throws Exception {
                                        ImageView iv_down_look = holder.getView(R.id.down_look_btn);
                                        holder.getView(R.id.down_look_btn).setEnabled(true);
                                        iv_down_look.setVisibility(View.INVISIBLE);
                                        if (StringUtils.notEmpty(FileDownLoadUtils.getInstance().getFilePath(Config
                                                        .CACHE_FILE_DIR,
                                                item.getFileName()))) {
                                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    setOpenFile(item);
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
            } else {
                ImageView iv_down_look = holder.getView(R.id.down_look_btn);
                iv_down_look.setVisibility(View.INVISIBLE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyToast.showToast(getActivity(), "文件为空，无法查看");

                    }
                });
            }
        }
    }

    private boolean judgePDF(String fileType) {
        if (fileType.equals("PDF") || fileType.equals("pdf"))
            return true;
        else
            return false;
    }

    private void setOpenFile(FileListBean dataBean) {
        switch (dataBean.getType()) {
            case "pdf":
            case "PDF":
                String tokenString = (String) SPUtils.get(getActivity(), SPUtils.ACCESS_TOKEN, "");
                String urlString = HttpURLUtil.newInstance().append("/cost/pdf/view")
                        .append(dataBean.getIdX() + "")
                        .toString() + ".htm?token=" + tokenString
                        + "&type=" + dataBean.getType();
                Bundle bundle = new Bundle();
                bundle.putString("TITLE", dataBean.getFileName());
                bundle.putString("URL", urlString);
                Intent intent = new Intent(getActivity(), WebVacationActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;


            case "xlsx":
                File tmpFile = new File(Config.CACHE_FILE_DIR, dataBean.getFileName());
                if (tmpFile.exists()) {
                    FileUtill.openFile(tmpFile, getActivity());
                } else {
                    MyToast.showToast(getActivity(), "文件不存在");
                }
                break;
            default:

                File tmpFile2 = new File(Config.CACHE_FILE_DIR, dataBean.getFileName());
                if (tmpFile2.exists()) {
                    FileUtill.openFile(tmpFile2, getActivity());
                } else {
                    MyToast.showToast(getActivity(), "文件不存在");
                }
                break;
        }
    }

    private void setICON(String fileType, View view) {
        switch (fileType.toLowerCase()) {
            case "pdf":
                view.setBackgroundResource(R.mipmap.file_pdf);
                break;
            case "xlsx":
                view.setBackgroundResource(R.mipmap.file_excel);
                break;
            case "xls":
                view.setBackgroundResource(R.mipmap.file_excel);
                break;
            case "csv":
                view.setBackgroundResource(R.mipmap.file_csv);
                break;
            case "word":
            case "docx":
            case "doc":
                view.setBackgroundResource(R.mipmap.file_word);
                break;
            case "pptx":
            case "ppt":
                view.setBackgroundResource(R.mipmap.file_ppt);
                break;
            case "rtf":
                view.setBackgroundResource(R.mipmap.file_rtf);
                break;
            case "mp3":
                view.setBackgroundResource(R.mipmap.file_mp3);
                break;
            case "rar":
            case "zip":
                view.setBackgroundResource(R.mipmap.file_yasuo);
                break;
            case "msg":
                view.setBackgroundResource(R.mipmap.file_msg);
                break;
            case "other":
                view.setBackgroundResource(R.mipmap.file_other_unkonw);
                break;
            default:
                view.setBackgroundResource(R.mipmap.file_other_unkonw);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
