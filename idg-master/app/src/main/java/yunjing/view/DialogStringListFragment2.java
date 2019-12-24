package yunjing.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.ScreenUtils;
import com.cxgz.activity.cxim.bean.StringList;
import com.google.gson.reflect.TypeToken;
import com.injoy.idg.R;

import java.util.List;

import yunjing.utils.DividerItemDecoration2;


/**
 * Created by selson on 2017/8/26.
 */
public class DialogStringListFragment2 extends DialogFragment
{
    private String content;

    /**
     * @param message
     * @return
     */
    public static DialogStringListFragment2 newInstance(String message)
    {
        DialogStringListFragment2 instance = new DialogStringListFragment2();
        Bundle args = new Bundle();
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
        View layout = inflater.inflate(R.layout.common_dialog_list_layout, null);

        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerView);

        height = ScreenUtils.getScreenHeight(getActivity());
        width = ScreenUtils.getScreenWidth(getActivity());
//        if (height != 0)
//            height = (height * 2) / 4;
        if (width != 0)
            width = (width * 3) / 5;

        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        params.width = width;
        recyclerView.setLayoutParams(params);

        setAdapter(recyclerView);
        return new AlertDialog.Builder(getActivity()).setView(layout)
                .create();
    }

    /**
     *
     */
    private class Adapter extends BaseQuickAdapter<StringList.Data, BaseViewHolder>
    {
        public Adapter(@Nullable List<StringList.Data> data)
        {
            super(R.layout.item_dialog_list_layout, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, StringList.Data item)
        {
            holder.setBackgroundColor(R.id.item_layout, getActivity().getResources().getColor(R.color.white));
            holder.setText(R.id.customer_contact_tv, item.getName());
        }
    }

    List<StringList.Data> datas;
    StringList.Data tmpData;

    private void setAdapter(RecyclerView recyclerView)
    {
        datas = SDGson.toObject(getArguments().getString("content"), new TypeToken<List<StringList.Data>>()
        {
        }.getType());

        if (datas != null)
        {
            Adapter adapter = new Adapter(datas);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.addItemDecoration(new DividerItemDecoration2(getActivity(),
                    DividerItemDecoration2.HORIZONTAL_LIST, R.drawable.recyclerview_divider,
                    ScreenUtils.dp2px(getActivity(), 1)));
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
        void onData(StringList.Data content);
    }

    private InputListener mListener;

    public void setInputListener(InputListener listener)
    {
        this.mListener = listener;
    }

}
