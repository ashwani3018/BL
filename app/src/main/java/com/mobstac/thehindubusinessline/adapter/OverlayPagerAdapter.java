package com.mobstac.thehindubusinessline.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mobstac.thehindubusinessline.R;

import java.util.ArrayList;

/**
 * Created by 9920 on 5/19/2017.
 */

public class OverlayPagerAdapter extends PagerAdapter {

    private ArrayList<Integer> mImageList;
    private Context mContext;

    public OverlayPagerAdapter(Context context, ArrayList<Integer> mImageList) {
        this.mImageList = mImageList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        if (mImageList == null)
            return 0;
        return mImageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.pager_item, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setBackgroundResource(mImageList.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
