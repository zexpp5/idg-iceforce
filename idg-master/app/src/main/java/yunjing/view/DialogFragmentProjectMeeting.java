package yunjing.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.ScreenUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;

import newProject.api.ListHttpHelper;
import newProject.company.invested_project.bean.BeanIceProject;

/**
 * Created by selson on 2017/8/26.
 * 项目 会议 审批
 */
public class DialogFragmentProjectMeeting extends DialogFragment
{
    int height;
    int width;
    Window dialogWindow;
    private boolean isCanOnClick = false;

    /**
     * @return
     */
    public static DialogFragmentProjectMeeting newInstance(boolean isOnClickToReturn, String id, String username)
    {
        DialogFragmentProjectMeeting instance = new DialogFragmentProjectMeeting();
        Bundle args = new Bundle();
        args.putBoolean("isOnClickToReturn", isOnClickToReturn);
        args.putString("id", id);
        args.putString("username", username);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onStart()
    {
        super.onStart();
//        Window win = getDialog().getWindow();
//        win.setBackgroundDrawable( new ColorDrawable(Color.TRANSPARENT));
//        DisplayMetrics dm = new DisplayMetrics();

//        getActivity().getWindowManager().getDefaultDisplay().getMetrics( dm );
//
//        WindowManager.LayoutParams params = win.getAttributes();
//        params.gravity = Gravity.BOTTOM;
//        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
//        params.width =  ViewGroup.LayoutParams.MATCH_PARENT;
//        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        win.setAttributes(params);

    }

    public void onResume()
    {
        super.onResume();
        dialogWindow = getDialog().getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = width;
        dialogWindow.setAttributes(lp);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View layout = inflater.inflate(R.layout.dialog_project_meeting_approve, null);

        if (getArguments() != null)
        {
            isCanOnClick = getArguments().getBoolean("isOnClickToReturn");
            id = getArguments().getString("id");
            username = getArguments().getString("username");
        }

        LinearLayout ll_dialog_main = (LinearLayout) layout.findViewById(R.id.ll_dialog_main);
        final EditText edt_approve_content = (EditText) layout.findViewById(R.id.edt_approve_content);
        TextView tv_save = (TextView) layout.findViewById(R.id.tv_save);

        height = ScreenUtils.getScreenHeight(getActivity());
        width = ScreenUtils.getScreenWidth(getActivity());
        if (height != 0)
            height = (int) (height * 0.3);
        if (width != 0)
            width = (int) (width * 0.8);

        ViewGroup.LayoutParams params = ll_dialog_main.getLayoutParams();
        params.height = height;
        params.width = width;
        ll_dialog_main.setLayoutParams(params);

        tv_save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isCanOnClick)
                {
                    if (StringUtils.notEmpty(edt_approve_content.getText().toString().trim()))
                    {
                        approveOpinion = edt_approve_content.getText().toString().trim();
                        postMeetingApprove();
                    } else
                    {
                        MyToast.showToast(getActivity(), "请输入审批内容");
                    }
                }
            }
        });

        return new AlertDialog.Builder(getActivity()).setView(layout)
                .create();
    }

    public interface InputListener
    {
        void onData(BeanIceProject content);
    }

    private InputListener mListener;

    public void setInputListener(InputListener listener)
    {
        this.mListener = listener;
    }

    String id = "";
    String approveOpinion = "";
    String username = "";

    private void postMeetingApprove()
    {
        ListHttpHelper.postMeetingApprove(getActivity(), id, approveOpinion, username, new SDRequestCallBack()
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(getActivity(), msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                MyToast.showToast(getActivity(), "审批成功");
                dismiss();
                BeanIceProject beanIceProject = new BeanIceProject(0, "sucess", "sucess");
                mListener.onData(beanIceProject);
            }
        });
    }
}
