package newProject.company.project_manager.investment_project.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.voice.VoiceListBean;

/**
 * Created by zsz on 2019/5/15.
 */

public class VoiceListAdapter extends BaseQuickAdapter<VoiceListBean,BaseViewHolder>{
    public VoiceListAdapter(@Nullable List<VoiceListBean> data) {
        super(R.layout.adapter_voice_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VoiceListBean item) {
        ImageView ivVoice = helper.getView(R.id.iv_voice);
        ivVoice.setBackgroundColor(0xffffffff);
        ivVoice.setImageResource(R.drawable.play_annex_voice);
        helper.addOnClickListener(R.id.iv_voice);
        helper.addOnClickListener(R.id.iv_remove);
        helper.setText(R.id.tv_second,item.getSecond()+"″");
    }
}
