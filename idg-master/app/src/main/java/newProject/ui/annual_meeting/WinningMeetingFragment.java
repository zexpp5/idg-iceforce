package newProject.ui.annual_meeting;

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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.bean.MettingPrizeListBean;
import newProject.bean.PrizeInfoListBean;
import yunjing.bean.SelectItemBean;
import yunjing.utils.CommonDialog;
import yunjing.utils.PopupDialog;
import yunjing.utils.ToastUtils;

/**
 * Created by tujingwu on 2018/1/17.
 */

public class WinningMeetingFragment extends Fragment {
    @Bind(R.id.tv_win)
    TextView mTVWin;
    @Bind(R.id.tv_win_number)
    TextView mTVWinNumber;
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;

    private int mBid;
    List<PrizeInfoListBean.DataBean> listDatas = new ArrayList<>();
    List<SelectItemBean> mDialogLists = new ArrayList<>();
    SelectItemBean mCurrentSelect;
    private ListAdapter mListAdapter;

    public static WinningMeetingFragment newInstance(int bid) {
        WinningMeetingFragment winningMeetingFragment = new WinningMeetingFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("bid", bid);
        winningMeetingFragment.setArguments(bundle);
        return winningMeetingFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.winning_meeting_list_fragment, container,false);
        ButterKnife.bind(this, inflate);
        initFragemnt();
        return inflate;
    }

    private void initFragemnt() {
        Bundle arguments = getArguments();
        if (null != arguments) {
            mBid = arguments.getInt("bid");
        }

        mTVWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showListDialog();
            }
        });
        initAdapter();
        getNetData();
    }

    private void showListDialog() {
        if (mDialogLists.size() <= 0) {
            ToastUtils.show(getContext(), "暂无奖项");
            return;
        }
        PopupDialog.showmMonthDialogUtil(mCurrentSelect, getActivity(), "奖 项", mDialogLists, "", new CommonDialog.CustomerTypeInterface() {
            @Override
            public void menuItemClick(SelectItemBean bean, int index) {
                mCurrentSelect = bean;
                mCurrentSelect.setSelectIndex(index);
                getListNetData(bean.getId());
                mTVWin.setText(bean.getSelectString()+"");
            }
        });
    }

    private void initAdapter() {
        mListAdapter = new ListAdapter(R.layout.item_meeting_win_prize, listDatas);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerview.setAdapter(mListAdapter);
    }

    private void getNetData() {
        ListHttpHelper.getPrize(getContext(), mBid + "", new SDRequestCallBack(MettingPrizeListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                ToastUtils.show(getContext(), msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                MettingPrizeListBean bean = (MettingPrizeListBean) responseInfo.getResult();
                List<MettingPrizeListBean.DataBean> data = bean.getData();
                if (null != data) {
                    if (data.size() <= 0)
                        return;
                    mTVWin.setText(null == data.get(0).getName() ? "" : data.get(0).getName());
                    getListNetData(data.get(0).getEid() + "");
                    for (int i = 0; i < data.size(); i++) {
                        mDialogLists.add(new SelectItemBean(data.get(i).getName(), i, data.get(i).getEid() + ""));
                    }
                    if (mDialogLists.size() > 0) {
                        mCurrentSelect = mDialogLists.get(0);
                        mCurrentSelect.setSelectIndex(0);
                    }
                }
            }
        });
    }


    private void getListNetData(String eid) {
        ListHttpHelper.getPrizeInfo(getContext(), eid, new SDRequestCallBack(PrizeInfoListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                ToastUtils.show(getContext(), msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                PrizeInfoListBean bean = (PrizeInfoListBean) responseInfo.getResult();
                if (null != bean && null != bean.getData()) {
                    setListInfoData(bean.getData());
                }
            }
        });
    }

    private void setListInfoData(List<PrizeInfoListBean.DataBean> data) {
        if (null == data)
            return;
        mListAdapter.setNewData(data);
        mTVWinNumber.setText(data.size() + "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


    public class ListAdapter extends BaseQuickAdapter<PrizeInfoListBean.DataBean, BaseViewHolder> {

        public ListAdapter(@LayoutRes int layoutResId, @Nullable List<PrizeInfoListBean.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, PrizeInfoListBean.DataBean item) {
            helper.setText(R.id.name, null == item.getName() ? "" : item.getName());

            if (getData().size() != 0 && helper.getLayoutPosition() == getData().size()-1){
                helper.setVisible(R.id.view_line,false);
            }else{
                helper.setVisible(R.id.view_line,true);
            }

            ImageView head = helper.getView(R.id.head);
            if ( null == item.getIcon() || item.getIcon().isEmpty()) {
                head.setImageResource(R.mipmap.contact_icon);
            } else {
                Glide.with(getContext())
                        .load(item.getIcon())
                        .error(R.mipmap.contact_icon)
                        .into(head);
            }
        }
    }
}
