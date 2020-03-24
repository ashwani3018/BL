package com.mobstac.thehindubusinessline.utils;

import android.content.Context;
import android.content.Intent;

import com.mobstac.thehindubusinessline.activity.CommentActivity;
import com.mobstac.thehindubusinessline.activity.DetailActivity;
import com.mobstac.thehindubusinessline.listener.CustomScrollListenerForScrollView;
import com.mobstac.thehindubusinessline.model.ArticleBean;
import com.mobstac.thehindubusinessline.model.BookmarkBean;
import com.mobstac.thehindubusinessline.model.ImageBean;
import com.mobstac.thehindubusinessline.model.SectionArticleList;
import com.mobstac.thehindubusinessline.model.SectionsContainingArticleBean;
import com.mobstac.thehindubusinessline.model.databasemodel.ArticleDetail;
import com.mobstac.thehindubusinessline.model.databasemodel.ImageData;
import com.mobstac.thehindubusinessline.model.databasemodel.SectionsSubsection;
import com.mobstac.thehindubusinessline.view.CustomArticleScrollView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by 9920 on 5/16/2017.
 */

public class ArticleUtil {

    public static ArrayList<ArticleDetail> convertArticleBeanListToArticleDetailList(List<ArticleBean> mResults) {
        ArrayList<ArticleDetail> mConvertedData = new ArrayList<>();
        if (mResults != null && mResults.size() > 0) {
            for (ArticleBean articleBean : mResults) {
                ArticleDetail articleDetail = new ArticleDetail(articleBean.getAid(), articleBean.getSid(), articleBean.getSname(), articleBean.getPd()
                        , articleBean.getTi(), articleBean.getAu(), articleBean.getAl(), articleBean.getGmt(), articleBean.getDe(), articleBean.getLe()
                        , articleBean.getVid(), articleBean.getYoutube_video_id(), articleBean.getIs_rn(), articleBean.getHi(), articleBean.getParentId(), articleBean.getParentName()
                        , articleBean.getAudioLink(), articleBean.getInsertionTime(), articleBean.getAdd_pos(), articleBean.getIm_thumbnail(), articleBean.getArticleType()
                        , getImageList(articleBean.getMe()), convertArticleBeanListToArticleDetailList(articleBean.getRn()), getSectionSubSection(articleBean.getSections()), articleBean.getIm_thumbnail_v2(), articleBean.getLocation());
                articleDetail.setOd(articleBean.getOd());
                articleDetail.setPid(articleBean.getPid());
                articleDetail.setBk(articleBean.getBk());
                articleDetail.setPage(articleBean.getPage());
                mConvertedData.add(articleDetail);
            }
        }
        return mConvertedData;
    }

    public static ArticleDetail convertToArticleDetail(ArticleBean articleBean) {
        if (articleBean != null && articleBean.isValid()) {
            ArticleDetail articleDetail = new ArticleDetail(articleBean.getAid(), articleBean.getSid(), articleBean.getSname(), articleBean.getPd()
                    , articleBean.getTi(), articleBean.getAu(), articleBean.getAl(), articleBean.getGmt(), articleBean.getDe(), articleBean.getLe()
                    , articleBean.getVid(), articleBean.getYoutube_video_id(), articleBean.getIs_rn(), articleBean.getHi(), articleBean.getParentId(), articleBean.getParentName()
                    , articleBean.getAudioLink(), articleBean.getInsertionTime(), articleBean.getAdd_pos(), articleBean.getIm_thumbnail(), articleBean.getArticleType()
                    , getImageList(articleBean.getMe()), convertArticleBeanListToArticleDetailList(articleBean.getRn()), getSectionSubSection(articleBean.getSections()), articleBean.getIm_thumbnail_v2(), articleBean.getLocation());
            articleDetail.setOd(articleBean.getOd());
            articleDetail.setPid(articleBean.getPid());
            articleDetail.setBk(articleBean.getBk());
            return articleDetail;
        } else
            return null;
    }

    private static ArrayList<ImageData> getImageList(RealmList<ImageBean> me) {
        ArrayList<ImageData> mImageData = new ArrayList<>();
        if (me.isValid() && me.size() > 0) {
            for (ImageBean imageBean : me) {
                mImageData.add(new ImageData(imageBean.getIm(), imageBean.getIm_v2(), imageBean.getCa()));
            }
        }
        return mImageData;
    }

    public static RealmList<ImageBean> getImageBeanList(List<ImageData> me) {
        RealmList<ImageBean> mImageData = new RealmList<>();
        if (me != null && me.size() > 0) {
            for (ImageData imageData : me) {
                ImageBean imageBean = new ImageBean();
                imageBean.setCa(imageData.getCa());
                imageBean.setIm(imageData.getIm());
                imageBean.setIm_v2(imageData.getIm_v2());
                mImageData.add(imageBean);
            }
        }
        return mImageData;
    }

    private static ArrayList<SectionsSubsection> getSectionSubSection(RealmList<SectionsContainingArticleBean> section) {
        ArrayList<SectionsSubsection> mSubSectionData = new ArrayList<>();
        if (section.isValid() && section.size() > 0) {
            for (SectionsContainingArticleBean subSection : section) {
                mSubSectionData.add(new SectionsSubsection(subSection.getSection_id(), subSection.getSection_name()));
            }
        }
        return mSubSectionData;
    }

    public static void launchDetailActivity(Context context, int articleId, String sectionId, String articleLink,
                                            boolean isFromPager, ArticleDetail articleBean) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailActivity.ARTICLE_ID, articleId);
        intent.putExtra(DetailActivity.ARTICLE_LINKS, articleLink);
        intent.putExtra(DetailActivity.IS_FROM_PAGER, isFromPager);
        intent.putExtra(DetailActivity.ARTICLE_DETAILS, articleBean);
        intent.putExtra(DetailActivity.SECTION_ID, sectionId);
        context.startActivity(intent);
    }

    public static void launchCommentActivity(Context context, String articleId, String articleLink, String articleTitle,
                                             String publishDate, String commentCount, boolean isPostCommentScreen , String imgUrl) {
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra(CommentActivity.ARTICLE_ID, articleId);
        intent.putExtra(CommentActivity.ARTICLE_LINK, articleLink);
        intent.putExtra(CommentActivity.ARTICLE_TITLE, articleTitle);
        intent.putExtra(CommentActivity.ARTICLE_TIME, publishDate);
        intent.putExtra(CommentActivity.COMMENT_COUNT, commentCount);
        intent.putExtra(CommentActivity.IS_POST_COMMENT, isPostCommentScreen);
        intent.putExtra(CommentActivity.IMAGE_URL, imgUrl);
        context.startActivity(intent);
    }


    public static ArrayList<SectionArticleList> convertArticleDetailToSectionArticleList(ArrayList<ArticleDetail> mResults) {
        ArrayList<SectionArticleList> mConvertedData = new ArrayList<>();
        if (mResults != null && mResults.size() > 0) {
            int i = 0;
            for (ArticleDetail mArticleDetail : mResults) {
                mConvertedData.add(new SectionArticleList(Constants.VIEW_TYPE_ARTICLE, mArticleDetail, i));
                i++;
            }
        }
        return mConvertedData;
    }

    public static void markAsBookMark(Realm mRealm, int aid, String sid, String sname, String pd, String od, String pid, String ti, String au, String al, String bk, String gmt, String de, String le, int hi, RealmList<ImageBean> me, long bookmarkDate, boolean isRead, int add_pos
            , String multimediaPath, String articleType, String vid, String parentId, String location) {
        mRealm.beginTransaction();
        BookmarkBean bookmarkBean = new BookmarkBean(aid, sid, sname, pd, od, pid, ti, au, al, bk, gmt, de, le, hi, me,
                bookmarkDate, isRead, add_pos,
                multimediaPath, articleType, vid, parentId, location);
        mRealm.copyToRealmOrUpdate(bookmarkBean);
        mRealm.commitTransaction();
    }

    public static void removeFromBookMark(Realm mRealm, BookmarkBean bookmarkBean) {
        mRealm.beginTransaction();
        bookmarkBean.deleteFromRealm();
        mRealm.commitTransaction();
    }


    public static void checkLoginStatus(CustomArticleScrollView mArticleScrollView, boolean isUserLoggedIn,
                                        String pid, String sid, CustomScrollListenerForScrollView customScrollListenerForScrollView) {
        if (isPortfolioArticle(pid, sid) && !isUserLoggedIn) {
            mArticleScrollView.setScrollViewListener(customScrollListenerForScrollView);
            mArticleScrollView.setScrollable(false);
        } else {
            mArticleScrollView.setScrollViewListener(customScrollListenerForScrollView);
            mArticleScrollView.setScrollable(true);
        }
    }

    public static boolean isPortfolioArticle(String pid, String sid) {
        if (pid != null && sid != null) {
            if (pid.equalsIgnoreCase(Constants.PORTFOLIO_SECTION_ID) || sid.equalsIgnoreCase(Constants.PORTFOLIO_SECTION_ID)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    public static boolean checkLoginStatusForOptionMenu(boolean isUserLoggedIn, String pid,
                                                        String sid, CustomScrollListenerForScrollView customScrollListenerForScrollView) {
        if (isPortfolioArticle(pid, sid) && !isUserLoggedIn) {
//            launchLoginPopupActivity(context);
            if (customScrollListenerForScrollView != null)
                customScrollListenerForScrollView.showTransprentView(true);
            return false;
        } else {
            return true;
        }
    }


}
