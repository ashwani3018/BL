package com.mobstac.thehindubusinessline.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.activity.InitialSetupActivity;
import com.mobstac.thehindubusinessline.activity.SplashActivity;
import com.mobstac.thehindubusinessline.adapter.TagAdapter;
import com.mobstac.thehindubusinessline.database.DatabaseJanitor;
import com.mobstac.thehindubusinessline.model.SectionTable;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.SharedPreferenceHelper;
import com.mobstac.thehindubusinessline.view.FlowLayout;
import com.mobstac.thehindubusinessline.view.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class InitialSetupCityFragment extends Fragment {

    private TagFlowLayout mFlowLayout;
    private TagAdapter<SectionTable> mAdapter;
    private RealmResults<SectionTable> mSectionList;
    private boolean isCity;
    private InitialSetupActivity mInitialSetupActivity;
    private List<String> mSelectedCities = new ArrayList<>();
    private List<String> mSelectedCitiesName = new ArrayList<>();
    private List<String> mSelectedCitiesPosition = new ArrayList<>();
    private boolean isFragmentVisibleToUser;

    public InitialSetupCityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        Log.d(TAG, "onAttach: ");
        mInitialSetupActivity = (InitialSetupActivity) activity;
    }

    public static InitialSetupCityFragment newInstance(boolean isCity) {
        InitialSetupCityFragment fragment = new InitialSetupCityFragment();
        Bundle args = new Bundle();
        args.putBoolean(Constants.IS_CITY, isCity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.isCity = getArguments().getBoolean(Constants.IS_CITY);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mRootView = inflater.inflate(R.layout.fragment_initial_setup_city, container, false);
        int parentId;
        if (isCity) {
            parentId = 11;
        } else {
            parentId = 0;
        }


//        mSectionList = DatabaseJanitor.getSectionList(parentId);
//        long id[] = {11, 89};
        mSectionList = DatabaseJanitor.getRegionalList();

    /*    mSaveButton = (Button) mRootView.findViewById(R.id.button_cityinterest_save);
        mResetButton = (Button) mRootView.findViewById(R.id.button_cityinterest_previous);

        mSaveButton.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Lato-Regular.ttf"));
        mSaveButton.setText("Done");
        mResetButton.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Lato-Regular.ttf"));

        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InitialSetupActivity.mOnBoardingViewPager.setCurrentItem(0);
            }
        });*/


      /*  mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(
                        getActivity(),
                        getString(R.string.ga_action),
                        "Cities of interest: Save button clicked",
                        getString(R.string.title_city_interest));
                FlurryAgent.logEvent("Cities of interest: Save button clicked");
                SharedPreferenceHelper.setStringArrayPref(
                        getActivity(),
                        Constants.PREFERENCES_CITY_INTEREST,
                        mSelectedCities);
                SharedPreferenceHelper.setStringArrayPref(
                        getActivity(),
                        Constants.PREFERENCES_CITY_INTEREST_POSITION,
                        mSelectedCitiesPosition);
                SharedPreferenceHelper.putBoolean(
                        getActivity(),
                        Constants.PREFERENCES_HOME_REFRESH,
                        true);
                SharedPreferenceHelper.setStringArrayPref(
                        getActivity(),
                        Constants.PREFERENCES_CITY_INTEREST_NAME,
                        mSelectedCitiesName);
                SharedPreferenceHelper.putBoolean(
                        getActivity(),
                        Constants.PREFERENCES_FETCH_PERSONALIZE_FEED,
                        true);

                Intent mIntent = new Intent(getActivity(), SplashActivity.class);
                startActivity(mIntent);
                SharedPreferenceHelper.putBoolean(
                        getActivity(),
                        Constants.IS_INTEREST_SELECTED,
                        true
                );
                mInitialSetupActivity.finish();
            }
        });*/
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final LayoutInflater mInflater = LayoutInflater.from(getActivity());
        mFlowLayout = (TagFlowLayout) view.findViewById(R.id.flowlayout_city_interest);
        //mFlowLayout.setMaxSelectCount(3);
        if (mSectionList != null && mSectionList.size() > 0) {
            mFlowLayout.setAdapter(mAdapter = new TagAdapter<SectionTable>(mSectionList) {

                @Override
                public View getView(FlowLayout parent, int position, SectionTable s) {
                    View view = mInflater.inflate(R.layout.onboarding_item, mFlowLayout, false);
                    TextView tv = (TextView) view.findViewById(R.id.news_feed_section_name);
                    tv.setText(s.getSecName());
//                    ImageView imageView = (ImageView) view.findViewById(R.id.checkIcon);
                    if (mAdapter != null && mAdapter.getPreCheckedList() != null && mAdapter.getPreCheckedList().size() > 0) {
                        if (getPreCheckedList().contains(position)) {
//                            imageView.setImageResource(R.mipmap.check);
                            tv.setTextColor(getResources().getColor(android.R.color.white));
                        }
                    }
                    return view;
                }
            });
        }

        List<String> mCitySelectedList = new ArrayList<>();
        for (int i = 0; i < mSectionList.size(); i++) {
            mCitySelectedList.add(String.valueOf(mSectionList.get(i).getSecId()));
        }

        mSelectedCities = SharedPreferenceHelper.getStringArrayPref(
                getActivity(),
                Constants.PREFERENCES_CITY_INTEREST);
        mSelectedCitiesPosition = SharedPreferenceHelper.getStringArrayPref(
                getActivity(),
                Constants.PREFERENCES_CITY_INTEREST_POSITION);
        ArrayList<Integer> mCitiesIndex = new ArrayList<>();

        if (mSelectedCities.size() > 0) {
            for (int i = 0; i < mSelectedCities.size(); i++) {
                int index = mCitySelectedList.indexOf(mSelectedCities.get(i));
                if (index >= 0) {
                    mCitiesIndex.add(mCitySelectedList.indexOf(mSelectedCities.get(i)));
                }
            }
        } else {
            List<String> mCitiesList = new ArrayList<>();
            for (int i = 0; i < mSectionList.size(); i++) {
                mCitiesList.add(mSectionList.get(i).getSecName());
            }
            /** set default selection to chennai and tamilnadu*/
            String sectionPreselect[] = {"Chennai", "Tamil Nadu"};
            for (int i = 0; i < sectionPreselect.length; i++) {
                int index = mCitiesList.indexOf(sectionPreselect[i]);
                if (index >= 0) {
                    mCitiesIndex.add(index);
                    mSelectedCities.add(String.valueOf(mSectionList.get(index).getSecId()));
                    mSelectedCitiesName.add(mSectionList.get(index).getSecName());
                }
            }
            /* dynamically fetching city locatin code --  commented*/
            /*List<AddressData> mLatLngList = getAllCitiesLatitudeAndLongitude(mCitiesList);
            *//** get current user location*//*
            Location mLocation = getLocation();
            Log.d("LOCs", "onViewCreated: " + mLatLngList);
            Log.d("LOCs", "onViewCreated: " + mCitiesList);
            *//** get the nearest city close to user's location*//*
            int nearestCityIndex = getNearestCities(mLocation , mLatLngList , mCitiesList);
            mCitiesIndex.add(nearestCityIndex);*/
        }
       /* if (mSelectedCitiesPosition.size() > 0) {
            for (int i = 0; i < mSelectedCitiesPosition.size(); i++) {
                mCitiesIndex.add(Integer.parseInt(mSelectedCitiesPosition.get(i)));
            }
        }
*/
        if (mAdapter != null) {
            mAdapter.setSelectedArrayList(mCitiesIndex);
        }
        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (mFlowLayout != null && mFlowLayout.getSelectedList() != null && mFlowLayout.getSelectedList().size() >= 0) {
//                    ImageView imageView = (ImageView) view.findViewById(R.id.checkIcon);
                    TextView mSectionTextView = (TextView) view.findViewById(R.id.news_feed_section_name);
                    if (mFlowLayout.getSelectedList().contains(position)) {
//                        imageView.setImageResource(R.mipmap.check);
                        mSectionTextView.setTextColor(getResources().getColor(android.R.color.white));
                    } else {
//                        imageView.setImageResource(R.mipmap.plus);
                        mSectionTextView.setTextColor(getResources().getColor(R.color.color_customize_text_normal));
                    }
                }
                return true;
            }
        });


        mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
//                getActivity().setTitle("choose:" + selectPosSet.toString());
                setSelectedCities(selectPosSet);
            }
        });
    }

    private void setSelectedCities(Set<Integer> selectPosSet) {

        mSelectedCities.clear();
        mSelectedCitiesPosition.clear();
        mSelectedCitiesName.clear();
        for (int s : selectPosSet) {
            mSelectedCities.add(String.valueOf(mSectionList.get(s).getSecId()));
            mSelectedCitiesPosition.add(String.valueOf(s));
            mSelectedCitiesName.add(mSectionList.get(s).getSecName());
        }
    }

/*    public Location getLocation() {
        Location location = null;
        // The minimum distance to change Updates in meters
        final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

        // The minimum time between updates in milliseconds
        final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

        try {
            LocationManager locationManager = (LocationManager) getActivity()
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                // First get location from Network Provider
                double latitude;
                double longitude;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                            }
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }*/

 /*   @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
*/
   /* private int getNearestCities(Location mLocation , List<AddressData> mLatLngList , List<String> mCitiesList){
        List<Integer> mDistance = new ArrayList<>();
        float[] distance = new float[1];

        //dummy user's latitude and longitude
        mLocation.setLatitude(8.088306);
        mLocation.setLongitude(77.538451);

        for (int i = 0; i < mLatLngList.size(); i++) {
            Location.distanceBetween(
                    mLocation.getLatitude() ,
                    mLocation.getLongitude() ,
                    mLatLngList.get(i).getLatitude() ,
                    mLatLngList.get(i).getLongitude() ,
                    distance);

            Log.d("LOCs", "getNearestCities: " + distance);
            mDistance.add((int) distance[0]);
        }

        Log.d("Docs", "getNearestCities: " + mDistance);
        int minDistance = Collections.min(mDistance);
        int index = mDistance.indexOf(minDistance);
        Log.d("DOCs", "getNearestCities: " + index);
        Log.d("Docs", "getNearestCities: " + mCitiesList);

        return index;
    }

    private List<AddressData> getAllCitiesLatitudeAndLongitude(List<String> mCitiesList){
        Geocoder mGeocoder = new Geocoder(getActivity());
        List<Address> mAddressList = new ArrayList<>();
        List<AddressData> mLatLngList = new ArrayList<>();
        try {
            for (int i = 0; i < mCitiesList.size(); i++) {
                mAddressList = mGeocoder.getFromLocationName(mCitiesList.get(i), 1);
                for (int j = 0; j < mAddressList.size(); j++) {
                    AddressData mAddressData = new AddressData(
                            mAddressList.get(j).getLatitude(),
                            mAddressList.get(j).getLongitude()
                    );
                    mLatLngList.add(mAddressData);
                }
                mAddressList.clear();
                Log.d("LOCs", "onViewCreated: ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mLatLngList;
    }*/

  /*  public class LocationAsync extends AsyncTask<List<String> , Void , Integer>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(List<String>... params) {
            *//** get all cities latitude and longitude *//*
            List<AddressData> mLatLngList = getAllCitiesLatitudeAndLongitude(params[0]);
            *//** get current user location*//*
            Location mLocation = getLocation();
            Log.d("LOCs", "onViewCreated: " + mLatLngList);
            Log.d("LOCs", "onViewCreated: " + params[0]);
            */

    /**
     * get the nearest city close to user's location
     *//*
            int nearestCityIndex = getNearestCities(mLocation , mLatLngList , params[0]);
            return nearestCityIndex;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

        }
    }*/
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isFragmentVisibleToUser = isVisibleToUser;
        if (isVisibleToUser && mInitialSetupActivity != null) {
            mInitialSetupActivity.setNextButtonText("Done");
            mInitialSetupActivity.setSkipButtonText("Previous");
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isFragmentVisibleToUser && mInitialSetupActivity != null) {
            mInitialSetupActivity.setNextButtonText("Done");
            mInitialSetupActivity.setSkipButtonText("Previous");
        }
    }

    public void saveButtonClicked() {
        GoogleAnalyticsTracker.setGoogleAnalyticsEvent(
                getActivity(),
                getString(R.string.ga_action),
                "Cities of interest: Save button clicked",
                getString(R.string.title_city_interest));
        FlurryAgent.logEvent("Cities of interest: Save button clicked");
        SharedPreferenceHelper.setStringArrayPref(
                getActivity(),
                Constants.PREFERENCES_CITY_INTEREST,
                mSelectedCities);
        SharedPreferenceHelper.setStringArrayPref(
                getActivity(),
                Constants.PREFERENCES_CITY_INTEREST_POSITION,
                mSelectedCitiesPosition);
        SharedPreferenceHelper.putBoolean(
                getActivity(),
                Constants.PREFERENCES_HOME_REFRESH,
                true);
        SharedPreferenceHelper.setStringArrayPref(
                getActivity(),
                Constants.PREFERENCES_CITY_INTEREST_NAME,
                mSelectedCitiesName);
        SharedPreferenceHelper.putBoolean(
                getActivity(),
                Constants.PREFERENCES_FETCH_PERSONALIZE_FEED,
                true);

        Intent mIntent = new Intent(getActivity(), SplashActivity.class);
        startActivity(mIntent);
        SharedPreferenceHelper.putBoolean(
                getActivity(),
                Constants.IS_INTEREST_SELECTED,
                true
        );
        mInitialSetupActivity.finish();
    }
}
