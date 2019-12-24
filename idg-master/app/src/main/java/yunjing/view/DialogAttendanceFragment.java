package yunjing.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.ScreenUtils;
import com.cxgz.activity.cxim.ui.company.bean.DeptBeanList;
import com.google.gson.reflect.TypeToken;
import com.injoy.idg.R;

import java.util.List;

import yunjing.utils.DividerItemDecoration2;


/**
 * Created by selson on 2017/8/26.
 */

public class DialogAttendanceFragment extends DialogFragment
{
    private String title;
    private String content;

    /**
     * @param title
     * @param message
     * @return
     */
    public static DialogAttendanceFragment newInstance(String title, String message)
    {
        DialogAttendanceFragment instance = new DialogAttendanceFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("content", message);
        instance.setArguments(args);
        return instance;
    }

    int height;
    int width;
    Window dialogWindow;

    public void onResume()
    {
        super.onResume();
        dialogWindow = getDialog().getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = width;
        dialogWindow.setAttributes(lp);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View layout = inflater.inflate(R.layout.common_dialog_layout, null);

        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerView);

        height = ScreenUtils.getScreenHeight(getActivity());
        width = ScreenUtils.getScreenWidth(getActivity());
        if (height != 0)
            height = (height * 2) / 4;
        if (width != 0)
            width = (width * 3) / 4;

        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        params.height = height;
        recyclerView.setLayoutParams(params);


        TextView title = (TextView) layout.findViewById(R.id.title);

        title.setText(getArguments().getString("title"));
        if (TextUtils.isEmpty(getArguments().getString("title")))
        {
            title.setVisibility(View.GONE);
        } else
        {
            title.setVisibility(View.VISIBLE);
        }

        TextView send_bottom_tv = (TextView) layout.findViewById(R.id.send_bottom_tv);
        send_bottom_tv.setText(getActivity().getResources().getString(R.string.choose_sex_sure));//确定
        TextView delete_bottom_tv = (TextView) layout.findViewById(R.id.delete_bottom_tv);
        delete_bottom_tv.setText(getActivity().getResources().getString(R.string.choose_sex_cancel));//取消


        RelativeLayout send_bottom_rl = (RelativeLayout) layout.findViewById(R.id.send_bottom_rl);
        RelativeLayout delete_bottom_rl = (RelativeLayout) layout.findViewById(R.id.delete_bottom_rl);
        send_bottom_rl.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (tmpData != null)
                    dismiss();

            }
        });
        delete_bottom_rl.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dismiss();
            }
        });

        setAdapter(recyclerView);
        return new AlertDialog.Builder(getActivity()).setView(layout)
                .create();
    }

    /**
     * 请假adapter
     */
    private class Adapter extends BaseQuickAdapter<DeptBeanList.Data, BaseViewHolder>
    {
        public Adapter(@Nullable List<DeptBeanList.Data> data)
        {
            super(R.layout.item_dialog_layout, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, DeptBeanList.Data item)
        {
            if (holder.getLayoutPosition() % 2 == 0)
            {
                holder.setBackgroundColor(R.id.item_layout, getActivity().getResources().getColor(R.color.white));
            } else
            {
                holder.setBackgroundColor(R.id.item_layout, getActivity().getResources().getColor(R.color.itemcolor));
            }

            holder.setText(R.id.customer_contact_tv, item.getDeptName());
        }
    }

    List<DeptBeanList.Data> datas;
    DeptBeanList.Data tmpData;

    private void setAdapter(RecyclerView recyclerView)
    {
        datas = SDGson.toObject(getArguments().getString("content"), new TypeToken<List<DeptBeanList.Data>>()
        {
        }.getType());

        if (datas != null)
        {
            Adapter adapter = new Adapter(datas);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.addItemDecoration(new DividerItemDecoration2(getActivity(),
                    DividerItemDecoration2.VERTICAL_LIST, R.drawable.recyclerview_divider,
                    ScreenUtils.dp2px(getActivity(), 10)));
            recyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                {
                    tmpData = datas.get(position);
                    mListener.onData(tmpData);
                    dismiss();
                }
            });
        }
    }

    public interface InputListener
    {
        void onData(DeptBeanList.Data content);
    }

    private InputListener mListener;

    public void setInputListener(InputListener listener)
    {
        this.mListener = listener;
    }

}
