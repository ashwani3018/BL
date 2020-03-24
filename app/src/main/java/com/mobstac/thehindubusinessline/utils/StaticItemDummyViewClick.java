package com.mobstac.thehindubusinessline.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.mobstac.thehindubusinessline.activity.BLInkActivity;
import com.mobstac.thehindubusinessline.activity.MainActivity;
import com.mobstac.thehindubusinessline.activity.PortfolioActivity;
import com.mobstac.thehindubusinessline.database.ApiCallHandler;
import com.mobstac.thehindubusinessline.database.DatabaseJanitor;


public class StaticItemDummyViewClick implements View.OnClickListener {

    String[] urlAndSectionId;
    Context mContext;

    public StaticItemDummyViewClick(String[] urlAndSectionId, Context context) {
        this.urlAndSectionId = urlAndSectionId;
        mContext = context;
    }

    @Override
    public void onClick(View view) {
        final int sectionId = Integer.valueOf(urlAndSectionId[1]);
        int clickPosition = -1;


        String from = "";
        if(mContext instanceof BLInkActivity) {
            from = ApiCallHandler.FROM_BLINK;
            clickPosition = DatabaseJanitor.getSubsectionPostion_1(from, sectionId);
        } else if(mContext instanceof PortfolioActivity) {
            from = ApiCallHandler.FROM_PORTFOLIO;
            clickPosition = DatabaseJanitor.getSubsectionPostion_1(from, sectionId);
        } else if(mContext instanceof MainActivity) {
            from = ApiCallHandler.FROM_HOME;
            clickPosition = DatabaseJanitor.getSectionPosition(sectionId);
        }


        if(clickPosition > 0) {
            if(mContext instanceof MainActivity) {
                ((MainActivity) mContext).changeFragmenAtIndex(clickPosition);
            } else if(mContext instanceof PortfolioActivity) {
                ((BLInkActivity) mContext).changeFragmenAtIndex(clickPosition);
            } else if(mContext instanceof BLInkActivity) {
                ((BLInkActivity) mContext).changeFragmenAtIndex(clickPosition);
            }
        } else {
            String parentName = DatabaseJanitor.getParentSectionName(sectionId);
            ////////
            if(clickPosition == -1 && from.equals(ApiCallHandler.FROM_PORTFOLIO)) {
                clickPosition = DatabaseJanitor.getSubsectionPostion_1(ApiCallHandler.FROM_BLINK, sectionId);
                if(clickPosition != -1)
                from = ApiCallHandler.FROM_BLINK;
            }
            if(clickPosition == -1 && from.equals(ApiCallHandler.FROM_PORTFOLIO)) {
                clickPosition = DatabaseJanitor.getSectionPosition(sectionId);
                if(clickPosition != -1)
                from = ApiCallHandler.FROM_HOME;
            }

            ////////////
            if(clickPosition == -1 && from.equals(ApiCallHandler.FROM_BLINK)) {
                clickPosition = DatabaseJanitor.getSubsectionPostion_1(ApiCallHandler.FROM_PORTFOLIO, sectionId);
                if(clickPosition != -1)
                from = ApiCallHandler.FROM_PORTFOLIO;
            }
            if(clickPosition == -1 && from.equals(ApiCallHandler.FROM_BLINK)) {
                clickPosition = DatabaseJanitor.getSectionPosition(sectionId);
                if(clickPosition != -1)
                from = ApiCallHandler.FROM_HOME;
            }

            /////////////
            if(clickPosition == -1 && from.equals(ApiCallHandler.FROM_HOME)) {
                clickPosition = DatabaseJanitor.getSubsectionPostion_1(ApiCallHandler.FROM_PORTFOLIO, sectionId);
                if(clickPosition != -1)
                from = ApiCallHandler.FROM_PORTFOLIO;
            }
            if(clickPosition == -1 && from.equals(ApiCallHandler.FROM_HOME)) {
                clickPosition = DatabaseJanitor.getSubsectionPostion_1(ApiCallHandler.FROM_BLINK, sectionId);
                if(clickPosition != -1)
                from = ApiCallHandler.FROM_BLINK;
            }

            if(clickPosition != -1) {
                if(from.equals(ApiCallHandler.FROM_BLINK)) {
                    Intent intent = new Intent(mContext, BLInkActivity.class);
                    intent.putExtra("clickPosition", clickPosition);
                    mContext.startActivity(intent);
                } else if(from.equals(ApiCallHandler.FROM_PORTFOLIO)) {
                    Intent intent = new Intent(mContext, PortfolioActivity.class);
                    intent.putExtra("clickPosition", clickPosition);
                    mContext.startActivity(intent);
                } else if(from.equals(ApiCallHandler.FROM_HOME)) {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra("clickPosition", clickPosition);
                    mContext.startActivity(intent);
                }
            }

        }

    }
}


