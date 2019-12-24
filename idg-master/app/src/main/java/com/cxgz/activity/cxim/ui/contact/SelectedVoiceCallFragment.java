package com.cxgz.activity.cxim.ui.contact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.cxgz.activity.cxim.ChatActivity;
import com.cxgz.activity.cxim.PersonalInfoActivity;
import com.entity.SDDepartmentEntity;
import com.injoy.idg.R;
import com.cxgz.activity.cx.base.BaseFragment;
import com.cxgz.activity.cx.utils.filter.UserFilter;
import com.cxgz.activity.cx.view.Sidebar.Sidebar;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.cxgz.activity.superqq.adapter.SDContactAdapter;
import com.superdata.im.constants.CxIMMessageType;
import com.utils.SPUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import yunjing.utils.DisplayUtil;

import static com.cxgz.activity.cxim.ui.contact.SDSelectVoiceCallActivity.INIT_USER_LIST;

/**
 * @author selson
 * @time 2016/4/27
 * 同事列表
 */
public class SelectedVoiceCallFragment extends BaseFragment implements OnItemClickListener
{
    private ListView listView;
    private Sidebar sidebar;

    private List<SDUserEntity> userEntities = new ArrayList<>();

    private SDContactAdapter adapter;

    private OnSelectedDataListener listener;

    /**
     * 是否移出自己
     */
    private boolean isNeedRemoveOwer;

    private boolean isHideTab;

    private int intType = 0;


    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            listener = (OnSelectedDataListener) activity;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString() + " must implement OnSelectedDataListener");
        }
    }

    @Override
    protected void init(View view)
    {
        sidebar = (Sidebar) view.findViewById(R.id.sidebar);
        initSideBarData();
        listView = (ListView) view.findViewById(R.id.list);

        Bundle bundle = getActivity().getIntent().getExtras();

        if (bundle != null)
        {
            userEntities = (List<SDUserEntity>) bundle.getSerializable(INIT_USER_LIST);
            isHideTab = bundle.getBoolean(SDSelectVoiceCallActivity.HIDE_TAB, true);
            intType = bundle.getInt(SDSelectVoiceCallActivity.INTTYPE, 0);
        }
        if (intType == 1)
        {
            userEntities = mUserDao.findAllUserNot();
            String myImAccount = DisplayUtil.getUserInfo(getActivity(), 5);
            for (int i = 0; i < userEntities.size(); i++)
            {
                Iterator<SDUserEntity> it = userEntities.iterator();
                while (it.hasNext())
                {
                    SDUserEntity x = it.next();
                    if (x.getImAccount().equals(myImAccount))
                    {
                        it.remove();
                    }
                }
            }
        } else
        {
            userEntities = mUserDao.findAllUserNot();
        }

        adapter = new SDContactAdapter(getActivity(), userEntities, isHideTab, false);
        listView.setAdapter(adapter);
        sidebar.setListView(listView);
        listView.setOnItemClickListener(this);
        ((SDSelectVoiceCallActivity) getActivity()).setUserFilter(new UserFilter(adapter, "name"));
    }

    /**
     * 初始化slidebar数据
     */
    private void initSideBarData()
    {
        List<String> aTozList = sidebar.getSectionAtoZList();
        aTozList.add(0, "↑");
        sidebar.setSections(sidebar.listToArray(aTozList));
        sidebar.setListView(listView);
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.sd_selected_contact_fragment;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id)
    {
        if (intType == 0)
        {
            SDUserEntity userEntity = adapter.getAll().get(position);
            Bundle bundle = new Bundle();
            bundle.putString(SPUtils.USER_ID, userEntity.getImAccount());
            openActivity(PersonalInfoActivity.class, bundle);
        } else if (intType == 1)
        {
            Intent singleChatIntent = new Intent(getActivity(), ChatActivity.class);
            singleChatIntent.putExtra(ChatActivity.EXTR_CHAT_TYPE, CxIMMessageType.SINGLE_CHAT.getValue());
            singleChatIntent.putExtra(ChatActivity.EXTR_CHAT_ACCOUNT, userEntities.get(position).getImAccount());
            singleChatIntent.putExtra(ChatActivity.EXTR_ONLY_HOT_CHAT, true);
            startActivity(singleChatIntent);
        }

//        int intType = 0;
//        Bundle bundle = ((SDSelectVoiceCallActivity) getActivity()).getIntent().getExtras();
//        if (bundle != null)
//            intType = bundle.getInt("intType");
//        if (intType == 0)
//        {
//            if (StringUtils.notEmpty(userEntity.getImAccount()))
//            {
//                //语音聊天
//                Intent voiceIntent = new Intent(getActivity(), VoiceActivity.class);
//                voiceIntent.setAction(Intent.ACTION_VIEW);
//                voiceIntent.putExtra(VoiceActivity.IM_ACCOUND, String.valueOf(userEntity.getImAccount()));
//                voiceIntent.putExtra(VoiceActivity.IS_CALL, true);
//                startActivity(voiceIntent);
//            } else
//            {
//                MyToast.showToast(getActivity(), "用户不在线,稍后再拨！");
//            }
//        }

    }

    public SDContactAdapter getAdapter()
    {
        return adapter;
    }

    public void setAdapter(SDContactAdapter adapter)
    {
        this.adapter = adapter;
    }

    public interface OnSelectedDataListener
    {
        void onSelectedContactData(List<SDUserEntity> userEntities);

        void onSelectedDpData(List<SDDepartmentEntity> departmentEntities);
    }
}
