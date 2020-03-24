package com.mobstac.thehindubusinessline.utils;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VukkleCommentCountTask extends AsyncTask<Void, Void, String> {

    private String articleID;
    public VukkleCommentCountListeners mCallback;

    public  void setVukkleCommentResult(VukkleCommentCountListeners callback) {
        this.mCallback = callback;
    }
    public interface VukkleCommentCountListeners {
        void onCountTaskCompletes(String aid,String result);
    }

    public VukkleCommentCountTask(String articleID) {
        this.articleID = articleID;
    }

    @Override
    protected String doInBackground(Void... params) {
        String article_id = articleID;
        URL url;
        HttpURLConnection connection = null;


        try {
            url = new URL(Constants.VUKKLE_COMMUNT_COUNT + article_id);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

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
            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        mCallback.onCountTaskCompletes(articleID,result);
    }
}