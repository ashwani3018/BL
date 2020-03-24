package com.mobstac.thehindubusinessline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.Businessline;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.model.BookmarkBean;
import com.mobstac.thehindubusinessline.model.databasemodel.ArticleDetail;
import com.mobstac.thehindubusinessline.utils.AppUtils;
import com.mobstac.thehindubusinessline.utils.ArticleUtil;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.PicassoUtils;
import com.mobstac.thehindubusinessline.utils.SharingArticleUtil;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by arvind on 30/8/16.
 */
public class RelatedArticleAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ArticleDetail> mRelatedArticleList;

    public RelatedArticleAdapter(Context mContext, ArrayList<ArticleDetail> mRelatedArticleList) {
        this.mContext = mContext;
        this.mRelatedArticleList = mRelatedArticleList;
    }

    @Override
    public int getCount() {
        if (mRelatedArticleList != null) {
            return mRelatedArticleList.size();
        } else {
            return 0;
        }
    }

    @Override
    public ArticleDetail getItem(int i) {
        return mRelatedArticleList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mRelatedArticleList.get(i).getAid();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Realm mRealm = Businessline.getRealmInstance();
        RelatedArticleViewHolder holder = null;
        if (view == null) {
            holder = new RelatedArticleViewHolder();
            view = (View) ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.cardview_article_list, viewGroup, false);
            holder.mArticleImageView = (ImageView) view.findViewById(R.id.imageview_article_list_image);
            holder.mArticleTextView = (TextView) view.findViewById(R.id.textview_article_list_header);
            holder.mShareArticleButton = (Button) view.findViewById(R.id.button_article_share);
            holder.mArticleTimeTextView = (TextView) view.findViewById(R.id.textview_time);
            holder.mBookmarkButton = (Button) view.findViewById(R.id.button_bookmark);
            holder.mMultimediaButton = (ImageButton) view.findViewById(R.id.multimedia_button);
            holder.mImageParentLayout = (FrameLayout) view.findViewById(R.id.imageParentLayout);
            holder.mArticleSectionName = (TextView) view.findViewById(R.id.section_name);
            view.setTag(holder);
        } else {
            holder = (RelatedArticleViewHolder) view.getTag();
        }


        final ArticleDetail bean = getItem(i);
        String articleType = bean.getArticleType();
        String multiMediaUrl = null;

        if (bean != null) {
            final BookmarkBean bookmarkBean = mRealm.where(BookmarkBean.class).equalTo("aid", bean.getAid()).findFirst();
            if (bookmarkBean != null && bookmarkBean.getAid() == bean.getAid()) {
                holder.mBookmarkButton.setBackgroundResource(R.drawable.bookmark_feed_filled);
            } else {
                holder.mBookmarkButton.setBackgroundResource(R.drawable.bookmark_feed);
            }

            if (bean.getHi() == 1) {
                holder.mImageParentLayout.setVisibility(View.VISIBLE);
                holder.mArticleImageView.setVisibility(View.VISIBLE);
                String image_v2 = bean.getMe().get(0).getIm_v2();
                if (image_v2 == null) {
                    image_v2 = bean.getMe().get(0).getIm();
                }
                if (!image_v2.isEmpty()) {
                    image_v2 = image_v2.replace(Constants.REPLACE_IMAGE_FREE, Constants.ROW_THUMB_SIZE);
                    PicassoUtils.downloadImage(mContext, image_v2, holder.mArticleImageView, R.mipmap.ph_newsfeed_th);
                }
            } else {
                if (articleType.equalsIgnoreCase(Constants.TYPE_PHOTO) || articleType.equalsIgnoreCase(Constants.TYPE_AUDIO) || articleType.equalsIgnoreCase(Constants.TYPE_VIDEO)) {
                    holder.mImageParentLayout.setVisibility(View.VISIBLE);
                    holder.mArticleImageView.setVisibility(View.VISIBLE);
                    PicassoUtils.downloadImage(mContext, "https//", holder.mArticleImageView, R.mipmap.ph_newsfeed_th);
                } else {
                    holder.mArticleImageView.setVisibility(View.GONE);
                    holder.mImageParentLayout.setVisibility(View.GONE);
                }
            }

            holder.mMultimediaButton.setVisibility(View.VISIBLE);
            switch (articleType) {
                case Constants.TYPE_ARTICLE:
                    holder.mMultimediaButton.setVisibility(View.GONE);
                    break;
                case Constants.TYPE_AUDIO:
                    holder.mMultimediaButton.setVisibility(View.VISIBLE);
                    holder.mMultimediaButton.setBackgroundResource(R.mipmap.audio);
                    multiMediaUrl = bean.getAudioLink();
                    break;
                case Constants.TYPE_PHOTO:
                    holder.mMultimediaButton.setVisibility(View.VISIBLE);
                    holder.mMultimediaButton.setBackgroundResource(R.mipmap.slide);
                    break;
                case Constants.TYPE_VIDEO:
                    holder.mMultimediaButton.setVisibility(View.VISIBLE);
                    holder.mMultimediaButton.setBackgroundResource(R.mipmap.play_updated);
                    multiMediaUrl = bean.getYoutube_video_id();
                    break;
            }

            holder.mArticleTextView.setText(bean.getTi());
            holder.mArticleSectionName.setText(bean.getSname());
            String publishDate = bean.getGmt();
            String diffDate = AppUtils.getDurationFormattedDate(AppUtils.changeStringToMillisGMT(publishDate));
            holder.mArticleTimeTextView.setText(diffDate);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ArticleDetailFragment fragment = ArticleDetailFragment.newInstance(bean.getAid(), null, bean.getAl(), false, bean);
//                    ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.FRAME_CONTENT, fragment).addToBackStack(null).commit();
                    ArticleUtil.launchDetailActivity(mContext, bean.getAid(), bean.getSid(), bean.getAl(), false, bean);
                    GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Related Article", "Related article: Article Clicked", "Article detail page");
                    FlurryAgent.logEvent("Related Article: " + "Article Clicked");
                }
            });

            final String multiMediaLink = multiMediaUrl;
            holder.mBookmarkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Related Article", "Related article: Bookmark button Clicked", "Article detail Fragment");
                    FlurryAgent.logEvent("Related Article: " + "Bookmark button Clicked");
                    if (bookmarkBean == null) {

                        ArticleUtil.markAsBookMark(mRealm, bean.getAid(), bean.getSid(), bean.getSname(), bean.getPd(),
                                bean.getOd(), bean.getPid(), bean.getTi(), bean.getAu(), bean.getAl(), bean.getBk(),
                                bean.getGmt(), bean.getDe(), bean.getLe(), bean.getHi(),
                                ArticleUtil.getImageBeanList(bean.getMe()), System.currentTimeMillis(),
                                false, bean.getAdd_pos(), multiMediaLink,
                                bean.getArticleType(), bean.getVid(), bean.getParentId(), bean.getLocation());

                        AppUtils.showToast(mContext, mContext.getResources().getString(R.string.added_to_bookmark));
                    } else {

                        ArticleUtil.removeFromBookMark(mRealm, bookmarkBean);
                        AppUtils.showToast(mContext, mContext.getResources().getString(R.string.removed_from_bookmark));
                    }
                    notifyDataSetChanged();
                }
            });
            holder.mShareArticleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharingArticleUtil.shareArticle(mContext, bean.getTi(), bean.getAl());
                }
            });
        }
        return view;
    }

    public class RelatedArticleViewHolder {
        public View mParentView;
        public ImageView mArticleImageView;
        public TextView mArticleTextView;
        public TextView mArticleTimeTextView;
        public Button mBookmarkButton;
        public Button mShareArticleButton;
        public ImageButton mMultimediaButton;
        public FrameLayout mImageParentLayout;
        public TextView mArticleSectionName;
    }
}
