package newProject.company.project_manager.investment_project;

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
import android.view.animation.DecelerateInterpolator;
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
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.okhttp.Request;
import com.superdata.im.entity.CxFileMessage;
import com.utils.SDToast;
import com.utils.SPUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.project_manager.investment_project.bean.AnnexListBean;
import newProject.company.project_manager.investment_project.bean.FileAnnexBean;
import newProject.company.vacation.WebVacationActivity;
import newProject.ui.annual_meeting.AnnualMeetingMainActivity;
import yunjing.utils.DisplayUtil;
import yunjing.view.StatusTipsView;


/**
 *
 */
public class AnnexListFragment extends ERPSumbitBaseFragment
{
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mLoadingView;
    private List<FileAnnexBean.DataBean> mDataLists = new ArrayList<>();
    private ApplyAdapter mApplyAdapter;
    private int mPage = 1;
    //总条数
    private int mListTotalSize = 0;

    public static Fragment newInstance(int id)
    {
        Bundle args = new Bundle();
        args.putInt("EID", id);
        AnnexListFragment fragment = new AnnexListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSelectedImg(List<String> imgPaths)
    {

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

    @Override
    protected int getContentLayout()
    {
        return R.layout.annex_list_layout;
    }

    @Override
    protected void init(View view)
    {
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            mEid = bundle.getInt("EID", 0);
        }
//        mEid = 16322;
        initRefresh();
        initRV();

        judgeAnnexPower(mEid + "");
    }

    private void getData()
    {
        if (!DisplayUtil.hasNetwork(getContext()))
        {
            mLoadingView.showAccessFail();
        } else
        {
            refresh();
            getNetData();
        }
    }

    private void refresh()
    {
        dataBeanList.clear();
        mPage = 1;
    }

    public void initRefresh()
    {
        mRefreshLayout.setEnableLoadmore(true);
        mRefreshLayout.setReboundInterpolator(new DecelerateInterpolator());
        mRefreshLayout.setDragRate(0.25f);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(getActivity()))
                {
                    refresh();
                    mPage = 1;
                    getNetData();
                    mRefreshLayout.finishRefresh(1000);
                    mRefreshLayout.setLoadmoreFinished(false);

                } else
                {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishRefresh(1000);
                }

            }
        });

        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener()
        {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(getActivity()))
                {
                    if (mDataLists.size() >= mListTotalSize)
                    {
                        mRefreshLayout.finishLoadmore(1000);
                        mRefreshLayout.setLoadmoreFinished(true);
                    } else
                    {
                        mPage = mPage + 1;
                        getNetData();
                        mRefreshLayout.finishLoadmore(1000);
                    }
                } else
                {
                    MyToast.showToast(getActivity(), "请检查网络连接");
                    mRefreshLayout.finishLoadmore(1000);
                }

            }
        });

        mLoadingView.showLoading();
        mLoadingView.setOnVisibleChangeListener(new StatusTipsView.OnVisibleChangeListener()
        {
            @Override
            public void onVisibleChanged(boolean visible)
            {
                if (mRecyclerview != null)
                {
                    mRecyclerview.setVisibility(visible ? View.GONE : View.VISIBLE);
                }
            }
        });

        mLoadingView.setOnRetryListener(new StatusTipsView.OnRetryListener()
        {
            @Override
            public void onRetry()
            {
                mLoadingView.showLoading();
                getNetData();
            }
        });
    }

    private List<AnnexListBean.DataBean> dataBeanList = new ArrayList<>();

    private void getNetData()
    {
        ListHttpHelper.getAnnexList(getActivity(), mEid + "/" + mPage, new SDRequestCallBack(FileAnnexBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                if (error.getExceptionCode() == 400)
                {
                    mLoadingView.showNoContent(msg);
                }
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                FileAnnexBean listBean = (FileAnnexBean) responseInfo.getResult();
                if (null != listBean && null != listBean.getData() && !listBean.getData().isEmpty())
                {
                    if (mPage == 1)
                    {
                        mListTotalSize = listBean.getTotal();
                        mDataLists.clear();
                    }
                    mDataLists.addAll(listBean.getData());
                    mLoadingView.hide();
                    mApplyAdapter.notifyDataSetChanged();
                } else if (mPage == 1)
                {
                    mLoadingView.showNoContent("暂未有附件");
                }
            }
        });
    }


    private void initRV()
    {
        mApplyAdapter = new ApplyAdapter(R.layout.annex_item_layout, mDataLists);

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

    private boolean isHasPower = false;

    private void judgeAnnexPower(String projId)
    {
        ListHttpHelper.judgeAnnexPower(getActivity(), projId, new SDRequestCallBack()
        {
            @Override

            public void onRequestFailure(HttpException error, String msg)
            {
                isHasPower = false;
                getData();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                isHasPower = true;
                getData();
            }
        });
    }

    private class ApplyAdapter extends BaseQuickAdapter<FileAnnexBean.DataBean, BaseViewHolder>
    {
        public ApplyAdapter(@LayoutRes int layoutResId, @Nullable List<FileAnnexBean.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(final BaseViewHolder holder, final FileAnnexBean.DataBean item)
        {

            View line_bottom = holder.getView(R.id.line_bottom);
            if (holder.getAdapterPosition() == mData.size() - 1)
            {
                line_bottom.setVisibility(View.INVISIBLE);
            } else
            {
                line_bottom.setVisibility(View.VISIBLE);
            }

            if (StringUtils.notEmpty(item.getAttaName()))
                holder.setText(R.id.annex_item_title, item.getAttaName());
            if (StringUtils.notEmpty(item.getUploadTime()))
                holder.setText(R.id.annex_item_time, item.getUploadTime());

            if (StringUtils.notEmpty(item.getFileType()) && StringUtils.notEmpty(holder.getView(R.id.annex_image)))
            {
                setICON(item.getFileType(), holder.getView(R.id.annex_image));
            } else
            {
                setICON("other", holder.getView(R.id.annex_image));
            }

            if (StringUtils.notEmpty(holder.getView(R.id.down_look_btn)) && StringUtils.notEmpty(item.getAttaName()))
            {
                if (judgePDF(item.getFileType()))
                {
                    holder.getView(R.id.down_look_btn).setVisibility(View.INVISIBLE);
                    holder.itemView.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            if (!isHasPower)
                            {
                                MyToast.showToast(getActivity(), "您没有查看的权限");
                                return;
                            }
                            setOpenFile(item);
                        }
                    });
                } else
                {
                    if (StringUtils.notEmpty(FileDownLoadUtils.getInstance().getFilePath(Config.CACHE_FILE_DIR,
                            item.getAttaName())))
                    {
                        holder.getView(R.id.down_look_btn).setVisibility(View.INVISIBLE);
                        holder.itemView.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                if (!isHasPower)
                                {
                                    MyToast.showToast(getActivity(), "您没有下载权限");
                                    return;
                                }
                                setOpenFile(item);
                            }
                        });
                    } else
                    {
                        holder.getView(R.id.down_look_btn).setVisibility(View.VISIBLE);
                        holder.getView(R.id.down_look_btn).setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                if (!isHasPower)
                                {
                                    MyToast.showToast(getActivity(), "您没有下载权限");
                                    return;
                                }
                                holder.getView(R.id.down_look_btn).setEnabled(false);
                                String tokenString = (String) SPUtils.get(getActivity(), SPUtils.ACCESS_TOKEN, "");
                                String unCodeFileName = "";

                                if (StringUtils.notEmpty(item.getAttaName()))
                                {   //后台要求必须转码两次。
                                    try
                                    {
                                        unCodeFileName = URLEncoder.encode(URLEncoder.encode(item.getAttaName(), "utf-8"),
                                                "utf-8");

                                    } catch (UnsupportedEncodingException e)
                                    {
                                        e.printStackTrace();
                                    }
                                } else
                                {
                                    unCodeFileName = "";
                                }

                                String downLoadUrl = HttpURLUtil.newInstance()
                                        .append("/project/detail/annex/download.htm?token=")
                                        .append(tokenString)
                                        .toString()
                                        + "&fileName=" + unCodeFileName
                                        + "&boxId=" + item.getBoxId() + ""
                                        + "&projId=" + mEid;

                                OkHttpUtils.get().url(downLoadUrl).build().execute(new FileCallBack(Config
                                        .CACHE_FILE_DIR, item.getAttaName())
                                {
                                    @Override
                                    public void inProgress(float progress)
                                    {
                                        float b = (float) (Math.round(progress * 100)) / 100;
                                        DecimalFormat decimalFormat = new DecimalFormat(".00");
//                                        holder.setText(R.id.down_look_btn, decimalFormat.format(b) + "%");
                                    }

                                    @Override
                                    public void onError(Request request, Exception e) throws Exception
                                    {
                                        ImageView iv_down_look = holder.getView(R.id.down_look_btn);
                                        iv_down_look.setVisibility(View.VISIBLE);
                                        holder.getView(R.id.down_look_btn).setEnabled(true);
                                    }

                                    @Override
                                    public void onResponse(File response) throws Exception
                                    {
                                        ImageView iv_down_look = holder.getView(R.id.down_look_btn);
                                        holder.getView(R.id.down_look_btn).setEnabled(true);
                                        iv_down_look.setVisibility(View.INVISIBLE);
                                        if (StringUtils.notEmpty(FileDownLoadUtils.getInstance().getFilePath(Config
                                                        .CACHE_FILE_DIR,
                                                item.getAttaName())))
                                        {
                                            holder.itemView.setOnClickListener(new View.OnClickListener()
                                            {
                                                @Override
                                                public void onClick(View v)
                                                {
                                                    if (!isHasPower)
                                                    {
                                                        MyToast.showToast(getActivity(), "您没有下载权限");
                                                        return;
                                                    }
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
            } else
            {
                ImageView iv_down_look = holder.getView(R.id.down_look_btn);
                iv_down_look.setVisibility(View.INVISIBLE);
                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        MyToast.showToast(getActivity(), "文件为空，无法查看");

                    }
                });
            }
        }
    }

    private boolean judgePDF(String fileType)
    {
        if (fileType.equals("PDF") || fileType.equals("pdf"))
            return true;
        else
            return false;
    }

    private void setOpenFile(FileAnnexBean.DataBean dataBean)
    {
        switch (dataBean.getFileType())
        {
            case "pdf":
                String tokenString = (String) SPUtils.get(getActivity(), SPUtils.ACCESS_TOKEN, "");
                String urlString = HttpURLUtil.newInstance().append("/pdf/view/proAnnex")
                        .append(dataBean.getBoxId() + "")
                        .toString() + ".htm?token=" + tokenString;
                Bundle bundle = new Bundle();
                bundle.putString("TITLE", dataBean.getAttaName());
                bundle.putString("URL", urlString);
                Intent intent = new Intent(getActivity(), WebVacationActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case "PDF":
                String tokenString2 = (String) SPUtils.get(getActivity(), SPUtils.ACCESS_TOKEN, "");
                String urlString2 = HttpURLUtil.newInstance().append("/pdf/view/proAnnex")
                        .append(dataBean.getBoxId() + "")
                        .toString() + ".htm?token=" + tokenString2;
                Bundle bundle2 = new Bundle();
                bundle2.putString("TITLE", dataBean.getAttaName());
                bundle2.putString("URL", urlString2);
                Intent intent2 = new Intent(getActivity(), WebVacationActivity.class);
                intent2.putExtras(bundle2);
                startActivity(intent2);
                break;

            case "xlsx":
                File tmpFile = new File(Config.CACHE_FILE_DIR, dataBean.getAttaName());
                if (tmpFile.exists())
                {
                    FileUtill.openFile(tmpFile, getActivity());
                } else
                {
                    MyToast.showToast(getActivity(), "文件不存在");
                }
                break;
//            case "xlsx":
//
//                break;
            default:
                File tmpFile2 = new File(Config.CACHE_FILE_DIR, dataBean.getAttaName());
                if (tmpFile2.exists())
                {
                    FileUtill.openFile(tmpFile2, getActivity());
                } else
                {
                    MyToast.showToast(getActivity(), "文件不存在");
                }
                break;
        }
    }

    private void setICON(String fileType, View view)
    {
        switch (fileType.toLowerCase())
        {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
