package com.mobstac.thehindubusinessline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.listener.OnExpandableListViewItemClickListener;
import com.mobstac.thehindubusinessline.model.SectionTable;

import java.util.List;

/**
 * Created by arvind on 24/12/16.
 */

public class NavigationExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<SectionTable> mSectionList;
    private OnExpandableListViewItemClickListener onExpandableListViewItemClickListener;

    public NavigationExpandableListViewAdapter(Context mContext, List<SectionTable> mSectionList, OnExpandableListViewItemClickListener onExpandableListViewItemClickListener) {
        this.mContext = mContext;
        this.mSectionList = mSectionList;
        this.onExpandableListViewItemClickListener = onExpandableListViewItemClickListener;
    }

    @Override
    public int getGroupCount() {
        return mSectionList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mSectionList.get(groupPosition).getSubSectionList().size();
    }

    @Override
    public SectionTable getGroup(int groupPosition) {
        return mSectionList.get(groupPosition);
    }

    @Override
    public SectionTable getChild(int groupPosition, int childPosition) {
        return mSectionList.get(groupPosition).getSubSectionList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return mSectionList.get(groupPosition).getSecId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return mSectionList.get(groupPosition).getSubSectionList().get(childPosition).getSecId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder mGroupViewHolder = null;
        if (convertView == null) {
            mGroupViewHolder = new GroupViewHolder();
            convertView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                    inflate(R.layout.navigation_group_row, parent, false);
            mGroupViewHolder.mSectionNameLayout = (LinearLayout) convertView.findViewById(R.id.parantSectionNameLayout);
            mGroupViewHolder.textView = (TextView) convertView.findViewById(R.id.title);
            mGroupViewHolder.mExpandButton = (Button) convertView.findViewById(R.id.expandButton);
            mGroupViewHolder.mNewTagImageView = (ImageView) convertView.findViewById(R.id.newTagImage);
            convertView.setTag(mGroupViewHolder);
        } else {
            mGroupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        String mSectionName = getGroup(groupPosition).getSecName();
        mGroupViewHolder.textView.setText(mSectionName);

        if (mSectionName != null && mSectionName.equalsIgnoreCase("PrintPlay")) {
            mGroupViewHolder.mNewTagImageView.setVisibility(View.VISIBLE);
        } else {
            mGroupViewHolder.mNewTagImageView.setVisibility(View.GONE);
        }

        List<SectionTable> mChildList = getGroup(groupPosition).getSubSectionList();
        if (mChildList != null && mChildList.size() > 0) {
            mGroupViewHolder.mExpandButton.setVisibility(View.VISIBLE);
        } else {
            mGroupViewHolder.mExpandButton.setVisibility(View.INVISIBLE);
        }

        if (isExpanded) {
            mGroupViewHolder.mExpandButton.setBackgroundResource(R.mipmap.minus_new);
        } else {
            mGroupViewHolder.mExpandButton.setBackgroundResource(R.mipmap.plus_n);
        }

        final GroupViewHolder finalMGroupViewHolder = mGroupViewHolder;
        mGroupViewHolder.mExpandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onExpandableListViewItemClickListener.onExpandButtonClick(groupPosition, isExpanded);
                if (isExpanded) {
                    finalMGroupViewHolder.mExpandButton.setBackgroundResource(R.mipmap.plus_n);
                } else {
                    finalMGroupViewHolder.mExpandButton.setBackgroundResource(R.mipmap.minus_new);
                }
            }
        });

        mGroupViewHolder.mSectionNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onExpandableListViewItemClickListener.onGroupNameTextClick(groupPosition, isExpanded);
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder = null;
        if (convertView == null) {
            childViewHolder = new ChildViewHolder();
            convertView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.navigation_list_row, parent, false);
            childViewHolder.textView = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.textView.setText(getChild(groupPosition, childPosition).getSecName());
        childViewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onExpandableListViewItemClickListener.onChildClick(groupPosition, childPosition, getGroupId(groupPosition));
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class ChildViewHolder {
        public TextView textView;
    }

    public class GroupViewHolder {
        public LinearLayout mSectionNameLayout;
        public TextView textView;
        public Button mExpandButton;
        public ImageView mNewTagImageView;
    }
}
