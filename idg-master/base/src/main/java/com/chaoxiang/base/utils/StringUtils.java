package com.chaoxiang.base.utils;

import android.util.Log;
import android.util.Patterns;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;

import java.io.InputStream;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * User: Selson
 * Date: 2016-04-20
 * FIXME
 */
public class StringUtils
{

    private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    public static boolean isEmpty(final Object o)
    {
        return toString(o).trim().length() == 0;
    }

    public static String toString(final Object o)
    {
        return toString(o, "");
    }

    public static String toString(final Object o, final String def)
    {
        return o == null ? def : o instanceof InputStream ? toString((InputStream) o)
                : o instanceof Reader ? toString((Reader) o) : o instanceof Object[] ? StringUtils.join(", ",
                (Object[]) o) : o instanceof Collection ? StringUtils.join(", ", (Collection<?>) o) : o
                .toString();
    }

    public static <T> String join(final String delimiter, final Collection<T> objs)
    {
        if (objs == null || objs.isEmpty())
            return "";

        final Iterator<T> iter = objs.iterator();
        final StringBuilder buffer = new StringBuilder(StringUtils.toString(iter.next()));

        while (iter.hasNext())
        {
            final T obj = iter.next();
            if (notEmpty(obj))
                buffer.append(delimiter).append(StringUtils.toString(obj));
        }
        return buffer.toString();
    }

    public static <T> String join(final String delimiter, final T... objects)
    {
        return join(delimiter, Arrays.asList(objects));
    }

    public static boolean isURL(String url)
    {
        boolean isURL = false;
        if (empty(url))
        {
            return false;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO)
        {
            isURL = Patterns.WEB_URL.matcher(url).matches();
        } else
        {
            Pattern urlPattern = Pattern.compile("[http|https]+[://]+[0-9A-Za-z:/[-]_#[?][=][.][&]]*");
            isURL = urlPattern.matcher(url).matches();
        }
        return isURL;
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email)
    {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * 去掉字符串的首尾空格
     *
     * @param str
     * @return
     */
    public static String spaceEdit(String str)
    {
        int start = 0, end = str.length() - 1;
        while (start <= end && str.charAt(start) == ' ')
            start++;
        while (start <= end && str.charAt(end) == ' ')
            end--;
        return str.substring(start, end + 1);
    }

    /**
     * 判断是不是一个合法的国内手机号码
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles)
    {
//        String regex = "^(((86|\\+86|0|)1[3456789][0-9]{9})|(\\d{3,4}-(\\d{7,8})))$";
        String regex = "^1[0-9]{10}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 判断是不是中文地址
     *
     * @param address
     * @return
     */
    public static boolean isAddress(String address)
    {
        String regex = "^[\u4e00-\u9fa5]{2}[\\w]*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(address);
        return m.matches();
    }

    /**
     * 判断是不是中文
     *
     * @param text
     * @return
     */
    public static boolean isChinese(String text)
    {
        String regex = "^[\u4e00-\u9fa5]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        if (m.matches())
        {
            return true;
        }
        return false;
    }

    /**
     * 判断是不是中文姓名
     *
     * @param name
     * @return
     */
    public static boolean isChineseName(String name)
    {
        String regex = "^[\u4e00-\u9fa5]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(name);
        if (m.matches())
        {
            String surnames =
                    "赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻柏水窦章云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞任袁柳酆鲍史唐费廉岑薛雷贺倪汤滕殷罗毕郝邬安常乐于时傅皮卞齐康伍余元卜顾孟平黄和穆萧尹姚邵湛汪祁毛禹狄米贝明臧计伏成戴谈宋茅庞熊纪舒屈项祝董梁杜阮蓝闵席季麻强贾路娄危江童颜郭梅盛林刁锺徐邱骆高夏蔡田樊胡凌霍虞万支柯昝管卢莫经房裘缪干解应宗丁宣贲邓郁单杭洪包诸左石崔吉钮龚程嵇邢滑裴陆荣翁荀羊於惠甄麴家封芮羿储靳汲邴糜松井段富巫乌焦巴弓牧隗山谷车侯宓蓬全郗班仰秋仲伊宫宁仇栾暴甘钭历戎祖武符刘景詹束龙叶幸司韶郜黎蓟溥印宿白怀蒲邰从鄂索咸籍赖卓蔺屠蒙池乔阳郁胥能苍双闻莘党翟谭贡劳逄姬申扶堵冉宰郦雍却璩桑桂濮牛寿通边扈燕冀僪浦尚农温别庄晏柴瞿阎充慕连茹习宦艾鱼容向古易慎戈廖庾终暨居衡步都耿满弘匡国文寇广禄阙东欧殳沃利蔚越夔隆师巩厍聂晁勾敖融冷訾辛阚那简饶空曾毋沙乜养鞠须丰巢关蒯相查后荆红游竺权逮盍益桓公万俟司马上官欧阳夏侯诸葛闻人东方赫连皇甫尉迟公羊澹台公冶宗政濮阳淳于单于太叔申屠公孙仲孙轩辕令狐钟离宇文长孙慕容司徒司空召有舜叶赫那拉丛岳寸贰皇侨彤竭端赫实甫集象翠狂辟典良函芒苦其京中夕之章佳那拉冠宾香果依尔根觉罗依尔觉罗萨嘛喇赫舍里额尔德特萨克达钮祜禄他塔喇喜塔腊讷殷富察叶赫那兰库雅喇瓜尔佳舒穆禄爱新觉罗索绰络纳喇乌雅范姜碧鲁张廖张简图门太史公叔乌孙完颜马佳佟佳富察费莫蹇称诺来多繁戊朴回毓税荤靖绪愈硕牢买但巧枚撒泰秘亥绍以壬森斋释奕姒朋求羽用占真穰翦闾漆贵代贯旁崇栋告休褒谏锐皋闳在歧禾示是委钊频嬴呼大威昂律冒保系抄定化莱校么抗祢綦悟宏功庚务敏捷拱兆丑丙畅苟随类卯俟友答乙允甲留尾佼玄乘裔延植环矫赛昔侍度旷遇偶前由咎塞敛受泷袭衅叔圣御夫仆镇藩邸府掌首员焉戏可智尔凭悉进笃厚仁业肇资合仍九衷哀刑俎仵圭夷徭蛮汗孛乾帖罕洛淦洋邶郸郯邗邛剑虢隋蒿茆菅苌树桐锁钟机盘铎斛玉线针箕庹绳磨蒉瓮弭刀疏牵浑恽势世仝同蚁止戢睢冼种涂肖己泣潜卷脱谬蹉赧浮顿说次错念夙斯完丹表聊源姓吾寻展出不户闭才无书学愚本性雪霜烟寒少字桥板斐独千诗嘉扬善揭祈析赤紫青柔刚奇拜佛陀弥阿素长僧隐仙隽宇祭酒淡塔琦闪始星南天接波碧速禚腾潮镜似澄潭謇纵渠奈风春濯沐茂英兰檀藤枝检生折登驹骑貊虎肥鹿雀野禽飞节宜鲜粟栗豆帛官布衣藏宝钞银门盈庆喜及普建营巨望希道载声漫犁力贸勤革改兴亓睦修信闽北守坚勇汉练尉士旅五令将旗军行奉敬恭仪母堂丘义礼慈孝理伦卿问永辉位让尧依犹介承市所苑杞剧第零谌招续达忻六鄞战迟候宛励粘萨邝覃辜初楼城区局台原考妫纳泉老清德卑过麦曲竹百福言第五佟爱年笪谯哈墨南宫赏伯佴佘牟商西门东门左丘梁丘琴后况亢缑帅微生羊舌海归呼延南门东郭百里钦鄢汝法闫楚晋谷梁宰父夹谷拓跋壤驷乐正漆雕公西巫马端木颛孙子车督仉司寇亓官鲜于锺离盖逯库郏逢阴薄厉稽闾丘公良段干开光操瑞眭泥运摩伟铁迮付";
            if (surnames.indexOf(name.substring(0, 1)) != -1)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是不是一个合法的momoka's pn
     *
     * @param pn
     * @return
     */
    public static boolean isPN(String pn)
    {
        pn = pn.replace(" ", "");
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(pn);
        return m.matches();
    }

    /**
     * 全角转半角
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input)
    {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++)
        {
            if (c[i] == 12288)
            {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 半角转全角
     *
     * @param input
     * @return
     */
    /*
     * public static String ToSBC(String input) { char[] c =
	 * input.toCharArray(); for (int i = 0; i < c.length; i++) { if (c[i] == 32)
	 * { c[i] = (char) 12288; continue; } if (c[i] < 127) c[i] = (char) (c[i] +
	 * 65248); } return new String(c); }
	 */

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue)
    {
        try
        {
            return Integer.parseInt(str);
        } catch (Exception e)
        {
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj)
    {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj)
    {
        try
        {
            return Long.parseLong(obj);
        } catch (Exception e)
        {
        }
        return 0;
    }

    /**
     * 字符串转布尔值
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b)
    {
        try
        {
            return Boolean.parseBoolean(b);
        } catch (Exception e)
        {
        }
        return false;
    }

    /**
     * 控制TextView的显示行数
     *
     * @param view
     * @param maxLine
     */
    public static void truncate(final TextView view, final int maxLine)
    {
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener()
        {
            public void onGlobalLayout()
            {
                if (view.getLineCount() > maxLine)
                {
                    int lineEndIndex = view.getLayout().getLineEnd(maxLine - 1);
                    String text = view.getText().subSequence(0, lineEndIndex - 3) + "...";
                    view.setText(text);
                }
            }
        });
    }

    public static String unescape(String s)
    {
        while (true)
        {
            int n = s.indexOf("&#");
            if (n < 0)
                break;
            int m = s.indexOf(";", n + 2);
            if (m < 0)
                break;
            try
            {
                s = s.substring(0, n) + (char) (Integer.parseInt(s.substring(n + 2, m))) + s.substring(m + 1);
            } catch (Exception e)
            {
                Log.i("StringUtils", e.toString());
                return s;
            }
        }
        s = s.replace("&quot;", "\"");
        s = s.replace("&lt;", "<");
        s = s.replace("&gt;", "<");
        s = s.replace("&amp;", "&");
        s = s.replace("&nbsp;", " ");
        return s;
    }

    public static String unescapeSpace(String s)
    {
        return s.replaceAll("&nbsp;", "");
    }

    public static String getAlpha(String str)
    {
        if (str == null)
        {
            return "~";
        }
        if (str.trim().length() == 0)
        {
            return "~";
        }
        char c = str.trim().substring(0, 1).charAt(0);
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches())
        {
            return (c + "").toUpperCase();
        } else
        {
            return "~";
        }
    }

    /**
     * 处理空字符串
     *
     * @param str
     * @param defaultValue
     * @return String
     */
    public static String doEmpty(String str, String defaultValue)
    {
        if (defaultValue == null)
        {
            defaultValue = "";
        }
        if (str == null || str.equalsIgnoreCase("null") || str.trim().equals("") || str.trim().equals("－请选择－"))
        {
            str = defaultValue;
        } else if (str.startsWith("null"))
        {
            str = str.substring(4, str.length());
        }
        return str.trim();
    }

    /**
     * 请选择
     */
    final static String PLEASE_SELECT = "请选择...";

    /*
     * 判断不为空
     */
    public static boolean notEmpty(Object o)
    {
        return o != null && !"".equals(o.toString().trim()) && !"null".equalsIgnoreCase(o.toString().trim())
                && !"undefined".equalsIgnoreCase(o.toString().trim()) && !PLEASE_SELECT.equals(o.toString().trim());
    }

    /*
     * 判断为空 为真=true，否则=false
     */
    public static boolean empty(Object o)
    {
        return o == null || "".equals(o.toString().trim()) || "null".equalsIgnoreCase(o.toString().trim())
                || "undefined".equalsIgnoreCase(o.toString().trim()) || PLEASE_SELECT.equals(o.toString().trim());
    }

    /**
     * 判断字符串 转换为数字 是否为真/假
     *
     * @param
     * @return
     * @Title: num
     */
    public static boolean num(Object o)
    {
        int n = 0;
        try
        {
            n = Integer.parseInt(o.toString().trim());
        } catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        if (n > 0)
        {
            return true;
        } else
        {
            return false;
        }
    }

    public static boolean decimal(Object o)
    {
        double n = 0;
        try
        {
            n = Double.parseDouble(o.toString().trim());
        } catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        if (n > 0.0)
        {
            return true;
        } else
        {
            return false;
        }
    }

    /**
     * 给JID返回用户名
     *
     * @param Jid
     * @return
     */
    public static String getUserNameByJid(String Jid)
    {
        if (empty(Jid))
        {
            return null;
        }
        if (!Jid.contains("@"))
        {
            return Jid;
        }
        return Jid.split("@")[0];
    }

    /**
     * 给用户名返回JID
     *
     * @param jidFor   域名//如ahic.com.cn
     * @param userName
     * @return
     */
    public static String getJidByName(String userName, String jidFor)
    {
        if (empty(jidFor) || empty(jidFor))
        {
            return null;
        }
        return userName + "@" + jidFor;
    }

    /**
     * 给用户名返回JID,使用默认域名ahic.com.cn
     *
     * @param userName
     * @return
     */
    public static String getJidByName(String userName)
    {
        String jidFor = "ahic.com.cn";
        return getJidByName(userName, jidFor);
    }

    /*
     * 插入到指定位置
     */
    public static String insertStringPosition(String s1, String s2, int m)
    {
        StringBuilder sb = new StringBuilder(s2);
        sb.insert(m, s1);
        return sb.toString();
    }

    /*
     * 判断字符串src是否包含一些字符 dest
	 */
    public static boolean containsString(String src, String dest)
    {
        boolean flag = false;
        if (src.contains(dest))
        {
            flag = true;
        }
        return flag;
    }

    /*
     * 判断字符串是否包含一些字符 indexOf
     */
    public static boolean indexOfString(String src, String dest)
    {
        boolean flag = false;
        if (src.indexOf(dest) != -1)
        {
            flag = true;
        }
        return flag;
    }

    /**
     * @param
     * @param string
     * @param i
     * @return 去除s字符串中的有这个string的第i个的。
     */
    public String remove(String s, String string, int i)
    {
        if (i == 1)
        {
            int j = s.indexOf(string);
            s = s.substring(0, j) + s.substring(j + 1);
            i--;
            return s;
        } else
        {
            int j = s.indexOf(string);
            i--;
            return s.substring(0, j + 1) + remove(s.substring(j + 1), string, i);
        }
    }

    /**
     * 去除最后一位。指定的
     *
     * @param soureString
     * @param tagString
     * @return
     */
    public static String removeLastString(String soureString, char tagString)
    {
        String resultString = "";
        if (StringUtils.notEmpty(soureString))
        {
            if (soureString.charAt(soureString.length() - 1) == tagString)
            {
                resultString = soureString.substring(0, soureString.length() - 1);
            }
        }
        return StringUtils.empty(resultString) ? soureString : resultString;
    }

    /**
     * @param s
     */
    public static boolean checkDecimalString46(String s)
    {
        if (notEmpty(s))
        {
            return s.matches("^([1-9]\\d{0,3}|0)(\\.\\d{1,6})?$");
        } else
        {
            return false;
        }
    }

    //9.2
    public static boolean checkDecimalString92(String s)
    {
        if (notEmpty(s))
        {
            //限定从1到9,
            return s.matches("^([1-9]\\d{0,8}|0)(\\.\\d{1,2})?$");
        } else
        {
            return false;
        }
    }

    //微信号码验证
    public static boolean checkWeiXin(String str)
    {
        if (notEmpty(str))
        {
            String wx = "^[a-zA-Z]{1}[-_a-zA-Z0-9]{5,19}+$";
            return str.matches(wx);
        } else
        {
            return false;
        }
    }

    //QQ号码验证
    public static boolean checkQQ(String str)
    {
        if (notEmpty(str))
        {
            String qq = "[1-9][0-9]{4,14}";
            return str.matches(qq);
        } else
        {
            return false;
        }
    }

    /**
     * 验证给定的字符串是否是身份证号
     * <br>
     * <br>身份证15位编码规则：dddddd yymmdd xx p
     * <br>dddddd：6位地区编码
     * <br>yymmdd：出生年(两位年)月日，如：910215
     * <br>xx：顺序编码，系统产生，无法确定
     * <br>p：性别，奇数为男，偶数为女
     * <br>
     * <br>
     * <br>身份证18位编码规则：dddddd yyyymmdd xxx y
     * <br>dddddd：6位地区编码
     * <br>yyyymmdd：出生年(四位年)月日，如：19910215
     * <br>xxx：顺序编码，系统产生，无法确定，奇数为男，偶数为女
     * <br>y：校验码，该位数值可通过前17位计算获得
     * <br>前17位号码加权因子为 Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ]
     * <br>验证位 Y = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ]
     * <br>如果验证码恰好是10，为了保证身份证是十八位，那么第十八位将用X来代替 校验位计算公式：Y_P = mod( ∑(Ai×Wi),11 )
     * <br>i为身份证号码1...17 位; Y_P为校验码Y所在校验码数组位置
     *
     * @param str
     * @return
     */
    public static boolean checkIdCard(String str)
    {
        if (notEmpty(str))
        {
            String idCard = "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|" +
                    "(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)$";
            return str.matches(idCard);
        } else
        {
            return false;
        }
    }


    public static boolean checkDecimalString64(String s)
    {
        if (notEmpty(s))
        {
            return s.matches("^([1-9]\\d{0,5}|0)(\\.\\d{1,4})?$");
        } else
        {
            return false;
        }
    }

    public static boolean checkDecimalNewString42(String s)
    {
        if (notEmpty(s))
        {
            //限定从1到9,
            return s.matches("^([1-9]\\d{0,3}|0)(\\.\\d{1,2})?$");
        } else
        {
            return false;
        }
    }

    public static boolean checkDecimalNewString64(String s)
    {
        if (notEmpty(s))
        {
            //限定从1到9,
            return s.matches("^([1-9]\\d{0,5}|0)(\\.\\d{1,4})?$");
        } else
        {
            return false;
        }
    }

    public static boolean checkDecimalString66(String s)
    {
        if (notEmpty(s))
        {
            //限定从1到9,
            return s.matches("([1-9]\\d{0,5}|0)(\\.\\d{1,6})?$");
        } else
        {
            return false;
        }
    }

    public static boolean checkDecimalString13_2(String s)
    {
        if (notEmpty(s))
        {
            //限定从1-9位,
            return s.matches("([1-9]\\d{0,12}|0)(\\.\\d{1,2})?$");
        } else
        {
            return false;
        }
    }

    public static String getPhoneString(String str)
    {
        String result = "";
//        if (str != null)
//            if (str.indexOf("_") != -1)
//            {
//                result = str.substring(0, str.indexOf("_"));
//            } else
//            {
//                result = str;
//            }
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        result = m.replaceAll("").trim();
        return result;
    }

    //提取数字
    public static String getNumberForName(String target)
    {
        String a = "";
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(target);
        a = m.replaceAll("").trim();
        return a;
    }

    public static String append(String append)
    {
        if (StringUtils.notEmpty(append))
        {
            StringBuffer buffer = new StringBuffer("");
            buffer.append("/" + append.trim().toString());
            return buffer.toString();
        } else
        {
            return "";
        }

    }

    public static String appendDH(List<String> list)
    {
        StringBuffer buf = new StringBuffer();
        if (list != null)
            for (int i = 0; i < list.size(); i++)
            {
                buf.append(list.get(i)).append(",");
            }

        if (buf.length() > 0)
        {
            buf.replace(buf.length() - 1, buf.length(), "");
        }
        return buf.toString();
    }

    /**
     * @param trage     原字符串
     * @param cutString 间隔标识
     * @return
     */
    public static String[] CutString(String trage, String cutString)
    {
        String s = new String(trage);
        String a[] = s.split(cutString);
        return a;
    }

    //String-->UniCode
    public static String stringToUnicode(String string)
    {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++)
        {
            // 取出每一个字符
            char c = string.charAt(i);
            // 转换为unicode
            //"\\u只是代号，请根据具体所需添加相应的符号"
            unicode.append("\\u" + Integer.toHexString(c));
        }
        return unicode.toString();
    }

    //UniCode-->String
    public static String unicodeToString(String unicode)
    {
        StringBuffer string = new StringBuffer();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++)
        {
            // 转换
            int data = Integer.parseInt(hex[i], 16);
            // 拼接成string
            string.append((char) data);
        }

        return string.toString();
    }


}
