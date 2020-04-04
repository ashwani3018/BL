package com.mobstac.thehindubusinessline.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
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
import com.mobstac.thehindubusinessline.activity.AdsBaseActivity;
import com.mobstac.thehindubusinessline.database.DatabaseJanitor;
import com.mobstac.thehindubusinessline.fragments.BookmarkArticleDetailFragment;
import com.mobstac.thehindubusinessline.fragments.BookmarksFragment;
import com.mobstac.thehindubusinessline.model.BookmarkBean;
import com.mobstac.thehindubusinessline.model.ImageBean;
import com.mobstac.thehindubusinessline.utils.AppUtils;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.PicassoUtils;
import com.mobstac.thehindubusinessline.utils.SharingArticleUtil;

import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by root on 26/8/16.
 */
public class BookmarkAdapter extends RealmRecyclerViewAdapter<BookmarkBean, RecyclerView.ViewHolder> {

    private Context mContext;
    private OrderedRealmCollection<BookmarkBean> mBookmarkArticleList;
    private String mTodaysDate;
    private String mUpdatedDate;
    private BookmarksFragment mBookmarksFragment;
    private AdsBaseActivity mMainActivity;

    public BookmarkAdapter(Context ctxParam, OrderedRealmCollection<BookmarkBean> mBookmarkList, BookmarksFragment fragment, AdsBaseActivity mainActivity) {
        super(ctxParam, mBookmarkList, true);
        mContext = ctxParam;
        mBookmarkArticleList = mBookmarkList;
        long mTimeInMilliSecond = System.currentTimeMillis();
        mTodaysDate = AppUtils.getDd_MMM_yy(mTimeInMilliSecond);
        mUpdatedDate = mTodaysDate;
        mBookmarksFragment = fragment;
        this.mMainActivity = mainActivity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder mViewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View articleView = inflater.inflate(R.layout.bookmark_article_items, parent, false);
        mViewHolder = new SectionViewHolder(articleView);
        return mViewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SectionViewHolder articleHolder = (SectionViewHolder) holder;
        fillArticleData(articleHolder, position);
    }


    private void fillArticleData(SectionViewHolder articleHolder, final int position) {
        final Realm mRealm = Businessline.getRealmInstance();
        final BookmarkBean bean;
        bean = mBookmarkArticleList.get(position);
        articleHolder.mBookmarkButton.setBackgroundResource(R.drawable.bookmark_feed_filled);
        String articleType = bean.getArticleType();
        List<ImageBean> mImageList = bean.getMe();
        if (mImageList != null && mImageList.size() > 0) {
            String imageUrl = bean.getMe().get(0).getIm_v2();
            if (imageUrl == null) {
                imageUrl = bean.getMe().get(0).getIm();
            }
            articleHolder.mArticleImageView.setVisibility(View.VISIBLE);
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
                break;
            case Constants.TYPE_PHOTO:
                articleHolder.mMultimediaButton.setVisibility(View.VISIBLE);
                articleHolder.mMultimediaButton.setBackgroundResource(R.mipmap.slide);
                break;
            case Constants.TYPE_VIDEO:
                articleHolder.mMultimediaButton.setVisibility(View.VISIBLE);
                articleHolder.mMultimediaButton.setBackgroundResource(R.mipmap.play_updated);
                break;
        }

        articleHolder.mArticleTextView.setText(bean.getTi());
        String publishTime = bean.getGmt();
        String timeDiff = AppUtils.getDurationFormattedDate(AppUtils.changeStringToMillisGMT(publishTime));
        articleHolder.mArticleUpdateTime.setText(timeDiff);
        articleHolder.mArticleSectionName.setText(bean.getSname());
        String mBookmarkdate = AppUtils.getDd_MMM_yy(bean.getBookmarkDate());
        if (position == 0) {
            mUpdatedDate = AppUtils.getDd_MMM_yy(bean.getBookmarkDate());
            if (mUpdatedDate.equalsIgnoreCase(mTodaysDate)) {
                articleHolder.mTimeHeaderTextview.setVisibility(View.VISIBLE);
                articleHolder.mTimeHeaderTextview.setText("Today");
            } else {
                articleHolder.mTimeHeaderTextview.setVisibility(View.VISIBLE);
                articleHolder.mTimeHeaderTextview.setText(mUpdatedDate);
            }
        } else {
            if (mBookmarkdate.equalsIgnoreCase(mUpdatedDate)) {
                articleHolder.mTimeHeaderTextview.setVisibility(View.GONE);

            } else {
                mUpdatedDate = AppUtils.getDd_MMM_yy(bean.getBookmarkDate());
                articleHolder.mTimeHeaderTextview.setText(AppUtils.getDd_MMM_yy(bean.getBookmarkDate()));
            }
        }
        articleHolder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Read Later", "Read later: Article clicked", "Read Later");
                FlurryAgent.logEvent("Read Later: " + "Article clicked");
                BookmarkArticleDetailFragment fragment = BookmarkArticleDetailFragment.newInstance(bean.getAid(), bean.getSid());
                ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.FRAME_CONTENT, fragment).addToBackStack(null).commit();
                if(mMainActivity !=null ) {
                    mMainActivity.expandToolbar(true);
                }
            }
        });
        articleHolder.mMultimediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Read Later", "Read later: Article clicked", "Read Later");
                FlurryAgent.logEvent("Read Later: " + "Article clicked");
                BookmarkArticleDetailFragment fragment = BookmarkArticleDetailFragment.newInstance(bean.getAid(), bean.getSid());
                ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.FRAME_CONTENT, fragment).addToBackStack(null).commit();
            }
        });

        if (!bean.isRead()) {
            articleHolder.mParentLayout.setAlpha(1f);
        } else {
            articleHolder.mParentLayout.setAlpha(.5f);
        }
        articleHolder.mBookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Read Later", mContext.getString(R.string.ga_bookmark_read_later_button_clicked), "Home Fragment");
                FlurryAgent.logEvent(mContext.getString(R.string.ga_bookmark_read_later_button_clicked));
                if (mBookmarkArticleList.size() == 1) {
                    mBookmarksFragment.showNoBookmarkView(true);
                }
                mRealm.beginTransaction();
                bean.deleteFromRealm();
                mRealm.commitTransaction();
                AppUtils.showToast(mContext, mContext.getResources().getString(R.string.removed_from_bookmark));
            }
        });
        articleHolder.mShareArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharingArticleUtil.shareArticle(mContext, bean.getTi(), bean.getAl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBookmarkArticleList.size();
    }

    public void setBookmarkList(OrderedRealmCollection<BookmarkBean> mBookmarkArticleList) {
        this.mBookmarkArticleList = mBookmarkArticleList;
        notifyDataSetChanged();
    }

    private int getArticlePositionPosition(int articleId) {
        int position = 0;
        List<BookmarkBean> mBookmarkList = DatabaseJanitor.getBookmarksArticles();
        for (BookmarkBean mSections : mBookmarkList) {
            if (mSections.getAid() == articleId) {
                return position;
            }
            position++;
        }
        return 0;
    }

    public class SectionViewHolder extends RecyclerView.ViewHolder {

        public View mParentView;
        public ImageView mArticleImageView;
        public TextView mArticleTextView;
        public TextView mArticleUpdateTime;
        public Button mBookmarkButton;
        public Button mShareArticleButton;
        public TextView mTimeHeaderTextview;
        public RelativeLayout mParentLayout;
        public ImageButton mMultimediaButton;
        public FrameLayout mImageParentLayout;
        public TextView mArticleSectionName;

        public SectionViewHolder(View itemView) {
            super(itemView);
            mParentView = itemView;
            mArticleImageView = (ImageView) itemView.findViewById(R.id.imageview_article_list_image);
            mArticleTextView = (TextView) itemView.findViewById(R.id.textview_article_list_header);
            mArticleUpdateTime = (TextView) itemView.findViewById(R.id.textview_time);
            mBookmarkButton = (Button) itemView.findViewById(R.id.button_bookmark);
            mShareArticleButton = (Button) itemView.findViewById(R.id.button_article_share);
            mTimeHeaderTextview = (TextView) itemView.findViewById(R.id.time_header);
            mParentLayout = (RelativeLayout) itemView.findViewById(R.id.layout_articles_root);
            mMultimediaButton = (ImageButton) itemView.findViewById(R.id.multimedia_button);
            mImageParentLayout = (FrameLayout) itemView.findViewById(R.id.imageParentLayout);
            mArticleSectionName = (TextView) itemView.findViewById(R.id.section_name);
        }
    }
}
