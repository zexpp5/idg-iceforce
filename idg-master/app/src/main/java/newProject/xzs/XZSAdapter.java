package newProject.xzs;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.utils.StringUtils;
import com.injoy.idg.R;
import com.utils.SPUtils;

import java.util.List;

/**
 * Created by selson on 2017/10/21.
 * 语音会议详情item
 */
public class XZSAdapter extends BaseMultiItemQuickAdapter<ReplyBean.DataBean, BaseViewHolder>
{
    public final static int COMPANY_HAVE = 0;
    public final static int COMPANY_NO = 1;
    public final static int COMPANY_MINE = 2;

    List<ReplyBean.DataBean> mData = null;

    public XZSAdapter(Context context, @Nullable List<ReplyBean.DataBean> data)
    {
        super(data);
        this.mData = data;
        this.mContext = context;
        addItemType(COMPANY_HAVE, R.layout.cxim_project_received_company);
        addItemType(COMPANY_NO, R.layout.cxim_row_received_message);
        addItemType(COMPANY_MINE, R.layout.cxim_row_sent_message);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ReplyBean.DataBean item)
    {
        helper.getView(R.id.timestamp).setVisibility(View.GONE);

        switch (helper.getItemViewType())
        {
            case COMPANY_HAVE:
                ViewTreeObserver viewTreeObserver=helper.getView(R.id.rl_content_new).getViewTreeObserver();
                viewTreeObserver.addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        helper.getView(R.id.copy_layout).setVisibility(View.GONE);
                    }
                });
                helper.getView(R.id.rl_content).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        helper.getView(R.id.copy_layout).setVisibility(View.VISIBLE);
                        return false;
                    }
                });
                helper.getView(R.id.copy_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        copyTo(item);
                        helper.getView(R.id.copy_layout).setVisibility(View.GONE);
                    }
                });
                helper.setText(R.id.tv_company_name, item.getCompanyName());
                helper.setText(R.id.tv_tax_number, item.getTaxNumber());
                helper.setText(R.id.tv_invoice_address, item.getInvoiceAddress());
                helper.setText(R.id.tv_account, item.getAccount());
                helper.setText(R.id.tv_bank_name, item.getOpenBank());
                helper.setText(R.id.tv_telephone, item.getTelephone());
                helper.setText(R.id.tv_fax_number, item.getFax());
//                Glide.with(mContext)
//                        .load(item.getIcon())
//                        .fitCenter()
//                        .placeholder(R.mipmap.temp_user_head)
//                        .error(R.mipmap.temp_user_head)
//                        .crossFade()
//                        .into((ImageView) helper.getView(R.id.img_user_icon));

                helper.getView(R.id.img_user_icon).setBackgroundResource(R.mipmap.icon_xzs);
                break;
            case COMPANY_NO:

                helper.setText(R.id.tv_chatcontent, item.getContent());
                helper.getView(R.id.img_user_icon).setBackgroundResource(R.mipmap.icon_xzs);

                break;

            case COMPANY_MINE:

                String iconUrl = (String) SPUtils.get(mContext, SPUtils.USER_ICON, "");
                if (StringUtils.notEmpty(iconUrl))
                {
                    Glide.with(mContext)
                            .load(iconUrl)
                            .fitCenter()
                            .placeholder(R.mipmap.temp_user_head)
                            .error(R.mipmap.temp_user_head)
                            .crossFade()
                            .into((ImageView) helper.getView(R.id.img_user_icon));
                }
                helper.setText(R.id.tv_chatcontent, item.getContent());

                break;
        }
    }


    public void copyTo(ReplyBean.DataBean item){
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        StringBuilder builder=new StringBuilder();
        builder.append("公司名称:"+item.getCompanyName()+"\n");
        builder.append("纳税号:"+item.getTaxNumber()+"\n");
        builder.append("开票地址:"+item.getInvoiceAddress()+"\n");
        builder.append("账户:"+item.getAccount()+"\n");
        builder.append("开户行:"+item.getOpenBank()+"\n");
        builder.append("电话:"+item.getTelephone()+"\n");
        builder.append("公司传真:"+item.getFax());
        ClipData clipData=ClipData.newPlainText("Label",builder);
        cm.setPrimaryClip(clipData);
    }


}
