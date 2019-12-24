package yunjing.view.excelpane;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static yunjing.view.excelpane.ScrollType.SCROLL_HORIZONTAL;
import static yunjing.view.excelpane.ScrollType.SCROLL_VERTICAL;

@IntDef({
        SCROLL_HORIZONTAL,
        SCROLL_VERTICAL
})
/**
 * Scroll type for drag and drop mode.
 */
@Retention(RetentionPolicy.SOURCE)
@interface ScrollType {
    int SCROLL_HORIZONTAL = 0;
    int SCROLL_VERTICAL = 1;
}
