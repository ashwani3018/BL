package com.mobstac.thehindubusinessline.moengage;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.leocardz.link.preview.library.LinkPreviewCallback;
import com.leocardz.link.preview.library.SourceContent;
import com.leocardz.link.preview.library.TextCrawler;
import com.mobstac.thehindubusinessline.model.NotificationBean;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.SharedPreferenceHelper;
import com.moe.pushlibrary.utils.MoEHelperUtils;
import com.moengage.firebase.MoEFireBaseMessagingService;
import com.moengage.push.PushManager;

import java.util.Map;


/**
 * Created by ashwani on 30/08/16.
 */
public class HinduMoEngFcmListenerService extends MoEFireBaseMessagingService {
    final String TAG = "HinduMoEngFcm";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //Log.i("", "");

        Map<String, String> data = remoteMessage.getData();

        boolean notificationEnabled = SharedPreferenceHelper.getBoolean(getApplicationContext(), Constants.NOTIFICATIONS, true);
        if (!notificationEnabled) {
            Log.i(TAG, "onMessageReceived: Notification not enabled from setting page");
            return;
        }

        if (null == data) {
            return;
        }

        MoEHelperUtils.dumpIntentExtras(remoteMessage.toIntent());

        /*if (data.containsKey(MoEHelperConstants.EXTRA_REGISTRATION_ID)) {
            final String registrationId = data.get(MoEHelperConstants.EXTRA_REGISTRATION_ID);
            PushManager.getInstance().refreshTokenInternal(this, registrationId,
                    PushManager.TOKEN_BY_MOE);
            return;
        }*/



        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */
        if (data.containsKey("ns_type_PN") && data.get("ns_type_PN").equalsIgnoreCase(Constants.ARTICLE)) {
            SharedPreferenceHelper.putBoolean(this, Constants.NEW_NOTIFICATION, true);

            try {
                final Map<String, String> arguments = data;
                final String actionURL = arguments.get("ns_action");
                final String type = arguments.get("ns_type_PN");
                final String title = arguments.get("gcm_title");
                final String description = arguments.get("gcm_alert");
                final String imageUrl = arguments.get("ns_image");
                final String sectionName = arguments.get("ns_sec_name");
                final String pub_date = arguments.get("ns_push_date");
                final boolean isHasBody = Boolean.parseBoolean(arguments.get("ns_has_body"));
                final String parentId = arguments.get("ns_parent_id");
                final String sectionId = arguments.get("ns_section_id");

                final String articleType;
                if (arguments.containsKey("ns_article_type")) {
                    articleType = arguments.get("ns_article_type");
                } else {
                    articleType = "article";
                }
                int article_id = 0;
                try {
                    article_id = Integer.parseInt(arguments.get("ns_article_id"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final int artId = article_id;

                NotificationBean bean = new NotificationBean(artId, actionURL, title, description,
                        imageUrl, sectionName, pub_date, type, isHasBody, System.currentTimeMillis(),
                        parentId, sectionId, false, articleType);
                SharedPreferenceHelper.addInJSONArray(this, bean);
                Intent mNotificationReceiver = new Intent(Constants.NOTIFICATION_INCOMING_FILTER);
                mNotificationReceiver.putExtra(Constants.NEW_NOTIFICATION, true);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(mNotificationReceiver);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if(data.containsKey("ns_url")) {
            TextCrawler mTextCrawler = new TextCrawler();
            String articleUrl = data.get("ns_url");

            // Following line is commented by Ashwani
//            mTextCrawler.makePreview(mPreviewCallback, articleUrl);

            // Following line is Added by Ashwani
            // Because Moengage "handlePushPayload" should be sent once it is success then only it should happen
            mTextCrawler.makePreview(new UrlPushTask(this, data), articleUrl);
        }


    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.i("", "");
    }


    private class UrlPushTask implements LinkPreviewCallback {

        private Map<String, String> mBundle;
        private Context mContext;

        public UrlPushTask(Context context, Map<String, String> bundle) {
            mContext = context;
            mBundle = bundle;
        }

        @Override
        public void onPre() {

        }

        @Override
        public void onPos(SourceContent sourceContent, boolean b) {
            if(!sourceContent.getFinalUrl().equals("")) {
                try {
                    String title = sourceContent.getTitle();
//                String description = sourceContent.getDescription();
                    String imageUrl = null;
                    if (sourceContent.getImages().size() > 0) {
                        imageUrl = sourceContent.getImages().get(0);
                    }
                    if (title == null || TextUtils.isEmpty(title)) {
                        return;
                    }
                    int length = sourceContent.getFinalUrl().split("/").length;
                    String demilitedArticleId = sourceContent.getFinalUrl().split("/")[length - 1].split("e")[1];
                    int articleId = Integer.parseInt(demilitedArticleId.substring(0, demilitedArticleId.length() - 1));
                    NotificationBean bean = new NotificationBean(articleId, "", title, title,
                            imageUrl, "", "", "", true, System.currentTimeMillis(),
                            "", "", false, Constants.TYPE_ARTICLE);
                    SharedPreferenceHelper.addInJSONArray(HinduMoEngFcmListenerService.this, bean);
                    Intent mNotificationReceiver = new Intent(Constants.NOTIFICATION_INCOMING_FILTER);
                    mNotificationReceiver.putExtra(Constants.NEW_NOTIFICATION, true);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(mNotificationReceiver);


                    mBundle.put("ns_url", sourceContent.getUrl());

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }


    }
}
