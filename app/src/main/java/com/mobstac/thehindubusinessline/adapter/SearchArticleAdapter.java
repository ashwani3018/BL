package com.mobstac.thehindubusinessline.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.Businessline;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.activity.AdsBaseActivity;
import com.mobstac.thehindubusinessline.activity.SplashActivity;
import com.mobstac.thehindubusinessline.fragments.ArticleDetailFragment;
import com.mobstac.thehindubusinessline.fragments.SearchLandingFragment;
import com.mobstac.thehindubusinessline.model.ArticleBean;
import com.mobstac.thehindubusinessline.model.BookmarkBean;
import com.mobstac.thehindubusinessline.model.databasemodel.ArticleDetail;
import com.mobstac.thehindubusinessline.utils.AppUtils;
import com.mobstac.thehindubusinessline.utils.ArticleUtil;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.NetworkUtils;
import com.mobstac.thehindubusinessline.utils.PicassoUtils;
import com.mobstac.thehindubusinessline.utils.SharingArticleUtil;

import java.util.List;

import io.realm.Realm;

public class SearchArticleAdapter extends RecyclerView.Adapter<SearchArticleAdapter.MyHolder> {

    private static final String TAG = "SearchArticleAdapter";

    private Context mContext;
    private List<ArticleDetail> mArticleList;
    private AdsBaseActivity mMainActivity;

    public SearchArticleAdapter(Context _context, List<ArticleDetail> _articleList, AdsBaseActivity _maBaseActivity) {
        mContext = _context;
        mArticleList = _articleList;
        this.mMainActivity = _maBaseActivity;
    }




    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View articleView = inflater.inflate(R.layout.cardview_article_list, parent, false);
        return new MyHolder(articleView);
    }

    @Override
    public void onBindViewHolder(MyHolder articleHolder, final int position) {



        final Realm mRealm = Businessline.getRealmInstance();
        String multiMediaUrl = null;
        final ArticleDetail bean;
        bean = mArticleList.get(position);

        final BookmarkBean bookmarkBean = mRealm.where(BookmarkBean.class).equalTo("aid", bean.getAid()).findFirst();
        if (bookmarkBean != null && bookmarkBean.getAid() == bean.getAid()) {
            articleHolder.mBookmarkButton.setBackgroundResource(R.drawable.bookmark_feed_filled);
        } else {
            articleHolder.mBookmarkButton.setBackgroundResource(R.drawable.bookmark_feed);
        }
        String articleType = bean.getArticleType();



        String imageUrl = bean.getIm_thumbnail();
        if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
            imageUrl = imageUrl.replace(Constants.REPLACE_IMAGE_FREE_SEARCH, Constants.ROW_THUMB_SIZE_SEARCH);
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


//        articleHolder.mMultimediaButton.setVisibility(View.VISIBLE);
        switch (articleType) {
            case Constants.TYPE_ARTICLE:
                articleHolder.mMultimediaButton.setVisibility(View.GONE);
                break;
            case Constants.TYPE_AUDIO:
                articleHolder.mMultimediaButton.setVisibility(View.VISIBLE);
                articleHolder.mMultimediaButton.setBackgroundResource(R.mipmap.audio);
                multiMediaUrl = bean.getAudioLink();
                break;
            case Constants.TYPE_PHOTO:
                articleHolder.mMultimediaButton.setVisibility(View.VISIBLE);
                articleHolder.mMultimediaButton.setBackgroundResource(R.mipmap.slide);
                break;
            case Constants.TYPE_VIDEO:
                articleHolder.mMultimediaButton.setVisibility(View.VISIBLE);
                articleHolder.mMultimediaButton.setBackgroundResource(R.mipmap.play_updated);
                multiMediaUrl = bean.getAudioLink();
                break;
        }

        articleHolder.mArticleTextView.setText(bean.getTi());
        String publishTime = bean.getGmt();
        String timeDiff = AppUtils.getDurationFormattedDate(AppUtils.changeStringToMillisGMT(publishTime));
        articleHolder.mArticleUpdateTime.setText(timeDiff);
        articleHolder.mArticleSectionName.setText(bean.getSname());


        articleHolder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isNetworkAvailable = NetworkUtils.isNetworkAvailable(mContext);
                if (isNetworkAvailable) {

                    if (mToolBarShows != null) {
                        mToolBarShows.onEvent(false);
                    }
                    GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Read Later", "Read later: Article clicked", "Read Later");
                    FlurryAgent.logEvent("Read Later: " + "Article clicked");

                    ArticleDetailFragment fragment = ArticleDetailFragment.newInstance(mArticleList.get(position).getAid(), mArticleList.get(position).getSid(), mArticleList.get(position).getAl(), true, mArticleList.get(position));
                    ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.search_landing_frameId, fragment)
                            .addToBackStack(null)
                            .commit();

//                mMainActivity.expandToolbar(true);
                } else {
                    Toast.makeText(mContext, "Please check the internet!",Toast.LENGTH_LONG).show();
                }
            }
        });


        final String multiMediaLink = multiMediaUrl;
        articleHolder.mBookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "SectionLanding", "SectionLanding: Bookmark button Clicked", "Section landing Fragment");
                FlurryAgent.logEvent("SectionLanding: " + " Bookmark button Clicked");
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

                Log.i("TAG", "onClick: NotifyDataSetChange");
                notifyDataSetChanged();
            }
        });


        articleHolder.mShareArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mToolBarShows != null) {
                    mToolBarShows.onEvent(true);
                    SharingArticleUtil.shareArticle(mContext, bean.getTi(), bean.getAl());
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return mArticleList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

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

        public MyHolder(View itemView) {
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


    public void onClearAllData(){
        if (mArticleList!= null) {
            mArticleList.clear();
        }
        notifyDataSetChanged();
    }


    private ToolBarShow mToolBarShows;

    public ToolBarShow  onShowToolBar(ToolBarShow _tToolBarShow){
        return  this.mToolBarShows =_tToolBarShow;
    }

    public interface ToolBarShow{
        void onEvent(boolean check);
    }
}
