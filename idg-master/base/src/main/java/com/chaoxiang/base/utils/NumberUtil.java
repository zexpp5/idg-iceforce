package com.chaoxiang.base.utils;

/**
 * User: Selson
 * Date: 2016-08-30
 * FIXME
 */

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;

public class NumberUtil
{

    private static final int DEF_DIV_SCALE = 2;

    /**
     * 使用java正则表达式去掉多余的.与0
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 判断字符串是否为数字
     *
     * @param num
     * @return
     */
    public static boolean isNumberic(String num)
    {
        return (null == num || num.length() <= 0 || num.matches("\\d{1,}")) ? true : false;
    }

    /**
     * @return 返回N位随机数
     */
    public static String randomNumber(int numSize)
    {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < numSize; i++)
        {
            result += random.nextInt(10);
        }
        System.out.print(result);
        return result;
    }

//    /**
//     * @return 返回纯数字和字母的GUID
//     */
//    public static String randomUUID()
//    {
//        return GuidUtil.randomGuidUtil().toString().replaceAll("-", "");
//    }
//
//    /**
//     * @param parm
//     * @return 返回指定位数的GUID
//     */
//    public static String randomUUID(int parm)
//    {
//        return GuidUtil.randomGuidUtil().toString().replaceAll("-", "").substring(0, parm);
//    }

    /**
     * * 两个Double数相加 *
     *
     * @param v1 *
     * @param v2 *
     * @return Double
     */
    public static Double add(Double v1, Double v2)
    {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return new Double(b1.add(b2).doubleValue());
    }

    /**
     * * 两个Double数相减 *
     *
     * @param v1 *
     * @param v2 *
     * @return Double
     */
    public static Double sub(Double v1, Double v2)
    {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return new Double(b1.subtract(b2).doubleValue());
    }

    /**
     * * 两个Double数相乘 *
     *
     * @param v1 *
     * @param v2 *
     * @return Double
     */
    public static Double mul(Double v1, Double v2)
    {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return new Double(b1.multiply(b2).doubleValue());
    }

    /**
     * * 两个Double数相除 *
     *
     * @param v1 *
     * @param v2 *
     * @return Double
     */
    public static Double div(Double v1, Double v2)
    {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return new Double(b1
                .divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP)
                .doubleValue());
    }

    /**
     * * 两个Double数相除，并保留scale位小数 *
     *
     * @param v1    *
     * @param v2    *
     * @param scale *
     * @return Double
     */
    public static Double div(Double v1, Double v2, int scale)
    {
        if (scale < 0)
        {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return new Double(b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP)
                .doubleValue());
    }

    /**
     * @param v1
     * @return 返回指定Double的负数
     */
    public static Double neg(Double v1)
    {
        return sub(new Double(0), v1);
    }


    /**
     * 返回金额的总数
     *
     * @param moneyTotal
     * @return
     */
    public static String reTurnNumber(double moneyTotal)
    {
        String moneyTotalString = String.valueOf(moneyTotal);
        BigDecimal bd;
        String resultString;
        if (moneyTotalString.indexOf("E") > 0)
        {
//            是科学计数法
            bd = new BigDecimal(moneyTotalString);
            resultString = bd.toPlainString();
        } else
        {
            resultString = (String) new DecimalFormat("#0.00").format(moneyTotal);
//           是科学小数
        }
        return resultString;
    }

    public static String reTurnNumber4(double moneyTotal)
    {
        String moneyTotalString = String.valueOf(moneyTotal);
        BigDecimal bd;
        String resultString;
        if (moneyTotalString.indexOf("E") > 0)
        {
//            是科学计数法
            bd = new BigDecimal(moneyTotalString);
            resultString = bd.toPlainString();
        } else
        {
            resultString = (String) new DecimalFormat("#0.0000").format(moneyTotal);
//           是科学小数
        }
        return resultString;
    }

    public static String reTurnNumber6(double moneyTotal)
    {
        String moneyTotalString = String.valueOf(moneyTotal);
        BigDecimal bd;
        String resultString;
        if (moneyTotalString.indexOf("E") > 0)
        {
//            是科学计数法
            bd = new BigDecimal(moneyTotalString);
            resultString = bd.toPlainString();
        } else
        {
            resultString = (String) new DecimalFormat("#0.000000").format(moneyTotal);
//           是科学小数
        }
        return resultString;
    }

    /**
     * 返回金额的总数
     * @param moneyTotal
     * @return
     */
    public static String reTurnNumber(String moneyTotal)
    {
        BigDecimal bd;
        String resultString;
        if (moneyTotal.indexOf("E") > 0)
        {
//            是科学计数法
            bd = new BigDecimal(moneyTotal);
            resultString = bd.toPlainString();
        } else
        {
            resultString = (String) new DecimalFormat("#0.00").format( Double.parseDouble(moneyTotal));
//           是科学小数
        }

        return resultString;
    }

    /**
     * 返回金额的总数lzk
     *
     * @param moneyTotal
     * @return
     */
    public static String reTurnNumberLzk(String moneyTotal)
    {
        String moneyTotalString = moneyTotal;
        BigDecimal bd;
        String resultString;
        if (moneyTotalString.indexOf("E") > 0)
        {
//            是科学计数法
            bd = new BigDecimal(moneyTotalString);
//        System.out.println(bd.toPlainString());.
            resultString = bd.toPlainString();
        } else
        {
            resultString = moneyTotalString;
        }
        return resultString;


    }

    /**
     * 返回金额的总数
     *
     * @param moneyTotal
     * @return
     */
    public static String reTurnNumber(Double moneyTotal)
    {
        String resultString;
        resultString = (String) new DecimalFormat("#0.00").format(new BigDecimal(moneyTotal));
        return resultString;
    }
}