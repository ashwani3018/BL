package com.mobstac.thehindubusinessline.retrofit;


import com.google.gson.JsonElement;
import com.mobstac.thehindubusinessline.model.ApiSuccessResponse;
import com.mobstac.thehindubusinessline.model.LoginResponse;
import com.mobstac.thehindubusinessline.model.NewsFeed;
import com.mobstac.thehindubusinessline.model.SearchSectionContentModel;
import com.mobstac.thehindubusinessline.model.Section;
import com.mobstac.thehindubusinessline.model.SectionConentModel;
import com.mobstac.thehindubusinessline.model.SignupResponse;
import com.mobstac.thehindubusinessline.model.UserDetailResponse;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by ashwani on 08/12/15.
 */
public interface TheHinduApiService {

    @POST("sectionList_v4.php")
    Call<Section> sectionApi(@retrofit2.http.Body Body body);

    @POST("newsFeed.php")
    Call<NewsFeed> newsFeedApi(@retrofit2.http.Body Body body);

    @POST("section-content.php")
    Call<SectionConentModel> sectionContentApi(@retrofit2.http.Body Body body);

    @POST("section-content.php")
    Flowable<SectionConentModel> sectionContentApiRX(@retrofit2.http.Body Body body);

    @FormUrlEncoded
    @POST("")
    Call<LoginResponse> doPortfolioLogin(@Url String url, @Field("grant_type") String grant_type,
                                         @Field("password") String password,
                                         @Field("username") String userName,
                                         @Header("Authorization") String auth);

    @FormUrlEncoded
    @POST("")
    Call<LoginResponse> requestRefreshToken(@Url String url, @Field("grant_type") String grant_type,
                                            @Field("refresh_token") String refresh_token,
                                            @Header("Authorization") String auth);

    @POST("")
    Call<SignupResponse> doPortfolioSignup(@Url String url, @retrofit2.http.Body HashMap body);

    @POST("")
    Call<SignupResponse> requestForgotPassword(@Url String url, @retrofit2.http.Body HashMap body);

    @GET("")
    Call<UserDetailResponse> getUserDetails(@Url String url, @Header("Authorization") String auth);

    @POST("")
    Call<ApiSuccessResponse> requestChangePassword(@Url String url, @Header("Authorization") String auth, @retrofit2.http.Body HashMap body);

    @POST("")
    Call<ApiSuccessResponse> requestUpdateProfile(@Url String url, @Header("Authorization") String auth, @retrofit2.http.Body Map body);

    @GET("")
    Call<SearchSectionContentModel> getSearchArticleList(@Url String url);


}
