package com.cxgz.activity.cxim.workCircle.widgets.videolist.visibility.calculator;


/**
 * This is basic interface for Visibility calculator.
 * Methods of it strongly depends on Scroll events from ListView or RecyclerView
 *
 * @author Wayne
 */
public interface ListItemsVisibilityCalculator {
    void onScrolled(int scrollState);
    void onScrollStateIdle();
}
