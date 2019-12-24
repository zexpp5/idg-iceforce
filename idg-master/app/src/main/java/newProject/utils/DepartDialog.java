package newProject.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.injoy.idg.R;

import java.util.ArrayList;
import java.util.List;

import yunjing.adapter.DialogListAdapter;
import yunjing.bean.ListDialogBean;
import yunjing.utils.DisplayUtil;
import yunjing.utils.DividerItemDecoration;

import static com.injoy.idg.R.id.tvTitle;

/**
 * Created by tu2460 on 2017/7/31.
 * 列表dialog,主要是针对 新增同事（不是通用dialog）
 */

public class DepartDialog extends Dialog {
    private boolean cancelTouchout;
    private Context mContext;
    private boolean isMultiselect = false;//是否是多选(默认单选)
    ;
    private SigleSelectListener sigleSelectListener;
    private MultiSelectListener multiSelectListener;
    private ListDialogBean sigleSelectData;//单选数据
    private ListDialogBean sigleOkData;//单选选中并且确定的数据
    private List<ListDialogBean> multiselectData;//多选数据

    private RecyclerView mRecyclerView;
    private TextView mTitleTV;
    private Button mOkBtn, mCancelBtn;
    private DialogListAdapter dialogListAdapter;

    /**
     * 单选监听接口
     */
    public interface SigleSelectListener {
        void itemSelectListener(ListDialogBean selectItemBean);

        void okListener();
    }

    /**
     * 多选监听接口
     */
    public interface MultiSelectListener {
        void itemSelectListener(List<ListDialogBean> selectItemData);
    }

    ;

    public static Builder newBuilder(Context context) {
        return new Builder(context);
    }


    private DepartDialog(Builder builder) {
        super(builder.context);
        mContext = builder.context;
        cancelTouchout = builder.cancelTouchout;
        isMultiselect = builder.isMultiselect;
        sigleSelectListener = builder.sigleSelectListener;
        multiSelectListener = builder.multiSelectListener;
    }

    private DepartDialog(Builder builder, int resStyle) {
        super(builder.context, resStyle);
        mContext = builder.context;
        cancelTouchout = builder.cancelTouchout;
        isMultiselect = builder.isMultiselect;
        sigleSelectListener = builder.sigleSelectListener;
        multiSelectListener = builder.multiSelectListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.depart_dialog);
        findView();
        setCanceledOnTouchOutside(cancelTouchout);
        initRecycerView();
        initListener();
        initBtnBg();
    }

    private void initBtnBg() {
        Drawable drawable = mContext.getResources().getDrawable(R.mipmap.public_add_white);
        Drawable drawable2 = mContext.getResources().getDrawable(R.mipmap.public_cancel_gray);
        drawable.setBounds(0, 0, 30, 30);
        drawable2.setBounds(0, 0, 30, 30);
        mOkBtn.setCompoundDrawables(drawable, null, null, null);
        mCancelBtn.setCompoundDrawables(drawable2, null, null, null);
    }

    private void findView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mTitleTV = (TextView) findViewById(tvTitle);
        mOkBtn = (Button) findViewById(R.id.btnOk);
        mCancelBtn = (Button) findViewById(R.id.btnCancel);
    }


    private void initRecycerView() {
        if (isMultiselect)
            multiselectData = new ArrayList<>();
        if (multiselectData != null)
            multiselectData.clear();
        List<ListDialogBean> data = new ArrayList<>();
        dialogListAdapter = new DialogListAdapter(R.layout.item_dialog_list_apply, data);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), R.drawable.recyclerview_divider);
        dividerItemDecoration.setDrawBorderTopAndBottom(true);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setAdapter(dialogListAdapter);
        dialogListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!isMultiselect) {//单选
                    sigleSelect(adapter, position);
                } else {//多选
                    multiselect(adapter, position);
                }
            }
        });
    }

    /**
     * 单选
     *
     * @param adapter
     * @param position
     */
    private void sigleSelect(BaseQuickAdapter adapter, int position) {
        for (int i = 0; i < adapter.getData().size(); i++) {
            if (i == position)
                ((ListDialogBean) adapter.getData().get(i)).setCheck(true);
            else
                ((ListDialogBean) adapter.getData().get(i)).setCheck(false);
        }

        sigleSelectData = (ListDialogBean) adapter.getData().get(position);
        if (!isMultiselect) {//单选
            if (null != sigleSelectData)
                sigleSelectListener.itemSelectListener(sigleSelectData);
            dismiss();
        } else if (null != multiselectData) {//多选
            // multiSelectListener.itemSelectListener(multiselectData);
            dismiss();
        } else
            dismiss();
        adapter.notifyDataSetChanged();
        dismiss();
    }


    /**
     * 多选
     *
     * @param adapter
     * @param position
     */
    private void multiselect(BaseQuickAdapter adapter, int position) {
        ListDialogBean listDialogBean = (ListDialogBean) adapter.getData().get(position);
        if (listDialogBean.isCheck())
            listDialogBean.setCheck(false);
        else {
            listDialogBean.setCheck(true);
            for (ListDialogBean ldb : multiselectData) {
                if (!ldb.getContent().equals(listDialogBean.getContent()))
                    multiselectData.add(listDialogBean);
            }
        }
        adapter.notifyDataSetChanged();
        dismiss();
    }

    private void initListener() {
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMultiselect) {//单选
                    sigleSelectListener.okListener();
                    dismiss();
                } else if (null != multiselectData) {//多选
                    // multiSelectListener.itemSelectListener(multiselectData);
                    dismiss();
                } else
                    dismiss();

            }
        });
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelStatu();
            }
        });
    }


    private void cancelStatu() {
        if (!isMultiselect && null != sigleSelectData) {//单选
            if (null != sigleOkData && sigleOkData != sigleSelectData) {
                sigleOkData.setCheck(true);
                sigleSelectData.setCheck(false);
            } else if (null == sigleOkData) {
                sigleSelectData.setCheck(false);
            }
        } else if (isMultiselect && null != multiselectData) {//多选
            for (ListDialogBean ldb : multiselectData) {
                //do something...
            }
        }
        dismiss();
    }


    public void setData(List<ListDialogBean> newData) {
        if (dialogListAdapter != null && null != newData) {
            dialogListAdapter.setNewData(newData);
            //定义布局宽和高具体值
            //  if (newData.size() > 5) {
            ViewGroup.LayoutParams layoutParams = mRecyclerView.getLayoutParams();
            layoutParams.width = DisplayUtil.dp2px(mContext, 250);//宽高设置具体大小
            layoutParams.height = DisplayUtil.dp2px(mContext, 200);
            mRecyclerView.setLayoutParams(layoutParams);
            //   }

            if (!isMultiselect) {
                //把选中状态赋值,好保存已经选择状态
                for (ListDialogBean ldb : newData) {
                    if (ldb.isCheck())
                        sigleOkData = ldb;
                }
            }


        }
    }

    public void setTitle(String titleStr) {
        if (TextUtils.isEmpty(titleStr))
            mTitleTV.setText("");
        else if (titleStr.length() == 2) {//老板说要空格好看点
            String oneStr = titleStr.substring(0, 1);
            String twoStr = titleStr.substring(1, titleStr.length());
            mTitleTV.setText(oneStr + "  " + twoStr);
        } else {
            mTitleTV.setText(titleStr);
        }
    }


    ;

    public static final class Builder {

        private Context context;
        private boolean cancelTouchout;
        private int resStyle = -1;
        private SigleSelectListener sigleSelectListener;//单选item点击监听
        private MultiSelectListener multiSelectListener;//多选item点击监听
        private boolean isMultiselect;//是否是多选


        public Builder(Context context) {
            this.context = context;
        }

        public Builder setItemListener(SigleSelectListener sigleSelectListener) {
            this.sigleSelectListener = sigleSelectListener;
            return this;
        }

        public Builder setItemListener(MultiSelectListener sigleSelectListener) {
            this.multiSelectListener = multiSelectListener;
            return this;
        }

        public Builder isMultiselect(boolean isMultiselect) {
            this.isMultiselect = isMultiselect;
            return this;
        }


        public Builder style(int resStyle) {
            this.resStyle = resStyle;
            return this;
        }

        public Builder cancelTouchout(boolean val) {
            cancelTouchout = val;
            return this;
        }

        public DepartDialog build() {
            if (resStyle != -1) {
                return new DepartDialog(this, resStyle);
            } else {
                return new DepartDialog(this);
            }
        }
    }

}
