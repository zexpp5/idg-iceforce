package yunjing.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.injoy.idg.R;

import yunjing.bean.TableBean;
import yunjing.view.excelpane.LinkedAdaptiveTableAdapter;
import yunjing.view.excelpane.TableDataSource;
import yunjing.view.excelpane.ViewHolderImpl;


/**
 * 资金附件适配器
 */
public class CapitalAnnexAdapter extends LinkedAdaptiveTableAdapter<ViewHolderImpl> {

    private final LayoutInflater mLayoutInflater;
    private final TableDataSource<TableBean, TableBean, TableBean, TableBean> mTableDataSource;
    private final int mColumnWidth;
    private final int mRowHeight;
    private final int mHeaderHeight;
    private final int mHeaderWidth;
    private Resources mResource;

    public CapitalAnnexAdapter(Context context, TableDataSource<TableBean, TableBean, TableBean, TableBean> tableDataSource) {
        mLayoutInflater = LayoutInflater.from(context);
        mTableDataSource = tableDataSource;
        mResource = context.getResources();
        mColumnWidth = mResource.getDimensionPixelSize(R.dimen.dp_column_width_100dp);
        mRowHeight = mResource.getDimensionPixelSize(R.dimen.dp_row_height_40dp);
        mHeaderHeight = mResource.getDimensionPixelSize(R.dimen.dp_column_header_height_40dp);
        mHeaderWidth = mResource.getDimensionPixelSize(R.dimen.dp_column_width_100dp);
    }

    @Override
    public int getRowCount() {
        return mTableDataSource.getRowsCount();
    }

    @Override
    public int getColumnCount() {
        return mTableDataSource.getColumnsCount();
    }

    //-------------------------------------holder---------------------------------

    //内容
    @NonNull
    @Override
    public ViewHolderImpl onCreateItemViewHolder(@NonNull ViewGroup parent) {
        return new TestViewHolder(mLayoutInflater.inflate(R.layout.item_excel_right_content, parent, false));
    }

    //头部，顶部
    @NonNull
    @Override
    public ViewHolderImpl onCreateColumnHeaderViewHolder(@NonNull ViewGroup parent) {
        return new TestHeaderColumnViewHolder(mLayoutInflater.inflate(R.layout.item_excel_head_column, parent, false));
    }

    //左边每行第一个item
    @NonNull
    @Override
    public ViewHolderImpl onCreateRowHeaderViewHolder(@NonNull ViewGroup parent) {
        return new TestHeaderRowViewHolder(mLayoutInflater.inflate(R.layout.item_excel_left_head, parent, false));
    }


    //左边顶部
    @NonNull
    @Override
    public ViewHolderImpl onCreateLeftTopHeaderViewHolder(@NonNull ViewGroup parent) {
        return new TestHeaderLeftTopViewHolder(mLayoutInflater.inflate(R.layout.item_excel_left_top, parent, false));
    }


    //-----------------------------------------------------bind----------------------------------------------

    /**
     * 内容bind
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolderImpl viewHolder, int row, int column) {
        final TestViewHolder vh = (TestViewHolder) viewHolder;
        TableBean itemData = mTableDataSource.getItemData(row, column);

        if (itemData == null) {
            itemData = new TableBean();
        }

        vh.tvRightContent.setVisibility(View.VISIBLE);
        vh.ivImage.setVisibility(View.INVISIBLE);
        vh.tvRightContent.setText(itemData.getContent());

        if (row % 2 == 0){
            if (mResource!=null) {
                vh.outerLayout.setBackgroundColor(mResource.getColor(R.color.table_row_bg));
            }
        }else{
            vh.outerLayout.setBackgroundColor(Color.WHITE);
        }

    }

    /**
     * 头部标题bind
     */
    @Override
    public void onBindHeaderColumnViewHolder(@NonNull ViewHolderImpl viewHolder, int column) {
        TestHeaderColumnViewHolder vh = (TestHeaderColumnViewHolder) viewHolder;

        vh.tvTopTitle.setText(mTableDataSource.getColumnHeaderData(column).getContent());  // skip left top header

    }

    /**
     * 左边标题bind
     */
    @Override
    public void onBindHeaderRowViewHolder(@NonNull ViewHolderImpl viewHolder, int row) {
        TestHeaderRowViewHolder vh = (TestHeaderRowViewHolder) viewHolder;
        vh.tvLeftTitle.setText(mTableDataSource.getItemData(row, 0).getContent());
        if (row % 2 == 0){
            if (mResource!=null) {
                vh.tvLeftTitle.setBackgroundColor(mResource.getColor(R.color.table_row_bg));
            }
        }else{
            vh.tvLeftTitle.setBackgroundColor(Color.WHITE);
        }
    }

    /**
     * 左边顶部bind
     */
    @Override
    public void onBindLeftTopHeaderViewHolder(@NonNull ViewHolderImpl viewHolder) {
        TestHeaderLeftTopViewHolder vh = (TestHeaderLeftTopViewHolder) viewHolder;
        vh.tvLeftTopTitle.setText(mTableDataSource.getFirstHeaderData().getContent());

    }

    @Override
    public int getColumnWidth(int column) {
        return mColumnWidth;
    }

    @Override
    public int getHeaderColumnHeight() {
        return mHeaderHeight;
    }

    @Override
    public int getRowHeight(int row) {
        return mRowHeight;
    }

    @Override
    public int getHeaderRowWidth() {
        return mHeaderWidth;
    }

    //------------------------------------- view holders ------------------------------------------

    private static class TestViewHolder extends ViewHolderImpl {
        RelativeLayout outerLayout;
        TextView tvRightContent;
        ImageView ivImage;

        private TestViewHolder(@NonNull View itemView) {
            super(itemView);
            outerLayout= (RelativeLayout) itemView.findViewById(R.id.right_content_layout);
            tvRightContent = (TextView) itemView.findViewById(R.id.tvText);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
        }
    }

    private static class TestHeaderColumnViewHolder extends ViewHolderImpl {
        TextView tvTopTitle;

        private TestHeaderColumnViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTopTitle = (TextView) itemView.findViewById(R.id.tvText);
        }
    }

    private static class TestHeaderRowViewHolder extends ViewHolderImpl {
        TextView tvLeftTitle;

        TestHeaderRowViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLeftTitle = (TextView) itemView.findViewById(R.id.tvText);
        }
    }

    private static class TestHeaderLeftTopViewHolder extends ViewHolderImpl {
        TextView tvLeftTopTitle;

        private TestHeaderLeftTopViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLeftTopTitle = (TextView) itemView.findViewById(R.id.tvText);
        }
    }
}
