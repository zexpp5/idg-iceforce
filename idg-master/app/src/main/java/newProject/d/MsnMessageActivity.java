package newProject.d;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.SDSelectContactActivity;
import com.cxgz.activity.cxim.http.ImHttpHelper;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;

import java.util.List;

import static yunjing.utils.IntentUtils.REQUEST_CODE_FOR_IM;


/**
 * Created by selson on 2017/11/22.
 */
public class MsnMessageActivity extends Activity
{
    public static String INFO = "info";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


    }

    private void getUser()
    {

    }

    List<SDUserEntity> userList = null;
    String tmpUsersString = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_CODE_FOR_IM:
                if (resultCode == RESULT_OK && data != null)
                {
                    //返回来的字符串
                    userList = (List<SDUserEntity>) data.getSerializableExtra(
                            SDSelectContactActivity.SELECTED_DATA);// str即为回传的值
                    if (userList != null && userList.size() > 0)
                    {
//                        setUser(userList);
                    } else
                    {
                        finish();
                    }
                }
                break;
        }
    }

    private void postUser()
    {
        if (StringUtils.notEmpty(tmpUsersString))
            ImHttpHelper.PostUserMessages(MsnMessageActivity.this, tmpUsersString, new SDRequestCallBack()
            {
                @Override
                public void onRequestFailure(HttpException error, String msg)
                {
                    MyToast.showToast(MsnMessageActivity.this, msg);
                    finish();
                }

                @Override
                public void onRequestSuccess(SDResponseInfo responseInfo)
                {
                    MyToast.showToast(MsnMessageActivity.this, R.string.request_succeed2);
                    finish();
                }
            });
    }
}
