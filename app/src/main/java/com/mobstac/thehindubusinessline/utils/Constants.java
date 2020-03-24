/*
 * Copyright (c) 2014 Mobstac TM
 * All Rights Reserved.
 * @since May 30, 2014 
 * @author rajeshcp
 */
package com.mobstac.thehindubusinessline.utils;

/**
 * @author rajeshcp
 */
public final class Constants {

    public final static String SECTION_LIST = "sectionList";
    public final static String LAST_UPDATE_TIME = "last_update_time";
    public final static String IS_LIVE = "is_live";
    public static final String NOTIFICATIONS = "notifications";
    public static final String LOAD_NOTIFICATIONS = "ns_load_notifications";

    public static final String SELECTED_FONT_SIZE = "current_size";
    public static final boolean IS_USER_LOGGED_IN = true;




    public static final String SECTION_ID = "section_id";

    public static final String URL = "url";
    public static final String ARTICLE_ID = "article_id";

    public static final String SAVE_TEXT_SIZE = "text_Size";
    public static final String FIRST_TAP = "first_tap";
    public static final String THIRD_SWIPE = "third_swipe";

    public static final String BANNER_SECTION_ID = "BannerSectionID";
    public static final String BANNER_SECTON_TYPE = "BannerSectionType";
    public static final String NEWSFEED_CHUNK_SIZE = "NewsFeedChunkSize";
    public static final String IS_CITY = "IsCity";
    public static final String IS_HOME_FIRST_TIME = "FetchHomeFirst";
    public static final String PREFERENCE_SECTION_COUNT = "count_section";


//    public static final String SEARCH_BY_ARTICLE_ID_URL = "http://hinduappserver1.ninestars.in/hindubl/service/api_v2.002/mobiles/search.php?guid=";




    public static final String WEB_ARTICLE_URL = "web_articles_url";



	/* BUS RELATED EVENTS*/

    public static final String EVENT_INSERTED_SECTION_API_DATABASE = "SectionApiToDB";
    public static final String EVENT_INSERTED_NEWSFEED_API_DATABASE = "NewsFeedApiToDB";
    public static final String EVENT_HOME_FAILURE = "Retrofit10secfail";
    public static final String EVENT_INSERTED_WIDGET_API_DATABASE = "WidgetApiToDB";

    /* SETTINGS */
    public static final String DETECT_LOCATION = "detect_location";
    public static final String DAY_MODE = "day_mode";
    public static final String PREFERENCES_CITY_INTEREST = "interested_cities";
    public static final String PREFERENCES_CITY_INTEREST_POSITION = "cities_position";
    public static final String PREFERENCES_CITY_INTEREST_NAME = "cities_name";
    public static final String PREFERENCES_NEWS_FEED = "interested_feeds";
    public static final String PREFERENCES_NEWS_FEED_POSITION = "feeds_position";
    public static final String PREFERENCES_IS_FIRST_TIME = "first_time";
    public static final String PREFERENCES_FETCH_PERSONALIZE_FEED = "fetch_personalize_feed";
    public static final String PREFERENCES_HOME_REFRESH = "refresh_home";


	/* MO ENGAGE */

    public static final String MOENGAGE_APPID = "RTWQ9FNB5AEY1SJNAL2SKUZS";
    public static final String MOENGAGE_DEBUG_APPID = "RTWQ9FNB5AEY1SJNAL2SKUZS";
    public static final String MOENGAGE_PREF_FIRST_LAUCH_KEY = "MOENGAGE_PREF_FIRST_LAUCH_KEY";
    public static final String PREF_NAME = "preferenceName";
    public static final String WATCHED_VIDEO_COUNT_KEY = "watchedVideoCount";
    public static final String BOOK_MARKED_ARTICLES_COUNT_KEY = "bookMarkedArticleCount";
    public static final String READ_ARTICLE_COUNT_KEY = "readArticleCount";
    public static final String COMMENT_GIVEN_BY_CURRENT_USER_COUNT_KEY = "commentCount";
    public static final String OLD_DATABASE = "old_database";

    /*pushnotification type*/
    public static final String ARTICLE = "article";
    public static final String SECTION = "section";
    public static final String DIGEST = "digest";
    public static final String UPGRADE = "upgrade";
    public static final String ADVERTISEMENT = "advertisement";
    public static final String NOTIFICATION_INCOMING_FILTER = "welcome_notification";
    public static final String NEW_NOTIFICATION = "new_notification";
    public static final String NEWS_DIGEST = "news digest";


    /*Article type for bookmark*/
    public static final String TYPE_ARTICLE = "article";
    public static final String TYPE_PHOTO = "photo";
    public static final String TYPE_AUDIO = "audio";
    public static final String TYPE_VIDEO = "video";

    public static final String ABOUT_US_TEXT = "<body>" +
            "<p>Kasturi &amp; Sons Ltd (KSL) was established in 1878, and is most famously known as The Hindu Group of Publications in India. With seven unique publications and four digital forums under its belt, it offers something substantial for everyone. The Hindu, the most revered and respected daily in the country, boasts of an extensive network of correspondents and covers news that is reliable and relevant. Its unparalleled reach and dominance makes The Hindu the undisputed No 1 English daily in South India.</p><p>The newspaper is carefully designed to ensure that sense is always placed ahead of sensationalism in line with its historical journalistic values and traditions. The Hindu&rsquo;s editorial section is held in high regard for its balanced views and opinions, effusing an independent editorial stance.</p><p>The recently redesigned Business Line, with its well-segmented verticals helps readers transition seamlessly to their topic of interest. Frontline, the fortnightly magazine provides exhaustive coverage of politics, world affairs, environment, human rights, literature and the arts. The Hindu Tamil and 11 sub brands on diverse themes focus on providing product quality over quantity. Sportstar, every sports enthusiast&rsquo;s favourite, is the largest read magazine of the genre in the country. The Hindu In School, designed to address the student as a thinking individual, provides the experience of a serious, yet lively newspaper. Not to exclude the tiny and loyal young reader, The Hindu Young World has evolved into a standalone magazine and is living up to its mandate of making learning fun.</p><p>KSL Digital, a new division of the KSL group, has been formed to focus on creating and scaling up innovative businesses in the internet, mobile and e-commerce space. The KSL Digital portfolio consists of the group&rsquo;s news portals</p>\n" +
            "<p> <a href=\"http://www.thehindu.com\">www.thehindu.com</a></p>\n" +
            "<p> <a href=\"http://www.thehindubusinessline.com\">www.thehindubusinessline.com</a></p>\n" +
            "<p> <a href=\"http://www.tamil.thehindu.com/\">http://www.tamil.thehindu.com</a></p>\n" +
            "<p>which offer in-depth and up-to-date news coverage and analyses. Apart from this, mobile apps are available for i-phone ,i-pad and android users, providing the convenience of news on the go. KSL Digital&rsquo;s e-commerce offerings include-</p>\n" +
            "<p> <a href=\"http://www.roofandfloor.com/\">www.roofandfloor.com</a>: An online property portal</p>\n" +
            "<p> <a href=\"http://www.bloncampus.com\">bloncampus.com</a>: &nbsp;A portal designed exclusively for B-Schoolers and MBA aspirants</p>\n" +
            "<p> <a href=\"http://www.sportstarlive.com\">sportstarlive.com</a>: A portal that provides contemporary, relevant and round-the-clock sports news</p>\n" +
            "<p> <a href=\"http://www.youngworldclub.com\">youngworldclub.com</a>: An interactive education portal of children that offers a variety of rich multimedia content which includes slideshows, quizzes, videos and activities aimed to make learning exciting</p>\n" +
            "<p><a href=\"http://www.Steptest.in\">Steptest.in</a>: The Standardized Test of English Proficiency (STEP) is an English language certification exam that uses the latest testing techniques and advanced technologies to deliver a practical, engaging and reliable test</p>\n" +
            "</body>";

    public static final String T_C_TEXT = "<body>" +
            "<p>Kasturi &amp; Sons Ltd [KSL or 'We'] is the sole and absolute owner of &amp; maintains <a href=\"http://www.thehindu.com/\">www.thehindu.com</a>, m.thehindu.com and other mobile applications (Apps) (hereinafter \"website\") to enhance public access to the The Hindu newspaper owned by KSL. This service is provided on an \"As Is\" basis &amp; is continually under development. KSL requires that all users/visitors to this site(s) on the World Wide Web (the \"Site\") adhere to these Terms &amp; Conditions [T&amp;C] &amp; Rules &amp; Regulations [R&amp;R]. By accessing this Site you/user/visitor automatically confirm your acknowledgment &amp; acceptance of these T&amp;C &amp; R&amp;R which is the agreement between the parties. Copyright/Trademarks [Collectively \"Intellectual Property Rights\"] The trademarks, logos &amp; service marks (\"Marks\") displayed on the Site are the property of KSL &amp; other parties. Users are prohibited from using any Marks for any purpose including, but not limited to use as metatags on other pages or sites on the World Wide Web without the written permission of KSL or such third party which may own the Marks. All information &amp; content including any software programs available on or through the Site (\"Content\") is protected by copyright. Users are prohibited from modifying, copying, distributing, transmitting, displaying, publishing, selling, licensing, creating derivative works or using any Content available on or through the Site for commercial or public purposes. KSl grants you a limited licence to access and make personal use of this site but not for commercial purposes. We permit the use of materials on this website subject to due credit is given to KSL. The use shall also be subject to non-commercial use and limited to personal or academic dissemination. (including on social media and use on all kinds of media). Copyright should be acknowledged. Such use should not infringe on the Intellectual Property Rights of any person. When using material on this website, proper attribution to authors and/or copyright holders must be given. User is granted a limited, revocable and non exclusive right to create hyperlinks to the home page or any other page of KSL/The Hindu so long as any of these links do not portray KSL, The Hindu or their podcast or products or services in a false, misleading, derogatory or offensive manner. You may not use KSL's logos or proprietary graphics or trademarks as part of the link without express permission. No Warranties ALL CONTENT ON THE SITE IS PROVIDED TO YOU \"AS IS\" WITHOUT WARRANTY OF ANY KIND EITHER EXPRESS OR IMPLIED INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY &amp; FITNESS FOR A PARTICULAR PURPOSE, TITLE, NON-INFRINGEMENT, SECURITY OR ACCURACY. KSL DOES NOT ENDORSE &amp; IS NOT RESPONSIBLE FOR THE ACCURACY OR RELIABILITY OF ANY OPINION, ADVICE OR STATEMENT MADE THROUGH THE SITE BY ANY PARTY OTHER THAN KSL. OTHER THAN AS REQUIRED UNDER APPLICABLE LAW, UNDER NO CIRCUMSTANCE WILL KSL BE LIABLE FOR ANY LOSS OR DAMAGE CAUSED BY A USER'S RELIANCE ON INFORMATION OBTAINED THROUGH THE SITE. IT IS THE RESPONSIBILITY OF THE USER TO EVALUATE THE ACCURACY, COMPLETENESS OR USEFULNESS OF ANY OPINION, ADVICE OR OTHER CONTENT AVAILABLE ON/THROUGH THE SITE. PLEASE SEEK THE ADVICE OF PROFESSIONALS, AS APPROPRIATE, REGARDING THE EVALUATION OF ANY SPECIFIC OPINION, ADVICE OR OTHER CONTENT. Limitation Of Liability KSL specifically disclaims any liability (whether based in contract, tort, strict liability or otherwise) for any direct, indirect, incidental, consequential, or special damages arising out of or in any way connected with access to or use of the Site, (even if KSL has been advised of the possibility of such damages) including liability associated with any viruses which may infect a user's computer equipment.</p>\n" +
            "</body>";

//    public static final int APP_EXCLUSIVE_SECTION_ID = 142;

    /* INITIAL SETUP */
    public static final String IS_INTEREST_SELECTED = "initialSetupScreen";
    public static final String OVERLAY_LIST = "overlay_list";
    public static final String HOME_OVERLAY_SCREEN_LOADED = "home_overlayscreenloaded";
    public static final String ARTICLE_OVERLAY_SCREEN_LOADED = "article_overlayscreenloaded";
    public static final String OVERLAY_TYPE = "overlay_type";
    public static final int TEXT_TO_SPEECH_TEXT_SIZE = 3000;
    public static final String NOTIFICATION_STORE = "notification_store";

    /*Vuccule Data*/
    public static final String VUUKLE_API_KEY = "d1a4a16d-cfaf-4ad2-8f9b-ddf5a1b257d2";
    public static final String VUUKLE_SECRET_KEY = "66b2cb7c-ac21-11e2-bc97-bc764e0492cc";
    public static final String VUUKLE_URL = "http://vuukle.com/api.asmx/getCommentCountListByHost?id=" + VUUKLE_API_KEY + "&host=thehindubusinessline.com&list=";
    public static final String VUKKLE_COMMUNT_COUNT ="https://api.vuukle.com/api/v1/Comments/getCommentCountListByHost?host=thehindubusinessline.com&articleIds=";

    /*URL for Forex*/
    public static final String COMPANY_NAME_LIST_URL = "http://tab.thehindu.com/businessline/cmas?gn=all";
    public static final String NSE_URL = "http://tab.thehindu.com/businessline/nse?cid=20559&callback=";
    public static final String BSE_URL = "http://tab.thehindu.com/businessline/bse?cid=20558&callback=";
    public static final String GOLD_DOLLAR_URL = "http://hinduappserver1.ninestars.in/hindubl/service/api_v2.001/mobiles/homeService.php";
    public static final String BSETopGainer_URL = "http://hinduappserver1.ninestars.in/hindubl/service/api_v2.001/mobiles/sensex-data.php?ticker=bsehigh";
    public static final String BSETopLoser_URL = "http://hinduappserver1.ninestars.in/hindubl/service/api_v2.001/mobiles/sensex-data.php?ticker=bselow";
    public static final String NSETopGainer_URL = "http://hinduappserver1.ninestars.in/hindubl/service/api_v2.001/mobiles/nifty-data.php?ticker=nsehigh";
    public static final String NSETopLoser_URL = "http://hinduappserver1.ninestars.in/hindubl/service/api_v2.001/mobiles/nifty-data.php?ticker=nselow";
    public static final String ForexData_URL = "http://hinduappserver1.ninestars.in/hindubl/service/api_v2.001/mobiles/forex.php?service=tp";

    public static final String THE_HINDU_URL = "http://www.thehindubusinessline.com";
    /*Protfolio section id*/
    public static final String PORTFOLIO_SECTION_ID = "5";


    public static final String GRANT_TYPE = "password";
    public static final String REFRESH_TOKEN_GRANT_TYPE = "refresh_token";
    public static final String AUTH = "Basic QXBwQ2xpZW50OkFwcFNlY3JldA==";
    public static final String PORTFOLIO_APP_TOKEN = "!@#878tHa";
    public static final String PORTFOLIO_USER_ACCESS_TOKEN = "accessToken";
    public static final String PORTFOLIO_USER_TOKEN_TYPE = "tokenType";
    public static final String PORTFOLIO_USER_REFRESH_TOKEN = "refreshToken";
    public static final String PORTFOLIO_USER_EXPIRES_IN = "expiresIn";
    public static final String PORTFOLIO_USER_SCOPE = "userScope";
    public static final String PORTFOLIO_IS_USER_LOGIN = "is_user_login";

    /*Test Url for the Portfolio*/
/*    public static final String PORTFOLIO_SING_UP_URL = "http://premium.thehindu.co.in/apiregistration/";
    public static final String PORTFOLIO_SING_IN_URL = "https://api.thehindu.co.in/oauth/token";
    public static final String PORTFOLIO_USER_DETAIL_URL = "https://api.thehindu.co.in/userinfo";
    public static final String PORTFOLIO_CHANGE_PASSWORD_URL = "https://api.thehindu.co.in/changepassword";
    public static final String PORTFOLIO_UPDATE_PROFILE_URL = "https://api.thehindu.co.in/updateProfile";
    public static final String PORTFOLIO_FORGOT_PASSWORD_URL = "http://premium.thehindu.co.in/apiforgot/";
    public static final String PORTFOLIO_REFRESH_TOKEN_URL = "https://api.thehindu.co.in/oauth/token";*/

    /*Live URLs for Portfolio*/
    public static final String PORTFOLIO_SING_UP_URL = "https://premium.thehindubusinessline.com/apiregistration/";
    public static final String PORTFOLIO_FORGOT_PASSWORD_URL = "https://premium.thehindubusinessline.com/apiforgot/";
    public static final String PORTFOLIO_SING_IN_URL = "https://login.thehindu.co.in/oauth/token";
    public static final String PORTFOLIO_USER_DETAIL_URL = "https://login.thehindu.co.in/userinfo";
    public static final String PORTFOLIO_CHANGE_PASSWORD_URL = "https://login.thehindu.co.in/changepassword";
    public static final String PORTFOLIO_UPDATE_PROFILE_URL = "https://login.thehindu.co.in/updateProfile";
    public static final String PORTFOLIO_REFRESH_TOKEN_URL = "https://login.thehindu.co.in/oauth/token";

    public static final int VIEW_TYPE_ARTICLE = 0;
    public static final int VIEW_TYPE_OUTBRAIN = 1;
    public static final int VIEW_TYPE_NATIVE_AD = 2;
    public static final int VIEW_TYPE_EXPLORE = 3;

    public static final String FROM_DESCRIPTION_HYPER_LINK_CLICK = "hyperLinkClick";

    public static final int APP_EXCLUSIVE_SECTION_ID = 142;


    public static final String REPLACE_IMAGE_FREE_SEARCH ="BINARY/thumbnail";
    public static final String ROW_THUMB_SIZE_SEARCH ="alternates/SQUARE_115";

    public static final String REPLACE_IMAGE_FREE ="FREE_660";
    public static final String ROW_THUMB_SIZE ="SQUARE_115";
    public static final String WIDGET_THUMB_SIZE ="LANDSCAPE_215";
    public static final String BANNER_THUMB_SIZE ="LANDSCAPE_355";

}
