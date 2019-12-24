

package com.cxgz.activity.superqq.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.base.BaseApplication;
import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.ImageUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cx.workcircle.SDBigImagePagerActivity;
import com.cxgz.activity.cxim.base.BaseFragment;
import com.cxgz.activity.cxim.bean.PersonalListBean;
import com.cxgz.activity.cxim.http.FileSubmitBean;
import com.cxgz.activity.cxim.http.ImHttpHelper;
import com.cxgz.activity.db.dao.SDUserDao;
import com.entity.SDFileListEntity;
import com.entity.update.UpdateEntity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.http.FileUpload;
import com.http.HttpURLUtil;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.superdata.im.utils.SPUtilsTool;
import com.ui.UpdatePasswordActivity;
import com.ui.activity.guide.GuideActivity;
import com.ui.http.BasicDataHttpHelper;
import com.ui.utils.HttpHelpEstablist;
import com.utils.AppUtils;
import com.utils.BadgerUtils;
import com.utils.CachePath;
import com.utils.CommonUtils;
import com.utils.DialogImUtils;
import com.utils.DialogUtilsIm;
import com.utils.FileUploadUtil;
import com.utils.FileUtil;
import com.utils.SDImgHelper;
import com.utils.SPUtils;
import com.utils.SexUtil;
import com.view.LoginOutDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.company.business_trip.BusinessTripAddActivity;
import tablayout.view.dialog.SelectImgDialog;
import tablayout.view.textview.FontTextView;
import yunjing.http.BaseHttpHelper;
import yunjing.utils.DisplayUtil;

import static android.app.Activity.RESULT_OK;

/**
 * 个人信息
 *
 * @author Amy
 * @date 20150610
 */
public class SDPersonalAlterFragment extends BaseFragment
{
    public static final int PSW = 0;// 密码
    public static final int SEX = 1;// 性别
    public static final int EMAIL = 7;// 邮箱
    public static final int PHONE = 8;// 电话

    public static final int REQUEST_CODE_CAMERA = 18;// 相机
    public static final int REQUEST_CODE_LOCAL = 19;// 照片
    private static final int NAME = 20;

    @Bind(R.id.ivContactsHead)
    SimpleDraweeView ivContactsHead;
    @Bind(R.id.layPic)
    RelativeLayout layPic;
    @Bind(R.id.tvAccount)
    FontTextView tvAccount;
    @Bind(R.id.layAccount)
    RelativeLayout layAccount;
    @Bind(R.id.tvPsw)
    FontTextView tvPsw;
    @Bind(R.id.layPsw)
    RelativeLayout layPsw;
    @Bind(R.id.tvName)
    FontTextView tvName;
    @Bind(R.id.layName)
    RelativeLayout layName;
    @Bind(R.id.tvSex)
    FontTextView tvSex;
    @Bind(R.id.laySex)
    RelativeLayout laySex;
    @Bind(R.id.tvEmail)
    FontTextView tvEmail;
    @Bind(R.id.layEmail)
    RelativeLayout layEmail;
    @Bind(R.id.tvPhone)
    FontTextView tvPhone;
    @Bind(R.id.layPhone)
    RelativeLayout layPhone;
    @Bind(R.id.login_out_ll)
    RelativeLayout loginOutLl;


    @Bind(R.id.ll_water_01)
    LinearLayout ll_water_01;

    @Bind(R.id.ll_water_02)
    LinearLayout ll_water_02;

    @Bind(R.id.ll_water)
    LinearLayout ll_water;
    @Bind(R.id.view_mid)
    View view_mid;


    @Bind(R.id.tvDept)
    FontTextView tvDept;
    @Bind(R.id.laydept)
    RelativeLayout laydept;
    @Bind(R.id.tvJob)
    FontTextView tvJob;
    @Bind(R.id.layJob)
    RelativeLayout layJob;
    @Bind(R.id.tvTelephone)
    FontTextView tvTelephone;
    @Bind(R.id.layTelephone)
    RelativeLayout layTelephone;

    @Bind(R.id.tvVersion)
    FontTextView tvVersion;
    @Bind(R.id.layVersion)
    RelativeLayout layVersion;

    private File imgFolder;// 相机拍照保存图片目录
    private File cameraImgPath;// 相机拍照保存图片路径
    private File tempFile; // 裁剪后保存图片路径

    /**
     * 头像路径
     */
    private String imgPath = "", Path = "", oldicon = "";

    private Uri uri, uriresult;
    private Bitmap bmap;

    private String tmpOldPsw, tmpNewPsw, tmpName, tmpSex, tmpEmail, tmpPhone, tmpIcon;
    private String imAccount;

    private String mCurrentVersionName;


    @Override
    protected void init(View view)
    {
        ButterKnife.bind(this, view);
        //这个是ImAccount
        imAccount = (String) SPUtils.get(getActivity(), SPUtils.IM_NAME, "");

        setTitle(getString(R.string.super_tab_05));
//        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.title_size)/);

        mCurrentVersionName = AppUtils.getVersionName(getActivity());
        if (StringUtils.notEmpty(mCurrentVersionName))
        {
            tvVersion.setText("V" + mCurrentVersionName);
        }

        updatePersonalInfo();

        String myNickName = DisplayUtil.getUserInfo(getActivity(), 1);
        if (StringUtils.notEmpty(myNickName))
        {
            ImageUtils.waterMarking(getActivity(), true, false, ll_water, myNickName);
            view_mid.setBackgroundColor(Color.parseColor("#f6f6f6"));
        }
    }

    /**
     * 显示头像
     *
     * @param icoUrl
     */
    protected void showHeadImg(final String icoUrl, final String imAccount)
    {
//        oldicon = FileDownloadUtil.getDownloadIP(annexWay, icoUrl);
        SDImgHelper.getInstance(getActivity()).loadSmallImg(icoUrl, R.mipmap.temp_user_head, ivContactsHead);
        ivContactsHead.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (icoUrl != null && !icoUrl.equals(""))
                {
                    BasicDataHttpHelper.postHighIcon(getActivity(), imAccount, new BasicDataHttpHelper.OnYesOrNoListener()
                    {
                        @Override
                        public void onYes(String s)
                        {
                            if (StringUtils.notEmpty(s))
                            {
                                Intent intent = new Intent(getActivity(), SDBigImagePagerActivity.class);
                                intent.putExtra(Constants.EXTRA_BIG_IMG_URIS, new String[]{s});
                                intent.putExtra(SDBigImagePagerActivity.IS_NEED_DEL, false);
                                intent.putExtra(SDBigImagePagerActivity.IS_HIDE_INDICATOR, true);
                                startActivity(intent);
                            } else
                            {
                                Intent intent = new Intent(getActivity(), SDBigImagePagerActivity.class);
                                intent.putExtra(Constants.EXTRA_BIG_IMG_URIS, new String[]{icoUrl});
                                intent.putExtra(SDBigImagePagerActivity.IS_NEED_DEL, false);
                                intent.putExtra(SDBigImagePagerActivity.IS_HIDE_INDICATOR, true);
                                startActivity(intent);
                            }

                        }

                        @Override
                        public void onNo(String s)
                        {
                            Intent intent = new Intent(getActivity(), SDBigImagePagerActivity.class);
                            intent.putExtra(Constants.EXTRA_BIG_IMG_URIS, new String[]{icoUrl});
                            intent.putExtra(SDBigImagePagerActivity.IS_NEED_DEL, false);
                            intent.putExtra(SDBigImagePagerActivity.IS_HIDE_INDICATOR, true);
                            startActivity(intent);
                        }
                    });
                } else
                {
                    Intent intent = new Intent(getActivity(), SDBigImagePagerActivity.class);
                    intent.putExtra(Constants.EXTRA_BIG_IMG_URIS, new String[]{"icon"});
                    intent.putExtra(SDBigImagePagerActivity.IS_NEED_DEL, false);
                    intent.putExtra(SDBigImagePagerActivity.IS_HIDE_INDICATOR, true);
                    startActivity(intent);
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode,
                                 Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK)
        {
            Bundle bundle;
            switch (requestCode)
            {
                case PSW:
//                    tmpOldPsw = bundle.getString("strOldPsw");
//                    tmpNewPsw = bundle.getString("strNewPsw");
                    break;

                case NAME:
                    bundle = intent.getExtras();
                    tmpName = bundle.getString("value");
//                    realName = inName.getText().toString();
                    postData();
                    break;

                case EMAIL:
                    bundle = intent.getExtras();
                    tmpEmail = bundle.getString("value");
                    postData();
                    break;

                case PHONE:
                    bundle = intent.getExtras();
                    tmpPhone = bundle.getString("value");

                    postData();
                    break;

                case REQUEST_CODE_CAMERA:
                    if (intent != null)
                    { // 可能尚未指定intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        // uri);
                        if (intent.hasExtra("data"))
                        {
                            Bitmap thumbnail = intent.getParcelableExtra("data");
                            // 得到bitmap后的操作
                        }
                    } else
                    {
                        // 由于指定了目标uri，存储在目标uri，intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        // uri);
                        CutPic(uri, REQUEST_CODE_CAMERA);
                        SDLogUtil.debug("图片路径：" + cameraImgPath);
                    }
                    break;

                case REQUEST_CODE_LOCAL:
                    //System.out.println("返回数据：" + intent.getData());
                    uri = intent.getData();
                    CutPic(uri, REQUEST_CODE_LOCAL);
                    break;

                case 200:
                    if (resultCode == RESULT_OK)
                    {
                        // 拿到剪切数据
                        if (uriresult != null)
                        {
                            imgPath = tempFile.getAbsolutePath();
                            ivContactsHead.setTag(imgPath);
                            try
                            {
                                bmap = BitmapFactory
                                        .decodeStream(getActivity().getContentResolver()
                                                .openInputStream(uriresult));
                                if (CommonUtils.isNetWorkConnected(getActivity()))
                                {
                                    ivContactsHead.setImageBitmap(bmap);
                                }
                            } catch (FileNotFoundException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                    postFile(imgPath);
                    break;
            }
        }
    }

    private void CutPic(Uri uri1, int code)
    {
        String filepath = Environment.getExternalStorageDirectory()
                + File.separator + CachePath.USER_HEADER + File.separator;
        tempFile = new File(filepath + "headimg.jpg");
        File file = new File(filepath);

        if (!file.exists())
        {
            file.mkdirs();
        }
        uriresult = Uri.fromFile(tempFile);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri1, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriresult);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, 200);
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.fragment_me_layout;
    }

    private void postData()
    {
        String url = HttpURLUtil.newInstance().append("sysuser").append("update").toString();
        RequestParams params = new RequestParams();

        if (StringUtils.notEmpty(tmpName))
        {
            params.addBodyParameter("name", tmpName);
        }
        if (StringUtils.notEmpty(tmpSex))
        {
            params.addBodyParameter("sex", tmpSex);
        }
        if (StringUtils.notEmpty(tmpEmail))
        {
            params.addBodyParameter("email", tmpEmail);
        }
        if (StringUtils.notEmpty(tmpPhone))
        {
            params.addBodyParameter("telephone", tmpPhone);
        }
        if (StringUtils.notEmpty(tmpIcon))
        {
            params.addBodyParameter("icon", tmpIcon);
        }

        List<SDFileListEntity> files = null;

//        if (!imgPath.equals(""))
//        {
//            files = new ArrayList<>();
//            files = addFiles(files, FileUploadPath.IMAGE_PREFIX);
//        }
        postRequest(url, files, params);
    }

    private boolean progressShow;
    private ProgressDialog pd;

    /**
     * 提交请求
     *
     * @param url
     * @param files
     */
    private void postRequest(String url, List<SDFileListEntity> files,
                             RequestParams params)
    {
        progressShow = true;
        pd = new ProgressDialog(getActivity());
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                progressShow = false;
            }
        });

        pd.setMessage(getString(R.string.Is_post));
        pd.show();
        FileUploadUtil.resumableUpload(getActivity().getApplication(), files, "", null, url, params, new FileUpload
                .UploadListener()
        {
            @Override
            public void onSuccess(SDResponseInfo responseInfo,
                                  HashMap<String, Object> returns)
            {
                MyToast.showToast(getActivity(), R.string.person_info_altersuccess);
                if (pd != null)
                {
                    pd.dismiss();
                }
                updatePersonalInfo();
                refreshContact();
            }

            @Override
            public void onProgress(int byteCount, int totalSize)
            {

            }

            @Override
            public void onFailure(Exception ossException)
            {
                pd.dismiss();
                MyToast.showToast(getActivity(), R.string.person_info_alterfail);
            }
        });
//        setResult(RESULT_OK);
    }

    protected SDUserDao mUserDao;

    private void refreshContact()
    {
        HttpHelpEstablist.getInstance().refreshContact(getActivity(), new SDRequestCallBack()
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(getActivity(), msg);
                pd.dismiss();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                pd.dismiss();
            }
        });
    }

    private void updatePersonalInfo()
    {
        if (StringUtils.empty(imAccount))
        {
            MyToast.showToast(getActivity(), R.string.request_fail_data);
            return;
        }

        ImHttpHelper.findPersonInfoByImAccount(getActivity(), true, mHttpHelper, imAccount, new
                SDRequestCallBack(PersonalListBean.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
//                MyToast.showToast(SDPersonalAlterActivity.this, "刷新个人信息失败！");
                        MyToast.showToast(getActivity(), msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        PersonalListBean personalListBean = (PersonalListBean) responseInfo.getResult();
                        if (personalListBean != null)
                            setPersonalInfo(personalListBean);
                    }
                });
    }

    private void setPersonalInfo(PersonalListBean personalInfo)
    {
        if (personalInfo.getData() != null)
        {
            showHeadImg(personalInfo.getData().getIcon(), personalInfo.getData().getImAccount());
            tvAccount.setText(personalInfo.getData().getAccount());
            tvName.setText(personalInfo.getData().getName());
            tvSex.setText(SexUtil.getSexStr(getActivity(), personalInfo.getData().getSex()));
            tvEmail.setText(personalInfo.getData().getEmail());
            tvPhone.setText(personalInfo.getData().getTelephone());
            //部门
            tvDept.setText(personalInfo.getData().getDeptName());
            tvJob.setText(personalInfo.getData().getJob());
            tvTelephone.setText(personalInfo.getData().getPhone());

            SPUtils.put(getActivity(), SPUtils.USER_ICON, personalInfo.getData().getIcon());
            SPUtils.put(getActivity(), SPUtils.USER_NAME, personalInfo.getData().getName());
        }
    }

    @Override
    public void onDestroy()
    {
        if (bmap != null && !bmap.isRecycled())
        {
            bmap.recycle();
        }
        super.onDestroy();
    }

    //附件添加的
    protected List<File> files;
    protected List<String> imgPaths;
    protected File voice;

    private void postFile(String imgPath)
    {
        if (StringUtils.notEmpty(imgPath))
        {
            imgPaths = new ArrayList<String>();
            imgPaths.add(imgPath);
            ImHttpHelper.submitFileApi(getActivity().getApplication(), "", false, files, imgPaths, voice, new
                    FileUpload.UploadListener()
                    {
                        @Override
                        public void onSuccess(SDResponseInfo responseInfo, HashMap<String, Object> result)
                        {
                            FileSubmitBean callBean = new SDGson().fromJson(responseInfo.getResult().toString(), new
                                    TypeToken<FileSubmitBean>()
                                    {
                                    }.getType());

                            tmpIcon = callBean.getData().get(0).getPath();
                            postData();
                        }

                        @Override
                        public void onProgress(int byteCount, int totalSize)
                        {

                        }

                        @Override
                        public void onFailure(Exception ossException)
                        {
                            MyToast.showToast(getActivity(), getResources().getString(R.string.request_fail));
                        }

                    });
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.ivContactsHead, R.id.layPic, R.id.layAccount, R.id.layPsw, R.id.layName, R.id.laySex, R.id.layEmail, R.id
            .layPhone, R.id.login_out_ll, R.id.laydept, R.id.layJob, R.id.layTelephone, R.id.layVersion})
    public void onViewClicked(View view)
    {
        Intent intent = null;
        Bundle bundle = null;
        switch (view.getId())
        {
            case R.id.ivContactsHead:

                break;
            case R.id.layPic:
                final SelectImgDialog dialog = new SelectImgDialog(getActivity(),
                        new String[]{getString(R.string.camera),
                                getString(R.string.album)});
                dialog.show();
                dialog.setOnSelectImgListener(new SelectImgDialog.OnSelectImgListener()
                {
                    @Override
                    public void onClickCanera(View v)
                    {
                        if (imgFolder == null)
                        {
                            imgFolder = new File(FileUtil.getSDFolder(),
                                    CachePath.CAMERA_IMG_PATH);
                        }
                        if (!imgFolder.exists())
                        {
                            imgFolder.mkdirs();
                        }
                        cameraImgPath = new File(imgFolder, "sd_img_"
                                + System.currentTimeMillis() + ".jpg");
                        Intent cameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        uri = Uri.fromFile(cameraImgPath);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
                    }

                    @Override
                    public void onClickCancel(View v)
                    {
                        dialog.cancel();
                    }

                    @Override
                    public void onClickAlum(View v)
                    {
                        Intent pictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pictureIntent, REQUEST_CODE_LOCAL);
                    }
                });
                break;
//            case R.id.layAccount:
//                break;
            case R.id.layPsw:
                //修改密码
                String userAccount = (String) SPUtils.get(getActivity(), SPUtils.USER_NAME, "");
                bundle = new Bundle();
                bundle.putString("phone", userAccount);
                bundle.putBoolean("isForget", false);
                openActivity(UpdatePasswordActivity.class, bundle);
                break;

//            case R.id.layName:
//                //修改名字
//                bundle = new Bundle();
//                bundle.putString("title", getString(R.string.contasts_name));
//                bundle.putString(SDCrmEditTextActivity.VALUES, tvName.getText().toString());
//                bundle.putInt(SDCrmEditTextActivity.MAX_SIZE, 12);
//                bundle.putBoolean(SDCrmEditTextActivity.IS_NAME, true);
//
//                intent = new Intent(getActivity(), SDCrmEditTextActivity.class);
//                if (bundle != null)
//                {
//                    intent.putExtras(bundle);
//                }
//                startActivityForResult(intent, NAME);
//
//                break;
//            case R.id.laySex:
//
////                mSexPicker.showAtLocation(ll_container, Gravity.END | Gravity.BOTTOM, 0,
////                        -mSexPicker.getHeight());
//
//                final SelectImgDialog sexDialog = new SelectImgDialog(getActivity(),
//                        new String[]{getString(R.string.man),
//                                getString(R.string.woman)}, "请选择性别");
//                sexDialog.show();
//                sexDialog.setOnSelectImgListener(new SelectImgDialog.OnSelectImgListener()
//                {
//                    @Override
//                    public void onClickCanera(View v)
//                    {
//                        tvSex.setText("男");
//                        tmpSex = "1";
//                        postData();
//                    }
//
//                    @Override
//                    public void onClickCancel(View v)
//                    {
//                        sexDialog.cancel();
//                    }
//
//                    @Override
//                    public void onClickAlum(View v)
//                    {
//                        tvSex.setText("女");
//                        tmpSex = "2";
//                        postData();
//                    }
//                });
//
//                break;
            case R.id.layEmail:
//                //修改邮箱
//                bundle = new Bundle();
//                bundle.putString("title", getString(R.string.person_email));
//                bundle.putString(SDCrmEditTextActivity.VALUES, tvEmail.getText().toString());
//                bundle.putInt(SDCrmEditTextActivity.MAX_SIZE, 30);
//                bundle.putBoolean(SDCrmEditTextActivity.IS_EMAIL, true);
//
//                intent = new Intent(getActivity(), SDCrmEditTextActivity.class);
//                if (bundle != null)
//                {
//                    intent.putExtras(bundle);
//                }
//                startActivityForResult(intent, EMAIL);
                // 必须明确使用mailto前缀来修饰邮件地址,如果使用
                // intent.putExtra(Intent.EXTRA_EMAIL, email)，结果将匹配不到任何应用

                break;
//            case R.id.layPhone:
//
//                //修改手机
//                bundle = new Bundle();
//                bundle.putString("title", getString(R.string.person_phone));
//                bundle.putString(SDCrmEditTextActivity.VALUES, tvPhone.getText().toString());
//                bundle.putInt(SDCrmEditTextActivity.MAX_SIZE, 11);
//                bundle.putBoolean(SDCrmEditTextActivity.IS_PHONE, true);
//
//                intent = new Intent(getActivity(), SDCrmEditTextActivity.class);
//                if (bundle != null)
//                {
//                    intent.putExtras(bundle);
//                }
//                startActivityForResult(intent, PHONE);
//
//
//                break;

            case R.id.login_out_ll:
                logoutDialog();
                break;

            case R.id.laydept:

                break;
            case R.id.layJob:
                BadgerUtils.getInstance().setRemoveCount(getActivity());
                break;
            case R.id.layTelephone:
                BadgerUtils.getInstance().setBadgerApplyCount(getActivity(), 17);
                break;
            case R.id.layVersion:
//                getUpdate();
                BadgerUtils.getInstance().setNotificationBadger(getActivity(), "个人信息", "你有一条未读消息" + DateUtils.formatNowDate
                        ("yyyy-MM-dd HH:mm:ss"), 10);
                break;
        }
    }

    private void getUpdate()
    {
        BaseHttpHelper.updateInfo(mHttpHelper, new SDRequestCallBack(UpdateEntity.class)
        {
            @Override
            public void onCancelled()
            {
                super.onCancelled();
            }

            @Override
            public void onRequestFailure(HttpException error, String msg)
            {

            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                UpdateEntity entity = (UpdateEntity) responseInfo.result;
                if (entity != null && StringUtils.notEmpty(entity.getData()))
                {
                    if (StringUtils.notEmpty(entity.getData().getIsOpen()))
                    {
                        if (entity.getData().getIsOpen().equals("true"))
                        {
                            if (StringUtils.empty(entity.getData().getVersionCode()))
                            {
                                if (AppUtils.getVersionCode(getActivity()) < (Integer.parseInt(entity.getData().getVersionCode
                                        ())))
                                {
                                    if (!SPUtilsTool.getVersionUpdate(getActivity(), Integer.parseInt(entity.getData()
                                            .getVersionCode())))
                                    {
                                        showDialog(entity);
                                    } else
                                    {

                                    }
                                } else
                                {
                                    DialogImUtils.getInstance().dialogCommom(getActivity(), "提示", "已经是最新版本"
                                            , "关闭", "", new DialogImUtils.OnYesOrNoListener()
                                            {
                                                @Override
                                                public void onYes()
                                                {

                                                }

                                                @Override
                                                public void onNo()
                                                {

                                                }
                                            });
                                }
                            } else
                            {
                                MyToast.showToast(getActivity(), "版本检查异常");
                            }
                        }
                    } else
                    {
                        MyToast.showToast(getActivity(), "版本检查异常");
                    }
                }
            }
        });
    }

    private void showDialog(final UpdateEntity entity)
    {
        DialogUtilsIm.showUpdataVersion(getActivity(), entity.getData().getDescription(), new DialogUtilsIm
                .OnYesOrNoAndCKListener()
        {
            @Override
            public void onYes()
            {
                if (StringUtils.notEmpty(entity.getData().getUrlLink()))
                {
                    download(entity);
                } else
                {
                    MyToast.showToast(getActivity(), "打开出错，请稍候再试!");
                }
            }

            @Override
            public void onNo()
            {
                //强制更新
                if (StringUtils.notEmpty(entity.getData().getStatus()))
                {
                    if (entity.getData().getStatus().equals("1"))
                    {
                        SDLogUtil.debug("版本-强制更新");
                        System.exit(0);
                    }
                    //普通更新
                    else if (entity.getData().getStatus().equals("2"))
                    {
                        SDLogUtil.debug("版本- 普通更新");
                    } else
                    {

                    }
                }
            }

            @Override
            public void onCheck(boolean isCheck)
            {
                if (isCheck)
                {
                    SPUtils.put(getActivity(), com.chaoxiang.base.utils.SPUtils.VERSION_UPDATE, true);
                }
            }
        });
    }

    public void download(UpdateEntity entity)
    {
        if (StringUtils.notEmpty(entity.getData().getUrlLink()))
        {
            if (entity.getData().getUrlLink().indexOf("http") != -1)
            {
                SDLogUtil.debug("版本-下载链接:" + entity.getData().getUrlLink());
                Uri uri = Uri.parse(entity.getData().getUrlLink().toString().trim());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                getActivity().startActivity(intent);
            } else
            {
                MyToast.showToast(getActivity(), "下载链接为空，请移步应用宝搜索下载");
            }
        }
    }

    public void logoutDialog()
    {
        LoginOutDialog.logoutDialog(getActivity(), new LoginOutDialog.loginOutListener()
        {
            @Override
            public void setTrue()
            {
                LoginOutDialog.logout(getActivity());
            }

            @Override
            public void setCancle()
            {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    //
//    /**
//     * 保存方法
//     */.··
//    private void saveBitmap(String path, String picName, Bitmap bm)
//    {
//        if (oldicon != null && !oldicon.isEmpty())
//        {
//            File f = new File(SDImgHelper.getInstance(this).getImagePath(oldicon));
//            Fresco.getImagePipeline().evictFromCache(Uri.parse(oldicon));
//            Fresco.getImagePipeline().evictFromMemoryCache(Uri.parse(oldicon));
//            Fresco.getImagePipeline().evictFromDiskCache(Uri.parse(oldicon));
//
//            if (f.exists())
//            {
//                f.delete();
//            }
//            FileOutputStream out = null;
//            try
//            {
//                out = new FileOutputStream(f);
//                bm.compress(Bitmap.CompressFormat.PNG, 90, out);
//            } catch (FileNotFoundException e)
//            {
//                e.printStackTrace();
//            } finally
//            {
//                try
//                {
//                    out.flush();
//                    out.close();
//                } catch (IOException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
}
