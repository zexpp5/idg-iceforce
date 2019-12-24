package newProject.ui.annual_meeting;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chaoxiang.base.utils.MyToast;
import com.cxgz.activity.cxim.view.ListViewForScrollView;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;

import java.util.ArrayList;
import java.util.List;

import newProject.api.ListHttpHelper;
import yunjing.view.CustomNavigatorBar;

/**
 * Created by Administrator on 2018/1/9.
 */

public class AnnualProgramFragment extends Fragment
{
    private ListView mListView;
    private VotingAdapter mAdapter;
    private int mBid;

    private void initViews(View view)
    {
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            mBid = bundle.getInt("EID");
        }
        mListView = (ListView) view.findViewById(R.id.voting_listview);
        mAdapter = new VotingAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        getListData(mBid + "");

    }


    public void getListData(String bid)
    {
        ListHttpHelper.getVotingList(getActivity(), bid, new SDRequestCallBack(VotingListBean.class)
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
                    if (bean.getData().getItemList() != null)
                    {
                        mAdapter.setItemVars(bean.getData().getItemList());
                    }
                }
            }
        });
    }

    private class VotingAdapter extends BaseAdapter
    {
        private Context mContext;
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
            if (mItemVars != null && mItemVars.size() > 0)
            {
                return mItemVars.get(position);
            } else
            {
                return null;
            }
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ViewHolder holder = null;
            if (convertView == null)
            {
                convertView = inflater.inflate(R.layout.program_item_layout, null);
                holder = new ViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.voting_item_text);
                holder.positionText = (TextView) convertView.findViewById(R.id.voting_item_position);
                convertView.setTag(holder);
            } else
            {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.positionText.setText(position + 1 + ".");

            if (mItemVars.get(position).getName() != null)
            {
                holder.textView.setText(mItemVars.get(position).getName());
            }
            return convertView;
        }


        public void setItemVars(List<VotingListBean.DataBean.ItemListBean> itemVars)
        {
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
        private TextView positionText;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.program_list_fragment, null);
//        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }


}
