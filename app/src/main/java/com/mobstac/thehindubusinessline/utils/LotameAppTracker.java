package com.mobstac.thehindubusinessline.utils;

import android.content.Context;
import com.lotame.android.CrowdControl;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by arvind on 24/6/16.
 */
public class LotameAppTracker {

    private static final int CLIENT_ID = 7879;
    private static final int TIMEOUT_MILLIS = 5000;
    private static CrowdControl ccHttp;

    public static void init(Context context) {
        if (ccHttp == null) {
            ccHttp = new CrowdControl(context, CLIENT_ID);
            ccHttp.startSession();
        }
    }

    public static synchronized CrowdControl getCrowdControl(Context context) {
        if (ccHttp == null) {
            ccHttp = new CrowdControl(context, CLIENT_ID);
            ccHttp.startSession();
        }
        return ccHttp;
    }

    public static void sendDataToLotameAnalytics(Context context, String actionName, String action) throws IOException {
        CrowdControl crowdControl = getCrowdControl(context);
        crowdControl.add(actionName, action);
        if (crowdControl.isInitialized()) {
            crowdControl.bcpAsync();
        }
    }

    public static String getLotameAudienceInfo(Context context) throws IOException {
        CrowdControl crowdControl = getCrowdControl(context);
        return crowdControl.getAudienceJSON(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
    }

}