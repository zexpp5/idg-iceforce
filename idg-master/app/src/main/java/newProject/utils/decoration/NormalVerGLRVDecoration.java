package newProject.utils.decoration;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

/**
 * Created by ybq on 2017/4/21.
 */

public class NormalVerGLRVDecoration extends RecyclerView.ItemDecoration {
    private Drawable divider;
    private int dividerSize;  //分割线的高或宽，单位：px
    private int margin = 0;

    /**
     * @param context
     * @param dividerSize               分割线的宽或高，单位：px
     * @param dividerDrawableResourceId 分割线的资源文件id
     */
    public NormalVerGLRVDecoration( Context context,
                                    int dividerSize,
                                    int dividerDrawableResourceId) {
        this.dividerSize = dividerSize;
        if (Build.VERSION.SDK_INT < 21) {
            this.divider = context.getResources().getDrawable(dividerDrawableResourceId);
        } else {
            this.divider = context.getResources()
                    .getDrawable(dividerDrawableResourceId, context.getTheme());
        }
    }

    /**
     * @param dividerSize     分割线的宽或高，单位：px
     * @param dividerDrawable 分割线drawable
     */
    public NormalVerGLRVDecoration(int dividerSize,
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
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
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
            final int rightTop = child.getTop() - params.topMargin + margin;
            final int rightBottom = child.getBottom() + params.bottomMargin + dividerSize-margin;
            final int rightLeft = child.getRight() + params.rightMargin;
            final int rightRight = rightLeft + dividerSize;
            divider.setBounds(rightLeft, rightTop, rightRight, rightBottom);
            divider.draw(canvas);
        }
    }


    public void setMargin(int margin) {
        this.margin = margin;
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
        if (((itemPosition - 1) % spanCount == 0)) {                          //第一条线
            leftDivider = 0;
            rightDivider = (int) (dividerSize * ((spanCount - 1d) / spanCount));

        } else if ((itemPosition) % spanCount == 0) {           //最后一条线
            rightDivider = 0;
            leftDivider = (int) (dividerSize * ((spanCount - 1d) / spanCount));
        } else {
            leftDivider = (int) (dividerSize * ((itemPosition % spanCount - 1d) / spanCount));
            rightDivider = (int) (dividerSize * ((spanCount - (double) itemPosition % spanCount) / spanCount));
        }


        Log.e("Hello", "verticalLineCount : " + verticalLineCount);
        outRect.set(leftDivider, 0, rightDivider, bottomDivider);
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
