package com.entity.gztutils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.bean_erp.CustomerTypeBean;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.http.HttpURLUtil;
import com.chaoxiang.base.config.Constants;
import com.utils.SDToast;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;

/**
 * 关炸天坑爹包
 * Created by cx on 2016/6/24.
 */
public class ZTUtils
{
    public static void moduleTipsDialog(Context context, int type)
    {
        ModuleTipsDialog moduleDialog = new ModuleTipsDialog(context, type);
    }

    public static boolean checkSelectParameterIsNull(EditText e, String string)
    {
//        if (TextUtils.isEmpty(e.getText().toString())) {
//            SDToast.showShort(string);
//            return true;
//        }
//    }
        return false;
    }

    public static void checkExistFile(CustomerTypeBean bean, List<String> imgPaths, List<File> files, File voice, TextView textView, Drawable nav_up)
    {
        nav_up.setBounds(0, 0, 14, 14);
        boolean isFileEmpty;
        if (bean != null)
            isFileEmpty = true;
        else if (!(imgPaths == null || imgPaths.size() == 0) || !(files == null || files.size() == 0) || !(voice == null))
            isFileEmpty = true;
        else
            isFileEmpty = false;
        if (textView != null)
        {
            if (isFileEmpty)
            {
                textView.setText("有");
                textView.setCompoundDrawables(null, null, nav_up, null);
            } else
            {
                textView.setText("无");
                textView.setCompoundDrawables(null, null, null, null);
            }
        }
    }

    public static void checkExistFile(List<String> imgPaths, List<File> files, File voice, TextView textView, Drawable nav_up)
    {
        nav_up.setBounds(0, 0, 14, 14);
        boolean isFileEmpty;
        if (!(imgPaths == null || imgPaths.size() == 0) || !(files == null || files.size() == 0) || !(voice == null))
            isFileEmpty = true;
        else
            isFileEmpty = false;
        if (textView != null)
        {
            if (isFileEmpty)
            {
                textView.setText("有");
                textView.setCompoundDrawables(null, null, nav_up, null);
            } else
            {
                textView.setText("无");
                textView.setCompoundDrawables(null, null, null, null);
            }
        }
    }

    public static void checkExistFile(CustomerTypeBean bean, List<String> imgPaths, List<File> files, File voice, TextView textView)
    {
        boolean isFileEmpty;
        if (bean != null)
            isFileEmpty = true;
        else if (!(imgPaths == null || imgPaths.size() == 0) || !(files == null || files.size() == 0) || !(voice == null))
            isFileEmpty = true;
        else
            isFileEmpty = false;
        if (textView != null)
        {
            if (isFileEmpty)
                textView.setText("有");
            else
                textView.setText("无");
        }
    }

    public static void checkExistFile(List<String> imgPaths, List<File> files, File voice, TextView textView)
    {
        boolean isFileEmpty;
        if (!(imgPaths == null || imgPaths.size() == 0) || !(files == null || files.size() == 0) || !(voice == null))
            isFileEmpty = true;
        else
            isFileEmpty = false;
        if (textView != null)
        {
            if (isFileEmpty)
                textView.setText("有");
            else
                textView.setText("无");
        }
    }

    /**
     * 判断年月日不小于当前时间
     */
    public static boolean checkSmallDate(String a[], int y, int m, int d)
    {
        if (y >= Integer.parseInt(a[0]))
        {
            if ((m + 1) > Integer.parseInt(a[1]))
            {
                return true;
            }
            if ((m + 1) == Integer.parseInt(a[1]))
            {
                if (d >= Integer.parseInt(a[2]))
                {
                    return true;
                } else
                {
                    SDToast.showLong("不能小于当前日");
                    return false;
                }
            } else
            {
                SDToast.showLong("不能小于当前月");
                return false;
            }
        } else
        {
            SDToast.showLong("不能小于当前年");
            return false;
        }
    }

    /**
     * 判断年月日不大于当前时间
     */
    public static boolean checkAndDate(String a[], int y, int m, int d)
    {
        if (y <= Integer.parseInt(a[0]))
        {
            if ((m + 1) < Integer.parseInt(a[1]))
            {
                return true;
            }
            if ((m + 1) == Integer.parseInt(a[1]))
            {
                if (d <= Integer.parseInt(a[2]))
                {
                    return true;
                } else
                {
                    SDToast.showLong("不能大于当前日");
                    return false;
                }
            } else
            {
                SDToast.showLong("不能大于当前月");
                return false;
            }
        } else
        {
            SDToast.showLong("不能大于当前年");
            return false;
        }
    }

    /**
     * 判断起始时间不大于结束时间
     */
    public static boolean checkStartTime(String startTime, String endTime)
    {
        startTime = startTime.trim();
        endTime = endTime.trim();
        String a[] = startTime.split("-");
        String b[] = endTime.split("-");
        if (Integer.parseInt(a[0]) > Integer.parseInt(b[0]))
        {
            SDToast.showShort("差旅起始时间不能大于结束时间");

            return true;
        } else
        {
            if (Integer.parseInt(a[0]) == Integer.parseInt(b[0]))
            {
                if (Integer.parseInt(a[1]) > Integer.parseInt(b[1]))
                {
                    SDToast.showShort("差旅起始时间不能大于结束时间");
                    return true;
                } else
                {
                    if (Integer.parseInt(a[1]) == Integer.parseInt(b[1]))
                    {
                        if (Integer.parseInt(a[2]) > Integer.parseInt(b[2]))
                        {
                            SDToast.showShort("差旅起始时间不能大于结束时间");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断起始时间不大于结束时间
     */
    public static boolean checkStartTime2(String startTime, String endTime)
    {
        startTime = startTime.trim();
        endTime = endTime.trim();
        String a[] = startTime.split("-");
        String b[] = endTime.split("-");
        if (Integer.parseInt(a[0]) > Integer.parseInt(b[0]))
        {
            SDToast.showShort("起始时间不能大于结束时间");
            return true;
        } else
        {
            if (Integer.parseInt(a[0]) == Integer.parseInt(b[0]))
            {
                if (Integer.parseInt(a[1]) > Integer.parseInt(b[1]))
                {
                    SDToast.showShort("起始时间不能大于结束时间");
                    return true;
                } else
                {
                    if (Integer.parseInt(a[1]) == Integer.parseInt(b[1]))
                    {
                        if (Integer.parseInt(a[2]) > Integer.parseInt(b[2]))
                        {
                            SDToast.showShort("起始时间不能大于结束时间");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断立项时间不大于启动时间
     */
    public static boolean checkStartTime3(String startTime, String endTime)
    {
        startTime = startTime.trim();
        endTime = endTime.trim();
        String a[] = startTime.split("-");
        String b[] = endTime.split("-");
        if (Integer.parseInt(a[0]) > Integer.parseInt(b[0]))
        {
            SDToast.showShort("立项时间不能大于启动时间");
            return true;
        } else
        {
            if (Integer.parseInt(a[0]) == Integer.parseInt(b[0]))
            {
                if (Integer.parseInt(a[1]) > Integer.parseInt(b[1]))
                {
                    SDToast.showShort("立项时间不能大于启动时间");
                    return true;
                } else
                {
                    if (Integer.parseInt(a[1]) == Integer.parseInt(b[1]))
                    {
                        if (Integer.parseInt(a[2]) > Integer.parseInt(b[2]))
                        {
                            SDToast.showShort("立项时间不能大于启动时间");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断double是否相等
     */
    public static boolean doubleEquation(double num1, double num2)
    {
        if ((num1 - num2 > -0.001) && (num1 - num2) < 0.001) return true;
        else return false;
    }

    /**
     * 判断edittext是否超出两位小数
     */
    public static TextWatcher doubleEdittext = new TextWatcher()
    {
        public void afterTextChanged(Editable edt)
        {
            String temp = edt.toString();
            int posDot = temp.indexOf(".");
            if (posDot <= 0) return;
            if (temp.length() - posDot - 1 > 2)
            {
                edt.delete(posDot + 3, posDot + 4);
            }
        }

        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
        {
        }

        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
        {
        }
    };

    /**
     * 判断是否两位小数 , 是就复制给edt不是就给个0.0
     */
    public static void decimal_2(double temp, TextView edt)
    {
        if ((temp < 0.0001) && (temp > -0.0001))
        {
            edt.setText(0.00 + "");
        } else
        {
            BigDecimal tempString = new BigDecimal(temp);
            tempString = tempString.setScale(2, BigDecimal.ROUND_HALF_UP);
            edt.setText(tempString + "");
        }
    }

    /**
     * 金额输入框中的内容限制（最大：小数点前13位，小数点后2位）
     */
    public static boolean checkDecimalString13_2(String s)
    {
        if (StringUtils.notEmpty(s))
        {
            return s.matches("^\\d{1,9}$|^\\d{1,13}[.]\\d{1,2}$");
        } else
        {
            return false;
        }
    }

    /**
     * 判断edittext整数不超过3位 , 小数不超过1位
     */
    public static InputFilter lengthFilter = new InputFilter()
    {
        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend)
        {
            // source:当前输入的字符
            // start:输入字符的开始位置
            // end:输入字符的结束位置
            // dest：当前已显示的内容
            // dstart:当前光标开始位置
            // dent:当前光标结束位置
            Log.i("", "source=" + source + ",start=" + start + ",end=" + end
                    + ",dest=" + dest.toString() + ",dstart=" + dstart
                    + ",dend=" + dend);
            if (dest.length() == 0 && source.equals("."))
            {
                return "0.";
            }
            String dValue = dest.toString();
            int posDot = dValue.indexOf(".");
            String[] splitArray = dValue.split("\\.");
            if (splitArray.length > 0)
            {
                String dotValue = splitArray[0];
                if (dotValue.length() == 13)
                {//整数位数
                    if (source.equals(".") && dend >= 2)
                    {//判断标点是否在最后或者倒数第一位
                        return null;
                    } else if (!(source.equals(".")) && dend == 4)
                    {//判断标点后是否是数字和是否最后一位
                        return null;
                    }
                    return "";
                }
            }
            if (splitArray.length > 1)
            {
                String dotValue = splitArray[1];
                if (dotValue.length() == 2)
                {//小数位数
                    return "";
                }
            }
            return null;
        }

    };

    /**
     * 判断手机号码正确性
     */
    public static boolean isMobileNO(String mobiles)
    {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    不知道那里又加了个17的 , sun the dog
    */
        String telRegex = "[1][3578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    /**
     * 判断审批是否需要选择审批人
     */
    public static boolean checkSelectApprovalPerson(int isAgree, List<SDUserEntity> selectedPersonData)
    {
        if (isAgree == 1)
        {//如果同意
            if (!(selectedPersonData.size() > 0))
            {//必须选择审批人
                SDToast.showShort("请选择审批人");
                return true;
            }
        } else
        {//如果不同意
            if (selectedPersonData.size() > 0)
            {//不能选择审批人
                SDToast.showShort("不能选择审批人");
                return true;
            }
        }
        return false;
    }

    public static void checkButton(String total, BaseAdapter adapter, int pageNumber, TextView bottomLeftBtn, TextView bottomRightBtn)
    {
        int index = 0;
        if (!TextUtils.isEmpty(total))
        {
            if (Integer.parseInt(total) % 15 == 0)
            {
                index = Integer.parseInt(total) / 15;
            } else
            {
                index = (Integer.parseInt(total) / 15) + 1;
            }
        }
        if (adapter != null)
        {
            int totalPage = index;
            if (pageNumber == totalPage)
            {//当前页数等于总页数
                if (pageNumber == 1)
                    bottomLeftBtn.setEnabled(false);//第一页
                else
                    bottomLeftBtn.setEnabled(true);//非第一页
                bottomRightBtn.setEnabled(false);
            } else if (pageNumber < totalPage)
            {//当前页数小于总页数
                if (pageNumber == 1)
                    bottomLeftBtn.setEnabled(false);//第一页
                else
                    bottomLeftBtn.setEnabled(true);//非第一页
                bottomRightBtn.setEnabled(true);
            } else
            {
                bottomLeftBtn.setEnabled(false);
                bottomRightBtn.setEnabled(false);
            }
        }
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static void showShareDialog(final String url, final String shareId, String title, String text, final Activity context, final OnSelectShareToListener mOnSelectShareToListener)
    {
        if (TextUtils.isEmpty(title))
            title = "   ";
        if (TextUtils.isEmpty(text))
            text = "   ";
        final Dialog dialog = new Dialog(context);
        final String shareUrl;
        if (null == url)
        {
            shareUrl = HttpURLUtil.newInstance().append("sale_cusAnn").append("share").append(shareId + ".htm").toString();
        } else if ("HXKH".equals(shareId))
        {//核心客户屁股后面加了东西
            shareUrl = /*HttpURLUtil.newInstance().toString()*/ Constants.getShareUrl() + url + ".htm?s_cusCore=2";
        } else
        {
            shareUrl = /*HttpURLUtil.newInstance().toString()*/ Constants.getShareUrl() + url + ".htm";
        }
        SDLogUtil.debug("shareUrl-----" + shareUrl);
        ShareDialog(shareUrl, shareId, title, text, context, mOnSelectShareToListener);

    }

    public static void showShareDialog(final int numInt, final String url, final String shareId, String title, String text, final Activity context, final OnSelectShareToListener mOnSelectShareToListener)
    {
        if (TextUtils.isEmpty(title))
            title = "   ";
        if (TextUtils.isEmpty(text))
            text = "   ";
        final Dialog dialog = new Dialog(context);
        final String shareUrl;
        if (null == url)
        {
            shareUrl = HttpURLUtil.newInstance().append("sale_cusAnn").append("share").append(shareId + ".htm").toString();
        } else if ("HXKH".equals(shareId))
        {//核心客户屁股后面加了东西
            shareUrl = /*HttpURLUtil.newInstance().toString()*/ Constants.getShareUrl() + url + ".htm?s_cusCore=2";
        } else
        {
            shareUrl = /*HttpURLUtil.newInstance().toString()*/ Constants.getShareUrl() + url + ".htm";
        }
        SDLogUtil.debug("shareUrl-----" + shareUrl);
        ShareDialog(numInt, shareUrl, shareId, title, text, context, mOnSelectShareToListener);

    }

    /**
     * 物流单独URL的分享
     *
     * @param url
     * @param shareId
     * @param title
     * @param text
     * @param context
     * @param mOnSelectShareToListener
     */
    public static void showLogisticShareDialog(final String url, final String shareId, String title, String text, final Activity context, final OnSelectShareToListener mOnSelectShareToListener)
    {

//        final String shareUrl;
//        if (null == url)
//        {
//            shareUrl = HttpURLUtil.newInstance().append("sale_cusAnn").append("share").append(shareId + ".htm").toString();
//        } else
//        {
//            shareUrl = /*HttpURLUtil.newInstance().toString()*/ com.chaoxiang.base.config.Constants.shareUrl + url + ".htm";
//        }

        ShareDialog(url, shareId, title, text, context, mOnSelectShareToListener);

    }


    //带搜索的分享
    public static void showShareDialog(final String url, final String searchkey, String searchValue, String shareId, String title, String text, final Activity context, final OnSelectShareToListener mOnSelectShareToListener)
    {
        try
        {
            final String shareUrl;
            if (null == url)
            {
                shareUrl = HttpURLUtil.newInstance().append("sale_cusAnn").append("share").append(shareId + ".htm?type=crm").toString();
            } else
            {
                String searchString = "";
                if (searchValue != null && !TextUtils.isEmpty(searchValue))
                {
                    searchString = "&" + searchkey + "=" + URLEncoder.encode(URLEncoder.encode(searchValue, "UTF-8"), "UTF-8");
                }

                shareUrl = /*HttpURLUtil.newInstance().toString()*/Constants.getShareUrl() + url + ".htm?type=crm" + searchString;
                SDLogUtil.debug("shareId-----" + shareId);
                SDLogUtil.debug("shareUrl-----" + shareUrl);
                ShareDialog(shareUrl, shareId, title, text, context, mOnSelectShareToListener);
            }
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }


    }

    //带搜索的分享
    public static void showShareDialog(final String url,
                                       final String searchkey, String searchValue,
                                       final String searchkey2, String searchValue2,
                                       String shareId, String title, String text,
                                       final Activity context, final OnSelectShareToListener mOnSelectShareToListener)
    {
        try
        {
            final String shareUrl;
            if (null == url)
            {
                shareUrl = HttpURLUtil.newInstance().append("sale_cusAnn").append("share").append(shareId + ".htm").toString();
            } else
            {
                String searchString = "";
                if (searchValue != null && !TextUtils.isEmpty(searchValue))
                {
                    searchString = "&" + searchkey + "=" + URLEncoder.encode(URLEncoder.encode(searchValue, "UTF-8"), "UTF-8");
                }

                String searchString2 = "";
                if (searchValue2 != null && !TextUtils.isEmpty(searchValue2))
                {
                    searchString2 = "&" + searchkey2 + "=" + URLEncoder.encode(URLEncoder.encode(searchValue2, "UTF-8"), "UTF-8");
                }

                shareUrl = /*HttpURLUtil.newInstance().toString()*/Constants.getShareUrl() + url + ".htm?type=crm" + searchString2 + searchString;
                SDLogUtil.debug("shareUrl-----" + shareUrl);
                ShareDialog(shareUrl, shareId, title, text, context, mOnSelectShareToListener);
            }
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }

    private static void ShareDialog(final String url, final String shareId, String title, String text, final Activity context, final OnSelectShareToListener mOnSelectShareToListener)
    {
//        if (TextUtils.isEmpty(title))
//            title = "   ";
//        if (TextUtils.isEmpty(text))
//            text = "   ";
//        final Dialog dialog = new Dialog(context);
//        final ShareAction shareAction = new ShareAction(context)
//                .withTitle(title)
//                .withText(text)
//                .withTargetUrl(url)
//                .withMedia(new UMImage(context, BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher)))
//                .setCallback(new UMShareListener()
//                {
//
//                    @Override
//                    public void onResult(SHARE_MEDIA share_media)
//                    {
//                        Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
//                        if (null != mOnSelectShareToListener)
//                            mOnSelectShareToListener.onSelectShareToOnResult(share_media);
//                        dialog.dismiss();
//                    }
//
//                    @Override
//                    public void onError(SHARE_MEDIA share_media, Throwable throwable)
//                    {
//                        LogUtils.e("shareError----------" + throwable.getLocalizedMessage());
//                        Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
//                        if (null != mOnSelectShareToListener)
//                            mOnSelectShareToListener.onSelectShareToOnError(share_media, throwable);
//                    }
//
//                    @Override
//                    public void onCancel(SHARE_MEDIA share_media)
//                    {
////                        Toast.makeText(context, "取消分享", Toast.LENGTH_SHORT).show();
//                        if (null != mOnSelectShareToListener)
//                            mOnSelectShareToListener.onSelectShareToOnCancel(share_media);
//                        dialog.dismiss();
//                    }
//                });
//        View contentView = LayoutInflater.from(context).inflate(R.layout.customer_notification_share_dialog, null);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(contentView);
//        dialog.setCanceledOnTouchOutside(true);
//        LinearLayout getifu = (LinearLayout) contentView.findViewById(R.id.getifu);
//        LinearLayout weixin = (LinearLayout) contentView.findViewById(R.id.weixin);
//        LinearLayout dingdangxiang = (LinearLayout) contentView.findViewById(R.id.dingdangxiang);
//        LinearLayout qq = (LinearLayout) contentView.findViewById(R.id.qq);
//        RelativeLayout share_cancel = (RelativeLayout) contentView.findViewById(R.id.share_cancel);
//        final String finalText = text;
//        final String finalTitle = title;
//        getifu.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                //个体富进销存
//                Intent intent = new Intent(context, ContactShareActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString(ContactShareActivity.SHARE_CONTENT, finalText);
//                bundle.putString(ContactShareActivity.SHARE_TITLE, finalTitle);
//                bundle.putString(ContactShareActivity.SHARE_URL, url);
//                bundle.putString(ContactShareActivity.SHARE_ICON_URL, Constants.IM_IMG_SHARE_FOR_IM);
//                bundle.putString(ContactShareActivity.SHARE_APP_NAME, AppInfoUtils.getAppName(context));
//
//                intent.putExtras(bundle);
//                context.startActivity(intent);
//
//                dialog.dismiss();
//                if (null != mOnSelectShareToListener)
//                    mOnSelectShareToListener.onSelectShareToOnShareDialogCancel();
//
//            }
//        });
//        weixin.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                shareAction.setPlatform(SHARE_MEDIA.WEIXIN).share();
//            }
//        });
//        dingdangxiang.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                //叮当享
//            }
//        });
//        qq.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                shareAction.setPlatform(SHARE_MEDIA.QQ).share();
//            }
//        });
//
//        share_cancel.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                dialog.dismiss();
//                if (null != mOnSelectShareToListener)
//                    mOnSelectShareToListener.onSelectShareToOnShareDialogCancel();
//            }
//        });
//
//
//        int isPay = (int) SPUtils.get(context, SPUtils.ISPAY, 0);
//        if (isPay == PublicAPI.PAY)
//        {//付费用户直接提交,否则弹限制提交框
//            dialog.show();
//        } else
//        {
//            FunctionLimitDialog exampleAccountDialog = new FunctionLimitDialog(context);
//            exampleAccountDialog.setOnExampleAccountDialogListener(new FunctionLimitDialog.OnExampleAccountDialogListener()
//            {
//                @Override
//                public void onCancel(DialogInterface dialog)
//                {
//                    dialog.dismiss();
//                    Intent intent = new Intent();
//                    intent.setClass(context, PayActivity.class);
//                    context.startActivity(intent);
//                }
//
//                @Override
//                public void onSure(DialogInterface dialog)
//                {
//                    dialog.dismiss();
//                }
//            });
//            exampleAccountDialog.show();
//        }
    }

    private static void ShareDialog(final int numGone, final String url, final String shareId, String title, String text, final Activity context, final OnSelectShareToListener mOnSelectShareToListener)
    {
//        if (TextUtils.isEmpty(title))
//            title = "   ";
//        if (TextUtils.isEmpty(text))
//            text = "   ";
//        final Dialog dialog = new Dialog(context);
//        final ShareAction shareAction = new ShareAction(context)
//                .withTitle(title)
//                .withText(text)
//                .withTargetUrl(url)
//                .withMedia(new UMImage(context, BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher)))
//                .setCallback(new UMShareListener()
//                {
//
//                    @Override
//                    public void onResult(SHARE_MEDIA share_media)
//                    {
//                        Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
//                        if (null != mOnSelectShareToListener)
//                            mOnSelectShareToListener.onSelectShareToOnResult(share_media);
//                        dialog.dismiss();
//                    }
//
//                    @Override
//                    public void onError(SHARE_MEDIA share_media, Throwable throwable)
//                    {
//                        LogUtils.e("shareError----------" + throwable.getLocalizedMessage());
//                        Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
//                        if (null != mOnSelectShareToListener)
//                            mOnSelectShareToListener.onSelectShareToOnError(share_media, throwable);
//                    }
//
//                    @Override
//                    public void onCancel(SHARE_MEDIA share_media)
//                    {
////                        Toast.makeText(context, "取消分享", Toast.LENGTH_SHORT).show();
//                        if (null != mOnSelectShareToListener)
//                            mOnSelectShareToListener.onSelectShareToOnCancel(share_media);
//                        dialog.dismiss();
//                    }
//                });
//        View contentView = LayoutInflater.from(context).inflate(R.layout.customer_notification_share_dialog, null);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(contentView);
//        dialog.setCanceledOnTouchOutside(true);
//        LinearLayout getifu = (LinearLayout) contentView.findViewById(R.id.getifu);
//
//        LinearLayout weixin = (LinearLayout) contentView.findViewById(R.id.weixin);
//
//        LinearLayout dingdangxiang = (LinearLayout) contentView.findViewById(R.id.dingdangxiang);
//        LinearLayout qq = (LinearLayout) contentView.findViewById(R.id.qq);
//
//        RelativeLayout share_cancel = (RelativeLayout) contentView.findViewById(R.id.share_cancel);
//        final String finalText = text;
//        final String finalTitle = title;
//
//        if (numGone == 1)
//        {
//            weixin.setVisibility(View.GONE);
//        }
//        if (numGone == 2)
//        {
//            qq.setVisibility(View.GONE);
//        }
//        if (numGone == 3)
//        {
//            getifu.setVisibility(View.GONE);
//        }
//        getifu.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                //个体富进销存
//                Intent intent = new Intent(context, ContactShareActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString(ContactShareActivity.SHARE_CONTENT, finalText);
//                bundle.putString(ContactShareActivity.SHARE_TITLE, finalTitle);
//                bundle.putString(ContactShareActivity.SHARE_URL, url);
//                bundle.putString(ContactShareActivity.SHARE_ICON_URL, Constants.IM_IMG_SHARE_FOR_IM);
//                bundle.putString(ContactShareActivity.SHARE_APP_NAME, AppInfoUtils.getAppName(context));
//
//                intent.putExtras(bundle);
//                context.startActivity(intent);
//
//                dialog.dismiss();
//                if (null != mOnSelectShareToListener)
//                    mOnSelectShareToListener.onSelectShareToOnShareDialogCancel();
//
//            }
//        });
//        weixin.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                shareAction.setPlatform(SHARE_MEDIA.WEIXIN).share();
//            }
//        });
//        dingdangxiang.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                //叮当享
//            }
//        });
//        qq.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                shareAction.setPlatform(SHARE_MEDIA.QQ).share();
//            }
//        });
//
//        share_cancel.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                dialog.dismiss();
//                if (null != mOnSelectShareToListener)
//                    mOnSelectShareToListener.onSelectShareToOnShareDialogCancel();
//            }
//        });
//
//
//        int isPay = (int) SPUtils.get(context, SPUtils.ISPAY, 0);
//        if (isPay == PublicAPI.PAY)
//        {//付费用户直接提交,否则弹限制提交框
//            dialog.show();
//        } else
//        {
//            FunctionLimitDialog exampleAccountDialog = new FunctionLimitDialog(context);
//            exampleAccountDialog.setOnExampleAccountDialogListener(new FunctionLimitDialog.OnExampleAccountDialogListener()
//            {
//                @Override
//                public void onCancel(DialogInterface dialog)
//                {
//                    dialog.dismiss();
//                    Intent intent = new Intent();
//                    intent.setClass(context, PayActivity.class);
//                    context.startActivity(intent);
//                }
//
//                @Override
//                public void onSure(DialogInterface dialog)
//                {
//                    dialog.dismiss();
//                }
//            });
//            exampleAccountDialog.show();
//        }
    }

    public interface OnSelectShareToListener
    {
//        void onSelectShareToOnResult(SHARE_MEDIA share_media);
//
//        void onSelectShareToOnError(SHARE_MEDIA share_media, Throwable throwable);
//
//        void onSelectShareToOnCancel(SHARE_MEDIA share_media);
//
//        void onSelectShareToOnCopay();
//
//        void onSelectShareToOnShareDialogCancel();
    }
}
