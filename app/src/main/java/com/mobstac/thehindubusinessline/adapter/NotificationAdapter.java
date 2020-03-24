package com.mobstac.thehindubusinessline.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.Businessline;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.database.DatabaseJanitor;
import com.mobstac.thehindubusinessline.listener.NotificatiionBookmarkButtonClick;
import com.mobstac.thehindubusinessline.model.BookmarkBean;
import com.mobstac.thehindubusinessline.model.NotificationBean;
import com.mobstac.thehindubusinessline.utils.AppUtils;
import com.mobstac.thehindubusinessline.utils.ArticleUtil;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.PicassoUtils;
import com.mobstac.thehindubusinessline.utils.SharingArticleUtil;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by arvind on 8/10/16.
 */

public class NotificationAdapter extends RealmRecyclerViewAdapter<NotificationBean, RecyclerView.ViewHolder> {

    Realm mRealm;
    private Context mContext;
    private OrderedRealmCollection<NotificationBean> mNotificationArticleList;
    private NotificatiionBookmarkButtonClick bookmarkButtonClick;

    public NotificationAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<NotificationBean> data, boolean autoUpdate
            , NotificatiionBookmarkButtonClick notificatiionBookmarkButtonClick) {
        super(context, data, autoUpdate);
        mContext = context;
        mNotificationArticleList = data;
        mRealm = Businessline.getRealmInstance();
        bookmarkButtonClick = notificatiionBookmarkButtonClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder mViewHolder = null;
        View articleView = inflater.inflate(R.layout.cardview_article_list, parent, false);
        mViewHolder = new NotificationAdapter.SectionViewHolder(articleView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NotificationAdapter.SectionViewHolder articleHolder = (NotificationAdapter.SectionViewHolder) holder;
        fillArticleData(articleHolder, position);

    }

    private void fillArticleData(NotificationAdapter.SectionViewHolder articleHolder, final int position) {
        final NotificationBean bean;
        if (mNotificationArticleList.get(position).isValid()) {
            bean = mNotificationArticleList.get(position);
            String articleType = bean.getArticleType();
            final BookmarkBean bookmarkBean = mRealm.where(BookmarkBean.class).equalTo("aid", bean.getArticleId()).findFirst();
            if (bookmarkBean != null && bookmarkBean.getAid() == bean.getArticleId()) {
                articleHolder.mBookmarkButton.setBackgroundResource(R.drawable.bookmark_feed_filled);
            } else {

                articleHolder.mBookmarkButton.setBackgroundResource(R.drawable.bookmark_feed);
            }

            final boolean hasBody = bean.isHasBody();

            String imageUrl = bean.getImageUrl();
            if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
                imageUrl = imageUrl.replace(Constants.REPLACE_IMAGE_FREE, Constants.ROW_THUMB_SIZE);
                articleHolder.mImageParentLayout.setVisibility(View.VISIBLE);
                articleHolder.mArticleImageView.setVisibility(View.VISIBLE);
                PicassoUtils.downloadImage(mContext, imageUrl, articleHolder.mArticleImageView, R.mipmap.ph_newsfeed_th);
            } else {
                if (articleType.equalsIgnoreCase(Constants.TYPE_PHOTO) || articleType.equalsIgnoreCase(Constants.TYPE_AUDIO) || articleType.equalsIgnoreCase(Constants.TYPE_VIDEO)) {
                    articleHolder.mImageParentLayout.setVisibility(View.VISIBLE);
                    articleHolder.mArticleImageView.setVisibility(View.VISIBLE);
                    PicassoUtils.downloadImage(mContext, "https//", articleHolder.mArticleImageView, R.mipmap.ph_newsfeed_th);
                } else {
                    articleHolder.mArticleImageView.setVisibility(View.GONE);
                    articleHolder.mImageParentLayout.setVisibility(View.GONE);
                }
            }

            articleHolder.mMultimediaButton.setVisibility(View.VISIBLE);
            switch (articleType) {
                case Constants.TYPE_ARTICLE:
                    articleHolder.mMultimediaButton.setVisibility(View.GONE);
                    break;
                case Constants.TYPE_AUDIO:
                    articleHolder.mMultimediaButton.setVisibility(View.VISIBLE);
                    articleHolder.mMultimediaButton.setBackgroundResource(R.mipmap.audio);
//                    multiMediaUrl = bean.getAudioLink();
                    break;
                case Constants.TYPE_PHOTO:
                    articleHolder.mMultimediaButton.setVisibility(View.VISIBLE);
                    articleHolder.mMultimediaButton.setBackgroundResource(R.mipmap.slide);
                    break;
                case Constants.TYPE_VIDEO:
                    articleHolder.mMultimediaButton.setVisibility(View.VISIBLE);
                    articleHolder.mMultimediaButton.setBackgroundResource(R.mipmap.play_updated);
//                    multiMediaUrl = bean.getYoutube_video_id();
                    break;
            }

            if (hasBody) {
                articleHolder.mBookmarkButton.setVisibility(View.VISIBLE);
                articleHolder.mRootLayout.setBackgroundColor(context.getResources().getColor(R.color.article_bg_color));
                articleHolder.mArticleTextView.setTextColor(context.getResources().getColor(R.color.article_list_text));
                articleHolder.mArticleUpdateTime.setTextColor(context.getResources().getColor(R.color.article_bottom_text_color));
            } else {
                articleHolder.mBookmarkButton.setVisibility(View.GONE);
                articleHolder.mRootLayout.setBackgroundColor(context.getResources().getColor(R.color.feaded_blue));
                articleHolder.mArticleTextView.setTextColor(context.getResources().getColor(R.color.white));
                articleHolder.mArticleUpdateTime.setTextColor(context.getResources().getColor(R.color.white));

            }

            articleHolder.mArticleTextView.setText(bean.getDescription());
            articleHolder.mArticleSectionName.setText(bean.getSectionName());
            String timeDiff = AppUtils.getDurationFormattedDate(bean.getInsertionTime());
            articleHolder.mArticleUpdateTime.setText(timeDiff);
            articleHolder.mParentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (hasBody) {
                        GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Notification", "Notification: Article Clicked", "Notification Fragment");
                        FlurryAgent.logEvent("Notification: " + "Article Clicked");
                        DatabaseJanitor.updateNotificationArticleReadFlag(bean.getArticleId());
                       /* ArticleDetailFragment fragment = ArticleDetailFragment.newInstance(bean.getArticleId(),
                                bean.getSectionId(), bean.getArticleUrl(), false, null);
                        ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.FRAME_CONTENT, fragment).addToBackStack(null).commit();*/
                        ArticleUtil.launchDetailActivity(mContext, bean.getArticleId(), null, bean.getArticleUrl(), false, null);

                      //  ArticleUtil.launchDetailActivity(mMainActivity, articleId, null, url, false, null);
                    }
                }
            });

            articleHolder.mBookmarkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hasBody) {
                        GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Notification", "Notification: Bookmark button Clicked", "Notification Fragment");
                        FlurryAgent.logEvent("Notification: " + "Bookmark button Clicked");
                        if (bookmarkBean == null) {
                            bookmarkButtonClick.bookMarkButtonClicked(String.valueOf(bean.getArticleId()));
                        } else {
                            mRealm.beginTransaction();
                            bookmarkBean.deleteFromRealm();
                            mRealm.commitTransaction();
                            notifyDataSetChanged();
                            AppUtils.showToast(mContext, mContext.getResources().getString(R.string.removed_from_bookmark));
                        }
                        notifyDataSetChanged();
                    }
                }
            });

            articleHolder.mShareArticleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharingArticleUtil.shareArticle(mContext, bean.getTitle(), bean.getArticleUrl());
                }
            });
        }
    }

    public class SectionViewHolder extends RecyclerView.ViewHolder {

        public View mParentView;
        public ImageView mArticleImageView;
        public TextView mArticleTextView;
        public TextView mArticleUpdateTime;
        public Button mBookmarkButton;
        public Button mShareArticleButton;
        public RelativeLayout mRootLayout;
        public ImageButton mMultimediaButton;
        public FrameLayout mImageParentLayout;
        public TextView mArticleSectionName;

        public SectionViewHolder(View itemView) {
            super(itemView);
            mParentView = itemView;
            mArticleImageView = (ImageView) itemView.findViewById(R.id.imageview_article_list_image);
            mArticleTextView = (TextView) itemView.findViewById(R.id.textview_article_list_header);
            mShareArticleButton = (Button) itemView.findViewById(R.id.button_article_share);
            mArticleUpdateTime = (TextView) itemView.findViewById(R.id.textview_time);
            mBookmarkButton = (Button) itemView.findViewById(R.id.button_bookmark);
            mRootLayout = (RelativeLayout) itemView.findViewById(R.id.layout_articles_root);
            mMultimediaButton = (ImageButton) itemView.findViewById(R.id.multimedia_button);
            mImageParentLayout = (FrameLayout) itemView.findViewById(R.id.imageParentLayout);
            mArticleSectionName = (TextView) itemView.findViewById(R.id.section_name);
        }
    }


}
