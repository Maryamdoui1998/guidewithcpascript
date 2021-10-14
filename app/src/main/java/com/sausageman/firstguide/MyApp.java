package com.sausageman.firstguide;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyApp extends Application {
    private static final String TAG = "MyApp";


    RequestQueue requestQueue;
    private static final String ONESIGNAL_APP_ID = "151e6dff-dd1d-497f-99d7-df4fe5f0b671";
    public static int isJsonDone = 0; // 0 not yet processed 1 json is done 2 error

    public static String NetworkAds = "admob";
    public static String BannerAdmob = "";
    public static String InterstitialAdmob = "";
    public static String NativeAdmob = "";
    public static String BannerFacebook = "";
    public static String InterstitialFacebook = "";
    public static String NativeFacebook = "";
    public static String NativeFacebookSingle = "";
    public static boolean ImageBanner = false;
    public static String ImageBannerImg = "https://media3.giphy.com/media/igVvRDJ4EbhstQyEKh/giphy.gif";
    public static String ImageBannerURL = "https://www.google.com";
    public static boolean ModeOn = true;

    SharedPreferences shared;
    SharedPreferences.Editor editor;

    public static final String sharedDataCountryCompleted = "shared_data_country_completed";
    public static final String sharedCountry = "shared_country";
    public static final String sharedCountryCode = "shared_country_code";
    public static final String sharedCity = "shared_city";
    public static final String sharedTimeZone = "shared_time_zone";
    public static final String sharedIp = "shared_ip";

    public static String jsonAppsUrl = "http://192.168.56.1/android/apps_item.json";
    public static String jsonIpApi = "http://ip-api.com/json";

    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        requestQueue = Volley.newRequestQueue(this);

        shared = getApplicationContext().getSharedPreferences("shared", Context.MODE_PRIVATE);
        editor = shared.edit();
        getDataCountry();

        callAds();
    }
    private void callAds() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, CustomUtils.JSON_LINK, null,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response)
                    {
                        try {
                            JSONObject GuideData = ((JSONObject) response).getJSONObject("GuideData");
                            JSONObject AdsController = GuideData.getJSONObject("AdsController");

                            NetworkAds = AdsController.getString("NetworkAds");
                            BannerAdmob = AdsController.getString("BannerAdmob");
                            InterstitialAdmob = AdsController.getString("InterstitialAdmob");
                            NativeAdmob = AdsController.getString("NativeAdmob");
                            BannerFacebook = AdsController.getString("BannerFacebook");
                            InterstitialFacebook = AdsController.getString("InterstitialFacebook");
                            NativeFacebook = AdsController.getString("NativeFacebook");
                            NativeFacebookSingle = AdsController.getString("NativeFacebookSingle");
                            ImageBanner = AdsController.getBoolean("ImageBanner");
                            ImageBannerImg = AdsController.getString("ImageBannerImg");
                            ImageBannerURL = AdsController.getString("ImageBannerURL");
                            ModeOn =AdsController.getBoolean("Mode_On");

                            isJsonDone = 1;

                        } catch (JSONException e) {
                            isJsonDone = 2;
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        isJsonDone = 2;
                    }
                });
        jsonObjectRequest.setShouldCache(false);
        requestQueue.add(jsonObjectRequest);
    }

    public void getDataCountry(){
        if(!shared.getBoolean(sharedDataCountryCompleted,false)){
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, MyApp.jsonIpApi, new Response.Listener<String>() {
                @Override
                public void onResponse(String resp) {
                    try {
                        JSONObject response = new JSONObject(resp);

                        editor.putString(sharedCity, response.getString("city"));
                        editor.putString(sharedCountry,response.getString("country"));
                        editor.putString(sharedCountryCode,response.getString("countryCode"));
                        editor.putString(sharedTimeZone,response.getString("timezone"));
                        editor.putString(sharedIp,response.getString("query"));
                        editor.putBoolean(sharedDataCountryCompleted,true);

                        Log.e(TAG, "My Country Data: " + response.getString("country"));
                        Log.e(TAG, "My City Data: " + response.getString("city"));
                        Log.e(TAG, "My Timezone Data: " + response.getString("timezone"));
                        Log.e(TAG, "My CountryCode Data: " + response.getString("countryCode"));
                        Log.e(TAG, "My Ip Data: " + response.getString("query"));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error){

                        }
                    }
            );
            stringRequest.setShouldCache(false);
            requestQueue.add(stringRequest);
        }

    }


}
