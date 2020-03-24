package com.mobstac.thehindubusinessline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.model.CommentListModel;
import com.mobstac.thehindubusinessline.utils.AppUtils;

/**
 * Created by ashwani on 26/12/15.
 */
public class CommentsListAdapter extends ArrayAdapter<CommentListModel.CommentFeedEntity> {


    public CommentsListAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.comments_layout, null, false);
            viewHolder.comment = (TextView) convertView.findViewById(R.id.comments_content);
            viewHolder.name = (TextView) convertView.findViewById(R.id.from);
            viewHolder.time = (TextView) convertView.findViewById(R.id.posted_on);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CommentListModel.CommentFeedEntity commentFeedEntity = getItem(position);
        String comment = commentFeedEntity.getComment();
        viewHolder.comment.setText(replaceSpecialChar(comment));
        viewHolder.name.setText(commentFeedEntity.getName());

        String date = commentFeedEntity.getTs();
        String formatedDate = AppUtils.getCommentFormattedDate(AppUtils.changeCommentDateStringToMillis(date));
        viewHolder.time.setText(formatedDate);

        return convertView;
    }

    private String replaceSpecialChar(String comment) {
        comment = comment.replaceAll("%3cbr%2f%3e", "\n");
        comment = comment.replaceAll("%3f", "?");
        comment = comment.replaceAll("%5a", "[");
        comment = comment.replaceAll("%5d", "]");
        comment = comment.replaceAll("%22", "\"");
        comment = comment.replaceAll("%27", "'");
        comment = comment.replaceAll("%26", "&");
        comment = comment.replaceAll("%3a", ":");
        comment = comment.replaceAll("%3b", ";");
        comment = comment.replaceAll("%0a", "\n");
        comment = comment.replaceAll("%25", "%");
        comment = comment.replaceAll("%2f", "/");
        comment = comment.replaceAll("%e2%80%99", "'");
        comment = comment.replaceAll("%e2%80%9c", "\"");
        comment = comment.replaceAll("%e2%80%9d", "\"");

        return comment;
    }

    private class ViewHolder {
        TextView comment;
        TextView name;
        TextView time;
    }

}
