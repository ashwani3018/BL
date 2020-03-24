package com.netoperation.util;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;

import static com.netoperation.util.NetConstants.EVENT_CHANGE_ACCOUNT_STATUS;
import static com.netoperation.util.NetConstants.EVENT_FORGOT_PASSWORD;
import static com.netoperation.util.NetConstants.EVENT_SIGNUP;
import static com.netoperation.util.NetConstants.RECO_ALL;
import static com.netoperation.util.NetConstants.RECO_bookmarks;
import static com.netoperation.util.NetConstants.RECO_briefcase;
import static com.netoperation.util.NetConstants.RECO_personalised;
import static com.netoperation.util.NetConstants.RECO_suggested;
import static com.netoperation.util.NetConstants.RECO_trending;
import static java.lang.annotation.RetentionPolicy.SOURCE;

public interface RetentionDef {

    @Retention(SOURCE)
    @StringDef({EVENT_SIGNUP, EVENT_FORGOT_PASSWORD, EVENT_CHANGE_ACCOUNT_STATUS})
    @interface userVerificationMode {}

    @Retention(SOURCE)
    @StringDef({RECO_ALL, RECO_bookmarks, RECO_personalised, RECO_suggested, RECO_trending, RECO_briefcase})
    @interface Recomendation {}
}
