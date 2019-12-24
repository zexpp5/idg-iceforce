package yunjing.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.db.dao.SDAllConstactsDao;
import com.cxgz.activity.db.dao.SDAllConstactsEntity;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.injoy.idg.R;
import com.utils.SPUtils;
import com.view.LoginOutDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import newProject.bean.CCBean;

import static com.injoy.idg.R.id.money;
import static com.utils.SPUtils.S_FINGERPRINTLOGIN;
import static com.utils.SPUtils.USER_ACCOUNT;


public class DisplayUtil
{
    public static String mAppNameTake = "IDG-";
    public static int mExplainLenght = 500;
    public static int mHeightSpace = 170;
    public static SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat mFormatterSubmit = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static int mTitleColor = 0xff35343a;
    public static int[] mPressResource = {R.mipmap.state_one_press, R.mipmap.state_two_press,
            R.mipmap.state_three_press, R.mipmap.state_four_press,
            R.mipmap.state_five_press, R.mipmap.state_six_press};
    public static int[] mNormalResource = {R.mipmap.state_one_normal, R.mipmap.state_two_normal,
            R.mipmap.state_three_normal, R.mipmap.state_four_normal,
            R.mipmap.state_five_normal, R.mipmap.state_six_normal};

    private FingerprintManagerCompat mFingerManger;
    private KeyguardManager mKeyManger;

    public static float sDensity;
    public static float sDensityDpi;
    public static int sScreenW;
    public static int sScreenH;
    public static ActivityManager sActivityManager;
    public static int sRelativeScreenW = 720;
    public static int sRelativeScreenH = 1280;

    public static void init(Activity activiy)
    {
        Display dis = activiy.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        dis.getMetrics(dm);
        int h = dis.getHeight();
        int w = dis.getWidth();
        sScreenW = w < h ? w : h;
        sScreenH = w < h ? h : w;
        sDensity = dm.density;
        sDensityDpi = dm.densityDpi;
        sActivityManager = (ActivityManager) activiy.getSystemService(Activity.ACTIVITY_SERVICE);
    }

    public static int getScreenW()
    {
        return sScreenW;
    }

    public static int getScreenH()
    {
        return sScreenH;
    }

    public static int getRealPixel2(int pxSrc)
    {
        int pix = (int) (pxSrc * sScreenW / sRelativeScreenW);
        if (pxSrc == 1 && pix == 0)
        {
            pix = 1;
        }
        return pix;
    }

    public static int dp2px(Context context, float dp)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 判断是否有网络
     *
     * @param context
     * @return
     */
    public static boolean hasNetwork(Context context)
    {
        if (context == null)
            return false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null)
        {
            if (info.isAvailable() == true)
                return true;
        }
        return false;
    }

    /**
     * 返回当前系统日期
     *
     * @return
     */
    public static String getTime()
    {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(now);

    }

    /**
     * 过滤表情
     *
     * @param context
     * @return
     */
    public static InputFilter getFilter(final Context context)
    {
        InputFilter emojiFilter = new InputFilter()
        {
            Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                    Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
            {
                Matcher emojiMatcher = emoji.matcher(source);
                if (emojiMatcher.find())
                {
                    MyToast.showToast(context, "不支持输入表情");
                    return "";
                }
                return null;
            }
        };

        return emojiFilter;
    }


    /**
     * 设置EditText可输入和不可输入状态
     *
     * @param editText
     * @param editable
     */

    public static void editTextable(EditText editText, boolean editable)
    {
        if (!editable)
        { // disable editing
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
            editText.setClickable(false); //
        } else
        { // enable editing
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.setClickable(true);
        }
    }

    /**
     * @param context
     * @param imAccount 0为关闭，1为打开，2为默认没有
     * @param meetid
     * @return
     */
    public static String getMeetingOnOff(Context context, String imAccount, String meetid)
    {
        String onOroff = "2";
        if (StringUtils.notEmpty(imAccount) && StringUtils.notEmpty(meetid))
        {
            onOroff = (String) SPUtils.get(context, imAccount + "_" + meetid, "2");
        }
        return onOroff;
    }

    /**
     * 获取用户头像，姓名，部门名称
     * @param context
     * @param which   0用户头像，1用户名字，2公司名称，3 用户id
     *                4用户等级，5im账号，6部门id，7用户职位
     * @return
     */
    public static String getUserInfo(Context context, int which)
    {
        if (which == 0)
        {
            String avatar = (String) SPUtils.get(context, SPUtils.USER_ICON, "");
            return avatar;
        } else if (which == 1)
        {
            String name = (String) SPUtils.get(context, SPUtils.USER_NAME, "");  //真名
            return name;
        } else if (which == 2)
        {
            String department = (String) SPUtils.get(context, SPUtils.COMPANY_DP_NAME, "");
            return department;
        } else if (which == 3)
        {
            String userId = (String) SPUtils.get(context, SPUtils.USER_ID, "");
            return userId;
        } else if (which == 4)
        {
            String level = (String) SPUtils.get(context, SPUtils.LEVEL, "3");
            String levelStr = level + "";
            return levelStr;
        } else if (which == 5)
        {   //imaccount
            String account = (String) SPUtils.get(context, SPUtils.IM_NAME, "");   //IM账号，一般用于通讯录
            return account;
        } else if (which == 6)
        {
            String dpId = (String) SPUtils.get(context, SPUtils.COMPANY_DP_ID, "");
            return dpId;
        } else if (which == 7)
        {
            String job = (String) SPUtils.get(context, SPUtils.COMPANY_JOB, "");
            return job;
        } else if (which == 8)
        {
            String account = (String) SPUtils.get(context, SPUtils.ACCOUNT, "");
            return account;
        } else if (which == 9)
        {
            String tmpInt = (String) SPUtils.get(context, SPUtils.IS_ANNUAL_TEMP, "");
            return tmpInt;
        }
        else if (which == 10)
        {
            String token = (String) SPUtils.get(context, SPUtils.ACCESS_TOKEN, "");
            return token;
        }
        else if (which == 11)
        {
            String loginUserName = (String) SPUtils.get(context, SPUtils.USER_ACCOUNT, "");  //登录账号
            return loginUserName;
        }

        return "";
    }

    public static void putMeetingOnOff(Context context, String imAccount, String meetid, String open)
    {
        SPUtils.put(context, imAccount + "_" + meetid, open);
    }

    /**
     * 判断有没有打开定位
     *
     * @param context
     * @return
     */
    public static boolean getLocatin(Context context)
    {
        boolean isShowLocation = false;
        String l = (String) SPUtils.get(context, SPUtils.LEVEL, ""); ////1=专属定制关闭；2=专属定制开启
        if (l.equals("2"))
        {
            String f = (String) SPUtils.get(context, SPUtils.S_LOCATION, "");
            if (f.equals("1"))
            {
                isShowLocation = true;
            } else
            {
                isShowLocation = false;
            }
        } else
        {
            isShowLocation = false;
        }

        return isShowLocation;
    }

    /**
     * 判断有没有打开已读未读
     *
     * @param context
     * @return
     */
    public static boolean getRead(Context context)
    {
        boolean isShowLocation = false;
        String l = (String) SPUtils.get(context, SPUtils.LEVEL, ""); ////1=专属定制关闭；2=专属定制开启
        if (l.equals("2"))
        {
            String f = (String) SPUtils.get(context, SPUtils.S_READ, "");
            if (f.equals("1"))
            {
                isShowLocation = true;
            } else
            {
                isShowLocation = false;
            }
        } else
        {
            isShowLocation = false;
        }

        return isShowLocation;
    }


    /**
     * 判断是否打开指纹界面
     *
     * @param context
     * @return
     */
    public static boolean getOpenZW(Context context)
    {
        boolean isShowZhiWen = false;
        if ((boolean) SPUtils.get(context, SPUtils.IS_FIRST_START, false))
        {
            String l = (String) SPUtils.get(context, SPUtils.LEVEL, ""); ////1=专属定制关闭；2=专属定制开启
            if (l.equals("2"))
            {
                String f = (String) SPUtils.get(context, SPUtils.S_FINGERPRINTLOGIN, "");
                if (f.equals("1"))
                {
                    isShowZhiWen = true;
                } else
                {
                    isShowZhiWen = false;
                }
            } else
            {
                isShowZhiWen = false;
            }
        } else
        {
            isShowZhiWen = false;
        }

        return isShowZhiWen;
    }

    public static String getInviteUrl(Activity activity)
    {
        String goUrl = (String) SPUtils.get(activity, SPUtils.INVITE_URL, "").toString();
        String userId = (String) SPUtils.get(activity, SPUtils.USER_ID, "").toString();
        if (StringUtils.notEmpty(goUrl))
        {
//            String a = "{" + "userId}";
            goUrl = goUrl.replaceAll("\\{", "im");
            goUrl = goUrl.replaceAll("\\}", "im").toString().trim();
            goUrl = goUrl.replaceAll("imuserIdim", userId);
        } else
        {
            goUrl = "http://wimapi.chinacloudapp.cn/wim-api/system/toInvitation/" + userId + ".htm";
        }
        return goUrl;
    }

    /**
     * 转抄送
     *
     * @param copyLists
     * @return
     * @throws Exception
     */
    public static String copyListToStringArray(List<CCBean> copyLists) throws Exception
    {
        JSONArray array = new JSONArray();
        if (copyLists == null)
        {
            return null;
        }
        if (copyLists.size() > 0)
        {
            for (int i = 0; i < copyLists.size(); i++)
            {
                JSONObject object = new JSONObject();
                object.put("name", copyLists.get(i).getName());
                object.put("eid", copyLists.get(i).getEid());
                object.put("imAccount", copyLists.get(i).getImAccount());
                array.put(object);
            }
        } else
        {
            return null;
        }

        return array.toString();
    }

    /**
     * 审批
     *
     * @param copyLists
     * @return
     * @throws Exception
     */
    public static String approvalListToStringArray(List<CCBean> copyLists) throws Exception
    {
        JSONArray array = new JSONArray();
        if (copyLists == null)
        {
            return null;
        }
        if (copyLists.size() > 0)
        {
            for (int i = 0; i < copyLists.size(); i++)
            {
                JSONObject object = new JSONObject();
                object.put("name", copyLists.get(i).getName());
                object.put("job", copyLists.get(i).getJob());
                object.put("no", copyLists.get(i).getNo());
                object.put("userId", copyLists.get(i).getUserId());
                array.put(object);
            }
        } else
        {
            return null;
        }

        return array.toString();
    }

    /**
     * 收软键盘
     *
     * @param context
     */
    public static void hideInputSoft(Context context)
    {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
        {
            imm.hideSoftInputFromWindow(((Activity) context).getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    /**
     * 权限提示框
     *
     * @param context
     */
    public static void showTipsDialog(Context context)
    {
        LoginOutDialog.TispDialog(context, new LoginOutDialog.loginOutListener()
        {
            @Override
            public void setTrue()
            {

            }

            @Override
            public void setCancle()
            {

            }
        }, "提 示", context.getResources().getString(R.string.menu_permssion_hint));
    }


    /**
     * 返回权限、硬件的判断
     *
     * @return
     */
    public boolean judgePermission(Activity activity)
    {
        mFingerManger = FingerprintManagerCompat.from(activity);
        mKeyManger = (KeyguardManager) activity.getSystemService(Context.KEYGUARD_SERVICE);
        //判断版本是否大于等于23
        if (Build.VERSION.SDK_INT < 23)
        {
            return false;
        }
        //硬件是否支持指纹识别
        if (!mFingerManger.isHardwareDetected())
        {
            return false;
        }
        //是否已经录入指纹
        if (!mFingerManger.hasEnrolledFingerprints())
        {
            return false;
        }
        //手机是否开启锁屏密码
        if (!mKeyManger.isKeyguardSecure())
        {
            return false;
        }

        return true;
    }

    public static String timesTwo(String time)
    {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        String times = sdr.format(new Date(lcc));
        return times;
    }

    public static String getName(String account, Context context)
    {
        String name = "";
        SDAllConstactsEntity sdAllConstactsEntity = SDAllConstactsDao.getInstance().findAllConstactsByAccount(account);
        if (sdAllConstactsEntity != null && sdAllConstactsEntity.getName() != null)
        {
            name = sdAllConstactsEntity.getName();
        }
        return name;
    }


}
