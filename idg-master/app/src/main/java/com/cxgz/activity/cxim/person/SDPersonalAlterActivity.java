package com.cxgz.activity.cxim.person;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.ImageUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.PhoneUtils;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.ScreenUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cx.workcircle.SDBigImagePagerActivity;
import com.cxgz.activity.cxim.ChatActivity;
import com.cxgz.activity.cxim.VoiceActivity;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.cxim.bean.PersonInfoBean;
import com.cxgz.activity.cxim.bean.PersonalListBean;
import com.cxgz.activity.cxim.bean.StringList;
import com.cxgz.activity.cxim.http.FileSubmitBean;
import com.cxgz.activity.cxim.http.ImHttpHelper;
import com.cxgz.activity.cxim.utils.SmileUtils;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.entity.SDFileListEntity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.http.FileUpload;
import com.http.HttpURLUtil;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.superdata.im.constants.CxIMMessageType;
import com.ui.UpdatePasswordActivity;
import com.ui.http.BasicDataHttpHelper;
import com.ui.utils.HttpHelpEstablist;
import com.utils.CachePath;
import com.utils.CommonUtils;
import com.utils.FileUploadUtil;
import com.utils.FileUtil;
import com.utils.SDImgHelper;
import com.utils.SDToast;
import com.utils.SPUtils;
import com.utils.SexUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tablayout.view.dialog.SelectImgDialog;
import tablayout.view.textview.FontTextView;
import yunjing.utils.BaseDialogUtils;
import yunjing.view.DialogStringListFragment;
import yunjing.view.DialogStringListFragment2;

import static com.superdata.im.processor.CxChatCxBaseProcessor.initData;


/**
 * 个人信息
 *
 * @author Amy
 * @date 20150610
 */
public class SDPersonalAlterActivity extends BaseActivity
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
    @Bind(R.id.ivRightGo)
    ImageView ivRightGo;
    @Bind(R.id.img02)
    ImageView img02;
    @Bind(R.id.view_password)
    View viewPassword;
    @Bind(R.id.rl_chat)
    RelativeLayout rl_chat;

    @Bind(R.id.send_chat) //发企信
            Button send_chat;
    @Bind(R.id.send_voice) //发语音
            Button send_voice;
    @Bind(R.id.send_phone) //打电话
            Button send_phone;
    @Bind(R.id.send_msm) //发短信
            Button send_msm;
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

    @Bind(R.id.ll_water)
    LinearLayout ll_water;

    @Bind(R.id.ll_water_01)
    LinearLayout ll_water_01;
    @Bind(R.id.ll_water_02)
    LinearLayout ll_water_02;

    @Bind(R.id.view_mid)
    View view_mid;

    private File imgFolder;// 相机拍照保存图片目录
    private File cameraImgPath;// 相机拍照保存图片路径
    private File tempFile; // 裁剪后保存图片路径

    private Context context;

    /**
     * 头像路径
     */
    private String imgPath = "", Path = "", oldicon = "";

    private Uri uri, uriresult;
    private Bitmap bmap;

    private boolean isOneSelf;// 是否本人

    private String tmpOldPsw, tmpNewPsw, tmpName, tmpSex, tmpEmail, tmpPhone, tmpIcon;
    private String userId_kefu;
    private String myImAccount;
    private String myUid_kefu;
    private String imAccount;

    @Override
    protected void init()
    {
        ButterKnife.bind(this);

        addLeftBtn(R.drawable.folder_back, new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
        {
            finish();
        }
        userId_kefu = bundle.getString(SPUtils.USER_ID_KEFU, "");
        //这个是ImAccount
        imAccount = bundle.getString(SPUtils.USER_ID, "");
        if (TextUtils.isEmpty(imAccount) && TextUtils.isEmpty(userId_kefu))
        {
            finish();
        }

        myImAccount = (String) SPUtils.get(this, SPUtils.IM_NAME, "");
        myUid_kefu = (String) SPUtils.get(this, SPUtils.USER_ID_KEFU, "");

        if (TextUtils.isEmpty(myImAccount) && TextUtils.isEmpty(myUid_kefu))
        {
            finish();
        }
        if (imAccount.equals(myImAccount))
        {
            isOneSelf = true;
        }

        if (isOneSelf)
        {
            setTitle(getString(R.string.editpersonaldata));
            rl_chat.setVisibility(View.GONE);

        } else
        {
            setTitle(getString(R.string.person_data));
            rl_chat.setVisibility(View.VISIBLE);
        }

        context = this;

        getLay();

        updatePersonalInfo();

        layEmail.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                if (StringUtils.notEmpty(tvEmail.getText().toString().trim()))
                {
                    copy(tvEmail.getText().toString().trim());
                }
                return true;
            }
        });
    }

    private void getLay()
    {
        boolean is_experience = (boolean) SPUtils.get(this, SPUtils.IS_EXPERIENCE, false);
        if (is_experience)
        {
            layPsw.setVisibility(View.GONE);
        }

        String myNickName = (String) SPUtils.get(SDPersonalAlterActivity.this, SPUtils.USER_NAME, "");
        if (StringUtils.notEmpty(myNickName))
        {
//            ImageUtils.waterMarking(SDPersonalAlterActivity.this, true, false, ll_water, myNickName);
            ImageUtils.waterMarking(SDPersonalAlterActivity.this, true, false, ll_water_01, myNickName);
            ImageUtils.waterMarking(SDPersonalAlterActivity.this, true, false, ll_water_02, myNickName);
        }

        if (isOneSelf)
        {
            layPic.setClickable(true);
            layPsw.setClickable(true);
            layName.setClickable(true);
            laySex.setClickable(true);
            layEmail.setClickable(true);
            layPhone.setClickable(true);
        } else
        {
            ivRightGo.setVisibility(View.INVISIBLE);
            img02.setVisibility(View.INVISIBLE);
            layPsw.setVisibility(View.GONE);
            viewPassword.setVisibility(View.GONE);

            layPic.setClickable(false);
            layPsw.setClickable(false);
            layName.setClickable(false);
            laySex.setClickable(false);
            layEmail.setClickable(false);
            layPhone.setClickable(false);
        }
    }

    /**
     * 显示头像
     *
     * @param icoUrl
     */
    protected void showHeadImg(final String icoUrl)
    {
//        oldicon = FileDownloadUtil.getDownloadIP(annexWay, icoUrl);
        SDImgHelper.getInstance(this).loadSmallImg(icoUrl, R.mipmap.temp_user_head, ivContactsHead);
        ivContactsHead.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (icoUrl != null && !icoUrl.equals(""))
                {
                    BasicDataHttpHelper.postHighIcon(SDPersonalAlterActivity.this, imAccount, new BasicDataHttpHelper
                            .OnYesOrNoListener()
                    {
                        @Override
                        public void onYes(String s)
                        {
                            if (StringUtils.notEmpty(s))
                            {
                                Intent intent = new Intent(SDPersonalAlterActivity.this, SDBigImagePagerActivity.class);
                                intent.putExtra(Constants.EXTRA_BIG_IMG_URIS, new String[]{s});
                                intent.putExtra(SDBigImagePagerActivity.IS_NEED_DEL, false);
                                intent.putExtra(SDBigImagePagerActivity.IS_HIDE_INDICATOR, true);
                                startActivity(intent);
                            } else
                            {
                                Intent intent = new Intent(SDPersonalAlterActivity.this, SDBigImagePagerActivity.class);
                                intent.putExtra(Constants.EXTRA_BIG_IMG_URIS, new String[]{icoUrl});
                                intent.putExtra(SDBigImagePagerActivity.IS_NEED_DEL, false);
                                intent.putExtra(SDBigImagePagerActivity.IS_HIDE_INDICATOR, true);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onNo(String s)
                        {
                            Intent intent = new Intent(SDPersonalAlterActivity.this, SDBigImagePagerActivity.class);
                            intent.putExtra(Constants.EXTRA_BIG_IMG_URIS, new String[]{icoUrl});
                            intent.putExtra(SDBigImagePagerActivity.IS_NEED_DEL, false);
                            intent.putExtra(SDBigImagePagerActivity.IS_HIDE_INDICATOR, true);
                            startActivity(intent);
                        }
                    });
                } else
                {
                    Intent intent = new Intent(SDPersonalAlterActivity.this, SDBigImagePagerActivity.class);
                    intent.putExtra(Constants.EXTRA_BIG_IMG_URIS, new String[]{"icon"});
                    intent.putExtra(SDBigImagePagerActivity.IS_NEED_DEL, false);
                    intent.putExtra(SDBigImagePagerActivity.IS_HIDE_INDICATOR, true);
                    startActivity(intent);
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK)
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
                                        .decodeStream(getContentResolver()
                                                .openInputStream(uriresult));
                                if (CommonUtils.isNetWorkConnected(context))
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
        SDPersonalAlterActivity.this.startActivityForResult(intent, 200);
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.sd_personal_info;
    }

    /**
     * lzk网络请求
     */
    private FileUpload upload;

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
        pd = new ProgressDialog(this);
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
        FileUploadUtil.resumableUpload(getApplication(), files, "", null, url, params, new FileUpload.UploadListener()
        {
            @Override
            public void onSuccess(SDResponseInfo responseInfo,
                                  HashMap<String, Object> returns)
            {
                MyToast.showToast(SDPersonalAlterActivity.this, R.string.person_info_altersuccess);
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
                MyToast.showToast(SDPersonalAlterActivity.this, R.string.person_info_alterfail);
            }
        });
//        setResult(RESULT_OK);
    }

    protected SDUserDao mUserDao;

    private void refreshContact()
    {
        HttpHelpEstablist.getInstance().refreshContact(this, new SDRequestCallBack()
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(SDPersonalAlterActivity.this, msg);
                pd.dismiss();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                pd.dismiss();
            }
        });
    }

    PersonalListBean personalListBean = null;

    private void updatePersonalInfo()
    {
        //更新数据
        if (StringUtils.empty(imAccount))
        {
            MyToast.showToast(SDPersonalAlterActivity.this, R.string.request_fail_data);
            return;
        }

        ImHttpHelper.findPersonInfoByImAccount(SDPersonalAlterActivity.this, true, mHttpHelper, imAccount, new
                SDRequestCallBack(PersonalListBean.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
//                MyToast.showToast(SDPersonalAlterActivity.this, "刷新个人信息失败！");
                        MyToast.showToast(SDPersonalAlterActivity.this, msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        personalListBean = (PersonalListBean) responseInfo.getResult();
                        if (personalListBean != null)
                            setPersonalInfo(personalListBean);
                    }
                });
    }

    private void setPersonalInfo(PersonalListBean personalInfo)
    {
        if (personalInfo.getData() != null)
        {
            showHeadImg(personalInfo.getData().getIcon());
            tvAccount.setText(personalInfo.getData().getAccount());
            tvName.setText(personalInfo.getData().getName());
            tvSex.setText(SexUtil.getSexStr(context, personalInfo.getData().getSex()));
            tvEmail.setText(personalInfo.getData().getEmail());
            tvPhone.setText(personalInfo.getData().getTelephone());

            //部门
            tvDept.setText(personalInfo.getData().getDeptName());
            tvJob.setText(personalInfo.getData().getJob());
            tvTelephone.setText(personalInfo.getData().getPhone());

            if (isOneSelf)
            {
                SPUtils.put(SDPersonalAlterActivity.this, SPUtils.USER_ICON, personalInfo.getData().getIcon());
                SPUtils.put(SDPersonalAlterActivity.this, SPUtils.USER_NAME, personalInfo.getData().getName());
            }
        }
    }

    /**
     * 添加文件参数
     *
     * @param files
     * @param upLoadPath
     */
    private List<SDFileListEntity> addFiles(List<SDFileListEntity> files, String upLoadPath)
    {
        if (!imgPath.equals(""))
        {
            File file = new File(imgPath);
            SDFileListEntity entity = new SDFileListEntity();
            entity.setEntity(this, file, upLoadPath, SDFileListEntity.ICON);
            files.add(entity);
        }
        return files != null ? files : null;
    }

    @Override
    protected void onDestroy()
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
            ImHttpHelper.submitFileApi(SDPersonalAlterActivity.this.getApplication(), "", false, files, imgPaths, voice, new
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
                            MyToast.showToast(SDPersonalAlterActivity.this, getResources().getString(R.string.request_fail));
                        }

                    });
        }
    }

    @OnClick({R.id.ivContactsHead, R.id.layPic, R.id.layAccount, R.id.layPsw, R.id.layName, R.id.laySex, R.id.layEmail, R.id
            .layPhone, R.id.send_chat, R.id.send_voice, R.id.send_phone, R.id.send_msm, R.id.laydept, R.id.layJob, R.id
            .layTelephone})
    public void onViewClicked(View view)
    {
        Intent intent = new Intent();

        Bundle bundle = null;
        switch (view.getId())
        {
            case R.id.ivContactsHead:
                break;
            case R.id.layPic:
                if (isOneSelf)
                {
                    final SelectImgDialog dialog = new SelectImgDialog(context,
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
                            Intent pictureIntent = new Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pictureIntent, REQUEST_CODE_LOCAL);
                        }
                    });
                }
                break;
            case R.id.layAccount:
                break;
            case R.id.layPsw:
                //修改密码
                if (isOneSelf)
                {
                    String userAccount = (String) SPUtils.get(SDPersonalAlterActivity.this, SPUtils.USER_NAME, "");
                    bundle = new Bundle();
                    bundle.putString("phone", userAccount);
                    bundle.putBoolean("isForget", false);
                    openActivity(UpdatePasswordActivity.class, bundle);
                }
                break;
            case R.id.layName:
                //修改名字
//                bundle = new Bundle();
//                bundle.putString("title", getString(R.string.contasts_name));
//                bundle.putString(SDCrmEditTextActivity.VALUES, tvName.getText().toString());
//                bundle.putInt(SDCrmEditTextActivity.MAX_SIZE, 12);
//                bundle.putBoolean(SDCrmEditTextActivity.IS_NAME, true);
//                openActivityForResult(SDCrmEditTextActivity.class, bundle, NAME);
                break;
            case R.id.laySex:
                //                mSexPicker.showAtLocation(ll_container, Gravity.END | Gravity.BOTTOM, 0,
//                        -mSexPicker.getHeight());
//
//                final SelectImgDialog sexDialog = new SelectImgDialog(context,
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
                break;
            case R.id.layEmail:
                //修改邮箱
//                bundle = new Bundle();
//                bundle.putString("title", getString(R.string.person_email));
//                bundle.putString(SDCrmEditTextActivity.VALUES, tvEmail.getText().toString());
//                bundle.putInt(SDCrmEditTextActivity.MAX_SIZE, 30);
//                bundle.putBoolean(SDCrmEditTextActivity.IS_EMAIL, true);
//                openActivityForResult(SDCrmEditTextActivity.class, bundle, EMAIL);
                String emailString = tvEmail.getText().toString().trim();
                if (StringUtils.notEmpty(emailString))
                {
                    Uri uri = Uri.parse("mailto:" + emailString);
                    String[] email = {emailString};
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
                    emailIntent.putExtra(Intent.EXTRA_CC, email); // 抄送人
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, ""); // 主题
                    emailIntent.putExtra(Intent.EXTRA_TEXT, ""); // 正文
                    startActivity(Intent.createChooser(emailIntent, "请选择邮件类应用"));
                }

                break;
            case R.id.layPhone:

                showPhone();

                break;

            case R.id.send_chat:

                String account = imAccount;
                Intent singleChatIntent = new Intent(SDPersonalAlterActivity.this, ChatActivity.class);
                singleChatIntent.putExtra(ChatActivity.EXTR_CHAT_TYPE, CxIMMessageType.SINGLE_CHAT.getValue());
                singleChatIntent.putExtra(ChatActivity.EXTR_CHAT_ACCOUNT, account);
                startActivity(singleChatIntent);

                break;
            case R.id.send_voice:

                String accounts = imAccount;
                Intent voiceIntent = new Intent(this, VoiceActivity.class);
                voiceIntent.setAction(Intent.ACTION_VIEW);
                voiceIntent.putExtra(VoiceActivity.IM_ACCOUND, accounts);
                voiceIntent.putExtra(VoiceActivity.IS_CALL, true);
                startActivity(voiceIntent);
                break;

            case R.id.send_phone:

                //打电话
                if (personalListBean != null && personalListBean.getData() != null && StringUtils.notEmpty(personalListBean
                        .getData().getTelephone()))
                {
                    intent.setAction(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + personalListBean.getData().getTelephone()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (intent.getComponent() != null || intent.getAction() != null)
                    {
                        startActivity(intent);
                    }
                } else
                {
                    SDToast.showShort("对方未录入电话");
                }
                break;

            case R.id.send_msm:
                //发短信
                if (personalListBean != null && personalListBean.getData() != null && StringUtils.notEmpty(personalListBean
                        .getData().getTelephone()))
                {
                    intent.setAction(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("smsto:" + personalListBean.getData().getTelephone()));
                    if (intent.getComponent() != null || intent.getAction() != null)
                    {
                        startActivity(intent);
                    }
                } else
                {
                    SDToast.showShort("对方未录入电话");
                }
                break;

            case R.id.laydept:
                break;
            case R.id.layJob:
                break;
            case R.id.layTelephone:
                break;
        }
    }

    private List<StringList.Data> mListsPhone = new ArrayList<StringList.Data>();

    private void initData()
    {
        mListsPhone.clear();
        StringList.Data data = new StringList.Data();
        data.setId(1);
        data.setName("呼叫号码");
        mListsPhone.add(data);

        StringList.Data data2 = new StringList.Data();
        data2.setId(2);
        data2.setName("发送短信");
        mListsPhone.add(data2);

        StringList.Data data3 = new StringList.Data();
        data3.setId(3);
        data3.setName("添加到手机通讯录");
        mListsPhone.add(data3);
    }

    private List<StringList.Data> mListsHowAdd = new ArrayList<StringList.Data>();

    private void initAddContactsData()
    {
        mListsHowAdd.clear();
        StringList.Data data = new StringList.Data();
        data.setId(1);
        data.setName("新建联系人");
        mListsHowAdd.add(data);

        StringList.Data data2 = new StringList.Data();
        data2.setId(2);
        data2.setName("添加到现有联系人");
        mListsHowAdd.add(data2);
    }

    private void showPhone()
    {
        initData();
        BaseDialogUtils.showListStringDialog2(SDPersonalAlterActivity.this, mListsPhone, new DialogStringListFragment2
                .InputListener()
        {
            @Override
            public void onData(StringList.Data content)
            {
                Intent intent = new Intent();
                if (content.getId() == 1)
                {
                    if (personalListBean != null && personalListBean.getData() != null && StringUtils.notEmpty(personalListBean
                            .getData().getTelephone()))
                    {
                        intent.setAction(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + personalListBean.getData().getTelephone()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (intent.getComponent() != null || intent.getAction() != null)
                        {
                            startActivity(intent);
                        }
                    } else
                    {
                        SDToast.showShort("对方未录入电话");
                    }
                } else if (content.getId() == 2)
                {
                    //发短信
                    if (personalListBean != null && personalListBean.getData() != null && StringUtils.notEmpty(personalListBean
                            .getData().getTelephone()))
                    {
                        intent.setAction(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("smsto:" + personalListBean.getData().getTelephone()));
                        if (intent.getComponent() != null || intent.getAction() != null)
                        {
                            startActivity(intent);
                        }
                    } else
                    {
                        SDToast.showShort("对方未录入电话");
                    }
                } else if (content.getId() == 3)
                {
                    if (personalListBean != null)
                    {
                        showHowAdd();
//                        PersonInfoBean personInfoBean = personalListBean.getData();
//                        PhoneUtils.getInstance().addContact(SDPersonalAlterActivity.this, "", personInfoBean
//                                .getName(), personInfoBean.getTelephone(), personInfoBean.getEmail(), "IDG资本");
                    }
                    {

                    }
                }
            }
        });
    }

    private void showHowAdd()
    {
        initAddContactsData();
        BaseDialogUtils.showListStringDialog2(SDPersonalAlterActivity.this, mListsHowAdd, new DialogStringListFragment2
                .InputListener()
        {
            @Override
            public void onData(StringList.Data content)
            {
                PersonInfoBean personInfoBean = personalListBean.getData();
                if (StringUtils.notEmpty(personInfoBean))
                {
                    if (content.getId() == 1)
                    {
                        Intent addNew = new Intent(Intent.ACTION_INSERT,
                                Uri.withAppendedPath(Uri.parse("content://com.android.contacts"), "contacts"));
                        addNew.setType("vnd.android.cursor.dir/person");
                        addNew.putExtra(ContactsContract.Intents.Insert.NAME, personInfoBean.getName());
                        addNew.putExtra(ContactsContract.Intents.Insert.PHONE, personInfoBean.getTelephone());
                        addNew.putExtra(ContactsContract.Intents.Insert.EMAIL, personInfoBean.getEmail());
                        addNew.putExtra(ContactsContract.Intents.Insert.COMPANY, "idgcapital");
                        startActivity(addNew);

                    } else if (content.getId() == 2)
                    {
                        Intent oldConstantIntent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
                        oldConstantIntent.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
                        oldConstantIntent.putExtra(ContactsContract.Intents.Insert.PHONE, personInfoBean.getTelephone());
                        oldConstantIntent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds
                                .Phone.TYPE_MOBILE);
//                        oldConstantIntent.putExtra(ContactsContract.Intents.Insert.NAME,  personInfoBean.getName());
                        oldConstantIntent.putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, 2);
                        oldConstantIntent.putExtra(ContactsContract.Intents.Insert.EMAIL, personInfoBean.getEmail());
                        oldConstantIntent.putExtra(ContactsContract.Intents.Insert.COMPANY, "idgcapital");
                        startActivity(oldConstantIntent);
                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private List<StringList.Data> mLists = new ArrayList<StringList.Data>();

    private void copy(final String info)
    {
        mLists.clear();
        StringList.Data data = new StringList.Data();
        data.setId(1);
        data.setName("复制邮箱地址");
        mLists.add(data);

        BaseDialogUtils.showListStringDialog(SDPersonalAlterActivity.this, mLists, new DialogStringListFragment
                .InputListener()
        {
            @Override
            public void onData(StringList.Data content)
            {
                if (content.getId() == 1)
                {
//                    String chatContent = SmileUtils.getSmiledText(context, info);
                    ClipboardManager cm = (ClipboardManager) SDPersonalAlterActivity.this.getSystemService(Context
                            .CLIPBOARD_SERVICE);
                    ClipData mClipData = ClipData.newPlainText("Label", info);
                    cm.setPrimaryClip(mClipData);
                    MyToast.showToast(SDPersonalAlterActivity.this, "内容已经复制到剪切板");
                }
            }
        });

    }


//
//    /**
//     * 保存方法
//     */
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
