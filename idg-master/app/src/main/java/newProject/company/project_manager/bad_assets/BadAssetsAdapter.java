package newProject.company.project_manager.bad_assets;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.util.List;

import yunjing.view.DrawText;

/**
 * @author: Created by Freeman on 2018/7/23
 */

public class BadAssetsAdapter extends BaseQuickAdapter<BadAssetsListBean.DataBean, BaseViewHolder> {

    public BadAssetsAdapter(@Nullable List<BadAssetsListBean.DataBean> data) {
        super(R.layout.adapter_bad_assets, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BadAssetsListBean.DataBean item) {

        DrawText drawText = helper.getView(R.id.draw_text);
        drawText.setText("不良");
        drawText.setColor(Color.rgb(132, 142, 153));
        helper.setText(R.id.one_title, item.getEname())
                .setText(R.id.one_content, item.getDealLeadName())
                .setText(R.id.two_content, item.getIndusName())
                .setText(R.id.three_content, item.getGrade());
    }
}
