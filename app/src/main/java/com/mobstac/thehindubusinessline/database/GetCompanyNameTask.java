package com.mobstac.thehindubusinessline.database;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.mobstac.thehindubusinessline.Businessline;
import com.mobstac.thehindubusinessline.model.CompanyData;
import com.mobstac.thehindubusinessline.model.CompanyNameModel;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import io.realm.Realm;

/**
 * Created by 9920 on 7/5/2017.
 */

public class GetCompanyNameTask extends AsyncTask<String, Void, CompanyNameModel> {
    private Context context;

    public GetCompanyNameTask(Context context) {
        this.context = context;
    }

    @Override
    protected CompanyNameModel doInBackground(String... params) {
        CompanyNameModel response = null;
        try {

            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = connection.getInputStream();
                Reader reader = new InputStreamReader(is);
                Gson gson = new Gson();
                response = gson.fromJson(reader, CompanyNameModel.class);
            }
            if (isCancelled()) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(final CompanyNameModel companyNameModel) {
        super.onPostExecute(companyNameModel);
        if (isCancelled()) {
            return;
        }
        if (companyNameModel != null) {
            final Realm mRealm = Businessline.getRealmInstance();
           /* final RealmResults<CompanyData> mCompanyDatas = mRealm.where(CompanyData.class).findAll();
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    mCompanyDatas.deleteAllFromRealm();
                }
            });*/
            mRealm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    List<CompanyData> mData = companyNameModel.getData();
                    realm.copyToRealmOrUpdate(mData);
                }
            });
        }
    }
}
