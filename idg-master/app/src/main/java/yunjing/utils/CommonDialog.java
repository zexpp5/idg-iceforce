package yunjing.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaoxiang.base.utils.ScreenUtils;
import com.cxgz.activity.cxim.adapter.CommonAdapter;
import com.cxgz.activity.cxim.adapter.ViewHolder;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.injoy.idg.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import yunjing.bean.SelectItemBean;

import static com.cxgz.activity.cxim.utils.MainTopMenuUtils.mContext;
import static com.injoy.idg.R.id.ll_search;
import static com.injoy.idg.R.id.query;
import static com.injoy.idg.R.id.warehouse_reason;
import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by Administrator on 2017/7/31.
 */
public class CommonDialog extends Dialog
{
    private static TextView send_bottom_tv;
    private static TextView delete_bottom_tv;
    public static SelectItemBean selectBean;
    static List<SelectItemBean> datas;
    static CommonDialog dialog = null;
    private Context mContext;

    public CommonDialog(Context context)
    {
        super(context);
        mContext = context;
    }

    public CommonDialog(Context context, int theme)
    {
        super(context, theme);
        mContext = context;
    }

    protected CommonDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
    {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    public static CustomerTypeInterface getmCustomerTypeInterface()
    {
        return mCustomerTypeInterface;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        if (!hasFocus)
        {
            return;
        }
//        setHeight();
    }

//    private void setHeight()
//    {
//        Window window = getWindow();
//        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
//        WindowManager.LayoutParams attributes = window.getAttributes();
//        if (window.getDecorView().getHeight() >= (int) (displayMetrics.heightPixels * 0.6))
//        {
//            attributes.height = (int) (displayMetrics.heightPixels * 0.6);
//        }
//        window.setAttributes(attributes);
//    }

    public static class Builder
    {
        private Context context;
        private String title;
        private CommonAdapter<SelectItemBean> adapter;
        private boolean changce = false;

        public Builder(Context context)
        {
            this.context = context;
        }

        public Builder(String title, Context context)
        {
            this.title = title;
            this.context = context;
        }

        public Builder(Context context, String title, boolean changce)
        {
            this.context = context;
            this.title = title;
            this.changce = changce;
        }

        public Context getContext()
        {
            return context;
        }

        public void setContext(Context context)
        {
            this.context = context;
        }

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public CommonDialog create(final List<SelectItemBean> list)
        {
            datas = list;
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            dialog = new CommonDialog(context);
            View layout = inflater.inflate(R.layout.commom_dialog_layout, null);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            final ListView mlistView = (ListView) layout.findViewById(R.id.lv_ls_project);

            if (list.size() > 5)
            {
                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                ViewGroup.LayoutParams params = mlistView.getLayoutParams();
//                params.height = (int) (displayMetrics.heightPixels * 0.5);
                params.height = (int) ScreenUtils.dp2px(context, 45) * 6;
                mlistView.setLayoutParams(params);
            }

            if (changce)
            {
                LinearLayout ll_search = (LinearLayout) layout.findViewById(R.id.ll_search);
                ll_search.setVisibility(View.VISIBLE);
                final EditText query = (EditText) layout.findViewById(R.id.query);
                ImageButton search_clear = (ImageButton) layout.findViewById(R.id.search_clear);
                query.addTextChangedListener(new TextWatcher()
                {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after)
                    {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count)
                    {
                        if (s.length() > 0)
                        {
                            String str_s = query.getText().toString().trim();
                            List<SelectItemBean> user_temp = new ArrayList<SelectItemBean>();
                            for (SelectItemBean user : list)
                            {
                                String uesrname = user.getSelectString();
                                if (uesrname.contains(str_s))
                                {
                                    user_temp.add(user);
                                }
                            }
                            setAdapter(mlistView, user_temp);
                        } else
                        {
                            setAdapter(mlistView, list);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s)
                    {

                    }
                });

                search_clear.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        query.setText("");
                    }
                });
            }

            TextView warehouse_reason = (TextView) layout.findViewById(R.id.warehouse_reason);//老板说仓库的出入库理由要加title
            warehouse_reason.setText(title);
            if (TextUtils.isEmpty(title))
            {
                warehouse_reason.setVisibility(View.GONE);
            } else
            {
                warehouse_reason.setVisibility(View.VISIBLE);
            }
//            View warehouse_reason_line =  layout_city.findViewById(R.id.warehouse_reason_line);//老板说仓库的出入库理由要加title
//            /**
//             * 无搜索栏隐藏线
//             */
//            warehouse_reason_line.setVisibility(View.GONE);

            send_bottom_tv = (TextView) layout.findViewById(R.id.send_bottom_tv);
            send_bottom_tv.setText(context.getResources().getString(R.string.choose_sex_sure));//确定
            delete_bottom_tv = (TextView) layout.findViewById(R.id.delete_bottom_tv);
            delete_bottom_tv.setText(context.getResources().getString(R.string.choose_sex_cancel));//取消
            RelativeLayout send_bottom_rl = (RelativeLayout) dialog.findViewById(R.id.send_bottom_rl);
            RelativeLayout delete_bottom_rl = (RelativeLayout) dialog.findViewById(R.id.delete_bottom_rl);
            send_bottom_rl.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    selectDialog();
                    if (dialog != null)
                    {
                        dialog.dismiss();
                    }
                }
            });
            delete_bottom_rl.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (dialog != null)
                    {
                        dialog.dismiss();
                    }
                }
            });

            setAdapter(mlistView, list);
            dialog.setContentView(layout);
            return dialog;
        }

        public CommonDialog create(List<SelectItemBean> list, int height, int width)
        {
            datas = list;
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            dialog = new CommonDialog(context);
            View layout = inflater.inflate(R.layout.commom_dialog_layout, null);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            ListView mlistView = (ListView) layout.findViewById(R.id.lv_ls_project);
            if (list.size() > 5)
            {
                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                ViewGroup.LayoutParams params = mlistView.getLayoutParams();
//                params.height = (int) (displayMetrics.heightPixels * 0.5);
                params.height = (int) ScreenUtils.dp2px(context, 45) * 6;
                mlistView.setLayoutParams(params);
            }
//            ViewGroup.LayoutParams params = mlistView.getLayoutParams();
//            params.height = height;
//            mlistView.setLayoutParams(params);

            TextView warehouse_reason = (TextView) layout.findViewById(R.id.warehouse_reason);//老板说仓库的出入库理由要加title
            warehouse_reason.setText(title);
            if (TextUtils.isEmpty(title))
            {
                warehouse_reason.setVisibility(View.GONE);
            } else
            {
                warehouse_reason.setVisibility(View.VISIBLE);
            }
//            View warehouse_reason_line =  layout_city.findViewById(R.id.warehouse_reason_line);//老板说仓库的出入库理由要加title
//            /**
//             * 无搜索栏隐藏线
//             */
//            warehouse_reason_line.setVisibility(View.GONE);
            send_bottom_tv = (TextView) layout.findViewById(R.id.send_bottom_tv);
            send_bottom_tv.setText(context.getResources().getString(R.string.choose_sex_sure));//确定
            delete_bottom_tv = (TextView) layout.findViewById(R.id.delete_bottom_tv);
            delete_bottom_tv.setText(context.getResources().getString(R.string.choose_sex_cancel));//取消
            RelativeLayout send_bottom_rl = (RelativeLayout) dialog.findViewById(R.id.send_bottom_rl);
            RelativeLayout delete_bottom_rl = (RelativeLayout) dialog.findViewById(R.id.delete_bottom_rl);
            send_bottom_rl.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    selectDialog();
                    if (dialog != null)
                    {
                        dialog.dismiss();
                    }
                }
            });
            delete_bottom_rl.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (dialog != null)
                    {
                        dialog.dismiss();
                    }
                }
            });

            setAdapter(mlistView, list);
            dialog.setContentView(layout);
            return dialog;
        }

        public void dismiss()
        {
            dialog.dismiss();
        }

        Map<Integer, Boolean> isChooseMap = new HashMap<>();//保存每一个item的状态

        private void setAdapter(ListView mlistView, final List<SelectItemBean> datas)
        {
            adapter = new CommonAdapter<SelectItemBean>(context, datas, R.layout.choose_item_layout)
            {
                @Override
                public void convert(ViewHolder helper, SelectItemBean item, final int position)
                {
                    helper.setText(R.id.customer_contact_tv, item.getSelectString());

                    if (selectBean != null)
                    {
                        if (position == selectBean.getSelectIndex())
                        {
                            ((RadioButton) helper.getView(R.id.RadioButton1)).setChecked(selectBean.isCheck());
                            datas.get(position).setCheck(true);
                        }
                    }

                    ((RadioButton) helper.getView(R.id.RadioButton1)).setChecked(datas.get(position).isCheck());
                    ((RadioButton) helper.getView(R.id.RadioButton1)).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            for (int i = 0; i < datas.size(); i++)
                            {
                                isChooseMap.put(i, false);
                                if (datas.get(position).isCheck())
                                {
                                    isChooseMap.put(position, false);
                                } else
                                {
                                    isChooseMap.put(position, true);
                                }
                            }
                            Iterator it = isChooseMap.entrySet().iterator();
                            while (it.hasNext())
                            {
                                Map.Entry entry = (Map.Entry) it.next();
                                Integer index = (Integer) entry.getKey();
                                Boolean isChoose = (Boolean) entry.getValue();
                                datas.get(index).setCheck(isChoose);
                            }

                            adapter.notifyDataSetChanged();
                            selectDialog();//说是要一点就选 , 临时加的 , 写个标记
                        }
                    });
                }
            };

            mlistView.setAdapter(adapter);
            mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    int newPosition = position;
                    for (int i = 0; i < datas.size(); i++)
                    {
                        isChooseMap.put(i, false);
                        if (datas.get(newPosition).isCheck())
                        {
                            isChooseMap.put(newPosition, false);
                        } else
                        {
                            isChooseMap.put(newPosition, true);
                        }
                    }
                    Iterator it = isChooseMap.entrySet().iterator();
                    while (it.hasNext())
                    {
                        Map.Entry entry = (Map.Entry) it.next();
                        Integer index = (Integer) entry.getKey();
                        Boolean isChoose = (Boolean) entry.getValue();
                        datas.get(index).setCheck(isChoose);
                    }

                    adapter.notifyDataSetChanged();
                    selectDialog();//说是要一点就选 , 临时加的 , 写个标记
                }
            });
        }

    }


    private static void selectDialog()
    {
        if (datas != null)
        {
            for (int i = 0; i < datas.size(); i++)
            {
                if (datas.get(i).isCheck())
                {
                    if (null != mCustomerTypeInterface)
                    {
                        mCustomerTypeInterface.menuItemClick(datas.get(i), i);
                        if (null != dialog)
                        {
                            dialog.dismiss();
                        }
                    }
                }
            }
        }
    }


    private static CustomerTypeInterface mCustomerTypeInterface;

    public static void setmCustomerTypeInterface(CustomerTypeInterface mCustomerTypeInterface)
    {
        CommonDialog.mCustomerTypeInterface = mCustomerTypeInterface;
    }

    public interface CustomerTypeInterface
    {
        void menuItemClick(SelectItemBean bean, int index);
    }
}