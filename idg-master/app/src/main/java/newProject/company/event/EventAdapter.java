package newProject.company.event;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.estate_project.bean.EstateList;
import yunjing.view.DrawText;

public class EventAdapter extends BaseQuickAdapter<EstateList.DataBean, BaseViewHolder> {
    public EventAdapter(@Nullable List<EstateList.DataBean> data) {
        super(R.layout.adapter_project_estate, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EstateList.DataBean item) {

        helper.setText(R.id.one_title, item.getProjName())
                .setText(R.id.two_content, item.getIndusGroupName())
                .setText(R.id.three_content, item.getZhDesc())
                .setText(R.id.one_content, item.getProjManagerNames());

        DrawText draw_text = helper.getView(R.id.draw_text);
        draw_text.setText(item.getIndusGroupName());
    }

}
