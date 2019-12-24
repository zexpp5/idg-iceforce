package newProject.ui.work.detail;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.ui.company.bean.DeptBeanList;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.injoy.idg.R;

import java.util.List;

/**
 * Created by selson on 2017/8/21.
 * 公司部门list
 */
public class UserAdapter extends BaseQuickAdapter<SDUserEntity, BaseViewHolder>
{
    List<SDUserEntity> mData = null;

    public UserAdapter(Context context, @Nullable List<SDUserEntity> data)
    {
        super(R.layout.activity_group_item, data);
        this.mData = data;
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, SDUserEntity item)
    {
        if (StringUtils.notEmpty(item.getName()))
            helper.setText(R.id.tv_nickName, item.getName());
        else
            helper.setText(R.id.tv_nickName, StringUtils.getPhoneString(item.getImAccount()));

        ImageView img_icon = helper.getView(R.id.icon);
        Glide.with(mContext)
                .load(item.getIcon())
                .fitCenter()
                .error(R.mipmap.contact_icon)
                .crossFade()
                .into(img_icon);
//                showHeadImg(item.getIcon(), helper);
//                helper.setText(R.id.tv_content, item.getAttached());

    }
}