package com.ns.clevertap;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.text.TextUtils;
import android.util.Log;

import com.clevertap.android.sdk.CleverTapAPI;
import com.netoperation.model.UserProfile;
import com.netoperation.util.AppDateUtil;
import com.netoperation.util.NetConstants;
import com.netoperation.util.THPPreferences;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;
import com.ns.utils.TextUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class CleverTapUtil {

    /**
     * Update CleverTap UserProfile
     */
    public static void cleverTapUpdateProfile(Context context, boolean isNewLoggin, UserProfile userProfilee, boolean isHasSubscriptionPlan, boolean isHasFreePlan) {
        if(userProfilee == null) {
            return;
        }
        Observable.just(userProfilee)
                .subscribeOn(Schedulers.newThread())
                .map(userProfile -> {
                    if(CleverTapAPI.getDefaultInstance(context) == null) {
                        return "";
                    }
                    CleverTapAPI clevertap = CleverTapAPI.getDefaultInstance(context);
                    HashMap<String, Object> profileUpdate = new HashMap<String, Object>();

                    profileUpdate.put(THPConstants.CT_KEY_platform,"app");

                    String email = userProfile.getEmailId();
                    String contact = userProfile.getContact();

                    if (!ResUtil.isEmpty(email)) {
                        //email = userProfile.getUserEmailOrContact();
                        profileUpdate.put(THPConstants.CT_Custom_KEY_Email, email);
                        profileUpdate.put(THPConstants.CT_KEY_Email, email);
                    }

                    if (!ResUtil.isEmpty(contact)) {
                        //contact = userProfile.getUserEmailOrContact();
                        profileUpdate.put(THPConstants.CT_KEY_Phone, THPConstants.MOBILE_COUNTRY_CODE + contact);
                        profileUpdate.put(THPConstants.CT_KEY_Mobile_Number, contact);
                    }


                    //Update pre-defined profile properties
                    profileUpdate.put(THPConstants.CT_KEY_Identity, userProfile.getUserId());
                    profileUpdate.put(THPConstants.CT_KEY_UserId, userProfile.getUserId());
                    profileUpdate.put(THPConstants.CT_KEY_DOB, userProfile.getDOB());
                    profileUpdate.put(THPConstants.CT_KEY_Name, userProfile.getFullNameForProfile());


                    profileUpdate.put(THPConstants.CT_KEY_Gender, userProfile.getGender());
                    profileUpdate.put(THPConstants.CT_KEY_Login_Source, THPPreferences.getInstance(context).getLoginSource());
                    profileUpdate.put(THPConstants.CT_KEY_isSubscribedUser, isHasSubscriptionPlan);
                    profileUpdate.put(THPConstants.CT_KEY_isFreeUser, isHasFreePlan);

                    String permission = Manifest.permission.ACCESS_COARSE_LOCATION;
                    int res = context.checkCallingOrSelfPermission(permission);
                    if (res == PackageManager.PERMISSION_GRANTED) {
                        Location location = clevertap.getLocation();
                        // do something with location,
                        // optionally set on CleverTap for use in segmentation etc
                        clevertap.setLocation(location);
                    }

                    //Update custom profile properties
                   if (userProfile.getUserPlanList() != null && userProfile.getUserPlanList().size() > 0) {
                       profileUpdate.put(THPConstants.CT_KEY_Plan_Type, userProfile.getUserPlanList().get(0).getPlanName());
                     //  profileUpdate.put(THPConstants.CT_KEY_Subscription_End_Date, userProfile.getUserPlanList().get(0).geteDate());
                     //  profileUpdate.put(THPConstants.CT_KEY_Subscription_Start_Date, userProfile.getUserPlanList().get(0).getsDate());

                       try {
                           final SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");

                           String sd = userProfile.getUserPlanList().get(0).getsDate();
                           final Date sDate = new Date(sdf.parse(sd).getTime());

                           String ed = userProfile.getUserPlanList().get(0).geteDate();
                           final Date eDate = new Date(sdf.parse(ed).getTime());

                           profileUpdate.put(THPConstants.CT_KEY_Subscription_End_Date, eDate);
                           profileUpdate.put(THPConstants.CT_KEY_Subscription_Start_Date, sDate);
                       }catch (Exception e){
                       }

                    } else {
                        profileUpdate.put(THPConstants.CT_KEY_Plan_Type, "Plan Expired");
                    }

                    if (isNewLoggin) {
                        clevertap.onUserLogin(profileUpdate);
                    } else {
                        clevertap.pushProfile(profileUpdate);
                    }
                    return "";
                })
                .subscribe(v -> {
                }, t -> {
                    Log.i("", "");
                });

    }

    public static void cleverTapDetailPageEvent(Context context, String IS_Article_PREMIUM, int articleId, String articleTitle,
                                                String articleLink, String sectionName, String articleType) {
        HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
        prodViewedAction.put(THPConstants.CT_KEY_platform,"app");
        prodViewedAction.put(THPConstants.CT_KEY_UserId, getUserId(context));
        prodViewedAction.put(THPConstants.CT_KEY_IS_Article_PREMIUM, IS_Article_PREMIUM);
        prodViewedAction.put(THPConstants.CT_KEY_Article_Id, articleId);
        prodViewedAction.put(THPConstants.CT_KEY_Article_Title, articleTitle);
        prodViewedAction.put(THPConstants.CT_KEY_Article_Link, articleLink);
        prodViewedAction.put(THPConstants.CT_KEY_Article_Section, sectionName);
        prodViewedAction.put(THPConstants.CT_KEY_Article_Type, articleType);
        //This Event is Replaced with @PageVisit event
        //CleverTapAPI.getDefaultInstance(context).pushEvent(THPConstants.CT_EVENT_ARTICLE_READ, prodViewedAction);
    }

    public static void cleverTapDetailPageEvent(Context context, boolean isBriefing, String IS_Article_PREMIUM, String from, int articleId, String articleTitle,
                                                String articleLink, String sectionName, String articleType, String Article_USER_TIME_SPENT) {
        HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
        prodViewedAction.put(THPConstants.CT_KEY_platform,"app");
        prodViewedAction.put(THPConstants.CT_KEY_UserId, getUserId(context));
        prodViewedAction.put(THPConstants.CT_KEY_IS_Article_PREMIUM, IS_Article_PREMIUM);
        if (isBriefing) {
            prodViewedAction.put(THPConstants.CT_KEY_Article_IS_FROM, "Briefing - " + from);
        } else {
            prodViewedAction.put(THPConstants.CT_KEY_Article_IS_FROM, from);
        }
        prodViewedAction.put(THPConstants.CT_KEY_Article_Id, articleId);
        prodViewedAction.put(THPConstants.CT_KEY_Article_Title, articleTitle);
        prodViewedAction.put(THPConstants.CT_KEY_Article_Link, articleLink);
        prodViewedAction.put(THPConstants.CT_KEY_Article_Section, sectionName);
        prodViewedAction.put(THPConstants.CT_KEY_Article_Type, articleType);
        prodViewedAction.put(THPConstants.CT_KEY_Article_USER_TIME_SPENT, Article_USER_TIME_SPENT);
        //This Event is Replaced with @PageVisit event
        //CleverTapAPI.getDefaultInstance(context).pushEvent(THPConstants.CT_EVENT_ARTICLE_READ, prodViewedAction);
    }

    public static void cleverTapTHPTabTimeSpent(Context context, String from, String Article_USER_TIME_SPENT,long timeInSeconds) {
        HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
        prodViewedAction.put(THPConstants.CT_KEY_AppSections, from);
        prodViewedAction.put(THPConstants.CT_KEY_platform,"app");
        prodViewedAction.put(THPConstants.CT_KEY_UserId, getUserId(context));
        prodViewedAction.put(THPConstants.CT_KEY_TimeSpent, Article_USER_TIME_SPENT);
        prodViewedAction.put(THPConstants.CT_KEY_TimeSpentSeconds, timeInSeconds);
        if(CleverTapAPI.getDefaultInstance(context) != null) {
            CleverTapAPI.getDefaultInstance(context).pushEvent(THPConstants.CT_TIME_SPENT, prodViewedAction);
        }

    }

    public static void cleverTapLogoutEvent(Context context, UserProfile userProfile) {
        if(userProfile == null) {
            return;
        }
        HashMap<String, Object> logoutActions = new HashMap<String, Object>();

        String email = userProfile.getEmailId();
        String contact = userProfile.getContact();

        if (ResUtil.isEmpty(email)) {
            email = userProfile.getUserEmailOrContact();
        }

        if (ResUtil.isEmpty(contact)) {
            contact = userProfile.getUserEmailOrContact();
        }

        logoutActions.put(THPConstants.CT_KEY_Name, userProfile.getFullName());
        logoutActions.put(THPConstants.CT_KEY_platform,"app");
        logoutActions.put(THPConstants.CT_Custom_KEY_Email, email);
        logoutActions.put(THPConstants.CT_KEY_Email, email);
        logoutActions.put(THPConstants.CT_KEY_Phone, contact);
        logoutActions.put(THPConstants.CT_KEY_UserId, userProfile.getUserId());
        logoutActions.put(THPConstants.CT_KEY_Login_Source, userProfile.getLoginSource());
        if(CleverTapAPI.getDefaultInstance(context) != null) {
            CleverTapAPI.getDefaultInstance(context).pushEvent(THPConstants.CT_EVENT_SIGNOUT, logoutActions);
        }

    }

    public static void cleverTapEvent(Context context,String eventName,HashMap<String,Object> map){
        if(map==null){
            map = new HashMap<>();
        }
        map.put(THPConstants.CT_KEY_platform,"app");
        map.put(THPConstants.CT_KEY_UserId, getUserId(context));
        if(CleverTapAPI.getDefaultInstance(context) != null) {
            CleverTapAPI.getDefaultInstance(context).pushEvent(eventName, map);
        }
    }


    public static void cleverTapEventPayNow(Context context,int packValue,String packDuration, String packName){

        HashMap<String,Object>   map = new HashMap<>();

        map.put(THPConstants.CT_KEY_platform,"app");
        map.put(THPConstants.CT_KEY_UserId, getUserId(context));
        map.put(THPConstants.CT_KEY_Pack_value,packValue);
        map.put(THPConstants.CT_KEY_Pack_duration,packDuration);
        map.put(THPConstants.CT_KEY_Pack_Name,packName);
        if(CleverTapAPI.getDefaultInstance(context) != null) {
            CleverTapAPI.getDefaultInstance(context).pushEvent(THPConstants.CT_EVENT_PAY_NOW, map);
        }
    }
    public static void cleverTapEventProductViewed(Context context,String packValue,String packDuration, String packName){

        HashMap<String,Object>   map = new HashMap<>();

        map.put(THPConstants.CT_KEY_platform,"app");
        map.put(THPConstants.CT_KEY_UserId, getUserId(context));
        map.put(THPConstants.CT_KEY_Pack_Name,packName);
        map.put(THPConstants.CT_KEY_Pack_value,packValue);
        map.put(THPConstants.CT_KEY_Pack_duration,packDuration);
        if(CleverTapAPI.getDefaultInstance(context) != null) {
            CleverTapAPI.getDefaultInstance(context).pushEvent(THPConstants.CT_EVENT_PRODUCT_VIEWED, map);
        }
    }

    public static void cleverTapBookmarkFavLike(Context context, String articleId, String from, String takenAction) {

        if(context == null) {
            return;
        }

        final Map<String, Object> map = new HashMap<>();

        map.put("ArticleId", articleId);
        if(from.equalsIgnoreCase(NetConstants.RECO_personalised)) {
            from = "My Stories";
        }
        map.put(THPConstants.CT_KEY_AppSections, from);
        map.put(THPConstants.CT_KEY_platform,"app");
        map.put(THPConstants.CT_KEY_UserId, getUserId(context));

        String eventName = "";
        switch (takenAction) {
            case "NetConstants.BOOKMARK_YES":
                eventName = THPConstants.CT_EVENT_READ_LATER;
                break;
//            case "NetConstants.BOOKMARK_NO":
//                eventName = "Article deleted from bookmark";
//                break;
            case "NetConstants.LIKE_YES":
                eventName = THPConstants.CT_EVENT_FAVOURTING;
                break;
            case "NetConstants.LIKE_NO":
                eventName = "Article Show fewer stories";
                break;
            case "UNDO":
                eventName = "Undo";
                break;
                default:
                    eventName = takenAction;

        }

        CleverTapAPI.getDefaultInstance(context).pushEvent(eventName, map);
    }


    public static void CleverTapWidget(Context context, CTWidgetTracking widgetTracking) {
        Observable.just(widgetTracking)
                .subscribeOn(Schedulers.newThread())
                .map(v->{
                    final List<List<String>> widgetArticleIdsList = widgetTracking.widgetArticleIdsList;
                    final List<String> widgetNames = widgetTracking.widgetName;
                    int count = 0;
                    for(String widgetType : widgetNames) {
                        final Map<String, Object> map = new HashMap<>();
                        map.put("Section Widget", widgetType);
                        map.put(THPConstants.CT_KEY_platform,"app");
                        map.put(THPConstants.CT_KEY_UserId, getUserId(context));
                        final List<String> articleIdList = widgetArticleIdsList.get(count);
                        String articleIds = "";
                        for(String str : articleIdList) {
                            articleIds +=str+", ";
                        }
                        map.put("ArticleIds", articleIds);
                        map.put("ArticleCount", widgetArticleIdsList.get(count).size());
                        CleverTapAPI.getDefaultInstance(context).pushEvent(THPConstants.CT_EVENT_WIDGET, map);
                        count++;
                    }
                return "";
                })
                .subscribe(v->{

                }, t->{

                });



    }

    public static void cleverTapEventSettings(Context context, String propertyName, String value) {
        Map<String, Object> map = new HashMap<>();
        map.put(propertyName, value);
        map.put(THPConstants.CT_KEY_platform, "app");
        map.put(THPConstants.CT_KEY_UserId, getUserId(context));
        if(CleverTapAPI.getDefaultInstance(context) != null)
        CleverTapAPI.getDefaultInstance(context).pushEvent(THPConstants.CT_EVENT_SETTINGS, map);
    }

    public static void cleverTapEventPaymentStatus(Context context,String status,int packValue,String packDuration,String packName,long startTime,long endTime) {



        String timeDuration = AppDateUtil.millisToMinAndSec((endTime >= 1000 ? endTime : 1000) - startTime);

        if(timeDuration.contains("-"))
            timeDuration = "0 minute 1 Sec";

        Map<String, Object> map = new HashMap<>();
        map.put(THPConstants.CT_KEY_Pack_value,packValue );
        map.put(THPConstants.CT_KEY_Pack_duration,packDuration);
        map.put(THPConstants.CT_KEY_Pack_Name,packName );
        map.put(THPConstants.CT_KEY_Conversion_Time,timeDuration);
        map.put(THPConstants.CT_KEY_platform, "app");
        map.put(THPConstants.CT_KEY_UserId, getUserId(context));

        if(CleverTapAPI.getDefaultInstance(context) != null) {
            if (status.equals("success")) {
                CleverTapAPI.getDefaultInstance(context).pushEvent(THPConstants.CT_EVENT_PAYMENT_SUCCESSFUL, map);
            } else {
                CleverTapAPI.getDefaultInstance(context).pushEvent(THPConstants.CT_EVENT_PAYMENT_FAILED, map);
            }
        }

    }

    public static void cleverTapEventBenefitsPage(Context context, String propertyName, String value) {
        Map<String, Object> map = new HashMap<>();
        map.put(propertyName, value);
        map.put(THPConstants.CT_KEY_platform, "app");
        map.put(THPConstants.CT_KEY_UserId, getUserId(context));
        if(CleverTapAPI.getDefaultInstance(context) != null)
            CleverTapAPI.getDefaultInstance(context).pushEvent(THPConstants.CT_EVENT_BENEFITS_PAGE, map);
    }

    public static void cleverTapEventHamberger(Context context, String section, String subsection) {
        Map<String, Object> map = new HashMap<>();
        if (subsection != null) {
            map.put(THPConstants.CT_KEY_SubSection, section + " --> " + subsection);
        } else {
            map.put(THPConstants.CT_KEY_Section, section);
        }
        map.put(THPConstants.CT_KEY_platform, "app");
        map.put(THPConstants.CT_KEY_UserId, getUserId(context));
        if(CleverTapAPI.getDefaultInstance(context) != null)
        CleverTapAPI.getDefaultInstance(context).pushEvent(THPConstants.CT_EVENT_HAMBERGER, map);
    }

    public static void cleverTapEventFreeTrial(Context context) {
        Map<String, Object> map = new HashMap<>();
        map.put(THPConstants.CT_KEY_Date_Subscription, AppDateUtil.getCurrentDateFormatted("dd/MM/yyyy"));
        map.put(THPConstants.CT_KEY_User_Type, "Free_trial");
        map.put(THPConstants.CT_KEY_Pack_duration, "1 Month");
        map.put(THPConstants.CT_KEY_platform, "app");
        map.put(THPConstants.CT_KEY_UserId, getUserId(context));
        if(CleverTapAPI.getDefaultInstance(context) != null)
        CleverTapAPI.getDefaultInstance(context).pushEvent(THPConstants.CT_EVENT_FREE_TRIAL, map);
    }

    /*
     * isArticle is 1 if page type is an Article Detail Page otherwise 0*/
    public static void cleverTapEventPageVisit(Context context, String pageType, int articleId, String section, String authors, int isArticle) {
        Map<String, Object> map = new HashMap<>();
        map.put(THPConstants.CT_KEY_PAGE_TYPE, pageType);
        map.put(THPConstants.CT_KEY_IS_ARTICLE, isArticle);
        if (section != null) {
            map.put(THPConstants.CT_KEY_SECTIONS, section);
        }
        if (isArticle == 1) {
            map.put(THPConstants.CT_KEY_ID, articleId);
            map.put(THPConstants.CT_KEY_AUTHOR, authors);
        }
        map.put(THPConstants.CT_KEY_platform, "app");
        map.put(THPConstants.CT_KEY_UserId, getUserId(context));
        if(CleverTapAPI.getDefaultInstance(context) != null)
        CleverTapAPI.getDefaultInstance(context).pushEvent(THPConstants.CT_EVENT_PAGE_VISITED, map);
    }

    private static String sUserId ="";
    private static String getUserId(Context context){
        if(ResUtil.isEmpty(sUserId)) {
            sUserId = THPPreferences.getInstance(context).getUserId();
        }
        return sUserId;
    }

    public static void clearUserId() {
        sUserId = "";
    }
}
