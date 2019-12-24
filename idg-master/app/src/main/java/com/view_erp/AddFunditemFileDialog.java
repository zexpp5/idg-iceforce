package com.view_erp;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bean_erp.CustomerTypeBean;
import com.entity.VoiceEntity;
import com.injoy.idg.R;
import com.utils_erp.FileDealUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class AddFunditemFileDialog extends Dialog {
    public AddFunditemFileDialog(Context context) {
        super(context);
    }

    public AddFunditemFileDialog(Context context, int theme) {
        super(context, theme);
    }

    protected AddFunditemFileDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private static FileDialogInterface mFileDialogInterface;

    public FileDialogInterface getmFileDialogInterface() {
        return mFileDialogInterface;
    }

    public void setmFileDialogInterface(FileDialogInterface mFileDialogInterface) {
        this.mFileDialogInterface = mFileDialogInterface;
    }


    public static class Builder {
        private Context context;
        private View contentView;
        private String title;
        private CustomerTypeBean logisticBean = null;

        public Builder(Context context) {
            this.context = context;
        }


        public View getContentView() {
            return contentView;
        }

        public void setContentView(View contentView) {
            this.contentView = contentView;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        /**
         * 自定义弹出dialog
         *
         * @param addImgPaths        图片
         * @param mVoiceEntity       语音
         * @param selectedAttachData 文件
         * @return
         */
        public AddFunditemFileDialog create( ArrayList<String> addImgPaths, VoiceEntity mVoiceEntity, ArrayList<File> selectedAttachData) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final AddFunditemFileDialog dialog = new AddFunditemFileDialog(context);
            View layout = inflater.inflate(R.layout.erp_fund_add_items_dialog, null);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
         /*   final TextView name = (TextView) layout_city.findViewById(R.id.name);
            final EditText number = (EditText) layout_city.findViewById(R.id.number);*/
       /*     if (logisticBean != null) {
                name.setText(logisticBean.getTitle());
                number.setText(logisticBean.getNumber());
            }*/
            ImageView photoImg = (ImageView) layout.findViewById(R.id.add_photo_btn_detail_img);
            ImageView picImg = (ImageView) layout.findViewById(R.id.add_pic_btn_detail_img);
            ImageView voiceImg = (ImageView) layout.findViewById(R.id.add_void_btn_detail_img);
            ImageView fileImg = (ImageView) layout.findViewById(R.id.add_file_btn_detail_img);
            //判断
            FileDealUtil.checkFilePicVoiceIsNullOrNot(addImgPaths, mVoiceEntity, selectedAttachData, photoImg, picImg, voiceImg, fileImg);
            //拍照
            ((LinearLayout) layout.findViewById(R.id.add_photo_btn_detail_ll)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mFileDialogInterface) {
                        mFileDialogInterface.talkPhoto();
                    }
                }
            });

            //图片
            ((LinearLayout) layout.findViewById(R.id.add_pic_btn_detail_ll)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mFileDialogInterface) {
                        mFileDialogInterface.selectPicture();
                    }
                }
            });
            //语音
            ((LinearLayout) layout.findViewById(R.id.add_void_btn_detail_ll)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mFileDialogInterface) {
                        mFileDialogInterface.recordVoice();
                    }
                }
            });
            //附件
            ((LinearLayout) layout.findViewById(R.id.add_file_btn_detail_ll)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mFileDialogInterface) {
                        mFileDialogInterface.choiceFile();
                    }
                }
            });
         /*   //物流公司
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLogisticCompany(name);
                }
            });*/
            //确认
            ((RelativeLayout) layout.findViewById(R.id.send_bottom_rl)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 /*   if (TextUtils.isEmpty(name.getText().toString())) {
                        SDToast.showShort("请选择物流公司");
                        return;
                    }
                    if (TextUtils.isEmpty(number.getText().toString())) {
                        SDToast.showShort("请输入快递单号");
                        return;
                    }
                    logisticBean.setNumber(number.getText().toString());*/
                    mFileDialogInterface.submit();
                    dialog.dismiss();
                }
            });
            //清空
            ((RelativeLayout) layout.findViewById(R.id.delete_bottom_rl)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 /*   name.setText("");
                    number.setText("");*/
                    /*logisticBean = null;*/
                    /*mFileDialogInterface.clear();*/
                }
            });
            dialog.setContentView(layout);
            return dialog;
        }
//
//        protected void clearFileAndPicAndVoice()
//        {
//            addImgPaths.clear();
//            selectedAttachData.clear();
//            mVoiceEntity = null;
//            if ((null != add_pic_btn_detail_img) && (null != add_pic_btn_detail_img) && (null != add_file_btn_detail_img))
//            {
//                FileDealUtil.checkFilePicVoiceIsNullOrNot(addImgPaths, mVoiceEntity, selectedAttachData, add_photo_btn_detail_img, add_pic_btn_detail_img, add_void_btn_detail_img, add_file_btn_detail_img);
//            }
//        }

      /*  //客户的类型
        private void toCompayDatas() {
            List<EPRBaseStaticBean.DataBean> beans = ERPBaseStaticUtil.getListStaticValues(context, "logistics_type");

            compayList.clear();
            for (int i = 0; i < beans.size(); i++) {
                compayList.add(new CustomerTypeBean(beans.get(i).getName(), i, beans.get(i).getValue()));
            }
        }*/

        List<CustomerTypeBean> compayList = new ArrayList<>();

        /*private void showLogisticCompany(final TextView name) {
            toCompayDatas();
            CustomerUtil.showCustomerTypeUtil(context, "", compayList, new CustomerTypeDialog.CustomerTypeInterface() {
                @Override
                public void menuItemClick(CustomerTypeBean bean) {
                    logisticBean = bean;
                    name.setText(bean.getTitle());
                }
            });
        }*/
    }


    public interface FileDialogInterface {
        //拍照
        void talkPhoto();

        //选择图片
        void selectPicture();

        //录音
        void recordVoice();

        //选择文件
        void choiceFile();

        void submit();

        void clear();
    }


}
