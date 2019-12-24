package com.entity.gztutils;

/**
 * Created by zy on 2016/11/29.
 */

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.injoy.idg.R;
import com.utils.SPUtils;

import java.io.Serializable;


public class ModuleTipsDialog extends Dialog {

    private TextView help_dialog_content, help_dialog_tel;
    private ImageView help_dialog_icon;
    private RelativeLayout help_dialog_tel_ll, erp_dialog_sure_tv_str, erp_dialog_cancer_tv_str;
    private int screenWidth, screenHeight;
    private ModuleTipsBean bean;
    private int type;
    private Context context;

    public ModuleTipsDialog(final Context context, int type) {
        super(context, R.style.PopProgressDialog);
        this.context = context;
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.erp_module_tips_dialog_layout);
        setCancelable(false);

        String typeJson = SPUtils.get(context, SPUtils.MODULE_TIPS_TYPE, "") + "";
        if (!TextUtils.isEmpty(typeJson)) {
            bean = new Gson().fromJson(typeJson, ModuleTipsBean.class);
        } else {
            bean = new ModuleTipsBean();
        }

        erp_dialog_sure_tv_str = (RelativeLayout) findViewById(R.id.erp_dialog_sure_tv_str);
        erp_dialog_sure_tv_str.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        switch (type) {
            case 1:
                if (TextUtils.isEmpty(bean.getType1())) {
                    bean.setType1("1");
                    goShow();
                }
                break;
            case 2:
                if (TextUtils.isEmpty(bean.getType2())) {
                    bean.setType2("2");
                    goShow();
                }
                break;
            case 3:
                if (TextUtils.isEmpty(bean.getType3())) {
                    bean.setType3("3");
                    goShow();
                }
                break;
            case 4:
                if (TextUtils.isEmpty(bean.getType4())) {
                    bean.setType4("4");
                    goShow();
                }
                break;
            case 5:
                if (TextUtils.isEmpty(bean.getType5())) {
                    bean.setType5("5");
                    goShow();
                }
                break;
            case 6:
                if (TextUtils.isEmpty(bean.getType6())) {
                    bean.setType6("6");
                    goShow();
                }
                break;
            case 7:
                if (TextUtils.isEmpty(bean.getType7())) {
                    bean.setType7("7");
                    goShow();
                }
                break;
            case 8:
                if (TextUtils.isEmpty(bean.getType8())) {
                    bean.setType8("8");
                    goShow();
                }
                break;
            case 9:
                if (TextUtils.isEmpty(bean.getType9())) {
                    bean.setType9("9");
                    goShow();
                }
                break;
        }
    }

    private void goShow() {
        SPUtils.put(context, SPUtils.MODULE_TIPS_TYPE, new Gson().toJson(bean) + "");
        show();

        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.width = (int) ((screenWidth / 2.5) * 2); // 宽度
        //lp.height = screenHeight /2; // 高度
        dialogWindow.setGravity(Gravity.CENTER);
        this.getWindow().setAttributes(lp);
    }

    class ModuleTipsBean implements Serializable {
        private String type1;
        private String type2;
        private String type3;
        private String type4;
        private String type5;
        private String type6;
        private String type7;
        private String type8;
        private String type9;

        public String getType1() {
            return type1;
        }

        public void setType1(String type1) {
            this.type1 = type1;
        }

        public String getType2() {
            return type2;
        }

        public void setType2(String type2) {
            this.type2 = type2;
        }

        public String getType3() {
            return type3;
        }

        public void setType3(String type3) {
            this.type3 = type3;
        }

        public String getType4() {
            return type4;
        }

        public void setType4(String type4) {
            this.type4 = type4;
        }

        public String getType5() {
            return type5;
        }

        public void setType5(String type5) {
            this.type5 = type5;
        }

        public String getType6() {
            return type6;
        }

        public void setType6(String type6) {
            this.type6 = type6;
        }

        public String getType7() {
            return type7;
        }

        public void setType7(String type7) {
            this.type7 = type7;
        }

        public String getType8() {
            return type8;
        }

        public void setType8(String type8) {
            this.type8 = type8;
        }

        public String getType9() {
            return type9;
        }

        public void setType9(String type9) {
            this.type9 = type9;
        }
    }
}

