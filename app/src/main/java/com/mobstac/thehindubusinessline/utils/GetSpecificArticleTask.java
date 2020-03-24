package com.mobstac.thehindubusinessline.utils;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;

import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.mobstac.thehindubusinessline.BuildConfig;
import com.mobstac.thehindubusinessline.activity.MainActivity;
import com.mobstac.thehindubusinessline.activity.WebActivity;
import com.mobstac.thehindubusinessline.model.SearchArticleById;
import com.mobstac.thehindubusinessline.retrofit.RetrofitAPICaller;
import com.mobstac.thehindubusinessline.view.CustomArticleScrollView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetSpecificArticleTask extends AsyncTask<Void, Void, SearchArticleById> {

    private int mArticleID;

    public GetSpecificArticleTask(int articleID) {
        this.mArticleID = articleID;
    }

    @Override
    protected SearchArticleById doInBackground(Void... params) {
        URL url;
        HttpURLConnection connection = null;

        try {
            int versionCode = BuildConfig.VERSION_CODE;
            int osVersion = android.os.Build.VERSION.SDK_INT;
            url = new URL(RetrofitAPICaller.SEARCH_BY_ARTICLE_ID_URL + mArticleID);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("device_type", "android");
            jsonObject.put("app_version", versionCode);
            jsonObject.put("os_version", osVersion);

            OutputStream os = connection.getOutputStream();
            os.write((jsonObject.toString()).getBytes("UTF-8"));
            os.close();


            //Get SingUpDetail
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            Gson gson = new Gson();
            SearchArticleById articleDetailModel = gson.fromJson(response.toString(), SearchArticleById.class);
            return articleDetailModel;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(SearchArticleById articleDeatils) {
        super.onPostExecute(articleDeatils);
        mSpecificArticleListener.onSpecialArticleResult(articleDeatils);
    }


    public SpecificArticleListener mSpecificArticleListener;

    public void setSpecialArticleListener(SpecificArticleListener specialArticle) {
        this.mSpecificArticleListener = specialArticle;
    }

    public interface SpecificArticleListener{
        void onSpecialArticleResult(SearchArticleById searchArticleById);
    }
}
