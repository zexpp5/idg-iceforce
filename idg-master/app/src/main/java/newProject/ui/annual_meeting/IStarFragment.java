package newProject.ui.annual_meeting;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chaoxiang.base.utils.MyToast;
import com.cxgz.activity.cx.base.BaseFragment;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.DialogUtilsIm;
import com.utils.SDToast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import newProject.api.ListHttpHelper;

/**
 * Istar唱将大比拼
 */
public class IStarFragment extends BaseFragment
{
    private Button mConfirmBtn;
    private ListView mListView;
    private VotingAdapter mAdapter;
    private int mBid;

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_istar_mian;
    }

    @Override
    protected void init(View view)
    {
        initViews(view);
    }

    private void initViews(View view)
    {
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            mBid = bundle.getInt("EID");
        }

        mConfirmBtn = (Button) view.findViewById(R.id.confirm_btn);
        mConfirmBtn.setOnClickListener(mOnClickListener);

        mListView = (ListView) view.findViewById(R.id.voting_listview);
        mAdapter = new VotingAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        getListData(mBid + "");

    }


    public void getListData(String bid)
    {
        ListHttpHelper.getIstarList(getActivity(), bid, new SDRequestCallBack(VotingListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(getActivity(), msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                VotingListBean bean = (VotingListBean) responseInfo.getResult();
                if (bean != null)
                {
                    if (bean.getData().getStartVote() == 1)
                    {
                        if (bean.getData().getIsVote() == 1)
                        {
                            mConfirmBtn.setOnClickListener(null);
                            mConfirmBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_radius_gray));

                            List<VotingListBean.DataBean.ItemListBean> listBeanList = bean.getData().getItemList();
                            List<VotingListBean.DataBean.VoteItemList> voteItemLists = bean.getData().getVoteItemList();
                            Iterator<VotingListBean.DataBean.ItemListBean> it = listBeanList.iterator();
                            while (it.hasNext())
                            {
                                VotingListBean.DataBean.ItemListBean itemListBean = it.next();
                                for (int i = 0; i < voteItemLists.size(); i++)
                                {
                                    if (voteItemLists.get(i).getEid() == itemListBean.getEid())
                                    {
                                        itemListBean.setCheck(true);
                                    }
                                }
                            }

                            if (bean.getData().getItemList() != null)
                            {
                                mAdapter.setItemVars(listBeanList, false);
                            }
                        } else
                        {
                            mConfirmBtn.setOnClickListener(mOnClickListener);
                            mConfirmBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_radius_yellow));
                            if (bean.getData().getItemList() != null)
                            {
                                mAdapter.setItemVars(bean.getData().getItemList(), true);
                            }
                        }
                    } else
                    {
                        mConfirmBtn.setOnClickListener(null);
                        mConfirmBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_radius_gray));
                        if (bean.getData().getItemList() != null)
                        {
                            mAdapter.setItemVars(bean.getData().getItemList(), false);
                        }
                        SDToast.showShort("投票通道暂未开启");
                    }

                }
            }
        });
    }


    public void commitData(String meetId, String itemIds)
    {
        ListHttpHelper.commitIstarList(getActivity(), meetId, itemIds, new SDRequestCallBack()
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(getActivity(), msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                if (responseInfo != null && responseInfo.getMsg() != null)
                {
                    MyToast.showToast(getActivity(), responseInfo.getMsg());
                    DialogUtilsIm.dialogPayFinish(getActivity(), "提 示", "投票成功", "确定", "", new
                            DialogUtilsIm
                                    .OnYesOrNoListener2()

                            {
                                @Override
                                public void onYes()
                                {

                                }
                            });
                    getListData(mBid + "");
                }
            }
        });
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v == mConfirmBtn)
            {
                if (mAdapter != null)
                {
                    String str = mAdapter.getSelect();
                    //提交
                    if (str.length() > 0)
                    {
                        commitData(mBid + "", str);
                    } else
                    {
                        MyToast.showToast(getActivity(), "请最少选择一个节目");
                    }
                }
            }
        }
    };


    private class VotingAdapter extends BaseAdapter
    {
        private Context mContext;
        private boolean mIsSelect = false;
        private List<VotingListBean.DataBean.ItemListBean> mItemVars = new ArrayList<>();

        public VotingAdapter(Context mContext)
        {
            this.mContext = mContext;
        }

        @Override
        public int getCount()
        {
            if (mItemVars.size() == 0)
            {
                return 0;
            } else
            {
                return mItemVars.size();
            }
        }

        @Override
        public Object getItem(int position)
        {
            return position;
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ViewHolder holder = null;
            if (convertView == null)
            {
                convertView = inflater.inflate(R.layout.voting_item_layout, null);
                holder = new ViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.voting_item_text);
                holder.imageView = (ImageView) convertView.findViewById(R.id.voting_item_img);
                convertView.setTag(holder);
            } else
            {
                holder = (ViewHolder) convertView.getTag();
            }
            if (mItemVars.get(position).isCheck())
            {
                holder.imageView.setImageResource(R.mipmap.check);
            } else
            {
                holder.imageView.setImageResource(R.mipmap.uncheck);
            }
            final ViewHolder finalHolder = holder;
           /* holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelect(position,finalHolder);
                }
            });*/
            if (mIsSelect)
            {
                holder.imageView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        setSelect(position, finalHolder);
                    }
                });
            }
            if (mItemVars.get(position).getName() != null)
            {
                holder.textView.setText(mItemVars.get(position).getName());
            }
            return convertView;

        }

        public void setSelect(int position, ViewHolder finalHolder)
        {
            if (mItemVars.get(position).isCheck())
            {
                mItemVars.get(position).setCheck(false);
                finalHolder.imageView.setImageResource(R.mipmap.uncheck);
            } else
            {
                int count = 0;
                for (int i = 0; i < mItemVars.size(); i++)
                {
                    if (mItemVars.get(i).isCheck())
                    {
                        count++;
                    }
                }
                if (count < 2)
                {
                    mItemVars.get(position).setCheck(true);
                    finalHolder.imageView.setImageResource(R.mipmap.check);
                } else
                {
                    MyToast.showToast(mContext, "最多选择两个节目");
                }
            }
        }

        public void setItemVars(List<VotingListBean.DataBean.ItemListBean> itemVars, boolean isSelect)
        {
            mIsSelect = isSelect;
            mItemVars.clear();
            mItemVars.addAll(itemVars);
            notifyDataSetChanged();
        }

        public String getSelect()
        {
            String str = "";
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < mItemVars.size(); i++)
            {
                if (mItemVars.get(i).isCheck())
                {
                    builder.append(mItemVars.get(i).getEid() + ",");
                }
            }
            if (builder.length() > 0)
            {
                str = builder.substring(0, builder.length() - 1);
            }
            return str;
        }
    }

    private class ViewHolder
    {
        private TextView textView;
        private ImageView imageView;
    }


}
