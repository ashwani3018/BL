package com.mobstac.thehindubusinessline.listener;

/**
 * Created by arvind on 25/12/16.
 */

public interface OnExpandableListViewItemClickListener {
    public void onExpandButtonClick(int groupPostion, boolean isExpanded);

    public void onGroupNameTextClick(int groupPostion, boolean isExpanded);

    public void onChildClick(int groupPostion, int childPosition, long parentId);
}
