package com.mobstac.thehindubusinessline.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.activity.StockDetailsActivity;
import com.mobstac.thehindubusinessline.model.CompanyData;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;

import java.util.List;

/**
 * Created by arvind on 26/9/16.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchRecyclerHolder> {
    private Context mContext;
    private List<CompanyData> mSearchList;

    public SearchAdapter(Context mContext, List<CompanyData> _searchList) {
        this.mContext = mContext;
        mSearchList = _searchList;
    }

    @Override
    public SearchRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.search_recycler_item, parent, false);

        return new SearchRecyclerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchRecyclerHolder holder, final int position) {
        final CompanyData bean = mSearchList.get(position);
        holder.title.setText(bean.getName());
//        holder.sname.setText(bean.getGp());
//        holder.publishDate.setText(bean.getGp());
        holder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Searched ", "Searched: Article Clicked", "Search Fragment");
                FlurryAgent.logEvent("Searched: " + "Article Clicked");
                CompanyData object = mSearchList.get(position);
                Intent i = new Intent(mContext, StockDetailsActivity.class);
                String nse = String.valueOf(object.getNse());
                String companyId = String.valueOf(object.getId());
                String companyName = object.getName();
                int bse = object.getBse();
                i.putExtra("tag", (bse == 0) ? 1 : (nse.equals(null)) ? 0 : "BSE");
                i.putExtra("bseCode", bse);
                i.putExtra("nseSymbol", nse);
                i.putExtra("companyId",companyId);
                i.putExtra("companyName", companyName);
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSearchList.size();
    }

    public void updateSearchedList(List<CompanyData> mList) {
        mSearchList = mList;
        notifyDataSetChanged();
    }

    public class SearchRecyclerHolder extends RecyclerView.ViewHolder {
        public View mParentLayout;
        public TextView title;
//        public TextView sname;
//        public TextView publishDate;

        public SearchRecyclerHolder(View itemView) {
            super(itemView);
            mParentLayout = itemView;
            title = (TextView) itemView.findViewById(R.id.title);
//            sname = (TextView) itemView.findViewById(R.id.sectionName);
//            publishDate = (TextView) itemView.findViewById(R.id.publish_date);
        }
    }
}
