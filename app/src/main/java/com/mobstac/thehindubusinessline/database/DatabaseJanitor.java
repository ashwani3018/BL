package com.mobstac.thehindubusinessline.database;

import android.text.TextUtils;

import com.mobstac.thehindubusinessline.Businessline;
import com.mobstac.thehindubusinessline.model.ArticleBean;
import com.mobstac.thehindubusinessline.model.BookmarkBean;
import com.mobstac.thehindubusinessline.model.NotificationBean;
import com.mobstac.thehindubusinessline.model.PersonalizeTable;
import com.mobstac.thehindubusinessline.model.PersonalizedID;
import com.mobstac.thehindubusinessline.model.SectionTable;
import com.mobstac.thehindubusinessline.model.WidgetsTable;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by root on 25/8/16.
 */
public class DatabaseJanitor {

    public static RealmResults<SectionTable> getSectionList(long parentId) {
        return Businessline.getRealmInstance().where(SectionTable.class).
                equalTo("parentId", parentId)
                .findAllAsync();
    }

    public static RealmResults<SectionTable> getRegionalList() {
        return Businessline.getRealmInstance().where(SectionTable.class)
                .equalTo("customScreen", 2)
                .findAllSorted("customScreenPri", Sort.ASCENDING);
    }


    public static RealmResults<SectionTable> getBurgerList(long parentId) {
        return Businessline.getRealmInstance().where(SectionTable.class).
                equalTo("parentId", parentId)
                .equalTo("show_on_burger", true)
                .equalTo("overridePriority", 0)
                .findAllAsync();
    }

    public static RealmResults<SectionTable> getHomePrioritySectionList(long parentId) {
        return Businessline.getRealmInstance().where(SectionTable.class)
                .equalTo("parentId", parentId)
                .equalTo("overridePriority", 0)
                .equalTo("show_on_burger", true)
                .findAll();
    }

    public static RealmResults<SectionTable> getOverriddenSections(long parentId) {
        return Businessline.getRealmInstance().where(SectionTable.class)
                .equalTo("parentId", parentId)
                .greaterThan("overridePriority", 0)
                .equalTo("show_on_burger", true)
                .findAllSortedAsync("overridePriority", Sort.ASCENDING);
    }

    public static RealmResults<PersonalizeTable> getPersonalizeTable() {
        return Businessline.getRealmInstance().where(PersonalizeTable.class).findAll();
    }

    public static RealmResults<WidgetsTable> getWidgetsTable() {
        return Businessline.getRealmInstance().where(WidgetsTable.class).findAll();
    }

    public static RealmResults<ArticleBean> getBannerArticle() {
        return Businessline.getRealmInstance().where(ArticleBean.class)
                .equalTo("isBanner", true)
                .equalTo("isHome", true).findAllSorted("insertionTime", Sort.ASCENDING);
    }

    public static RealmResults<ArticleBean> getHomeArticles() {
        return Businessline.getRealmInstance().where(ArticleBean.class)
                .equalTo("isHome", true).findAllSorted("insertionTime", Sort.ASCENDING);
    }

    public static RealmResults<ArticleBean> getNewsFeedArticle() {
        return Businessline.getRealmInstance().where(ArticleBean.class)
                .equalTo("isBanner", false)
                .equalTo("isHome", true).findAll();
    }


    public static RealmResults<ArticleBean> getPersonalizedFeedArticle(String secId) {
        return Businessline.getRealmInstance().where(ArticleBean.class)
                .equalTo("isHome", true)
                .equalTo("isBanner", false)
                .equalTo("sid", secId).findAllSorted("insertionTime", Sort.ASCENDING);
    }


    public static RealmResults<ArticleBean> getAllOverRidePersonalizedFeedArticle() {
        String[] fieldNames = {"opid", "pd"};
        Sort sort[] = {Sort.ASCENDING, Sort.DESCENDING};
        return Businessline.getRealmInstance().where(ArticleBean.class)
                .greaterThan("opid", 0)
                .equalTo("isBanner", false)
                .equalTo("isHome", true)
                .findAllSorted(fieldNames, sort);
    }

    public static List<ArticleBean> getAllNonOverridenPersonalizedFeedArticle() {
   /*     String[] fieldNames = {"pid", "pd"};
        Sort sort[] = {Sort.ASCENDING, Sort.DESCENDING};*/
        return Businessline.getRealmInstance().where(ArticleBean.class)
                .lessThanOrEqualTo("opid", 0)
                .equalTo("isBanner", false)
                .equalTo("isHome", true)
                .findAllSorted("pd", Sort.DESCENDING);
    }

    public static RealmResults<PersonalizedID> getPersonalizedFeedIds() {
        return Businessline.getRealmInstance().where(PersonalizedID.class)
                .findAll();
    }

    public static RealmResults<ArticleBean> getSectionContentArticle(String secId) {
        return Realm.getDefaultInstance().where(ArticleBean.class).equalTo("sid", secId).equalTo("isHome", false).equalTo("is_rn", false)
                .findAllSortedAsync("insertionTime", Sort.ASCENDING);
    }

    public static RealmResults<ArticleBean> getWidgetSectionContentArticle(String secId) {
        return Realm.getDefaultInstance().where(ArticleBean.class).equalTo("sid", secId).equalTo("isHome", false).equalTo("is_rn", false)
                .findAllSorted("insertionTime", Sort.ASCENDING);
    }

    public static RealmResults<SectionTable> getTopScrollSection() {
        return Businessline.getRealmInstance().where(SectionTable.class)
                .equalTo("parentId", 0)
                .notEqualTo("type", "static")
                .beginGroup()
                .equalTo("show_on_burger", true).or()
                .equalTo("show_on_explore", true)
                .endGroup()
                .findAllAsync();
    }

    public static RealmResults<SectionTable> getSectionNewsFeed() {
        return Businessline.getRealmInstance().where(SectionTable.class)
                .equalTo("customScreen", 1)
                .findAllSorted("customScreenPri", Sort.ASCENDING);
    }

    public static ArticleBean getArticleDetail(int articleId, String sid) {
        return Businessline.getRealmInstance().where(ArticleBean.class)
                .equalTo("aid", articleId).equalTo("sid", sid).findFirst();
    }

    public static RealmResults<BookmarkBean> getBookmarksArticles() {
        return Businessline.getRealmInstance().where(BookmarkBean.class).findAllSorted("bookmarkDate", Sort.DESCENDING);
    }

    public static RealmResults<BookmarkBean> getAllUnreadBookmarksArticles() {
        return Businessline.getRealmInstance().where(BookmarkBean.class).equalTo("isRead", false).findAllAsync();

    }

    public static RealmResults<NotificationBean> getAllUnreadNotificationArticles() {
        //long currentTimeInMilliSecconds = System.currentTimeMillis();
        //long twelveHours = 12 * 60 * 60 * 1000;
        //final long timeDifferenc = currentTimeInMilliSecconds - twelveHours;
        return Businessline.getRealmInstance().where(NotificationBean.class).equalTo("isRead", false)
                //.greaterThan("insertionTime", timeDifferenc)
                .findAllAsync();
    }

    public static void deleteAllBookmarksArticle() {
        Realm realm = Businessline.getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(BookmarkBean.class).findAll().deleteAllFromRealm();
            }
        });
    }

    public static void deleteAllNotificationArticle() {
        Realm realm = Businessline.getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(NotificationBean.class).findAll().deleteAllFromRealm();
            }
        });
    }

    public static RealmResults<SectionTable> getAllSectionNames(int parentId) {
        return Businessline.getRealmInstance().where(SectionTable.class)
                .equalTo("parentId", parentId).
                        notEqualTo("type", "static").findAll();
    }

    public static int getSectionPosition(int sectionId) {
        int position = 0;
        List<SectionTable> mSectionList = DatabaseJanitor.getTopScrollSection();
        for (SectionTable mSections : mSectionList) {
            if (mSections.getSecId() == sectionId) {
                return position;
            }
            position++;
        }
        return -1;
    }

    public static int getSubsectionPostion(int parentSectionId, int subSectionId) {

        int position = 0;
        List<SectionTable> subSections = Businessline.getRealmInstance().where(SectionTable.class).equalTo("parentId", parentSectionId).findAll();
        for (SectionTable mSections : subSections) {
            if (mSections.getSecId() == subSectionId) {
                return position;
            }
            position++;
        }
        return -1;
    }

    public static String getParentSectionName(int parentSectionId) {
        String sectionName = null;
//        sectionName = Businessline.getRealmInstance().where(SectionTable.class).equalTo("secId", parentSectionId).findFirst().getSecName();
        SectionTable table = Businessline.getRealmInstance().where(SectionTable.class).equalTo("secId", parentSectionId).findFirst();
        if(table == null) {
            return "";
        } else {
            return table.getSecName();
        }
    }

    public static RealmResults<SectionTable> getSectionDetails(int parentId) {
        return Businessline.getRealmInstance().where(SectionTable.class)
                .equalTo("secId", parentId).findAll();
    }



    public static void updateNotificationArticleReadFlag(int articleId) {
        Realm mRealm = Businessline.getRealmInstance();
        NotificationBean bean = mRealm.where(NotificationBean.class).equalTo("articleId", articleId).findFirst();
        if (bean != null && bean.isValid()) {
            mRealm.beginTransaction();
            bean.setRead(true);
            mRealm.commitTransaction();
        }
    }


    public static RealmResults<SectionTable> getSectionUsingFromKey(String fromSection) {
        Realm mRealm = Businessline.getRealmInstance();
        return mRealm.where(SectionTable.class)
                .equalTo("from", fromSection)
                .findAllAsync();
    }

    public static SectionTable getSectionUsingFromKey_1(String fromSection, int sectionId) {
        Realm mRealm = Businessline.getRealmInstance();
        return mRealm.where(SectionTable.class)
                .equalTo("from", fromSection)
                .equalTo("secId", sectionId)
                .findFirst();
    }


    public static int getSubsectionPostion_1(String fromSection, int subSectionId) {

        Realm mRealm = Businessline.getRealmInstance();
        List<SectionTable> subSections =  mRealm.where(SectionTable.class)
                .equalTo("from", fromSection)
                .findAll();

        int position = 0;
        for (SectionTable mSections : subSections) {
            if (mSections.getSecId() == subSectionId) {
                return position;
            }
            position++;
        }

        return -1;
    }

    public static RealmResults<SectionTable> getSectionTable(long sectionId, String tableFrom) {
        if(tableFrom == null || TextUtils.isEmpty(tableFrom)) {
            return Businessline.getRealmInstance().where(SectionTable.class).equalTo("secId", sectionId).findAllAsync();
        }
        else {
            return Businessline.getRealmInstance().where(SectionTable.class)
                    .equalTo("from", tableFrom)
                    .equalTo("secId", sectionId)
                    .findAllAsync();
        }
    }
}
