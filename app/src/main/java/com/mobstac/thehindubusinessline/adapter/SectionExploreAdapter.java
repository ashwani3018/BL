package com.mobstac.thehindubusinessline.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.fragments.SlidingSectionFragment;
import com.mobstac.thehindubusinessline.model.SectionTable;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.PicassoUtils;

import java.util.List;

/**
 * Created by root on 25/8/16.
 */
public class SectionExploreAdapter extends RecyclerView.Adapter<SectionExploreAdapter.ViewHolder> {
    private Context mContext;
    private List<SectionTable> mExploreTablesList;
    private long sectionId;
    private String mParentSecctionName;

    public SectionExploreAdapter(Context ctxParam, List<SectionTable> mExploreListParam, long sectionId, String sectionName) {
        mContext = ctxParam;
        mExploreTablesList = mExploreListParam;
        this.sectionId = sectionId;
        mParentSecctionName = sectionName;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View exploreView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.cardview_explore_column, parent, false);
        return new ViewHolder(exploreView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String imageUrl = mExploreTablesList.get(position).getImage_v2();
        if (imageUrl == null) {
            imageUrl = mExploreTablesList.get(position).getImage();
        }
        if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
            imageUrl = imageUrl.replace(Constants.REPLACE_IMAGE_FREE, Constants.WIDGET_THUMB_SIZE);
            PicassoUtils.downloadImage(mContext, imageUrl, holder.mExploreImageView, R.mipmap.ph_exploresections_th);

        } else {
            holder.mExploreImageView.setImageResource(R.mipmap.ph_exploresections_th);
        }

        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SlidingSectionFragment fragment = SlidingSectionFragment.newInstance(SlidingSectionFragment.FROM_NORMAL, position, true, sectionId, mParentSecctionName);
                ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.FRAME_CONTENT, fragment).addToBackStack(null).commit();

                GoogleAnalyticsTracker
                        .setGoogleAnalyticsEvent(
                                mContext,
                                "Explore",
                                "Clicked",
                                "Explore - " + mParentSecctionName + " - " + mExploreTablesList.get(position).getSecName());
                FlurryAgent.logEvent("Explore - " + mParentSecctionName + " - " + mExploreTablesList.get(position).getSecName());

            }
        });
        holder.mExploreTextView.setText(mExploreTablesList.get(position).getSecName());

    }

    @Override
    public int getItemCount() {
        return mExploreTablesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView parentView;
        private ImageView mExploreImageView;
        private TextView mExploreTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            parentView = (CardView) itemView.findViewById(R.id.layout_explore_root);
            mExploreImageView = (ImageView) itemView.findViewById(R.id.imageview_explore_section);
            mExploreTextView = (TextView) itemView.findViewById(R.id.textview_explore_section);
        }
    }
}
