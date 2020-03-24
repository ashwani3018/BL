package com.mobstac.thehindubusinessline.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.fragments.SlidingArticleFragment;
import com.mobstac.thehindubusinessline.model.databasemodel.ArticleDetail;
import com.mobstac.thehindubusinessline.model.databasemodel.SectionsSubsection;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.PicassoUtils;

import java.util.ArrayList;
import java.util.List;

public class WidgetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_WIDGET = 1;
    private final int VIEW_OPINION = 4;
    private final int SECTION_OPINION = 26;
    private Context mContext;
    private ArrayList<ArticleDetail> mWidgetList;
    private int sectionId;

    public WidgetAdapter(Context ctxParam, ArrayList<ArticleDetail> mWidgetListParam, int sectionId) {
        mContext = ctxParam;
        mWidgetList = mWidgetListParam;
        this.sectionId = sectionId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder mViewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEW_WIDGET:
                View widgetView = inflater.inflate(R.layout.layout_widget_recycler_column, parent, false);
                mViewHolder = new WidgetViewHolder(widgetView);
                break;
            case VIEW_OPINION:
                View opinionView = inflater.inflate(R.layout.layout_widget_opinion, parent, false);
                mViewHolder = new OpinionViewHolder(opinionView);
                break;
        }
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case VIEW_WIDGET:
                WidgetViewHolder widgetViewHolder = (WidgetViewHolder) holder;
                fillWidgetData(widgetViewHolder, position);
                break;
            case VIEW_OPINION:
                OpinionViewHolder opinionViewHolder = (OpinionViewHolder) holder;
                fillOpinionData(opinionViewHolder, position);
                break;
        }
    }


    @Override
    public int getItemCount() {
        if (mWidgetList.size() > 5) {
            return 5;
        } else {
            return mWidgetList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (sectionId) {
            case SECTION_OPINION:
                return VIEW_OPINION;
            default:
                return VIEW_WIDGET;
        }
    }


    private void fillWidgetData(final WidgetViewHolder mWidgetViewHolder, final int position) {
        final ArticleDetail bean = mWidgetList.get(position);
        if (bean != null) {

            String imageUrl = bean.getIm_thumbnail_v2();
            if (imageUrl == null) {
                imageUrl = bean.getIm_thumbnail();
            }
            if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
                imageUrl = imageUrl.replace(Constants.REPLACE_IMAGE_FREE, Constants.WIDGET_THUMB_SIZE);
                PicassoUtils.downloadImage(mContext, imageUrl, mWidgetViewHolder.mWidgetImageView, R.mipmap.ph_toppicks_th);
            } else {
                mWidgetViewHolder.mWidgetImageView.setImageResource(R.mipmap.ph_toppicks_th);
            }
            
            mWidgetViewHolder.mWidgetTextView.setText(bean.getTi());

            mWidgetViewHolder.mWidgetLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Widget", "Widget: Article Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Widget: " + " Article Clicked");
                    SlidingArticleFragment fragment = SlidingArticleFragment.newInstance(
                            position, bean.getSid(), false);
                    ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.FRAME_CONTENT, fragment).addToBackStack(null).commit();
                }
            });
        }
    }


    private void fillOpinionData(OpinionViewHolder mOpinionViewHolder, final int position) {
        final ArticleDetail bean = mWidgetList.get(position);
        if (bean != null) {
            List<SectionsSubsection> mSections = bean.getSections();
            if (mSections != null && mSections.size() > 0) {
                mOpinionViewHolder.mWidgetTextView.setText(mSections.get(0).getSection_name());
            } else {
                mOpinionViewHolder.mWidgetTextView.setText("Opinion");
            }
            String description = bean.getTi();
            if (description != null) {
                mOpinionViewHolder.mWidgetDescripitionTextView.setText(
                        Html.fromHtml(description)
                );
            } else {
                mOpinionViewHolder.mWidgetDescripitionTextView.setText("");
            }
            mOpinionViewHolder.mRootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Widget Openion", "Widget Openion: Article Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Widget Openion: " + " Article Clicked");
                    SlidingArticleFragment fragment = SlidingArticleFragment.newInstance(
                            position, bean.getSid(), false);
                    ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.FRAME_CONTENT, fragment).addToBackStack(null).commit();
                }
            });
        }
    }


    private class WidgetViewHolder extends RecyclerView.ViewHolder {
        private ImageView mWidgetImageView;
        private TextView mWidgetTextView;
        private LinearLayout mWidgetLayout;

        private WidgetViewHolder(View itemView) {
            super(itemView);
            mWidgetLayout = (LinearLayout) itemView.findViewById(R.id.layout_widget);
            mWidgetImageView = (ImageView) itemView.findViewById(R.id.imageview_widget_image);
            mWidgetTextView = (TextView) itemView.findViewById(R.id.textview_widget_text);
        }
    }

    private class OpinionViewHolder extends RecyclerView.ViewHolder {

        private TextView mWidgetTextView;
        private TextView mWidgetDescripitionTextView;
        private LinearLayout mRootLayout;

        private OpinionViewHolder(View itemView) {
            super(itemView);

            mWidgetTextView = (TextView) itemView.findViewById(R.id.textview_opinion_title);
            mWidgetDescripitionTextView = (TextView) itemView.findViewById(R.id.textview_opinion_content);
            mRootLayout = (LinearLayout) itemView.findViewById(R.id.layout_root_opinion);
        }
    }
}
