package yunjing.utils;

import com.chaoxiang.base.utils.StringUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by selson on 2017/8/15.
 */
public class JsonUtils
{
    private JsonUtils()
    {

    }

    private static class HttpJsonUtilsHelper
    {
        private static final JsonUtils JSON_UTILS = new JsonUtils();

    }

    public static synchronized JsonUtils getInstance()
    {
        return HttpJsonUtilsHelper.JSON_UTILS;
    }

    public List<NameValuePair> reTurnPair(String jsonString)
    {
        List<NameValuePair> pairs = new ArrayList<>();
        if (StringUtils.notEmpty(jsonString))
            try
            {
                JSONObject jsonObject = new JSONObject(jsonString);
                if (null != jsonObject)
                {
                    Iterator<String> keyIter = jsonObject.keys();
                    while (keyIter.hasNext())
                    {
                        String keyStr = keyIter.next();
                        String itemObject = jsonObject.getString(keyStr);
                        if (StringUtils.notEmpty(itemObject))
                        {
                            pairs.add(new BasicNameValuePair(keyStr, itemObject));
                        }
                    }
                }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }

        return pairs;
    }

    public List<NameValuePair> reTurnPair2(String jsonString)
    {
        List<NameValuePair> pairs = new ArrayList<>();
        if (StringUtils.notEmpty(jsonString))
            try
            {
                JSONObject jsonObject = new JSONObject(jsonString);
                if (null != jsonObject)
                {
                    Iterator<String> keyIter = jsonObject.keys();
                    while (keyIter.hasNext())
                    {
                        String keyStr = keyIter.next();
                        String itemObject = jsonObject.getString(keyStr);
                        if (null != itemObject)
                        {
                            pairs.add(new BasicNameValuePair(keyStr, itemObject));
                        }
                    }
                }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }

        return pairs;
    }
}
