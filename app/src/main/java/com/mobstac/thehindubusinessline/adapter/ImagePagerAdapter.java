package com.mobstac.thehindubusinessline.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.model.ImageGallaryUrl;
import com.mobstac.thehindubusinessline.utils.PicassoUtils;

import java.util.ArrayList;

/**
 * Created by arvind on 16/9/16.
 */
public class ImagePagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<ImageGallaryUrl> mImageUrlList;

    public ImagePagerAdapter(Context mContext, ArrayList<ImageGallaryUrl> mImageUrlList) {
        this.mContext = mContext;
        this.mImageUrlList = mImageUrlList;
    }

    @Override
    public int getCount() {
        return mImageUrlList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.image_pager_item, container, false);
        ImageView mImageView = (ImageView) view.findViewById(R.id.detail_image);
        String imageUrl = mImageUrlList.get(position).getImageUrl();
        if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
            PicassoUtils.downloadImage(mContext, imageUrl, mImageView, R.mipmap.ph_topnews_th);
        } else {
            mImageView.setImageResource(R.mipmap.ph_topnews_th);
        }
        ((TextView) view.findViewById(R.id.slideshow_title)).setText(mImageUrlList.get(position).getTitle());
        String capton = mImageUrlList.get(position).getCaption();
        if (capton != null) {
            capton = replaceSpecialChar(capton);
            ((TextView) view.findViewById(R.id.caption)).setText(Html.fromHtml(capton));
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private String replaceSpecialChar(String comment) {
        comment = comment.replaceAll("&amp;quot;", "\"");
        return comment;
    }
}
