package com.mobstac.thehindubusinessline.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobstac.thehindubusinessline.Businessline;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.activity.AdsBaseActivity;
import com.mobstac.thehindubusinessline.adapter.SearchAdapter;
import com.mobstac.thehindubusinessline.adapter.SearchArticleAdapter;
import com.mobstac.thehindubusinessline.model.CompanyData;
import com.mobstac.thehindubusinessline.model.SearchSectionContentModel;
import com.mobstac.thehindubusinessline.model.databasemodel.ArticleDetail;
import com.mobstac.thehindubusinessline.retrofit.RetrofitAPICaller;
import com.mobstac.thehindubusinessline.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mobstac.thehindubusinessline.retrofit.RetrofitAPICaller.SEARCH_BY_ARTICLE_TITLE_URL;

public class SearchLandingFragment extends BaseFragment implements SearchArticleAdapter.ToolBarShow {

    private static final String TAG = "SearchLandingFragment";

    private AdsBaseActivity mMainActivity;
    private List<CompanyData> mSearchList  = new RealmList<>();
    private SearchView searchView;
    private RecyclerView mRecyclerView;
    private SearchAdapter mSearchCompanyAdapter;
    private boolean mSearchTileBoolaen  = false;
    private EditText mSearchEdt;
    private SearchArticleAdapter mSearchArticlAdapter;
    private RelativeLayout mPopUpLayout;
    private TextView mShowSearchResponse;

    private ProgressBar mProgressbar;
    private boolean mCheckToolbar = false;

    private List<ArticleDetail> mArticleDetailList = new ArrayList<>();

    public static SearchLandingFragment getInstance(Bundle bundle) {
        SearchLandingFragment fragment = new SearchLandingFragment();
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (AdsBaseActivity) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mMainActivity = (AdsBaseActivity) activity;
    }

    @Override
    public void getDataFromDB() {

    }

    @Override
    public void showLoadingFragment() {

    }

    @Override
    public void hideLoadingFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSearchTileBoolaen = getArguments().getBoolean("search_title_show");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_landing, container, false);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        mPopUpLayout = mMainActivity.mToolbar.findViewById(R.id.popUpTitleLayoutID);

        mShowSearchResponse = view.findViewById(R.id.noArticle);
        mProgressbar = view.findViewById(R.id.progressBarId);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.searchRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);


        // Initialize the adapter for Article and Company
        mSearchCompanyAdapter = new SearchAdapter(getContext(), mSearchList);
        mSearchArticlAdapter = new SearchArticleAdapter(getContext(), mArticleDetailList, mMainActivity);
        mSearchArticlAdapter.onShowToolBar(this);

        // Initialize the search view by EditText
        mSearchEdt = view.findViewById(R.id.search_edtId);

        // Check title show Article or Stocks
        if (mSearchTileBoolaen) {
            mSearchEdt.setHint(R.string.search_by_aticle_hint);
            onEventArticle();
            mRecyclerView.setAdapter(mSearchArticlAdapter);
        } else {
            mSearchEdt.setHint(R.string.search_by_stock_hint);
            onEventStock();
            mRecyclerView.setAdapter(mSearchCompanyAdapter);
        }



        mSearchEdt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (mSearchEdt.getRight() - mSearchEdt.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        mSearchEdt.setText("");
                        mShowSearchResponse.setText("");
                        if (mSearchTileBoolaen) {
                            mSearchArticlAdapter.onClearAllData();
                            Log.d(TAG, "onTouch: ");
                        } else {
                            getSearchedData("");
                            Log.d(TAG, "onTouch: ");
                            mRecyclerView.setVisibility(View.GONE);
                        }

                        mSearchEdt.onEditorAction(EditorInfo.IME_ACTION_DONE);

                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void onEventArticle() {
        Log.d(TAG, "onEventArticle: ");
        mSearchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    mSearchEdt.onEditorAction(EditorInfo.IME_ACTION_DONE);
//                    mProgressbar.setVisibility(View.VISIBLE);
                    String searchTxt =v.getText().toString();
                    if (searchTxt.isEmpty() && searchTxt.equals("")) {
                        mShowSearchResponse.setVisibility(View.VISIBLE);
                        mShowSearchResponse.setText("Please enter key!");
                    } else {
//                        mProgressbar.setVisibility(View.GONE);

                        boolean isNetworkAvailable = NetworkUtils.isNetworkAvailable(getContext());
                        if (!isNetworkAvailable) {
                            mShowSearchResponse.setVisibility(View.VISIBLE);
                            mShowSearchResponse.setText(getResources().getText(R.string.snackbar_title_text));
                        } else {
                            mShowSearchResponse.setVisibility(View.GONE);
                            if (mSearchTileBoolaen) {
                                searchApiCall(v.getText().toString());
                            }
                        }
                    }
                    Log.d(TAG, "onEditorAction: ");
                    return true;
                }
                return false;
            }
        });
    }

    private void onEventStock() {
        Log.d(TAG, "onEventStock: ");
        mSearchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    if (!mSearchTileBoolaen) {
                        //mSearchEdt.onEditorAction(EditorInfo.IME_ACTION_DONE);
                        getSearchedData(s.toString());
                        Log.d(TAG, "onTextChanged: ");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void getSearchedData(String searchText) {
        if (!searchText.toString().equals("")) {

            Realm mRealm = Businessline.getRealmInstance();
            OrderedRealmCollection<CompanyData> companyList = mRealm.where(CompanyData.class)
                    .contains("name", searchText, Case.INSENSITIVE).findAll();

            if (companyList.size() > 0) {
                mShowSearchResponse.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mSearchCompanyAdapter = new SearchAdapter(getContext(), companyList);
                mRecyclerView.setAdapter(mSearchCompanyAdapter);
            } else {
                mRecyclerView.setVisibility(View.GONE);
                mShowSearchResponse.setVisibility(View.VISIBLE);
                mShowSearchResponse.setText("No result found!");
            }

        }
    }

    private void searchApiCall(String title) {
        mProgressbar.setVisibility(View.VISIBLE);

        if (mSearchArticlAdapter != null) {
            mSearchArticlAdapter.onClearAllData();
        }

        Call<SearchSectionContentModel> call = RetrofitAPICaller.getInstance(mMainActivity).getWebserviceAPIs()
                .getSearchArticleList(SEARCH_BY_ARTICLE_TITLE_URL+title);
        call.enqueue(new Callback<SearchSectionContentModel>() {
            @Override
            public void onResponse(Call<SearchSectionContentModel> call, Response<SearchSectionContentModel> response) {


                onSuccessResponse(response.body().getData());



            }

            @Override
            public void onFailure(Call<SearchSectionContentModel> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
                mProgressbar.setVisibility(View.GONE);
            }
        });


    }

    private void onSuccessResponse(List<ArticleDetail> response) {

        mProgressbar.setVisibility(View.GONE);
        mArticleDetailList = response;

        if (mArticleDetailList.size() > 0) {
            mShowSearchResponse.setVisibility(View.GONE);
            mSearchArticlAdapter = new SearchArticleAdapter(getContext(), mArticleDetailList, mMainActivity);
            mSearchArticlAdapter.onShowToolBar(this);

            mRecyclerView.setAdapter(mSearchArticlAdapter);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mShowSearchResponse.setVisibility(View.VISIBLE);
            mShowSearchResponse.setText("No result found!");
        }
    }

    public void setEditValues(boolean check) {

        if (mRecyclerView != null){
            mRecyclerView.setVisibility(View.GONE);
        }

        mSearchEdt.setText("");
        getSearchedData("");
        if (mSearchArticlAdapter != null) {
            mSearchArticlAdapter.onClearAllData();
        }

        if (check) {
            mSearchEdt.setHint(R.string.search_by_aticle_hint);
            mSearchTileBoolaen = check;
            onEventArticle();
        } else {
            mSearchEdt.setHint(R.string.search_by_stock_hint);
            mSearchTileBoolaen = check;
            onEventStock();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mPopUpLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCheckToolbar) {
            mPopUpLayout.setVisibility(View.VISIBLE);
        } else {
            mPopUpLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onEvent(boolean check) {
        mCheckToolbar = check;
    }
}
