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
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.ScreenUtils;
import com.google.gson.reflect.TypeToken;
import com.injoy.idg.R;

import java.util.List;


import newProject.company.invested_project.bean.BeanIceProject;
import yunjing.utils.DividerItemDecoration2;

/**
 * Created by selson on 2017/8/26.
 * 项目弹窗
 */
public class DialogFragmentProject extends DialogFragment
{
    /**
     * @param title
     * @param message
     * @return
     */
    public static DialogFragmentProject newInstance(boolean isShowCheck, boolean isAdjust, boolean isOnClickToReturn, String
            title, String message)
    {
        DialogFragmentProject instance = new DialogFragmentProject();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("content", message);
        args.putBoolean("isAdjust", isAdjust);
        args.putBoolean("isShowCheck", isShowCheck);
        args.putBoolean("isOnClickToReturn", isOnClickToReturn);

        instance.setArguments(args);
        return instance;
    }

    int height;
    int width;
    Window dialogWindow;
    private boolean isCanOnClick = false;
    private boolean isShowCheck = false;

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
        View layout = inflater.inflate(R.layout.dialog_fragment_project_layout, null);

        if (getArguments() != null)
        {
            isCanOnClick = getArguments().getBoolean("isOnClickToReturn");
            isShowCheck = getArguments().getBoolean("isShowCheck");
        }

        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerView);
        TextView title = (TextView) layout.findViewById(R.id.title);

        height = ScreenUtils.getScreenHeight(getActivity());
        width = ScreenUtils.getScreenWidth(getActivity());

        datas = SDGson.toObject(getArguments().getString("content"), new TypeToken<List<BeanIceProject>>()
        {
        }.getType());

        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        if (datas != null && datas.size() > 4)
        {
            if (height != 0)
                height = (height * 2) / 5;
            params.height = height;
        } else
        {
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        if (width != 0)
            width = (width * 3) / 4;

//        params.width = width;
        recyclerView.setLayoutParams(params);

        title.setText(getArguments().getString("title"));

        if (TextUtils.isEmpty(getArguments().getString("title")))
        {
            title.setVisibility(View.GONE);
        } else
        {
            title.setVisibility(View.VISIBLE);
        }

        setAdapter(recyclerView);
        return new AlertDialog.Builder(getActivity()).setView(layout)
                .create();
    }

    /**
     *
     */
    private class Adapter extends BaseQuickAdapter<BeanIceProject, BaseViewHolder>
    {
        public Adapter(@Nullable List<BeanIceProject> data)
        {
            super(R.layout.item_dialog_project_type, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, BeanIceProject item)
        {
            holder.setText(R.id.customer_contact_tv, item.getValue());
//            if (holder.getLayoutPosition() % 2 == 0)
//            {
            holder.setBackgroundColor(R.id.item_layout, getActivity().getResources().getColor(R.color.white));
//            } else
//            {
//                holder.setBackgroundColor(R.id.item_layout, getActivity().getResources().getColor(R.color.itemcolor));
//            }
//            if (isShowCheck)
//            {
////                ((RadioButton) holder.getView(R.id.RadioButton1)).setChecked(true);
//            } else
//            {
//                holder.getView(R.id.RadioButton1).setVisibility(View.GONE);
//            }
        }
    }

    List<BeanIceProject> datas;
    BeanIceProject tmpData;

    private void setAdapter(RecyclerView recyclerView)
    {
        if (datas != null)
        {
            Adapter adapter = new Adapter(datas);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.addItemDecoration(new DividerItemDecoration2(getActivity(),
                    DividerItemDecoration2.VERTICAL_LIST, R.drawable.recyclerview_divider,
                    ScreenUtils.dp2px(getActivity(), 0)));
            recyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                {
                    tmpData = datas.get(position);
                    if (!isCanOnClick)
                    {
                        mListener.onData(tmpData);
                        dismiss();
                    }
                }
            });
        }
    }

    public interface InputListener
    {
        void onData(BeanIceProject content);
    }

    private InputListener mListener;

    public void setInputListener(InputListener listener)
    {
        this.mListener = listener;
    }

}
