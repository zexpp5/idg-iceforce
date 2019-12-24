package newProject.utils.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author  Created by ybq on 2017/4/21.
 * 仅适用于使用LinearLayoutManager的RecycleView绘制分割线
 */

public class FullLLRVDecoration extends RecyclerView.ItemDecoration {
    private Drawable divider;
    //分割线的高或宽，单位：px
    private int dividerSize;

    /**
     * @param context
     * @param dividerSize               分割线的宽或高，单位：px
     * @param dividerDrawableResourceId 分割线的资源文件id
     */
    public FullLLRVDecoration(@NonNull Context context,
                              int dividerSize,
                              int dividerDrawableResourceId) {
        this.dividerSize = dividerSize;
        if (Build.VERSION.SDK_INT < 21) {
            this.divider = ContextCompat.getDrawable(context, dividerDrawableResourceId);
        } else {
            this.divider = context.getResources()
                    .getDrawable(dividerDrawableResourceId, context.getTheme());
        }
    }

    /**
     * @param dividerSize     分割线的宽或高，单位：px
     * @param dividerDrawable 分割线drawable
     */
    public FullLLRVDecoration(int dividerSize,
                              @NonNull Drawable dividerDrawable) {
        this.dividerSize = dividerSize;
        this.divider = dividerDrawable;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            int orientation = ((LinearLayoutManager) layoutManager).getOrientation();
            if (orientation == LinearLayoutManager.VERTICAL) {
                drawHorizontalLine(canvas, parent, state);
            } else {
                drawVerticalLine(canvas, parent, state);
            }
        }
    }

    private void drawVerticalLine(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            //draw left
            final int leftTop = child.getTop() - params.topMargin;
            final int leftBottom = child.getBottom() + params.bottomMargin;
            final int leftLeft = child.getLeft() - params.leftMargin - dividerSize;
            final int leftRight = leftLeft + dividerSize;
            divider.setBounds(leftLeft, leftTop, leftRight, leftBottom);
            divider.draw(canvas);
            //draw right
            final int rightTop = child.getTop() - params.topMargin;
            final int rightBottom = child.getBottom() + params.bottomMargin;
            final int rightLeft = child.getRight() + params.rightMargin;
            final int rightRight = rightLeft + dividerSize;
            divider.setBounds(rightLeft, rightTop, rightRight, rightBottom);
            divider.draw(canvas);
        }
    }

    private void drawHorizontalLine(Canvas canvas,
                                    RecyclerView parent,
                                    RecyclerView.State state) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            //draw top
            final int topLeft = child.getLeft() - params.leftMargin;
            final int topRight = child.getRight() + params.rightMargin;
            final int topBottom = child.getTop() + params.topMargin;
            final int topTop = topBottom - dividerSize;
            divider.setBounds(topLeft, topTop, topRight, topBottom);
            divider.draw(canvas);
            //draw bottom
            final int bottomLeft = child.getLeft() - params.leftMargin;
            final int bottomRight = child.getRight() + params.rightMargin;
            final int bottomTop = child.getBottom() + params.bottomMargin;
            final int bottomBottom = bottomTop + dividerSize;
            divider.setBounds(bottomLeft, bottomTop, bottomRight, bottomBottom);
            divider.draw(canvas);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect,
                               View view,
                               RecyclerView parent,
                               RecyclerView.State state) {
        int childCount = parent.getAdapter().getItemCount();
        int itemPosition = parent.getChildAdapterPosition(view);
        itemPosition += 1;

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            int orientation = ((LinearLayoutManager) layoutManager).getOrientation();
            if (orientation == LinearLayoutManager.VERTICAL) {
                //draw hor line
                if (itemPosition == childCount) {
                    outRect.set(0, dividerSize, 0, dividerSize);
                } else {
                    outRect.set(0, dividerSize, 0, 0);
                }
            } else {
                //draw ver line
                if (itemPosition == childCount) {
                    outRect.set(dividerSize, 0, dividerSize, 0);
                } else {
                    outRect.set(dividerSize, 0, 0, 0);
                }
            }
        }
    }
}
