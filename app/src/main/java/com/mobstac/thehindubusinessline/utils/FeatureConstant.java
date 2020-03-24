package com.mobstac.thehindubusinessline.utils;



import android.app.Activity;
import android.content.Context;

import com.mobstac.thehindubusinessline.model.SectionAdapterItem;
import com.mobstac.thehindubusinessline.model.databasemodel.ArticleDetail;

import java.util.ArrayList;

/**
 * Created by ashwanisingh on 22/12/17.
 */

public class FeatureConstant {

    public static final boolean PAGINATION_REQUIRED = true;
    public static final boolean BRANCH_IO_REQUIRED = true;
    public static final boolean NEW_ADS_INDEXING_REQUIRED = true;
    public static final boolean FACEBOOK_ADS_REQUIRED = true;
    public static final boolean NEW_GALLERY_REQUIRED = false;


    public static ArrayList<SectionAdapterItem> getSectionRevisedData(int alreadyLoadedCount, ArrayList<ArticleDetail> articleList, boolean hasSubSection) {


        final ArrayList<SectionAdapterItem> items = new ArrayList<>();


        int count = 0;
        String cartoonSecId = "69";

        for (ArticleDetail article : articleList) {

            int viewType = -1;

            if(count == 2) {
                SectionAdapterItem item = null;
                if(hasSubSection) {
                    viewType = SectionAdapterItem.EXPLORE;
                    item = new SectionAdapterItem(article, viewType);
                    item.setArticleActualPos(count);
                    items.add(item);


                    /*viewType = SectionAdapterItem.ARTICLE;
                    SectionAdapterItem item1 = new SectionAdapterItem(article, viewType);
                    item.setArticleActualPos(count);
                    items.add(item1);*/
                } else {
//                        viewType = SectionAdapterItem.NATIVE_AD;
//                        item = new SectionAdapterItem(null, viewType);
//                        items.add(item);

                    // This is for article
                    viewType = SectionAdapterItem.ARTICLE;
                    item = new SectionAdapterItem(article, viewType);
                    item.setArticleActualPos(count);
                    items.add(item);
                }

            }
            else if(count == 3) {
                SectionAdapterItem item = null;
                if (hasSubSection) {

//                        viewType = SectionAdapterItem.NATIVE_AD;
//                        item = new SectionAdapterItem(null, viewType);
//                        items.add(item);


                    // This is for article
                    viewType = SectionAdapterItem.ARTICLE;
                    item = new SectionAdapterItem(article, viewType);
                    item.setArticleActualPos(count);
                    items.add(item);
                }
                else {
                    if (article.getSid().equals(cartoonSecId)) {
                        viewType = SectionAdapterItem.CARTOON;
                    } else {
                        viewType = SectionAdapterItem.ARTICLE;
                    }

                    item = new SectionAdapterItem(article, viewType);
                    item.setArticleActualPos(count);
                    items.add(item);
                }

            }
            else /*if (count == 6) {
                viewType = SectionAdapterItem.OUTBRAIN;
                SectionAdapterItem item = new SectionAdapterItem(null, viewType);
                items.add(item);

                // This is for article
                viewType = SectionAdapterItem.ARTICLE;
                SectionAdapterItem itemA = new SectionAdapterItem(article, viewType);
                itemA.setArticleActualPos(count);
                items.add(itemA);

            } else*/ if (article.getSid().equals(cartoonSecId)) {
                    viewType = SectionAdapterItem.CARTOON;
                    SectionAdapterItem item = new SectionAdapterItem(article, viewType);
                    item.setArticleActualPos(count);
                    items.add(item);
                }  else {
                    viewType = SectionAdapterItem.ARTICLE;
                    SectionAdapterItem item = new SectionAdapterItem(article, viewType);
                    item.setArticleActualPos(count);
                    items.add(item);
                }


            count++;
        }

        return items;
    }




    // This is Test ads
//    public static String firstInline = "ca-app-pub-3940256099942544/6300978111";

    public static String firstInline = "/22390678/BL_Android_HP_Footer";
    public static String secondInline = "/22390678/BL_Android_HP_Middle";
    public static String thirdInline = "/22390678/BL_Android_HP_Bottom";
    public static String forthInline = "/22390678/BL_Android_SP01";

    public static String firstNative = "/22390678/BL_Android_Native01";
    public static String secondNative = "/22390678/BL_Android_Native02";
    public static String thirdNative = "/22390678/BL_Android_Native03";




//    public static ArrayList<SectionAdapterItem> getSectionRevisedDataFromLoadMore(ArrayList<ArticleDetail> articleList) {
//
//        final ArrayList<SectionAdapterItem> items = new ArrayList<>();
//
//        int count = 0;
//        String cartoonSecId = "69";
//
//        for (ArticleDetail article : articleList) {
//
//            int viewType = -1;
//
//            if(count == 2) {
//                if(hasSubSection) {
//                    viewType = SectionAdapterItem.EXPLORE;
//                } else {
//                    viewType = SectionAdapterItem.NATIVE_AD;
//                }
//            }
//            else if(count == 3) {
//                if (hasSubSection) {
//                    viewType = SectionAdapterItem.NATIVE_AD;
//                } else {
//                    if (article.getSid().equals(cartoonSecId)) {
//                        viewType = SectionAdapterItem.CARTOON;
//                    } else {
//                        viewType = SectionAdapterItem.ARTICLE;
//                    }
//                }
//            }
//            else if(count == 6) {
//                viewType = SectionAdapterItem.OUTBRAIN;
//            } else {
//                if (article.getSid().equals(cartoonSecId)) {
//                    viewType = SectionAdapterItem.CARTOON;
//                }
//                if(mLoaderPosition == position/* && isLoadingAdded*/) {
//                    Log.i("", "");
//                    viewType =  SectionAdapterItem.LOADING;
//                }
//                viewType = SectionAdapterItem.ARTICLE;
//            }
//
//            if(viewType != -1) {
//                SectionAdapterItem item = new SectionAdapterItem(article, viewType);
//                items.add(item);
//            }
//
//            count++;
//        }
//
//        return items;
//    }
}
