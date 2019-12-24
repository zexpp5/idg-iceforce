package com.cxgz.activity.cx.msg.email;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.injoy.idg.R;
import com.chaoxiang.base.utils.AESUtils;
import com.cxgz.activity.cx.base.BaseActivity;
import com.cxgz.activity.cx.utils.dialog.SDStyleDialog;
import com.cxgz.activity.cx.view.FixGridLayout;
import com.cxgz.activity.cx.view.MultiImageSelectorActivity;
import com.cxgz.activity.cxim.utils.DensityUtil;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.entity.SDFileListEntity;
import com.chaoxiang.base.config.Constants;
import com.utils.FileUtil;
import com.utils.SDToast;
import com.utils.SPUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;

import paul.arian.fileselector.FileSelectionActivity;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;

/**
 * 发邮件
 *
 * @author 李文俊
 */
public class MailSendActivity extends BaseActivity implements
        SDStyleDialog.OnStyle1DialogListener
{
    private static final int REQUEST_CODE_SELECT_FILE = 826;
    private View btnAddContact, btnAddCc;
    private LinearLayout llFileList;
    private RelativeLayout btnAddFile;
    public static final String RECIPIENTS = "recipients";
    private CustomNavigatorBar mNavigatorBar;

    private FixGridLayout mLayout;
    private FixGridLayout mLayoutCc;
    List<String> names = new ArrayList<>();
    List<String> cc = new ArrayList<>();
    private EditText ed_sender;
    private EditText ed_cc;
    private boolean isChange;
    private File cameraImgPath;
    private File imgFolder;
    private List<SDFileListEntity> filePath = new ArrayList<>();

    /**
     * 图片显示区的图片集合
     */
    protected ArrayList<String> addImgPaths = new ArrayList<>();

    private EditText et_content;
    private EditText et_title;
    private String content;
    private String sendType;

    @Override
    protected void init()
    {
        mNavigatorBar = (CustomNavigatorBar) findViewById(R.id.title_bar);
        mNavigatorBar.setLeftImageOnClickListener(mOnClickListener);
        mNavigatorBar.setLeftTextOnClickListener(mOnClickListener);
        mNavigatorBar.setRightTextVisible(true);
        mNavigatorBar.setRightTextOnClickListener(mOnClickListener);
        sendType = getIntent().getStringExtra("type");

        if (sendType == null)
            mNavigatorBar.setMidText("我的邮箱");
        else if (sendType != null && sendType.equals("reply"))
            mNavigatorBar.setMidText(getString(R.string.reply));
        else
            mNavigatorBar.setMidText(getString(R.string.forward));

        btnAddContact = findViewById(R.id.btnAddContact);
        btnAddCc = findViewById(R.id.btnAddCc);
        btnAddFile = (RelativeLayout) findViewById(R.id.btnAddFile);
        llFileList = (LinearLayout) findViewById(R.id.llFileList);

        et_content = (EditText) findViewById(R.id.et_content);
        et_title = (EditText) findViewById(R.id.et_title);

        btnAddCc.setOnClickListener(this);
        btnAddContact.setOnClickListener(this);
        btnAddFile.setOnClickListener(this);

        mLayout = (FixGridLayout) findViewById(R.id.ll_contact);
        mLayoutCc = (FixGridLayout) findViewById(R.id.ll_cc);
        ed_sender = setListener(0, getString(R.string.p_semail));
        ed_cc = setListener(1, getString(R.string.p_cemail));
        String title = getIntent().getStringExtra("title");
        String recipients = getIntent().getStringExtra(RECIPIENTS);
        content = getIntent().getStringExtra("content");
        if (sendType == null || sendType != null && !sendType.equals("forward"))
        {
            if (recipients != null)
            {
                String[] recipient = recipients.split(",");
                if (recipient.length == 0)
                {
                    addEmail(0, recipients);
                }
                for (int i = 0; i < recipient.length; i++)
                {
                    addEmail(0, recipient[i]);
                }
            }
        }
        if (title != null)
        {
            if (sendType == null)
            {
            } else if (sendType != null && sendType.equals("reply"))
            {
                et_title.setText("Re：" + title);
            } else
                et_title.setText("Fwd：" + title);
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v == mNavigatorBar.getLeftImageView() || v == mNavigatorBar.getLeftText())
            {
                finish();
            }
            if (v == mNavigatorBar.getRightText())
            {
                if (!ed_sender.getText().toString().trim().equals(""))
                {
                    if (!addRec())
                    {
                        return;
                    }
                }
                if (!ed_cc.getText().toString().trim().equals(""))
                {
                    if (!addCc())
                    {
                        return;
                    }
                }
                if (et_content.getText().toString().trim().equals(""))
                {
                    SDToast.showShort(getString(R.string.p_content));
                    return;
                }
                if (et_title.getText().toString().trim().equals(""))
                {
                    SDToast.showShort(getString(R.string.p_title));
                    return;
                }
                if (names.size() == 0)
                {
                    SDToast.showShort(getString(R.string.p_semail));
                    return;
                }
                String tmpHost = SPUtils.get(MailSendActivity.this,
                        SPUtils.EMAIL_HOST, "").toString();
                if (tmpHost == null || tmpHost.equals(""))
                {
                    SDToast.showShort("请配置好邮箱再来发送。");
                    return;
                }
                MailSender sender = new MailSender(MailSendActivity.this);
                String emailContent = et_content.getText().toString().trim();
                if (sendType == null)
                {
                } else if (sendType != null && sendType.equals("reply"))
                {
                    emailContent += "<br /><br />------- 原始邮件 -------<br /><br />"
                            + content;
                } else
                {
                    emailContent += "<br /><br />------- 原始邮件 -------<br /><br />"
                            + content;
                }
                try
                {
                    if (userId == null)
                    {
                        userId = DisplayUtil.getUserInfo(MailSendActivity.this, 3);
                    }
                    String recHost = tmpHost;
                    String host = "smtp." + recHost.substring(recHost.indexOf(".") + 1, recHost.length());
                    host = "smtp-mail.outlook.com";
                    sender.startSend(host, "587",
                            AESUtils.des(userId + "!@#", (String) SPUtils.get(MailSendActivity.this, SPUtils.EMAIL_PWD, ""), Cipher.DECRYPT_MODE),
                            SPUtils.get(MailSendActivity.this, SPUtils.EMAIL_NAME, "").toString(),
                            emailContent, et_title.getText().toString().trim(), /*ed_sender.getText().toString(),*/
                            names, cc, filePath, new MailSender.Callback()
                            {
                                @Override
                                public void onSuccess()
                                {
                                    SDToast.showShort(getString(R.string.save_suceess));
                                    finish();
                                }

                                @Override
                                public void onFailed(Exception e)
                                {
                                    SDToast.showShort(getString(R.string.operation_fails));
                                }
                            });
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }
    };
    private boolean canDelete = true;
    private boolean canDeleteCc = true;

    /**
     * 设置邮箱收件人控件
     */
    private EditText setListener(final int pos, String hint)
    {
        FixGridLayout tmpLayout;
        if (pos == 0)
        {
            tmpLayout = mLayout;
        } else
        {
            tmpLayout = mLayoutCc;
        }
        final FixGridLayout layout = tmpLayout;
        EditText et = new EditText(this);
        et.setBackgroundColor(Color.TRANSPARENT);
        et.setPadding(8, 8, 8, 8);
        et.setHint(hint);
        et.setLayoutParams(new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        et.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                .getDimensionPixelSize(R.dimen.content_size));
        et.setSingleLine();
        et.addTextChangedListener(new TextWatcher()
        {
            String before = null;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after)
            {
                before = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                canDelete = false;
            }
        });
        final EditText tmpEd = et;
        layout.addView(et);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                if (isChange)
                {
                    if (layout.getLastWidth() != 0)
                    {
                        int ws = layout.getRight() - layout.getLeft()
                                - layout.getLastWidth();
                        if (ws > 100)
                            tmpEd.setLayoutParams(new RelativeLayout.LayoutParams(
                                    ws, tmpEd.getMeasuredHeight()));
                        else
                            tmpEd.setLayoutParams(new RelativeLayout.LayoutParams(
                                    LayoutParams.MATCH_PARENT,
                                    LayoutParams.WRAP_CONTENT));
                        isChange = false;
                    }
                }
            }
        });
        return et;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (event.getKeyCode() == KeyEvent.KEYCODE_DEL)
        {
            if (ed_sender.hasFocus() && !canDelete)
            {
                if (ed_sender.getText().toString().trim().equals(""))
                    canDelete = true;
            } else if (ed_cc.hasFocus() && !canDeleteCc)
            {
                if (ed_cc.getText().toString().trim().equals(""))
                    canDeleteCc = true;
            } else if (ed_sender.hasFocus()
                    && ed_sender.getText().toString().trim().equals("")
                    && delete != null)
            {
                String tmpEmail = delete.getText().toString();
                mLayout.removeView(delete);
                names.remove(tmpEmail);
                delete = null;
            } else if (ed_cc.hasFocus() && canDeleteCc && deleteCc != null)
            {
                String tmpEmail = deleteCc.getText().toString();
                mLayoutCc.removeView(deleteCc);
                cc.remove(tmpEmail);
                deleteCc = null;
            } else if (ed_sender.hasFocus()
                    && ed_sender.getText().toString().trim().equals("")
                    && delete == null)
            {
                if (mLayout.getChildCount() != 1)
                {
                    delete = (Button) mLayout.getChildAt(mLayout
                            .getChildCount() - 2);
                    delete.setBackgroundColor(getResources().getColor(
                            R.color.btn_green_noraml));
                }
            } else if (ed_cc.hasFocus()
                    && ed_cc.getText().toString().trim().equals("")
                    && deleteCc == null)
            {
                if (mLayoutCc.getChildCount() != 1)
                {
                    deleteCc = (Button) mLayoutCc.getChildAt(mLayoutCc
                            .getChildCount() - 2);
                    deleteCc.setBackgroundColor(getResources().getColor(
                            R.color.btn_green_noraml));
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view)
    {
        super.onClick(view);
        switch (view.getId())
        {
            case R.id.btnAddContact: // 选择联系人
                addRec();
                break;
            case R.id.btnAddFile: // 选择文件
                SDStyleDialog dialog = new SDStyleDialog(this, new String[]{
                        getString(R.string.p_photo),
                        getString(R.string.p_file)}, SDStyleDialog.STYLE_TWO_TYPE, this);
                dialog.showBottom();
                break;
            case R.id.btnAddCc: // 选择抄送
                addCc();
                break;
        }
    }

    /**
     * 添加抄送人
     *
     * @return
     */
    private boolean addCc()
    {
        String ccSender = ed_cc.getText().toString().trim();
        if (ccSender != null && !ccSender.equals("")
                && !cc.contains(ccSender) && !names.contains(ccSender))
        {
            addEmail(1, ccSender);
            canDeleteCc = true;
            return true;
        } else if (cc.contains(ccSender) || names.contains(ccSender))
        {
            SDToast.showShort(getString(R.string.p_email_a));
            return false;
        } else
        {
            SDToast.showShort(getString(R.string.p_email));
            return false;
        }
    }

    private boolean addRec()
    {
        String email = ed_sender.getText().toString().trim();
        if (email != null && !email.equals("") && !names.contains(email)
                && !cc.contains(email))
        {
            addEmail(0, email);
            canDelete = true;
            return true;
        } else if (names.contains(email) || cc.contains(email))
        {
            SDToast.showShort(getString(R.string.p_email_a));
            return false;
        } else
        {
            SDToast.showShort(getString(R.string.p_email));
            return false;
        }
    }

    private Button delete;

    private Button deleteCc;

    /**
     * 添加邮箱
     *
     * @param pos   0是发送人，1是抄送
     * @param email
     */
    private void addEmail(final int pos, String email)
    {
        FixGridLayout m1 = null;
        List<String> list = null;
        EditText tmped = null;
        if (pos == 0)
        {
            m1 = mLayout;
            list = names;
            tmped = ed_sender;
        } else
        {
            m1 = mLayoutCc;
            list = cc;
            tmped = ed_cc;
        }
        final EditText ed = tmped;
        Pattern pattern = Pattern
                .compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))" +
                        "([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches())
        {
            final CheckBox btn = new CheckBox(MailSendActivity.this);
            btn.setText(email);
            btn.setButtonDrawable(getResources().getDrawable(
                    android.R.color.transparent));
            btn.setTextColor(getResources().getColor(R.color.white));
            btn.setLayoutParams(new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            btn.setPadding(8, 8, 8, 8);
            btn.setBackgroundColor(getResources().getColor(R.color.blue));
            m1.addView(btn, list.size());
            list.add(email);
            ed.setText("");
            btn.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (pos == 0)
                    {
                        if (btn.equals(delete))
                        {
                            return;
                        }
                        ed.requestFocus();
                        btn.setBackgroundColor(getResources().getColor(
                                R.color.btn_green_noraml));
                        if (delete != null)
                        {
                            delete.setBackgroundColor(getResources().getColor(
                                    R.color.blue));
                        }
                        delete = btn;
                    } else
                    {
                        if (btn.equals(deleteCc))
                        {
                            return;
                        }
                        ed.requestFocus();
                        btn.setBackgroundColor(getResources().getColor(
                                R.color.btn_green_noraml));
                        if (deleteCc != null)
                        {
                            deleteCc.setBackgroundColor(getResources()
                                    .getColor(R.color.blue));
                        }
                        deleteCc = btn;
                    }
                }
            });
        } else
        {
            SDToast.showShort(getString(R.string.p_email_e));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == 0)
            {
                final List<SDUserEntity> userList = (List<SDUserEntity>) data
                        .getSerializableExtra("SelectedData");// str即为回传的值
                for (int i = 0; i < userList.size(); i++)
                {
                    // mEdReceiver.insertEditText(userList.get(i).getRealName());
                    Button btn = new Button(MailSendActivity.this);
                    btn.setText(userList.get(i).getName());
                    mLayout.addView(btn, names.size());
                    // names.add(userList.get(i).get);
                }
                isChange = true;
            } else if (requestCode == REQUEST_CODE_SELECT_FILE)
            {
                List<File> object = (List<File>) data
                        .getSerializableExtra(FileSelectionActivity.FILES_TO_UPLOAD);
                if (object.size() > 0)
                {
                    for (int i = 0; i < object.size(); i++)
                    {
                        addFile(object.get(i).toString());
                    }

                }
            } else if (requestCode == Constants.OPEN_SYSTEM_CAMERA_REQUEST_CODE)
            {
                String imgPath = cameraImgPath.getAbsolutePath();
                addFile(imgPath);
            } else if (requestCode == Constants.OPEN_SYSTEM_ABLUM_REQUEST_CODE)
            {
//				if (data != null) {
//					Uri uri = data.getData();
//					String imgPath = getAblumImgPath(uri);
//					addFile(imgPath);
//				}
                if (resultCode == RESULT_OK && data != null)
                {
                    ArrayList<String> selectedImgData = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    for (String str : selectedImgData)
                    {
                        addFile(str);
                    }
                }
            }
        }
    }

    /**
     * 添加文件
     *
     * @param imgPath
     */
    private void addFile(final String imgPath)
    {
        final SDFileListEntity entity = new SDFileListEntity();
        entity.setAndroidFilePath(imgPath);
        File file = new File(imgPath);
        entity.setEntity(this, file, "", SDFileListEntity.FILE);
        filePath.add(entity);
        FileInputStream fis = null;
        int s = 0;
        try
        {
            fis = new FileInputStream(file);
            s = fis.available();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        final View view = LayoutInflater.from(this).inflate(
                R.layout.sd_im_newmsg_file_item, null, false);
        ((TextView) view.findViewById(R.id.filename)).setText(file.getName());
        ((TextView) view.findViewById(R.id.filesize)).setText("" + FileUtil.formetFileSize(s));
        view.findViewById(R.id.cancel).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                llFileList.removeView(view);
                filePath.remove(entity);
                if (filePath != null && filePath.isEmpty())
                {
                    llFileList.setPadding(0, 0, 0, 0);
                }
            }
        });
        llFileList.setPadding(DensityUtil.dip2px(this, 12),
                DensityUtil.dip2px(this, 5), DensityUtil.dip2px(this, 12),
                DensityUtil.dip2px(this, 5));
        llFileList.addView(view);
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.sd_im_newmail_activity;
    }

    @Override
    public void clickCancelListener(View v)
    {

    }

    @Override
    public void itemClickListener(View parent, View view, int position, long id)
    {
        switch (position)
        {
            /*case 0: // 拍照
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
                cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(cameraImgPath));
                startActivityForResult(cameraIntent,
                        Constants.OPEN_SYSTEM_CAMERA_REQUEST_CODE);
                break;*/
            case 0:// 选择照片
                Intent pictureIntent = new Intent(MailSendActivity.this, MultiImageSelectorActivity.class);
                // 是否显示拍摄图片
                pictureIntent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
                // 最大可选择图片数量
                pictureIntent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
                pictureIntent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, addImgPaths);
                pictureIntent.putExtra(MultiImageSelectorActivity.ORIGINAL, false);
                pictureIntent.putExtra(MultiImageSelectorActivity.ORIGINAL_SHOW, false);
                // 选择模式
                startActivityForResult(pictureIntent, Constants.OPEN_SYSTEM_ABLUM_REQUEST_CODE);
                break;
            case 1: // 选择文件
                FileUtil.startFilePicker(this, REQUEST_CODE_SELECT_FILE, null);
                break;
        }
    }
}
