package com.utils_erp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.base_erp.ERPAddGoodsSumbitBaseFragment;
import com.base_erp.ERPSumbitBaseFragment;
import com.bean_erp.CustomerTypeBean;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.SDLogUtil;
import com.cxgz.activity.cx.view.MultiImageSelectorActivity;
import com.cxgz.activity.cx.workcircle.SDBigImagePagerActivity;
import com.cxgz.activity.cxim.manager.AudioPlayManager;
import com.entity.VoiceEntity;
import com.entity.administrative.employee.Annexdata;
import com.google.gson.reflect.TypeToken;
import com.chaoxiang.base.config.Constants;
import com.injoy.idg.R;
import com.ui.activity.VoiceVideoActivity;
import com.utils.CachePath;
import com.utils.FileDownloadUtil;
import com.utils.FileUtil;
import com.utils.StringUtil;
import com.utils.VoicePlayUtil;
import com.view_erp.AddGoodsFileDialog;
import com.view_erp.FileDialog;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import paul.arian.fileselector.FileSelectionActivity;
import tablayout.view.dialog.SelectImgDialog;
import yunjing.bean.CapitalAnnexBean;

/**
 * 附件处理类
 */
public class FileDealUtil implements AudioPlayManager.OnVoiceListener
{


    private static SDGson mGson = new SDGson();
    protected static int annexWay;

    private static FileDealUtil instance = null;

    private static SelectImgDialog selectImgDialog;

    private static FileDialog mFileDialog;

    private static AddGoodsFileDialog mGoodsFileDialog;

    private static AddGoodsFileDialog mAddGoodsFileDialog;

    /**
     * 最多选取多少张图片
     */
    private static final int maxSelectedImgNum = 9;


    /**
     * 相机拍照保存图片目录
     */
    public static File imgFolder = new File(FileUtil.getSDFolder(), CachePath.CAMERA_IMG_PATH);


    protected static void showSelectImgDialog(Context context, final Fragment fragment, final ArrayList<String> addImgPaths)
    {
        if (selectImgDialog == null)
        {
            selectImgDialog = new SelectImgDialog(context,
                    new String[]{StringUtil.getResourceString(context, R.string.camera), StringUtil.getResourceString(context, R.string.album)});
            selectImgDialog.setOnSelectImgListener(new SelectImgDialog.OnSelectImgListener()
            {
                @Override
                public void onClickCanera(View v)
                {
                    takePhoto(null, fragment, addImgPaths, Constants.OPEN_SYSTEM_CAMERA_REQUEST_CODE);

                }

                @Override
                public void onClickCancel(View v)
                {
                    //TODO
                }

                @Override
                public void onClickAlum(View v)
                {
                    // FileDealUtil.selectImgFromDir(null, getRootFragment(), addImgPaths, Constants.OPEN_SYSTEM_ABLUM_REQUEST_CODE);
                }
            });
        }
        selectImgDialog.show();
    }

    public static void showFileDialog(final Context context, final Fragment fragment, final ArrayList<String> addImgPaths, final VoiceEntity mVoiceEntity, final ArrayList<File> selectedAttachData, final File cameraImgPath)
    {
        if (mFileDialog != null)
        {
            mFileDialog.dismiss();
            mFileDialog = null;
        }
        mFileDialog = new FileDialog.Builder(context).create(addImgPaths, mVoiceEntity, selectedAttachData);

        mFileDialog.show();
        mFileDialog.setmFileDialogInterface(new FileDialog.FileDialogInterface()
        {
            @Override
            public void talkPhoto()
            {
//                FileDealUtil.takePhoto(null, fragment, addImgPaths, Constants.OPEN_SYSTEM_CAMERA_REQUEST_CODE, cameraImgPath);
                ERPSumbitBaseFragment.cameraImgPath = FileDealUtil.takePhoto(null, fragment, addImgPaths, Constants.OPEN_SYSTEM_CAMERA_REQUEST_CODE);
            }

            @Override
            public void selectPicture()
            {
                FileDealUtil.selectImgFromDir(null, fragment, addImgPaths, Constants.OPEN_SYSTEM_ABLUM_REQUEST_CODE);
            }

            @Override
            public void recordVoice()
            {
                if (null != mVoiceEntity)
                {
                    showVoiceOnClick(mVoiceEntity, context, fragment);
                } else
                {
                    FileDealUtil.recordVoice(null, fragment, Constants.OPEN_VOICE_REQUEST_CODE);
                }
            }

            @Override
            public void choiceFile()
            {
                FileDealUtil.selectFile(null, fragment, selectedAttachData, Constants.OPEN_FILE_PICKER_REQUEST_CODE);
            }

            @Override
            public void cancleDialog()
            {
                mFileDialog.dismiss();
            }
        });
    }


    public static void showFileDialogAndCancer(final Context context, final Fragment fragment, final ArrayList<String> addImgPaths,
                                               final VoiceEntity mVoiceEntity, final ArrayList<File> selectedAttachData,
                                               final FileDialog.FileDialogCancerInterface mFileDialogCancerInterface)
    {
        if (mFileDialog != null)
        {
            mFileDialogCancerInterface.cancleDialog();
            mFileDialog.dismiss();
            mFileDialog = null;
        }
        mFileDialog = new FileDialog.Builder(context).create(addImgPaths, mVoiceEntity, selectedAttachData);

        mFileDialog.show();
        mFileDialog.setmFileDialogInterface(new FileDialog.FileDialogInterface()
        {
            @Override
            public void talkPhoto()
            {
//                FileDealUtil.takePhoto(null, fragment, addImgPaths, Constants.OPEN_SYSTEM_CAMERA_REQUEST_CODE, cameraImgPath);
                ERPSumbitBaseFragment.cameraImgPath = FileDealUtil.takePhoto(null, fragment, addImgPaths, Constants.OPEN_SYSTEM_CAMERA_REQUEST_CODE);
            }

            @Override
            public void selectPicture()
            {
                FileDealUtil.selectImgFromDir(null, fragment, addImgPaths, Constants.OPEN_SYSTEM_ABLUM_REQUEST_CODE);
            }

            @Override
            public void recordVoice()
            {
                if (null != mVoiceEntity)
                {
                    showVoiceOnClick(mVoiceEntity, context, fragment);
                } else
                {
                    FileDealUtil.recordVoice(null, fragment, Constants.OPEN_VOICE_REQUEST_CODE);
                }
            }

            @Override
            public void choiceFile()
            {
                FileDealUtil.selectFile(null, fragment, selectedAttachData, Constants.OPEN_FILE_PICKER_REQUEST_CODE);
            }

            @Override
            public void cancleDialog()
            {
                if (null != mFileDialogCancerInterface)
                {
                    mFileDialogCancerInterface.cancleDialog();
                    mFileDialog.dismiss();
                }

            }
        });
    }


    public static FileDialog showFileDialogAndCancerNew(final Context context, final Fragment fragment, final ArrayList<String> addImgPaths,
                                                        final VoiceEntity mVoiceEntity, final ArrayList<File> selectedAttachData, final File cameraImgPath,
                                                        final FileDialog.FileDialogCancerInterface mFileDialogCancerInterface, FileDialog.FileDialogInterface mFileDialogInterface)
    {
        if (mFileDialog != null)
        {
            mFileDialog.dismiss();
            mFileDialog = null;
        }
        mFileDialog = new FileDialog.Builder(context).create(addImgPaths, mVoiceEntity, selectedAttachData);

        mFileDialog.show();
        return mFileDialog;
//        mFileDialog.setmFileDialogInterface(new FileDialog.FileDialogInterface() {
//            @Override
//            public void talkPhoto() {
//                FileDealUtil.takePhoto(null, fragment, addImgPaths, Constants.OPEN_SYSTEM_CAMERA_REQUEST_CODE, cameraImgPath);
//            }
//
//            @Override
//            public void selectPicture() {
//                FileDealUtil.selectImgFromDir(null, fragment, addImgPaths, Constants.OPEN_SYSTEM_ABLUM_REQUEST_CODE);
//            }
//
//            @Override
//            public void recordVoice() {
//                if(null!=mVoiceEntity){
//                    showVoiceOnClick(mVoiceEntity,context,fragment);
//                }else {
//                    FileDealUtil.recordVoice(null, fragment, Constants.OPEN_VOICE_REQUEST_CODE);
//                }
//            }
//
//            @Override
//            public void choiceFile() {
//                FileDealUtil.selectFile(null, fragment, selectedAttachData, Constants.OPEN_FILE_PICKER_REQUEST_CODE);
//            }
//
//            @Override
//            public void cancleDialog() {
//                if(null!=mFileDialogCancerInterface){
//                    mFileDialogCancerInterface.cancleDialog();
//                    mFileDialog.dismiss();
//                }
//
//            }
//        });
    }


    //语音
    public static void showVoiceOnClick(VoiceEntity mVoiceEntity, Context context, Fragment fragment)
    {

        AudioPlayManager audioPlayManager = AudioPlayManager.getInstance();
        audioPlayManager.setOnVoiceListener(new AudioPlayManager.OnVoiceListener()
        {
            @Override
            public void playPositionChange(int currentPosition)
            {

            }

            @Override
            public void playCompletion()
            {

            }

            @Override
            public void playDuration(int duration)
            {

            }

            @Override
            public void playException(Exception e)
            {

            }

            @Override
            public void playStopVioce()
            {

            }
        });
        if (null != context)
        {
            AudioPlayManager.getInstance().play(context, mVoiceEntity.getFilePath(), null);
        } else
        {
            AudioPlayManager.getInstance().play(fragment.getActivity(), mVoiceEntity.getFilePath(), null);
        }

    }


    public static void showFileDialog(final Context context, final Fragment fragment, final ArrayList<String> addImgPaths, final VoiceEntity mVoiceEntity, final ArrayList<File> selectedAttachData)
    {
        if (mFileDialog != null)
        {
            mFileDialog.dismiss();
            mFileDialog = null;
        }
        mFileDialog = new FileDialog.Builder(context).create(addImgPaths, mVoiceEntity, selectedAttachData);

        mFileDialog.show();

        mFileDialog.setmFileDialogInterface(new FileDialog.FileDialogInterface()
        {
            @Override
            public void talkPhoto()
            {
                FileDealUtil.takePhoto(null, fragment, addImgPaths, Constants.OPEN_SYSTEM_CAMERA_REQUEST_CODE);
            }

            @Override
            public void selectPicture()
            {
                FileDealUtil.selectImgFromDir(null, fragment, addImgPaths, Constants.OPEN_SYSTEM_ABLUM_REQUEST_CODE);
            }

            @Override
            public void recordVoice()
            {
                //FileDealUtil.recordVoice(null, fragment, Constants.OPEN_VOICE_REQUEST_CODE);
                if (null != mVoiceEntity)
                {
                    showVoiceOnClick(mVoiceEntity, context, fragment);
                } else
                {
                    FileDealUtil.recordVoice(null, fragment, Constants.OPEN_VOICE_REQUEST_CODE);
                }
            }

            @Override
            public void choiceFile()
            {
                FileDealUtil.selectFile(null, fragment, selectedAttachData, Constants.OPEN_FILE_PICKER_REQUEST_CODE);
            }

            @Override
            public void cancleDialog()
            {
                mFileDialog.dismiss();
            }
        });
    }

    //新增商品
    public static void showAddGoodsFileDialog(Context context, final GetLogisticsInterface getLogisticsInterface, CustomerTypeBean bean, final Fragment fragment, final ArrayList<String> addImgPaths, final VoiceEntity mVoiceEntity, final ArrayList<File> selectedAttachData, final File cameraImgPath)
    {
        if (mAddGoodsFileDialog != null)
        {
            mAddGoodsFileDialog.dismiss();
            mAddGoodsFileDialog = null;
        }
        mAddGoodsFileDialog = new AddGoodsFileDialog.Builder(context).create(bean, addImgPaths, mVoiceEntity, selectedAttachData);

        mAddGoodsFileDialog.show();

        mAddGoodsFileDialog.setmFileDialogInterface(new AddGoodsFileDialog.FileDialogInterface()
        {
            @Override
            public void talkPhoto()
            {
                ERPAddGoodsSumbitBaseFragment.cameraImgPath = FileDealUtil.takePhoto(null, fragment, addImgPaths, Constants.ADD_GOODS_OPEN_SYSTEM_CAMERA_REQUEST_CODE);
            }

            @Override
            public void selectPicture()
            {
                FileDealUtil.selectImgFromDir(null, fragment, addImgPaths, Constants.ADD_GOODS_OPEN_SYSTEM_ABLUM_REQUEST_CODE);
            }

            @Override
            public void recordVoice()
            {
                FileDealUtil.recordVoice(null, fragment, Constants.ADD_GOODS_OPEN_VOICE_REQUEST_CODE);
            }

            @Override
            public void choiceFile()
            {
                FileDealUtil.selectFile(null, fragment, selectedAttachData, Constants.ADD_GOODS_OPEN_FILE_PICKER_REQUEST_CODE);
            }

            @Override
            public void submit(CustomerTypeBean bean)
            {
                getLogisticsInterface.submit(bean);
            }

            @Override
            public void clear()
            {
                getLogisticsInterface.clear();
            }
        });
        mAddGoodsFileDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
        {

            @Override
            public void onDismiss(DialogInterface dialog)
            {
                getLogisticsInterface.disms();
            }
        });
    }

    //新增商品
    public static void showAddGoodsFileDialog(Context context, final GetLogisticsInterface getLogisticsInterface, CustomerTypeBean bean, final Fragment fragment, final ArrayList<String> addImgPaths, final VoiceEntity mVoiceEntity, final ArrayList<File> selectedAttachData)
    {
        if (mAddGoodsFileDialog != null)
        {
            mAddGoodsFileDialog.dismiss();
            mAddGoodsFileDialog = null;
        }
        mAddGoodsFileDialog = new AddGoodsFileDialog.Builder(context).create(bean, addImgPaths, mVoiceEntity, selectedAttachData);

        mAddGoodsFileDialog.show();

        mAddGoodsFileDialog.setmFileDialogInterface(new AddGoodsFileDialog.FileDialogInterface()
        {
            @Override
            public void talkPhoto()
            {

            }

            @Override
            public void selectPicture()
            {
                FileDealUtil.selectImgFromDir(null, fragment, addImgPaths, Constants.ADD_GOODS_OPEN_SYSTEM_ABLUM_REQUEST_CODE);
            }

            @Override
            public void recordVoice()
            {
                FileDealUtil.recordVoice(null, fragment, Constants.ADD_GOODS_OPEN_VOICE_REQUEST_CODE);
            }

            @Override
            public void choiceFile()
            {
                FileDealUtil.selectFile(null, fragment, selectedAttachData, Constants.ADD_GOODS_OPEN_FILE_PICKER_REQUEST_CODE);
            }

            @Override
            public void submit(CustomerTypeBean bean)
            {
                getLogisticsInterface.submit(bean);
            }

            @Override
            public void clear()
            {
                getLogisticsInterface.clear();
            }
        });
    }

    @Override
    public void playPositionChange(int currentPosition)
    {

    }

    @Override
    public void playCompletion()
    {

    }

    @Override
    public void playDuration(int duration)
    {

    }

    @Override
    public void playException(Exception e)
    {

    }

    @Override
    public void playStopVioce()
    {

    }

    public interface GetLogisticsInterface
    {
        void submit(CustomerTypeBean bean);

        void clear();

        void disms();
    }

    //验证是否有值
    public static void checkFilePicVoiceIsNullOrNot(ArrayList<String> addImgPaths, VoiceEntity mVoiceEntity, ArrayList<File> selectedAttachData,
                                                    ImageView photoImg, ImageView picImg, ImageView voiceImg, ImageView fileImg)
    {
//        if ((mVoiceEntity == null) && (selectedAttachData.size() == 0) && (addImgPaths.size() == 0)) {
//            return;
//        }
        //首先是图片
        if (addImgPaths != null)
        {
            if (addImgPaths.size() == 0)
            {//图片
                picImg.setImageResource(R.mipmap.pic_img_bg_gray);
            } else
            {
                picImg.setImageResource(R.mipmap.pic_img_bg);
            }
        }
        //首先是语音
        if (mVoiceEntity == null)
        {
            voiceImg.setImageResource(R.mipmap.voice_img_bg_gray);
        } else
        {
            voiceImg.setImageResource(R.mipmap.voice_img_bg);
        }
        //文件
        if (selectedAttachData != null)
        {
            if (selectedAttachData.size() == 0)
            {//
                fileImg.setImageResource(R.mipmap.fj_img_bg_gray);
            } else
            {
                fileImg.setImageResource(R.mipmap.fj_img_bg);
            }
        }





    }

    //验证是否有值

    /**
     * 云镜
     *带资金附件
     */
    public static void checkFilePicVoiceIsNullOrNot
    (ArrayList<String> addImgPaths, VoiceEntity mVoiceEntity,
     ArrayList<File> selectedAttachData, ImageView photoImg,
     ImageView picImg, ImageView voiceImg, ImageView fileImg ,
     List<CapitalAnnexBean.CapitalAnnexDataBean> mCapitalLists, ImageView capital)
    {
//        if ((mVoiceEntity == null) && (selectedAttachData.size() == 0) && (addImgPaths.size() == 0)) {
//            return;
//        }
        //首先是图片
        if (addImgPaths != null)
        {
            if (addImgPaths.size() == 0)
            {//图片
                picImg.setImageResource(R.mipmap.pic_img_bg_gray);
            } else
            {
                picImg.setImageResource(R.mipmap.pic_img_bg);
            }
        }
        //首先是语音
        if (mVoiceEntity == null)
        {
            voiceImg.setImageResource(R.mipmap.voice_img_bg_gray);
        } else
        {
            voiceImg.setImageResource(R.mipmap.voice_img_bg);
        }
        //文件
        if (selectedAttachData != null)
        {
            if (selectedAttachData.size() == 0)
            {//
                fileImg.setImageResource(R.mipmap.fj_img_bg_gray);
            } else
            {
                fileImg.setImageResource(R.mipmap.fj_img_bg);
            }
        }

        //资金
        if (mCapitalLists != null)
        {
            if (mCapitalLists.size() == 0)
            {//
                capital.setImageResource(R.mipmap.money_img_bg_gray);
            } else
            {
                capital.setImageResource(R.mipmap.money_img_bg);
            }
        }

    }

    /**
     * //拍照类一
     *
     * @param activity    当前activity
     * @param fragment    当前fragment
     * @param addImgPaths 拍照的存储的路径
     * @param code        -- Constants.OPEN_SYSTEM_CAMERA_REQUEST_CODE 默认
     */
    public static File takePhoto(Activity activity, Fragment fragment, ArrayList<String> addImgPaths, int code)
    {
        File cameraImgPath = null;
        if (FileUtil.isSdcardExist())
        {
            if (null != addImgPaths)
            {
                if (addImgPaths.size() < 9)
                {
                    cameraImgPath = new File(imgFolder, "sd_img_" + System.currentTimeMillis() + ".jpg");
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraImgPath));
                    if (null != activity)
                    {
                        activity.startActivityForResult(cameraIntent, code);
                    } else
                    {
                        fragment.startActivityForResult(cameraIntent, code);
                    }
                }
            }
        }
        return cameraImgPath;
    }

    /**
     * //拍照类二
     *
     * @param activity    当前activity
     * @param fragment    当前fragment
     * @param addImgPaths 拍照的存储的路径
     * @param code        -- Constants.OPEN_SYSTEM_CAMERA_REQUEST_CODE 默认
     */
    public static void takePhoto(Activity activity, Fragment fragment, ArrayList<String> addImgPaths, int code, File cameraImgPath)
    {
        cameraImgPath = null;
        if (FileUtil.isSdcardExist())
        {
            if (null != addImgPaths)
            {
                if (addImgPaths.size() < 9)
                {
                    cameraImgPath = new File(imgFolder, "sd_img_" + System.currentTimeMillis() + ".jpg");
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraImgPath));
                    if (null != activity)
                    {
                        activity.startActivityForResult(cameraIntent, code);
                    } else
                    {
                        fragment.startActivityForResult(cameraIntent, code);
                    }
                }
            }
        }

    }

    /**
     * 选择图片
     *
     * @param activity
     * @param fragment
     * @param addImgPaths
     * @param code
     */
    public static void selectImgFromDir(Activity activity, Fragment fragment, ArrayList<String> addImgPaths, int code)
    {
        if (null != addImgPaths)
        {
            if (addImgPaths.size() <= 9)
            {
                Intent pictureIntent = null;
                if (null != activity)
                {
                    pictureIntent = new Intent(activity, MultiImageSelectorActivity.class);
                } else
                {
                    pictureIntent = new Intent(fragment.getActivity(), MultiImageSelectorActivity.class);
                }
                // 是否显示拍摄图片
                pictureIntent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
                // 最大可选择图片数量
                pictureIntent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxSelectedImgNum);
                pictureIntent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, addImgPaths);
                if (null != activity)
                {
                    activity.startActivityForResult(pictureIntent, code);
                } else
                {
                    fragment.startActivityForResult(pictureIntent, code);
                }
            }
        }
    }

    /**
     * 录音
     *
     * @param activity
     * @param fragment
     * @param code     Constants.OPEN_VOICE_REQUEST_CODE
     */
    public static void recordVoice(Activity activity, Fragment fragment, int code)
    {
        //语音
        Intent intent = null;
        if (null != activity)
        {
            intent = new Intent(activity, VoiceVideoActivity.class);
            activity.startActivityForResult(intent, code);

        } else
        {
            intent = new Intent(fragment.getActivity(), VoiceVideoActivity.class);
            fragment.startActivityForResult(intent, code);

        }
    }

    /**
     * @param activity
     * @param fragment
     * @param selectedAttachData
     * @param code
     */
    public static void selectFile(Activity activity, Fragment fragment, ArrayList<File> selectedAttachData, int code)
    {   
        Intent intent = null;
        if (null != activity)
        {
            intent = new Intent(activity, FileSelectionActivity.class);
            intent.putExtra("list", (Serializable) selectedAttachData);
            activity.startActivityForResult(intent, code);

        } else
        {
            intent = new Intent(fragment.getActivity(), FileSelectionActivity.class);
            intent.putExtra("list", (Serializable) selectedAttachData);
            fragment.startActivityForResult(intent, code);

        }
    }


    /**
     * 詳情顯示附件的
     *
     * @param context
     * @param annexdata
     */
    public static void showDetailFileDialog(Context context, List<Annexdata> annexdata)
    {
        if (mFileDialog != null)
        {
            mFileDialog.dismiss();
            mFileDialog = null;
        }
        mFileDialog = new FileDialog.Builder(context).create(annexdata);
        mFileDialog.show();
    }

    /**
     * 商品详情显示附件
     *
     * @param context
     * @param annexdata
     */
    public static void showGoodsDetailFileDialog(Context context, CustomerTypeBean bean, List<Annexdata> annexdata)
    {
        if (mGoodsFileDialog != null)
        {
            mGoodsFileDialog.dismiss();
            mGoodsFileDialog = null;
        }
        mGoodsFileDialog = new AddGoodsFileDialog.Builder(context).create(bean, annexdata);
        mGoodsFileDialog.show();
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
    private static Type type = new TypeToken<ArrayList<Annexdata>>()
    {
    }.getType();

    public static void checkFileOrImgOrVoice(final Context context, final List<Annexdata> annexdatas, ImageView img, ImageView voiceImg, ImageView fileImg)
    {
        fileImg.setImageResource(R.mipmap.fj_img_bg_gray);
        img.setImageResource(R.mipmap.pic_img_bg_gray);
        voiceImg.setImageResource(R.mipmap.voice_img_bg_gray);
        boolean isFile = false;
        boolean isImg = false;
        boolean isVoice = false;
        for (int i = 0; i < annexdatas.size(); i++)
        {
            String type = annexdatas.get(i).getType();
            if (!TextUtils.isEmpty(type))
            {
                //图片
                if (type.equals(Constants.IMAGE_PREFIX_NEW_RETURN) || type.equals(Constants.IMAGE_PREFIX_NEW_01_RETURN) ||
                        type.equals(Constants.IMAGE_PREFIX_NEW_02_RETURN) || type.equals(Constants.IMAGE_PREFIX_NEW_03_RETURN))
                {
                    isImg = true;
                } else if (type.equals("spx"))
                {
                    isVoice = true;
                } else
                {
                    isFile = true;
                }
            } else
            {
                isFile = true;
            }
        }

        if (isFile)
        {
            fileImg.setImageResource(R.mipmap.fj_img_bg);
            fileImg.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    showFile(context, annexdatas, 3L, 3);
                }
            });
        } else
        {
            fileImg.setImageResource(R.mipmap.fj_img_bg_gray);
        }
        if (isImg)
        {
            img.setImageResource(R.mipmap.pic_img_bg);
            img.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    showFile(context, annexdatas, 1L, 1);
                }
            });
        } else
        {
            img.setImageResource(R.mipmap.pic_img_bg_gray);
        }

        if (isVoice)
        {
            voiceImg.setImageResource(R.mipmap.voice_img_bg);
            voiceImg.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    showFile(context, annexdatas, 2L, 2);
                }
            });
        } else
        {
            voiceImg.setImageResource(R.mipmap.voice_img_bg_gray);
        }


    }


    //1图片，2语音，3附件
    //显示附件
    private static void showFile(Context context, List<Annexdata> annexs, long identify, int flage)
    {
        List<Annexdata> imageFiles = null;
        List<Annexdata> voiceFiles = null;
        List<Annexdata> attachFiles = null;
        cacheImage.clear();
        cacheVoice.clear();
        cacheAttach.clear();
        if (cacheImage.containsKey(identify) || cacheVoice.containsKey(identify) || cacheAttach.containsKey(identify))
        {
            if (cacheImage.containsKey(identify))
            {
                imageFiles = mGson.fromJson(cacheImage.get(identify), type);
                //showImageFile(imageFiles);
                if (flage == 1)
                {
                    showImgFileReturn(context, imageFiles);
                }
            }
            if (cacheVoice.containsKey(identify))
            {
                voiceFiles = mGson.fromJson(cacheVoice.get(identify), type);
                if (flage == 2)
                {
                    showVoicFileViewReturn(context, voiceFiles);
                }
            }
            if (cacheAttach.containsKey(identify))
            {
                attachFiles = mGson.fromJson(cacheAttach.get(identify), type);
                //showAttachFile(attachFiles);
                if (flage == 3)
                {
                    showAttachFileReturn(context, attachFiles);
                }
            }
        } else
        {
            Object[] objs = getFileList(annexs);
            if (objs != null)
            {
                imageFiles = (List<Annexdata>) objs[0];
                voiceFiles = (List<Annexdata>) objs[1];
                attachFiles = (List<Annexdata>) objs[2];
                cacheImage.put(identify, mGson.toJson(imageFiles));
                cacheVoice.put(identify, mGson.toJson(voiceFiles));
                cacheAttach.put(identify, mGson.toJson(attachFiles));
                // showImageFile(imageFiles);
                if (flage == 1)
                {
                    showImgFileReturn(context, imageFiles);
                } else if (flage == 2)
                {
                    showVoicFileViewReturn(context, voiceFiles);
                } else if (flage == 3)
                {
                    showAttachFileReturn(context, attachFiles);
                }
            }
        }
    }

    //显示返回的图片
    private static void showImgFileReturn(Context context, final List<Annexdata> attachFiles)
    {
        if (attachFiles != null && !attachFiles.isEmpty())
        {
            String[] bigImgUrl = new String[attachFiles.size()];
            String[] smallImgUrl = new String[attachFiles.size()];
            for (int i = 0; i < attachFiles.size(); i++)
            {
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


    private static void showVoicFileViewReturn(Context context, final List<Annexdata> voiceFile)
    {
        if (voiceFile != null && !voiceFile.isEmpty())
        {
            VoicePlayUtil.getInstance().setData(context, voiceFile.get(0));

        }
    }

    private static void showAttachFileReturn(Context context, final List<Annexdata> attachFiles)
    {
        if (attachFiles != null && !attachFiles.isEmpty())
        {
            FileUtil.startFileNewDialog(context, attachFiles);
        }
    }


    /**
     * 获取不同附件列表
     *
     * @param annexs
     * @return
     */
    protected static Object[] getFileList(List<Annexdata> annexs)
    {
        annexWay = 1;
        if (annexs != null && !annexs.isEmpty())
        {
            List<Annexdata> imageFiles = new ArrayList<Annexdata>();
            List<Annexdata> voiceFiles = new ArrayList<Annexdata>();
            List<Annexdata> attachFiles = new ArrayList<Annexdata>();
            for (Annexdata fileListEntity : annexs)
            {
                if (annexWay == 1)
                {
                    String srcName = fileListEntity.getSrcName();
                    SDLogUtil.debug("srcName===" + srcName);
                    switch (FileUtil.getNewFileType(fileListEntity))
                    {
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
                } else if (annexWay == 2)
                {
                    String type = fileListEntity.getType();
                    int showType = fileListEntity.getShowType();
                    if (showType == 1)
                    {
                        attachFiles.add(fileListEntity);
                    } else
                    {
                        if (type.equals("spx"))
                        {
                            voiceFiles.add(fileListEntity);
                        } else
                        {
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
        } else
        {
            return null;
        }
    }

    private static String mySbtring(String srcName)
    {
        if (srcName.contains(Constants.IMAGE_PREFIX_NEW))
        {
            return (srcName.substring(0, srcName.indexOf(Constants.IMAGE_PREFIX_NEW)));
        } else if (srcName.contains(Constants.IMAGE_PREFIX_NEW_01))
        {
            return (srcName.substring(0, srcName.indexOf(Constants.IMAGE_PREFIX_NEW_01)));
        } else if (srcName.contains(Constants.IMAGE_PREFIX_NEW_02))
        {
            return (srcName.substring(0, srcName.indexOf(Constants.IMAGE_PREFIX_NEW_02)));
        } else if (srcName.contains(Constants.IMAGE_PREFIX_NEW_03))
        {
            return (srcName.substring(0, srcName.indexOf(Constants.IMAGE_PREFIX_NEW_03)));
        } else
        {
            return (srcName.substring(0, srcName.indexOf(Constants.RADIO_PREFIX_NEW)));
        }
    }


}
