package com.cxgz.activity.superqq.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.injoy.idg.R;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.chat.IMCallLog;
import com.chaoxiang.entity.dao.DaoSession;
import com.chaoxiang.entity.dao.IMCallLogDao;
import com.cxgz.activity.cxim.utils.MainTopMenuUtils;
import com.cxgz.activity.cx.utils.TimeUtils;
import com.cxgz.activity.cxim.VideoActivity;
import com.cxgz.activity.cxim.VoiceActivity;
import com.cxgz.activity.cxim.adapter.CommonAdapter;
import com.cxgz.activity.cxim.adapter.ViewHolder;
import com.cxgz.activity.cxim.base.BaseFragment;
import com.facebook.drawee.view.SimpleDraweeView;
import com.superdata.im.constants.CxVoiceCallStatus;
import com.utils.SDImgHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 通话
 */
public class SuperCallFragment extends BaseFragment implements
        AdapterView.OnItemLongClickListener,
        AdapterView.OnItemClickListener,
        VoiceActivity.OnVoiceChatFinishListener,
        VideoActivity.OnVideoChatFinishListener
{
    private static final String TAG = "SuperCallFragment";

    private IMCallLogDao imCallLogDao;

    private ListView listView;
    private CommonAdapter<IMCallLog> adapter;
    private List<IMCallLog> callLogs = new ArrayList<>();

    @Override
    protected int getContentLayout()
    {
        return R.layout.super_call_tab;
    }

    @Override
    protected void init(View view)
    {
        setTitle(R.string.super_tab_02);

        addRightBtn(R.mipmap.menu_add, new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                MainTopMenuUtils.getInstance(getActivity()).showMenuMeeting(view, "1");
            }
        });


//        addRightBtn(R.mipmap.search, new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                //查询通讯录
////                Intent intent = new Intent(getActivity(), SearchContactActivity.class);
////                intent.putExtra(SearchContactActivity.SEARCH_DATA, (Serializable) searchData);
////                startActivity(intent);
//            }
//        });

        addLeftBtn(R.drawable.folder_back, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().finish();
            }
        });

        listView = (ListView) view.findViewById(R.id.list);
        
        listView.setAdapter(adapter = new CommonAdapter<IMCallLog>(getActivity(), callLogs, R.layout.item_call_log)
        {
            @Override
            public void convert(ViewHolder helper, IMCallLog item, int position)
            {
                SDImgHelper.getInstance(getActivity()).loadSmallImg(item.getHeadUrl(), R.mipmap.temp_user_head, (SimpleDraweeView) helper.getView(R.id.iv_header_img));
                helper.setText(R.id.tv_name, item.getWithName());
                long ctime = item.getCreateTime();
                long todayStart = TimeUtils.getTodayTimestamp();

                if (ctime >= todayStart)
                {
                    //今天
                    helper.setText(R.id.tv_time, TimeUtils.timeFormat(ctime, "HH:mm:ss"));
                } else
                {
                    //不是今天
                    helper.setText(R.id.tv_time, TimeUtils.timeFormat(ctime, "yyyy/MM/dd"));
                }
//                if (item.getCallMode() == 1)
//                {
//                    //拨出
//                    helper.setImageView(R.id.icon_call, R.mipmap.call_try);
//                } else
//                {
//                    //拨入
//                    if (item.getAnswerState() == 1)
//                    {
//                        //已接
//                        helper.setImageView(R.id.icon_call, R.mipmap.call_answer);
//                    } else
//                    {
//                        //未接
//                        helper.setImageView(R.id.icon_call, R.mipmap.call_missed);
//                    }
//                }

                if (item.getCallMode() == 1)
                {
                    //拨出成功
                    if (item.getAnswerState() == CxVoiceCallStatus.VOICE_SEND_SUCCESS)
                    {
                        helper.setImageView(R.id.icon_call, R.mipmap.call_try);
                    }
                    //播出失败
                    else if (item.getAnswerState() == CxVoiceCallStatus.VOICE_SEND_FAILURE)
                    {
                        helper.setImageView(R.id.icon_call, R.mipmap.call_missed_failure);
                    }
                } else
                {
                    //对方拨入
                    if (item.getAnswerState() == CxVoiceCallStatus.VOICE_RECEIVE_SUCCESS)
                    {
                        //已接
                        helper.setImageView(R.id.icon_call, R.mipmap.call_answer);
                    }
                    //对方拨入失败
                    else if (item.getAnswerState() == CxVoiceCallStatus.VOICE_RECEIVE_FAILURE)
                    {
                        //未接
                        helper.setImageView(R.id.icon_call, R.mipmap.call_missed);
                    }
                }
            }
        });

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        VoiceActivity.addChatFinishListener(this);

        initDao();

        loadDB();
    }

    private void initDao()
    {
        DaoSession daoSession = IMDaoManager.getInstance().getDaoSession();
        if (daoSession != null)
        {
            imCallLogDao = daoSession.getIMCallLogDao();
        }
    }

    private void loadDB()
    {
        SDLogUtil.debug(TAG, "loadDB");
        if (listView == null)
        {
            return;
        }
        callLogs = imCallLogDao.loadAllByTimeDesc();
        if (callLogs == null)
        {
            callLogs = new ArrayList<>();
        }
        adapter.setAll(callLogs);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        final int position = i - listView.getHeaderViewsCount();
        if (position < 0)
        {
            return;
        }
        Intent intent = null;
        IMCallLog imCallLog = adapter.getItem(position);
        String imAccound = null;
        if (imCallLog.getAnswerType() == 1)
        {
            //音频
            if (imCallLog.getCallMode() == 1)
            {
                //拨出
                imAccound = imCallLog.getToImAccound();
            } else
            {
                //拨入
                imAccound = imCallLog.getFromImAccound();
            }
            intent = new Intent(getActivity(), VoiceActivity.class);

        } else
        {
            //视频
            if (imCallLog.getCallMode() == 1)
            {
                //拨出
                imAccound = imCallLog.getToImAccound();
            } else
            {
                //拨入
                imAccound = imCallLog.getFromImAccound();
            }
            intent = new Intent(getActivity(), VideoActivity.class);
        }

        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra(VideoActivity.IM_ACCOUND, imAccound);
        intent.putExtra(VideoActivity.IS_CALL, true);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        final int position = i - listView.getHeaderViewsCount();
        if (position < 0)
        {
            return false;
        }

        new AlertDialog.Builder(getActivity()).setMessage(R.string.want_to_delete_the_log).setPositiveButton(R.string.sure, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                IMCallLog imCallLog = adapter.getItem(position);
                imCallLogDao.delete(imCallLog);
                loadDB();
            }
        }).setNegativeButton(R.string.cancel, null).show();
        return true;
    }

    @Override
    public void onDestroy()
    {
        VoiceActivity.removeChatFinishListener(this);
        VideoActivity.removeChatFinishListener(this);
        super.onDestroy();
    }

    @Override
    public void onVideoChatFinish()
    {
        SDLogUtil.debug(TAG, "onVideoChatFinish");
        loadDB();
    }

    @Override
    public void onVoiceChatFinish()
    {
        SDLogUtil.debug(TAG, "onVoiceChatFinish");
        loadDB();
    }


}
