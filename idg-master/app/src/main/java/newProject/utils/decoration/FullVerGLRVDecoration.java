package newProject.utils.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

/**
 * @author  Created by ybq on 2017/4/21.
 */

public class FullVerGLRVDecoration extends RecyclerView.ItemDecoration {
    private Drawable divider;
    //分割线的高或宽，单位：px
    private int dividerSize;

    /**
     * @param context
     * @param dividerSize               分割线的宽或高，单位：px
     * @param dividerDrawableResourceId 分割线的资源文件id
     */
    public FullVerGLRVDecoration( Context context,
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
    public FullVerGLRVDecoration(int dividerSize,
                                 @NonNull Drawable dividerDrawable) {
        this.dividerSize = dividerSize;
        this.divider = dividerDrawable;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        drawHorizontal(canvas, parent);
        drawVertical(canvas, parent);
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            //draw top
            final int topLeft = child.getLeft() - params.leftMargin - dividerSize;
            final int topRight = child.getRight() + params.rightMargin + dividerSize;
            final int topBottom = child.getTop() + params.topMargin;
            final int topTop = topBottom - dividerSize;
            divider.setBounds(topLeft, topTop, topRight, topBottom);
            divider.draw(canvas);
            //draw bottom
            final int bottomLeft = child.getLeft() - params.leftMargin - dividerSize;
            final int bottomRight = child.getRight() + params.rightMargin + dividerSize;
            final int bottomTop = child.getBottom() + params.bottomMargin;
            final int bottomBottom = bottomTop + dividerSize;
            divider.setBounds(bottomLeft, bottomTop, bottomRight, bottomBottom);
            divider.draw(canvas);
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            //draw right
            final int rightTop = child.getTop() - params.topMargin;
            final int rightBottom = child.getBottom() + params.bottomMargin + dividerSize;
            final int rightLeft = child.getRight() + params.rightMargin;
            final int rightRight = rightLeft + dividerSize;
            divider.setBounds(rightLeft, rightTop, rightRight, rightBottom);
            divider.draw(canvas);
            //draw left
            final int leftTop = child.getTop() - params.topMargin;
            final int leftBottom = child.getBottom() + params.bottomMargin + dividerSize;
            final int leftLeft = child.getLeft() - params.leftMargin - dividerSize;
            final int leftRight = leftLeft + dividerSize;
            divider.setBounds(leftLeft, leftTop, leftRight, leftBottom);
            divider.draw(canvas);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect,
                               View view,
                               RecyclerView parent,
                               RecyclerView.State state) {
        int childCount = parent.getAdapter().getItemCount();
        int spanCount = getSpanCount(parent);
        int verticalLineCount = childCount / spanCount;

        int itemPosition = parent.getChildAdapterPosition(view);
        itemPosition += 1;
        int leftDivider = 0;
        int rightDivider = 0;
        int bottomDivider = dividerSize;


        if (((itemPosition - 1) % spanCount == 0)) {
            //第一条线
            leftDivider = dividerSize;
            rightDivider = (int) (dividerSize * (1d / spanCount));

        } else if ((itemPosition) % spanCount == 0) {
            //最后一条线
            rightDivider = dividerSize;
            leftDivider = (int) (dividerSize * (1d / spanCount));
        } else {

            leftDivider = (int) (dividerSize * (((spanCount + 1d - itemPosition % spanCount) / spanCount)));
            rightDivider = (int) (dividerSize * (((spanCount + 1d - spanCount - 1d + itemPosition % spanCount) / spanCount)));
        }
        Log.e("Hello", "verticalLineCount : " + verticalLineCount);
        outRect.set(leftDivider, itemPosition <= spanCount ? dividerSize : 0, rightDivider, bottomDivider);
    }

    private int getSpanCount(RecyclerView parent) {
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }


}
