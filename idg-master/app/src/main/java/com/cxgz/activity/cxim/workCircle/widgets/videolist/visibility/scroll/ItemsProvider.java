package com.cxgz.activity.cxim.workCircle.widgets.videolist.visibility.scroll;


import com.cxgz.activity.cxim.workCircle.widgets.videolist.visibility.items.ListItem;

/**
 *
 * @author Wayne
 */
public interface ItemsProvider {

    ListItem getListItem(int position);

    int listItemSize();

}
