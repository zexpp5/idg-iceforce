package com.view_erp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.SDLogUtil;
import com.cxgz.activity.cx.workcircle.SDBigImagePagerActivity;
import com.entity.VoiceEntity;
import com.entity.administrative.employee.Annexdata;
import com.google.gson.reflect.TypeToken;
import com.chaoxiang.base.config.Constants;
import com.injoy.idg.R;
import com.utils.FileDownloadUtil;
import com.utils.FileUtil;
import com.utils.VoicePlayUtil;
import com.utils_erp.FileDealUtil;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class FileDialog extends Dialog {
    private static SDGson mGson;
    protected static int annexWay;

    public FileDialog(Context context) {
        super(context);
    }

    public FileDialog(Context context, int theme) {
        super(context, theme);
    }

    protected FileDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private static FileDialogInterface mFileDialogInterface;

    public FileDialogInterface getmFileDialogInterface() {
        return mFileDialogInterface;
    }

    public void setmFileDialogInterface(FileDialogInterface mFileDialogInterface) {
        this.mFileDialogInterface = mFileDialogInterface;
    }

    private static FileDialogCancerInterface mFileDialogCancerInterface;

    public static FileDialogCancerInterface getmFileDialogCancerInterface() {
        return mFileDialogCancerInterface;
    }

    public static void setmFileDialogCancerInterface(FileDialogCancerInterface mFileDialogCancerInterface) {
        FileDialog.mFileDialogCancerInterface = mFileDialogCancerInterface;
    }

    public static class Builder {
        private Context context;
        private View contentView;
        private String title;
        private int screenWidth, screenHeight;

        public Builder(Context context) {
            this.context = context;
            mGson = new SDGson();
            DisplayMetrics dm = new DisplayMetrics();
            dm = context.getApplicationContext().getResources().getDisplayMetrics();
            screenWidth = dm.widthPixels;
            screenHeight = dm.heightPixels;
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
        public FileDialog create(ArrayList<String> addImgPaths, VoiceEntity mVoiceEntity, ArrayList<File> selectedAttachData) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final FileDialog dialog = new FileDialog(context);
            View layout = inflater.inflate(R.layout.erp_file_select_dialog_layout, null);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            Window dialogWindow = dialog.getWindow();
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = (int) ((screenWidth / 2.5) * 2); // 宽度
            //lp.height = screenHeight /2; // 高度
            dialogWindow.setGravity(Gravity.CENTER);
            dialog.getWindow().setAttributes(lp);

            ImageView photoImg = (ImageView) layout.findViewById(R.id.add_photo_btn_detail_img);
            ImageView picImg = (ImageView) layout.findViewById(R.id.add_pic_btn_detail_img);
            ImageView voiceImg = (ImageView) layout.findViewById(R.id.add_void_btn_detail_img);
            ImageView fileImg = (ImageView) layout.findViewById(R.id.add_file_btn_detail_img);
            TextView cancerTv=(TextView)layout.findViewById(R.id.erp_dialog_cancer_font_tv);//取消
            cancerTv.setText(R.string.file_dialog_cancer_str);
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

            //取消
            ((RelativeLayout) layout.findViewById(R.id.erp_dialog_cancer_tv_str)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mFileDialogInterface) {
                        mFileDialogInterface.cancleDialog();
                    } else {
                        dialog.dismiss();
                    }
                }
            });

            //确定
            ((RelativeLayout) layout.findViewById(R.id.erp_dialog_sure_tv_str)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (null != mFileDialogInterface) {
//                        mFileDialogInterface.cancleDialog();
//                    } else {
//                        dialog.dismiss();
//                    }
                    dialog.dismiss();
                }
            });

            dialog.setContentView(layout);
            return dialog;
        }

        public FileDialog create(List<Annexdata> annexdata) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final FileDialog dialog = new FileDialog(context);
            View layout = inflater.inflate(R.layout.erp_file_select_dialog_layout, null);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            Window dialogWindow = dialog.getWindow();
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = (int) ((screenWidth / 2.5) * 2); // 宽度
            //lp.height = screenHeight /2; // 高度
            dialogWindow.setGravity(Gravity.CENTER);
            dialog.getWindow().setAttributes(lp);

            ImageView photoImg = (ImageView) layout.findViewById(R.id.add_photo_btn_detail_img);
            ImageView picImg = (ImageView) layout.findViewById(R.id.add_pic_btn_detail_img);
            ImageView voiceImg = (ImageView) layout.findViewById(R.id.add_void_btn_detail_img);
            ImageView fileImg = (ImageView) layout.findViewById(R.id.add_file_btn_detail_img);

            RelativeLayout sureRl=(RelativeLayout)layout.findViewById(R.id.erp_dialog_sure_tv_str);//确认
            sureRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dialog != null){
                        dialog.dismiss();
                    }
                }
            });
//            sureRl.setVisibility(View.GONE);
            //erp_dialog_cancer_font_tv
            TextView cancerTv=(TextView)layout.findViewById(R.id.erp_dialog_cancer_font_tv);//取消
//            cancerTv.setTextColor(context.getResources().getColor(R.color.top_bg));


            checkFileOrImgOrVoice(context, annexdata, picImg, voiceImg, fileImg);
            //取消
            ((RelativeLayout) layout.findViewById(R.id.erp_dialog_cancer_tv_str)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mFileDialogInterface) {
                        mFileDialogInterface.cancleDialog();
                    } else {
                        dialog.dismiss();
                    }
                }
            });
            dialog.setContentView(layout);
            return dialog;
        }

    }

    public interface FileDialogCancerInterface{
        void cancleDialog();
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

        //取消
        void cancleDialog();
    }

    /**
     * 图片缓存集合
     */
    private static Map<Long, String> cacheImage = new HashMap<>();
    /**
     * 语音缓存集合
     */
    private static Map<Long, String> cacheVoice = new HashMap<>();
    /**
     * 附件缓存集合
     */
    private static Map<Long, String> cacheAttach = new HashMap<>();
    private static Type type = new TypeToken<ArrayList<Annexdata>>() {
    }.getType();


    private static void checkFileOrImgOrVoice(final Context context, final List<Annexdata> annexdatas, ImageView img, ImageView voiceImg, ImageView fileImg) {
        fileImg.setImageResource(R.mipmap.fj_img_bg_gray);
        img.setImageResource(R.mipmap.pic_img_bg_gray);
        voiceImg.setImageResource(R.mipmap.voice_img_bg_gray);
        boolean isFile = false;
        boolean isImg = false;
        boolean isVoice = false;
        for (int i = 0; i < annexdatas.size(); i++) {
            String type = annexdatas.get(i).getType();
            if (!TextUtils.isEmpty(type)) {
                //图片
                if (type.equals(Constants.IMAGE_PREFIX_NEW_RETURN) || type.equals(Constants.IMAGE_PREFIX_NEW_01_RETURN) ||
                        type.equals(Constants.IMAGE_PREFIX_NEW_02_RETURN) || type.equals(Constants.IMAGE_PREFIX_NEW_03_RETURN)) {
                    isImg = true;
                } else if (type.equals("spx")) {
                    isVoice = true;
                } else {
                    isFile = true;
                }
            } else {
                isFile = true;
            }
        }
        //文件
        if (isFile) {
            fileImg.setImageResource(R.mipmap.fj_img_bg);
            fileImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFile(context, annexdatas, 3L, 3);
                }
            });
        } else {
            fileImg.setImageResource(R.mipmap.fj_img_bg_gray);
        }
        //图片
        if (isImg) {
            img.setImageResource(R.mipmap.pic_img_bg);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFile(context, annexdatas, 1L, 1);
                }
            });
        } else {
            img.setImageResource(R.mipmap.pic_img_bg_gray);
        }
        //语音
        if (isVoice) {
            voiceImg.setImageResource(R.mipmap.voice_img_bg);
            voiceImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFile(context, annexdatas, 2L, 2);
                }
            });
        } else {
            voiceImg.setImageResource(R.mipmap.voice_img_bg_gray);
        }
    }


    //1图片，2语音，3附件
    //显示附件
    private static void showFile(Context context, List<Annexdata> annexs, long identify, int flage) {
        List<Annexdata> imageFiles = null;
        List<Annexdata> voiceFiles = null;
        List<Annexdata> attachFiles = null;
        cacheImage.clear();
        cacheVoice.clear();
        cacheAttach.clear();
        if (cacheImage.containsKey(identify) || cacheVoice.containsKey(identify) || cacheAttach.containsKey(identify)) {
            if (cacheImage.containsKey(identify)) {
                imageFiles = mGson.fromJson(cacheImage.get(identify), type);
                //showImageFile(imageFiles);
                if (flage == 1) {
                    showImgFileReturn(context, imageFiles);
                }
            }
            if (cacheVoice.containsKey(identify)) {
                voiceFiles = mGson.fromJson(cacheVoice.get(identify), type);
                if (flage == 2) {
                    showVoicFileViewReturn(context, voiceFiles);
                }
            }
            if (cacheAttach.containsKey(identify)) {
                attachFiles = mGson.fromJson(cacheAttach.get(identify), type);
                //showAttachFile(attachFiles);
                if (flage == 3) {
                    showAttachFileReturn(context, attachFiles);
                }
            }
        } else {
            Object[] objs = getFileList(annexs);
            if (objs != null) {
                imageFiles = (List<Annexdata>) objs[0];
                voiceFiles = (List<Annexdata>) objs[1];
                attachFiles = (List<Annexdata>) objs[2];
                cacheImage.put(identify, mGson.toJson(imageFiles));
                cacheVoice.put(identify, mGson.toJson(voiceFiles));
                cacheAttach.put(identify, mGson.toJson(attachFiles));
                // showImageFile(imageFiles);
                if (flage == 1) {
                    showImgFileReturn(context, imageFiles);
                } else if (flage == 2) {
                    showVoicFileViewReturn(context, voiceFiles);
                } else if (flage == 3) {
                    showAttachFileReturn(context, attachFiles);
                }
            }
        }
    }

    //显示返回的图片
    private static void showImgFileReturn(Context context, final List<Annexdata> attachFiles) {
        if (attachFiles != null && !attachFiles.isEmpty()) {
            String[] bigImgUrl = new String[attachFiles.size()];
            String[] smallImgUrl = new String[attachFiles.size()];
            for (int i = 0; i < attachFiles.size(); i++) {
                bigImgUrl[i] = FileDownloadUtil.getDownloadIP(2, attachFiles.get(i).getPath());
                smallImgUrl[i] = FileDownloadUtil.getDownloadIP(2, attachFiles.get(i).getPath());
            }
            Intent intent = new Intent(context, SDBigImagePagerActivity.class);
            intent.putExtra(Constants.EXTRA_BIG_IMG_URIS, bigImgUrl);
            intent.putExtra(Constants.EXTRA_SMALL_IMG_URIS, smallImgUrl);
            intent.putExtra(SDBigImagePagerActivity.IS_NEED_DEL, false);
            intent.putExtra(Constants.EXTRA_IMAGE_INDEX, 0);
            context.startActivity(intent);
        }
    }


    private static void showVoicFileViewReturn(Context context, final List<Annexdata> voiceFile) {
        if (voiceFile != null && !voiceFile.isEmpty()) {
            VoicePlayUtil.getInstance().setData(context, voiceFile.get(0));

        }
    }

    private static void showAttachFileReturn(Context context, final List<Annexdata> attachFiles) {
        if (attachFiles != null && !attachFiles.isEmpty()) {
            FileUtil.startFileNewDialog(context, attachFiles);
        }
    }


    /**
     * 获取不同附件列表
     *
     * @param annexs
     * @return
     */
    protected static Object[] getFileList(List<Annexdata> annexs) {
        annexWay = 1;
        if (annexs != null && !annexs.isEmpty()) {
            List<Annexdata> imageFiles = new ArrayList<Annexdata>();
            List<Annexdata> voiceFiles = new ArrayList<Annexdata>();
            List<Annexdata> attachFiles = new ArrayList<Annexdata>();
            for (Annexdata fileListEntity : annexs) {
                if (annexWay == 1) {
                    String srcName = fileListEntity.getSrcName();
                    SDLogUtil.debug("srcName===" + srcName);
                    switch (FileUtil.getNewFileType(fileListEntity)) {
                        case 1:
                            //图片
                            SDLogUtil.syso("图片类型");
                            srcName = mySbtring(srcName);
                            Annexdata imgFile = new Annexdata();
                            imgFile.setFileSize(fileListEntity.getFileSize());
                            imgFile.setId(fileListEntity.getId());
                            imgFile.setPath(fileListEntity.getPath());
                            imgFile.setSrcName(srcName);
                            imgFile.setType(fileListEntity.getType());
                            imageFiles.add(imgFile);
                            break;
                        case 2:
                            //语音
                            SDLogUtil.syso("语音类型");
                            srcName = srcName.substring(0, srcName.indexOf(Constants.RADIO_PREFIX_NEW));
                            Annexdata voiceFile = new Annexdata();
                            voiceFile.setFileSize(fileListEntity.getFileSize());
                            voiceFile.setId(fileListEntity.getId());
                            voiceFile.setPath(fileListEntity.getPath());
                            voiceFile.setSrcName(srcName);
                            voiceFile.setType(fileListEntity.getType());
                            voiceFiles.add(voiceFile);
                            break;
                        case 3:
                            //附件
                            SDLogUtil.syso("附件类型");
                            attachFiles.add(fileListEntity);
                            break;
                    }
                } else if (annexWay == 2) {
                    String type = fileListEntity.getType();
                    int showType = fileListEntity.getShowType();
                    if (showType == 1) {
                        attachFiles.add(fileListEntity);
                    } else {
                        if (type.equals("spx")) {
                            voiceFiles.add(fileListEntity);
                        } else {
                            imageFiles.add(fileListEntity);
                        }
                    }
                }

            }
            Object[] objs = new Object[3];
            objs[0] = imageFiles;
            objs[1] = voiceFiles;
            objs[2] = attachFiles;
            return objs;
        } else {
            return null;
        }
    }

    private static String mySbtring(String srcName) {
        if (srcName.contains(Constants.IMAGE_PREFIX_NEW)) {
            return (srcName.substring(0, srcName.indexOf(Constants.IMAGE_PREFIX_NEW)));
        } else if (srcName.contains(Constants.IMAGE_PREFIX_NEW_01)) {
            return (srcName.substring(0, srcName.indexOf(Constants.IMAGE_PREFIX_NEW_01)));
        } else if (srcName.contains(Constants.IMAGE_PREFIX_NEW_02)) {
            return (srcName.substring(0, srcName.indexOf(Constants.IMAGE_PREFIX_NEW_02)));
        } else if (srcName.contains(Constants.IMAGE_PREFIX_NEW_03)) {
            return (srcName.substring(0, srcName.indexOf(Constants.IMAGE_PREFIX_NEW_03)));
        } else {
            return (srcName.substring(0, srcName.indexOf(Constants.RADIO_PREFIX_NEW)));
        }
    }
}
