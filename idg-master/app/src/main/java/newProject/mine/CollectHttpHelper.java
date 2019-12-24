package newProject.mine;

import android.content.Context;
import android.text.TextUtils;

import com.chaoxiang.base.utils.StringUtils;
import com.erp_https.BaseAPI;
import com.http.HttpURLUtil;
import com.http.SDHttpHelper;
import com.http.callback.SDRequestCallBack;
import com.lidroid.xutils.http.RequestParams;

import org.apache.http.message.BasicNameValuePair;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by Administrator on 2017/10/26.
 */

public class CollectHttpHelper extends BaseAPI
{

    /**
     * 7个列表
     *
     * @param which
     * @param pageNumber
     * @param mHttpHelper
     * @param callBack
     */
    public static void getSevenList(int which, String pageNumber, SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = "";
        if (which == 0)
        {
            url = HttpURLUtil.newInstance().append("companyAccount").append("list").append(pageNumber).toString();
        } else if (which == 1)
        {
            url = HttpURLUtil.newInstance().append("invoice").append("list").append(pageNumber).toString();
        } else if (which == 2)
        {
            url = HttpURLUtil.newInstance().append("companyAddress").append("list").append(pageNumber).toString();
        } else if (which == 3)
        {
            url = HttpURLUtil.newInstance().append("personageCard").append("list").append(pageNumber).toString();
        } else if (which == 4)
        {
            url = HttpURLUtil.newInstance().append("idNumber").append("list").append(pageNumber).toString();
        } else if (which == 5)
        {
            url = HttpURLUtil.newInstance().append("logiAddress").append("list").append(pageNumber).toString();
        } else if (which == 6)
        {
            url = HttpURLUtil.newInstance().append("other").append("list").append(pageNumber).toString();
        }
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, false, callBack);
    }

    //公司账号
    public static void commitAccount(String companyName, String openBank, String accountNum, String annex,
                                     String eid, SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("companyAccount").append("save").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(companyName))
        {
            pairs.add(new BasicNameValuePair("companyName", companyName));
        }
        if (!TextUtils.isEmpty(openBank))
        {
            pairs.add(new BasicNameValuePair("openBank", openBank));
        }
        if (!TextUtils.isEmpty(accountNum))
        {
            pairs.add(new BasicNameValuePair("accountNum", accountNum));
        }
        if (!TextUtils.isEmpty(annex))
        {
            pairs.add(new BasicNameValuePair("annex", annex));
        }
        if (!TextUtils.isEmpty(eid))
        {
            pairs.add(new BasicNameValuePair("eid", eid));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    //公司账号详情
    public static void getAccount(String eid, SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("companyAccount").append("detail").append(eid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.get(url, params, true, callBack);
    }


    //开票
    public static void commitTicket(String companyName, String openBank, String taxNumber,
                                    String billingAddress, String account, String telephone, String fax,
                                    String annex, String eid, SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("invoice").append("save").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(companyName))
        {
            pairs.add(new BasicNameValuePair("companyName", companyName));
        }
        if (!TextUtils.isEmpty(openBank))
        {
            pairs.add(new BasicNameValuePair("openBank", openBank));
        }
        if (!TextUtils.isEmpty(taxNumber))
        {
            pairs.add(new BasicNameValuePair("taxNumber", taxNumber));
        }
        if (!TextUtils.isEmpty(billingAddress))
        {
            pairs.add(new BasicNameValuePair("invoiceAddress", billingAddress));
        }
        if (!TextUtils.isEmpty(account))
        {
            pairs.add(new BasicNameValuePair("account", account));
        }
        if (!TextUtils.isEmpty(telephone))
        {
            pairs.add(new BasicNameValuePair("telephone", telephone));
        }
        if (!TextUtils.isEmpty(fax))
        {
            pairs.add(new BasicNameValuePair("fax", fax));
        }
        if (!TextUtils.isEmpty(annex))
        {
            pairs.add(new BasicNameValuePair("annex", annex));
        }
        if (!TextUtils.isEmpty(eid))
        {
            pairs.add(new BasicNameValuePair("eid", eid));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    //开票详情
    public static void getTicket(String eid, SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("invoice").append("detail").append(eid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.get(url, params, true, callBack);
    }


    //公司地址
    public static void commitAddress(String companyName, String address, String telephone, String annex,
                                     String eid, SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("companyAddress").append("save").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(companyName))
        {
            pairs.add(new BasicNameValuePair("companyName", companyName));
        }
        if (!TextUtils.isEmpty(address))
        {
            pairs.add(new BasicNameValuePair("address", address));
        }
        if (!TextUtils.isEmpty(telephone))
        {
            pairs.add(new BasicNameValuePair("telephone", telephone));
        }
        if (!TextUtils.isEmpty(annex))
        {
            pairs.add(new BasicNameValuePair("annex", annex));
        }
        if (!TextUtils.isEmpty(eid))
        {
            pairs.add(new BasicNameValuePair("eid", eid));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    //公司地址详情
    public static void getAddress(String eid, SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("companyAddress").append("detail").append(eid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.get(url, params, true, callBack);
    }

    //个人卡号
    public static void commitCard(String card, String bank, String telephone, String annex,
                                  String eid, SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("personageCard").append("save").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(card))
        {
            pairs.add(new BasicNameValuePair("card", card));
        }
        if (!TextUtils.isEmpty(bank))
        {
            pairs.add(new BasicNameValuePair("bank", bank));
        }
        if (!TextUtils.isEmpty(telephone))
        {
            pairs.add(new BasicNameValuePair("telephone", telephone));
        }
        if (!TextUtils.isEmpty(annex))
        {
            pairs.add(new BasicNameValuePair("annex", annex));
        }
        if (!TextUtils.isEmpty(eid))
        {
            pairs.add(new BasicNameValuePair("eid", eid));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    //个人卡号详情
    public static void getCard(String eid, SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("personageCard").append("detail").append(eid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.get(url, params, true, callBack);
    }

    //证件号码
    public static void commitIdNumber(String idNumber, String type, String annex,
                                      String eid, SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("idNumber").append("save").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(idNumber))
        {
            pairs.add(new BasicNameValuePair("idNumber", idNumber));
        }
        if (!TextUtils.isEmpty(type))
        {
            pairs.add(new BasicNameValuePair("type", type));
        }
        if (!TextUtils.isEmpty(annex))
        {
            pairs.add(new BasicNameValuePair("annex", annex));
        }
        if (!TextUtils.isEmpty(eid))
        {
            pairs.add(new BasicNameValuePair("eid", eid));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    //证件号码详情
    public static void getIdNumber(String eid, SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("idNumber").append("detail").append(eid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.get(url, params, true, callBack);
    }

    //物流地址
    public static void commitLogistic(String addressee, String telephone, String receiveAddress, String annex,
                                      String eid, SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("logiAddress").append("save").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(addressee))
        {
            pairs.add(new BasicNameValuePair("addressee", addressee));
        }
        if (!TextUtils.isEmpty(receiveAddress))
        {
            pairs.add(new BasicNameValuePair("receiveAddress", receiveAddress));
        }
        if (!TextUtils.isEmpty(telephone))
        {
            pairs.add(new BasicNameValuePair("telephone", telephone));
        }
        if (!TextUtils.isEmpty(annex))
        {
            pairs.add(new BasicNameValuePair("annex", annex));
        }
        if (!TextUtils.isEmpty(eid))
        {
            pairs.add(new BasicNameValuePair("eid", eid));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    //物流地址详情
    public static void getLogistic(String eid, SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("logiAddress").append("detail").append(eid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.get(url, params, true, callBack);
    }

    //其他
    public static void commitOther(String title, String remark, String annex,
                                   String eid, SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("other").append("save").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(title))
        {
            pairs.add(new BasicNameValuePair("title", title));
        }
        if (!TextUtils.isEmpty(remark))
        {
            pairs.add(new BasicNameValuePair("remark", remark));
        }
        if (!TextUtils.isEmpty(annex))
        {
            pairs.add(new BasicNameValuePair("annex", annex));
        }
        if (!TextUtils.isEmpty(eid))
        {
            pairs.add(new BasicNameValuePair("eid", eid));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    //其他详情
    public static void getOther(String eid, SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("other").append("detail").append(eid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.get(url, params, true, callBack);
    }

    //报销审批数字
    public static void getBxNum(Context context, SDRequestCallBack callBack)
    {
        SDHttpHelper mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("cost/approve/list/count")
                .toString();
        RequestParams params = new RequestParams();
        mHttpHelper.post(url, params, false, callBack);
    }

}
