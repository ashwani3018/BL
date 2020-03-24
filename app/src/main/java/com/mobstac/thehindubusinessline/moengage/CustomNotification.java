package com.mobstac.thehindubusinessline.moengage;

import android.app.Notification;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.mobstac.thehindubusinessline.R;
import com.moengage.pushbase.push.MoEngageNotificationUtils;
import com.moengage.pushbase.push.PushMessageListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


/**
 * Created by 9920 on 4/27/2017.
 */

public class CustomNotification extends PushMessageListener {
    private String TAG = "CustomNotification";

    @Override
    public void customizeNotification(Notification notification, Context context, Bundle extras) {
        Log.i(TAG, "customizeNotification: ");
        String title = MoEngageNotificationUtils.getNotificationTitleIfAny(extras);
        String description = MoEngageNotificationUtils.getNotificationContentTextIfAny(extras);
        String imageUrl = null;
        if (MoEngageNotificationUtils.isImageNotification(extras)) {
            imageUrl = extras.getString("gcm_image_url");
        }
        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.custom_collapsed_notification);
        remoteViews.setTextViewText(R.id.title_textview, title);
        remoteViews.setTextViewText(R.id.description_textview, description);
        remoteViews.setLong(R.id.time_textview, "setTime", System.currentTimeMillis());

        notification.contentView = remoteViews;

        if (Build.VERSION.SDK_INT >= 16) {
            RemoteViews expandedView = new RemoteViews(context.getPackageName(), R.layout.custom_expanded_notification);
            try {
                expandedView.setTextViewText(R.id.title_textview, title);
                expandedView.setTextViewText(R.id.description_textview, description);
                expandedView.setLong(R.id.time_textview, "setTime", System.currentTimeMillis());
                if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
                    expandedView.setImageViewBitmap(R.id.notification_image, BitmapFactory.decodeStream((InputStream) new URL(imageUrl).getContent()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            notification.bigContentView = expandedView;

        }
        super.customizeNotification(notification, context, extras);

    }


}
