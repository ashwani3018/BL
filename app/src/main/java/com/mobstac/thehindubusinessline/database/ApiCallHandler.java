package com.mobstac.thehindubusinessline.database;

import android.content.Context;
import android.util.Log;

import com.mobstac.thehindubusinessline.Businessline;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.model.ArticleBean;
import com.mobstac.thehindubusinessline.model.HomeEvents;
import com.mobstac.thehindubusinessline.model.NewsFeed;
import com.mobstac.thehindubusinessline.model.NewsFeedPersonalizedData;
import com.mobstac.thehindubusinessline.model.NotificationSubscribe;
import com.mobstac.thehindubusinessline.model.PersonalizeTable;
import com.mobstac.thehindubusinessline.model.PersonalizedID;
import com.mobstac.thehindubusinessline.model.RetrofitResponseFromEventBus;
import com.mobstac.thehindubusinessline.model.Section;
import com.mobstac.thehindubusinessline.model.SectionConentModel;
import com.mobstac.thehindubusinessline.model.SectionTable;
import com.mobstac.thehindubusinessline.model.WidgetsTable;
import com.mobstac.thehindubusinessline.retrofit.Body;
import com.mobstac.thehindubusinessline.retrofit.RetrofitAPICaller;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.NetworkUtils;
import com.mobstac.thehindubusinessline.utils.SharedPreferenceHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arvind on 23/8/16.
 */
public class ApiCallHandler {

    public static final String FROM_SECTION = "section";
    public static final String FROM_PORTFOLIO = "portfolio";
    public static final String FROM_BLINK = "blink";
    public static final String FROM_HOME = "home";
    private static final String TAG = "ApiCallHandler";

    public static void fetchSectionCall(final Context context) {

        Call<Section> call = RetrofitAPICaller.getInstance(context).getWebserviceAPIs()
                .sectionApi(new Body());
        call.enqueue(new Callback<Section>() {
            @Override
            public void onResponse(Call<Section> call, Response<Section> response) {
                if (response.isSuccessful() && response.body() != null) {
                    /* Loading Banner into SharedPreferences */
                    SharedPreferenceHelper.putInt(context, Constants.BANNER_SECTION_ID,
                            response.body().getData().getHome().getBanner().getSecId());
                    SharedPreferenceHelper.putString(context, Constants.BANNER_SECTON_TYPE,
                            response.body().getData().getHome().getBanner().getType());

                    insertSectionData(response.body(), context);
                }
            }

            @Override
            public void onFailure(Call<Section> call, Throwable t) {
                Log.i(TAG, " Section Failure" + t.toString());
                Businessline.getmEventBus().post(new HomeEvents(Constants.EVENT_HOME_FAILURE));
            }
        });
    }

    public static void insertSectionData(Section section, Context ctxParam) {
        Realm realm = Businessline.getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<SectionTable> result = realm.where(SectionTable.class)
                        .findAll();
                result.deleteAllFromRealm();
            }
        });

        String staticPageUrl = "";
        boolean isStaticPageEnable = false;
        int positionInListView = -1;

        if(section.getData().getHome().staticPageUrl == null) {
            staticPageUrl = "";
            isStaticPageEnable = false;
            positionInListView = -1;
        } else {
            staticPageUrl = section.getData().getHome().staticPageUrl.getUrlAndSectionId();
            isStaticPageEnable = section.getData().getHome().staticPageUrl.isEnabled;
            positionInListView = section.getData().getHome().staticPageUrl.position;
        }


        realm.beginTransaction();
        realm.copyToRealmOrUpdate(new SectionTable(1000, "Home", "home", 0, 1, 0, true, false, null, null,
                "", 0, 0, "", FROM_SECTION, staticPageUrl, isStaticPageEnable, positionInListView, null));
        realm.commitTransaction();

        for (Section.DataEntity.SectionEntity sectionName : section.getData().getSection()) {
            realm.beginTransaction();
            RealmList<SectionTable> subSection = null;
            if (sectionName.getSubSections() != null && sectionName.getSubSections().size() > 0) {
                subSection = new RealmList<>();
                for (Section.DataEntity.SectionEntity subsection : sectionName.getSubSections()) {

                    if(subsection.staticPageUrl == null) {
                        staticPageUrl = "";
                        isStaticPageEnable = false;
                        positionInListView = -1;
                    } else {
                        staticPageUrl = subsection.staticPageUrl.getUrlAndSectionId();
                        isStaticPageEnable = subsection.staticPageUrl.isEnabled;
                        positionInListView = subsection.staticPageUrl.position;
                    }

                    subSection.add(new SectionTable(
                            subsection.getSecId(), subsection.getSecName(), subsection.getType(),
                            subsection.getParentId(), subsection.getPriority(),
                            subsection.getOverridePriority(),
                            subsection.isShow_on_burger(),
                            subsection.isShow_on_explore(), subsection.getImage(),
                            null, subsection.getLink(), subsection.getCustomScreen(),
                            subsection.getCustomScreenPri(), subsection.getWebLink(), FROM_SECTION,
                            staticPageUrl, isStaticPageEnable, positionInListView, subsection.getImage_v2()));
                }
            }

            if(sectionName.staticPageUrl == null) {
                staticPageUrl = "";
                isStaticPageEnable = false;
                positionInListView = -1;
            } else {
                staticPageUrl = sectionName.staticPageUrl.getUrlAndSectionId();
                isStaticPageEnable = sectionName.staticPageUrl.isEnabled;
                positionInListView = sectionName.staticPageUrl.position;
            }

            realm.copyToRealmOrUpdate(new SectionTable(
                    sectionName.getSecId(),
                    sectionName.getSecName(),
                    sectionName.getType(), sectionName.getParentId(), sectionName.getPriority(),
                    sectionName.getOverridePriority(),
                    sectionName.isShow_on_burger(),
                    sectionName.isShow_on_explore(), sectionName.getImage(),
                    subSection, sectionName.getLink(), sectionName.getCustomScreen(),
                    sectionName.getCustomScreenPri(), sectionName.getWebLink(), FROM_SECTION,
                    staticPageUrl, isStaticPageEnable, positionInListView, sectionName.getImage_v2()));

            realm.copyToRealmOrUpdate(
                    new NotificationSubscribe(
                            sectionName.getSecId(),
                            sectionName.getSecName(),
                            0,
                            new Date()));
            realm.commitTransaction();
        }

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(new SectionTable(1001,
                ctxParam.getResources().getString(R.string.info_aboutus), "static", 0, 1000, 0,
                true, false, null, null, "", 0, 0, "", FROM_SECTION, "", false, -1,"" ));
        realm.commitTransaction();

    /*    realm.beginTransaction();
        realm.copyToRealmOrUpdate(new SectionTable(1002,
                ctxParam.getResources().getString(R.string.info_help), "static", 0, 1000, 0, true,
                false, null, null, "", 0, 0, ""));
        realm.commitTransaction();
*/

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(new SectionTable(1003,
                ctxParam.getResources().getString(R.string.info_termsconditions), "static",
                0, 1000, 0, true, false, null, null, "", 0, 0, "", FROM_SECTION, "", false, -1,""));
        realm.commitTransaction();

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(new SectionTable(1004,
                ctxParam.getResources().getString(R.string.privacy_policy), "static",
                0, 1000, 0, true, false, null, null, "", 0, 0, "", FROM_SECTION, "", false, -1,""));
        realm.commitTransaction();

        /* Inserting widgets data*/

        realm.beginTransaction();
        realm.delete(WidgetsTable.class);
        realm.commitTransaction();

        for (Section.DataEntity.HomeEntity.WidgetEntity widgets :
                section.getData().getHome().getWidget()) {
            realm.beginTransaction();

            realm.copyToRealmOrUpdate(new WidgetsTable(
                    widgets.getSecId(),
                    widgets.getParentSecId(),
                    widgets.getHomePriority(),
                    widgets.getOverridePriority(),
                    widgets.getSecName(),
                    widgets.getType()
            ));

            realm.commitTransaction();
        }


        /*Insert portfolio sections*/

        for (Section.DataEntity.SectionEntity sectionName : section.getData().getPortfolio()) {
            realm.beginTransaction();
            RealmList<SectionTable> subSection = null;
            if (sectionName.getSubSections() != null && sectionName.getSubSections().size() > 0) {
                subSection = new RealmList<>();
                for (Section.DataEntity.SectionEntity subsection : sectionName.getSubSections()) {
                    if(subsection.staticPageUrl == null) {
                        staticPageUrl = "";
                        isStaticPageEnable = false;
                        positionInListView = -1;
                    } else {
                        staticPageUrl = subsection.staticPageUrl.getUrlAndSectionId();
                        isStaticPageEnable = subsection.staticPageUrl.isEnabled;
                        positionInListView = subsection.staticPageUrl.position;
                    }
                    subSection.add(new SectionTable(
                            subsection.getSecId(), subsection.getSecName(), subsection.getType(),
                            subsection.getParentId(), subsection.getPriority(),
                            subsection.getOverridePriority(),
                            subsection.isShow_on_burger(),
                            subsection.isShow_on_explore(), subsection.getImage(),
                            null, subsection.getLink(), subsection.getCustomScreen(),
                            subsection.getCustomScreenPri(), subsection.getWebLink(), FROM_PORTFOLIO,
                            staticPageUrl, isStaticPageEnable, positionInListView, subsection.getImage_v2()));
                }
            }

            if(sectionName.staticPageUrl == null) {
                staticPageUrl = "";
                isStaticPageEnable = false;
                positionInListView = -1;
            } else {
                staticPageUrl = sectionName.staticPageUrl.getUrlAndSectionId();
                isStaticPageEnable = sectionName.staticPageUrl.isEnabled;
                positionInListView = sectionName.staticPageUrl.position;
            }

            realm.copyToRealmOrUpdate(new SectionTable(
                    sectionName.getSecId(),
                    sectionName.getSecName(),
                    sectionName.getType(), sectionName.getParentId(), sectionName.getPriority(),
                    sectionName.getOverridePriority(),
                    sectionName.isShow_on_burger(),
                    sectionName.isShow_on_explore(), sectionName.getImage(),
                    subSection, sectionName.getLink(), sectionName.getCustomScreen(),
                    sectionName.getCustomScreenPri(), sectionName.getWebLink(), FROM_PORTFOLIO,
                    staticPageUrl, isStaticPageEnable, positionInListView, sectionName.getImage_v2()));

            realm.copyToRealmOrUpdate(
                    new NotificationSubscribe(
                            sectionName.getSecId(),
                            sectionName.getSecName(),
                            0,
                            new Date()));
            realm.commitTransaction();
        }
        /*portfolio section end*/

        /*Insert BLINK Data*/

        for (Section.DataEntity.SectionEntity sectionName : section.getData().getBLink()) {
            realm.beginTransaction();
            RealmList<SectionTable> subSection = null;
            if (sectionName.getSubSections() != null && sectionName.getSubSections().size() > 0) {
                subSection = new RealmList<>();
                for (Section.DataEntity.SectionEntity subsection : sectionName.getSubSections()) {
                    if(subsection.staticPageUrl == null) {
                        staticPageUrl = "";
                        isStaticPageEnable = false;
                        positionInListView = -1;
                    } else {
                        staticPageUrl = subsection.staticPageUrl.getUrlAndSectionId();
                        isStaticPageEnable = subsection.staticPageUrl.isEnabled;
                        positionInListView = subsection.staticPageUrl.position;
                    }
                    subSection.add(new SectionTable(
                            subsection.getSecId(), subsection.getSecName(), subsection.getType(),
                            subsection.getParentId(), subsection.getPriority(),
                            subsection.getOverridePriority(),
                            subsection.isShow_on_burger(),
                            subsection.isShow_on_explore(), subsection.getImage(),
                            null, subsection.getLink(), subsection.getCustomScreen(),
                            subsection.getCustomScreenPri(), subsection.getWebLink(), FROM_BLINK,
                            staticPageUrl, isStaticPageEnable, positionInListView, subsection.getImage_v2()));
                }
            }

            if(sectionName.staticPageUrl == null) {
                staticPageUrl = "";
                isStaticPageEnable = false;
                positionInListView = -1;
            } else {
                staticPageUrl = sectionName.staticPageUrl.getUrlAndSectionId();
                isStaticPageEnable = sectionName.staticPageUrl.isEnabled;
                positionInListView = sectionName.staticPageUrl.position;
            }

            realm.copyToRealmOrUpdate(new SectionTable(
                    sectionName.getSecId(),
                    sectionName.getSecName(),
                    sectionName.getType(), sectionName.getParentId(), sectionName.getPriority(),
                    sectionName.getOverridePriority(),
                    sectionName.isShow_on_burger(),
                    sectionName.isShow_on_explore(), sectionName.getImage(),
                    subSection, sectionName.getLink(), sectionName.getCustomScreen(),
                    sectionName.getCustomScreenPri(), sectionName.getWebLink(), FROM_BLINK,
                    staticPageUrl, isStaticPageEnable, positionInListView,sectionName.getImage_v2()));

            realm.copyToRealmOrUpdate(
                    new NotificationSubscribe(
                            sectionName.getSecId(),
                            sectionName.getSecName(),
                            0,
                            new Date()));
            realm.commitTransaction();
        }

        /*end BLink*/


        /* Inserting personalize data*/

        realm.beginTransaction();
        realm.delete(PersonalizeTable.class);
        realm.commitTransaction();


        for (Section.DataEntity.HomeEntity.PersonalizeEntity personalize :
                section.getData().getHome().getPersonalize()) {
            realm.beginTransaction();

            realm.copyToRealmOrUpdate(new PersonalizeTable(
                    personalize.getSecId(),
                    personalize.getHomePriority(),
                    personalize.getOverridePriority(),
                    personalize.getSecName(),
                    personalize.getType()
            ));

            realm.commitTransaction();

        }


        NetworkUtils.saveSectionSyncTimePref(ctxParam, Constants.SECTION_LIST);

        Businessline.getmEventBus().post(new HomeEvents(Constants.EVENT_INSERTED_SECTION_API_DATABASE));


    }


    public static void fetchHomeData(final Context context, final boolean isHome, long lastSycTime, final boolean isDeleteAllHomeData) {
        List<String> mPersonalizeList = new ArrayList<>();
        final boolean isFetchFromPersonalizeFeed = SharedPreferenceHelper.getBoolean(
                context,
                Constants.PREFERENCES_FETCH_PERSONALIZE_FEED,
                false);
        if (isFetchFromPersonalizeFeed) {
            mPersonalizeList = SharedPreferenceHelper.getStringArrayPref(
                    context,
                    Constants.PREFERENCES_NEWS_FEED);

            List<String> mSelectedCities = SharedPreferenceHelper.getStringArrayPref(
                    context,
                    Constants.PREFERENCES_CITY_INTEREST);
            for (int i = 0; i < mSelectedCities.size(); i++) {
                mPersonalizeList.add(mSelectedCities.get(i));
            }
            if (mPersonalizeList.contains("11") && mSelectedCities.size() > 0) {
                mPersonalizeList.remove("11");
            }
        } else {
            RealmResults<PersonalizeTable> mPersonalizeData = DatabaseJanitor.getPersonalizeTable();

            if (mPersonalizeData.size() != 0) {
                for (PersonalizeTable mPersonalizeTable : mPersonalizeData) {
                    mPersonalizeList.add(String.valueOf(mPersonalizeTable.getSecId()));
                    Log.d(TAG, "fetchHomeData: Personalize ids" + mPersonalizeTable.getSecId());
                }
            }
        }

        int bannerId = SharedPreferenceHelper.getInt(context, Constants.BANNER_SECTION_ID, 0);

        Call<NewsFeed> call = RetrofitAPICaller.getInstance(context).getWebserviceAPIs()
                .newsFeedApi(new Body(mPersonalizeList, String.valueOf(bannerId), lastSycTime));
        call.enqueue(new Callback<NewsFeed>() {
            @Override
            public void onResponse(Call<NewsFeed> call, Response<NewsFeed> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "onResponse: Home Success");
                    insertHomeData(response.body(), context, isHome, isDeleteAllHomeData);
                }
            }

            @Override
            public void onFailure(Call<NewsFeed> call, Throwable t) {
                Log.i(TAG, "Home Failure" + t.toString());
                Businessline.getmEventBus().post(new HomeEvents(Constants.EVENT_HOME_FAILURE));
            }
        });
    }

    public static void insertHomeData(NewsFeed newsData, Context context, boolean isHome, boolean isDeleteAllData) {
        Realm realm = Businessline.getRealmInstance();
        NewsFeed.NewsFeedBean mNewsFeedBean = newsData.getNewsFeed();
        if (mNewsFeedBean != null) {
            List<ArticleBean> mBannerData = mNewsFeedBean.getBanner();
            List<Integer> mPersonalizedData = mNewsFeedBean.getPersonalizeID();
            List<NewsFeedPersonalizedData> mPersonalizedNewsFeedData = mNewsFeedBean.getArticles();

            if (isDeleteAllData) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<ArticleBean> mAllHomeData = DatabaseJanitor.getHomeArticles();
                        mAllHomeData.deleteAllFromRealm();
                    }
                });

            }

            // insert personalized data into database
            if (mPersonalizedData != null && mPersonalizedData.size() > 0) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<PersonalizedID> result = realm.where(PersonalizedID.class)
                                .findAll();
                        result.deleteAllFromRealm();
                    }
                });

                for (int personalizeID : mPersonalizedData) {
                    realm.beginTransaction();
                    PersonalizedID mPersonalizedID = new PersonalizedID();
                    mPersonalizedID.setPersonalizeID(personalizeID);
                    realm.copyToRealmOrUpdate(mPersonalizedID);
                    realm.commitTransaction();

                }
            }

            //insert banner data into database
            if (mBannerData != null && mBannerData.size() > 0) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<ArticleBean> mOldBannerData = DatabaseJanitor.getBannerArticle();
                        Log.i(TAG, "execute: Delete Banner Data " + mOldBannerData.size());
                        mOldBannerData.deleteAllFromRealm();
                    }
                });

                for (ArticleBean bean : mBannerData) {
                    realm.beginTransaction();
                    bean.setHome(true);
                    bean.setBanner(true);
                    bean.setInsertionTime(System.currentTimeMillis());
                    realm.copyToRealm(bean);
                    realm.commitTransaction();
                    Log.i(TAG, "insert banner data: ");
                }
            }

            //insert personalized newsfeed into database

            if (mPersonalizedNewsFeedData != null && mPersonalizedNewsFeedData.size() > 0) {
                for (NewsFeedPersonalizedData mNewsFeedData : mPersonalizedNewsFeedData) {
                    if (mNewsFeedData != null && mNewsFeedData.getData() != null && mNewsFeedData.getData().size() > 0) {
                        final String sectionId = mNewsFeedData.getSec_id();
                        final List<ArticleBean> mNewsFeed = mNewsFeedData.getData();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                RealmResults<ArticleBean> mOldNewsFeed = DatabaseJanitor.getPersonalizedFeedArticle(sectionId);
                                Log.i(TAG, "execute: Delete news feed for SetionId " + sectionId);
                                mOldNewsFeed.deleteAllFromRealm();
                            }
                        });
                        if (mNewsFeed.size() > 0) {
                            SharedPreferenceHelper.putInt(context, Constants.NEWSFEED_CHUNK_SIZE, mNewsFeed.size());
                        }
                        realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                for (ArticleBean mArticleBean : mNewsFeed) {
                                    mArticleBean.setHome(true);
                                    mArticleBean.setInsertionTime(System.currentTimeMillis());
                                    realm.copyToRealm(mArticleBean);
                                    Log.i(TAG, "insert news feed data: " + mArticleBean.getSid());
                                }
                            }
                        });

                    }
                }
            }

            NetworkUtils.saveSectionSyncTimePref(context, "Home");
        }

        Businessline.getmEventBus().post(new HomeEvents(Constants.EVENT_INSERTED_NEWSFEED_API_DATABASE));
    }

    public static void fetchSectionContent(final Context context, final String id,
                                           final String type, final boolean isHome, long sycTime) {
        Log.i("Section Failure", id+" id , "+type+" type ,"+sycTime +" time");
        Call<SectionConentModel> call = RetrofitAPICaller.getInstance(context).getWebserviceAPIs()
                .sectionContentApi(new Body(id, type, sycTime));
        call.enqueue(new Callback<SectionConentModel>() {
            @Override
            public void onResponse(Call<SectionConentModel> call, Response<SectionConentModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    insertSectionContentData(response.body(), context, id, type, isHome);
                    Businessline.getmEventBus().post(new RetrofitResponseFromEventBus(id, type, true));
                }
            }

            @Override
            public void onFailure(Call<SectionConentModel> call, Throwable t) {
                Log.i("Section Failure", t.toString());
                Businessline.getmEventBus().post(new HomeEvents(Constants.EVENT_HOME_FAILURE));
                Businessline.getmEventBus().post(new RetrofitResponseFromEventBus(id, type, false));
            }
        });

    }




    //================================================================  RX  fetchSectionContent (Start)
    public static void fetchSectionContentRX(Consumer onNext, Consumer onError, final Context context, final int page, final String id, final String type, final boolean isHome, long sycTime) {

        Log.i("Section Failure", id+" id , "+type+" type ,"+sycTime +" time");
        Flowable<SectionConentModel> call = RetrofitAPICaller.getInstance(context).getWebserviceAPIs().sectionContentApiRX(new Body(id, type, sycTime, page));
        call.subscribeOn(Schedulers.newThread())
                .map(new Function<SectionConentModel, Boolean>() {
                    @Override
                    public Boolean apply(SectionConentModel sectionConentModel) throws Exception {
                        RealmConfiguration mRealmConfiguration = new RealmConfiguration.Builder()
                                .name("TheHinduBusinessline.realm")
                                .schemaVersion(Businessline.DATABASE_SCHEMA_VERSION)
                                .migration(new TheHinduRealmMigration())
                                .build();
                        final Realm realm = Realm.getInstance(mRealmConfiguration);
                        final List<ArticleBean> mArticleList = sectionConentModel.getData().getArticle();

                        if (mArticleList != null && mArticleList.size() > 0) {
                            if (page == 1) {
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        RealmResults<ArticleBean> result = realm.where(ArticleBean.class)
                                                .equalTo("sid", id)
                                                .equalTo("isHome", false)
                                                .equalTo("is_rn", false)
                                                .findAll();
                                        result.deleteAllFromRealm();
                                    }
                                });
                            }

                            final ArrayList<ArticleBean> finalArticleList = new ArrayList<>();
                            finalArticleList.addAll(mArticleList);

                           /* for (ArticleBean bean : mArticleList) {
//                           ArticleBean data = realm.where(ArticleBean.class).equalTo("aid", bean.getAid()).findFirst();

                                ArticleBean data = realm.where(ArticleBean.class).equalTo("sid", id).equalTo("aid", bean.getAid()).findFirst();
                                if (data == null) {
                                    finalArticleList.add(bean);
                                }
                            }*/


                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm bgRealm) {
                                    for (ArticleBean bean : finalArticleList) {
                                        // This is for duplicate check
                                        bean.setInsertionTime(System.currentTimeMillis());
                                        bean.setPage(page);
                                        bgRealm.copyToRealm(bean);

                                    }
                                }
                            });

                            NetworkUtils.saveSectionSyncTimePref(context, sectionConentModel.getData().getSname());
                            Log.d(TAG, "apply 1 : "+sectionConentModel.getData().getSname());
                        }

//                        NetworkUtils.saveSectionSyncTimePref(context, sectionConentModel.getData().getSname());

                        int count = SharedPreferenceHelper.getInt(context, Constants.PREFERENCE_SECTION_COUNT, 0);

                        count = count + 1;

                        SharedPreferenceHelper.putInt(context, Constants.PREFERENCE_SECTION_COUNT, count);
                        if (isHome) {
                            List<WidgetsTable> mWidgetsList = DatabaseJanitor.getWidgetsTable();
                            int size = mWidgetsList.size();
                            if (isHome && count == size) {
                                Businessline.getmEventBus().post(new HomeEvents(Constants.EVENT_INSERTED_WIDGET_API_DATABASE));
                            }
                        }

                        return true;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError);

    }


    //================================================================ RX  fetchSectionContent (End)



    public static void insertSectionContentData(SectionConentModel sectionConentModel,
                                                Context context,
                                                final String id,
                                                final String type,
                                                boolean isHome) {
        Log.i("Section Failure", id+" id , "+type+" type ,");
        final Realm realm = Businessline.getRealmInstance();
        final List<ArticleBean> mArticleList = sectionConentModel.getData().getArticle();
        if (mArticleList != null && mArticleList.size() > 0) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<ArticleBean> result = realm.where(ArticleBean.class)
                            .equalTo("sid", id)
                            .equalTo("isHome", false)
                            .equalTo("is_rn", false)
                            .findAll();
                    result.deleteAllFromRealm();
                }
            });

            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    for (ArticleBean bean : mArticleList) {
                        bean.setInsertionTime(System.currentTimeMillis());
                        bgRealm.copyToRealm(bean);
                        Log.i(TAG, "insertSectionContentData: " + bean.getSname());
                        Log.i(TAG, "insertSectionContentData: " + bean.getTi());
                    }
                }
            });



        }

        // Problem for first time pagination not send 0 for synch time
//        NetworkUtils.saveSectionSyncTimePref(context, sectionConentModel.getData().getSname());
        int count = SharedPreferenceHelper.getInt(context, Constants.PREFERENCE_SECTION_COUNT, 0);

        count = count + 1;

        SharedPreferenceHelper.putInt(context, Constants.PREFERENCE_SECTION_COUNT, count);
        if (isHome) {
            List<WidgetsTable> mWidgetsList = DatabaseJanitor.getWidgetsTable();
            int size = mWidgetsList.size();
            if (isHome && count == size) {
                Businessline.getmEventBus().post(new HomeEvents(Constants.EVENT_INSERTED_WIDGET_API_DATABASE));
            }
        }
    }


}
