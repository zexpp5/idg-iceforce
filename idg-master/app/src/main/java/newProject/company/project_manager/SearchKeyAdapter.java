package newProject.company.project_manager;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author: Created by aniu on 2018/6/21.
 */

public class SearchKeyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private String keyword;

    public SearchKeyAdapter(@Nullable List<String> data) {
        super(R.layout.adapter_search_key, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String text) {
        SpannableString spannableString = null;
        if (!TextUtils.isEmpty(keyword)) {
            spannableString = matcherSearchTitle(Color.RED, text, keyword);
        }
        TextView tv_item = helper.getView(R.id.tv_item);
        tv_item.setText(spannableString != null ? spannableString : text);
    }

    public static SpannableString matcherSearchTitle(int color, String text,
                                                     String keyword) {
        SpannableString s = new SpannableString(text);
        Pattern p = Pattern.compile(keyword);
        Matcher m = p.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            s.setSpan(new ForegroundColorSpan(color), start, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return s;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
