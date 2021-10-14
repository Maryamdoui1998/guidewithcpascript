package com.sausageman.firstguide.Ads;

import android.app.Activity;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.tapdaq.sdk.TMBannerAdView;
import com.tapdaq.sdk.Tapdaq;
import com.tapdaq.sdk.TapdaqConfig;
import com.tapdaq.sdk.common.TMBannerAdSizes;
import com.tapdaq.sdk.listeners.TMAdListener;
import com.tapdaq.sdk.listeners.TMInitListener;
import com.tapdaq.sdk.model.TMAdSize;
import com.sausageman.firstguide.R;

import java.util.Arrays;
import java.util.List;

public class Banner_Ads {
    public static TMBannerAdView mBannerAd;
    public static String mAppId = "610af88c08fe6c2d735d6966";
    public static String mClientKey = "e2c25baa-345b-45ac-9159-e4c3fdb94e09";

    public static void testDevice (Activity activity){

        List<String> testDeviceIds = Arrays.asList("21572f7c-843a-4a96-98cc-e3385adcba80");
        RequestConfiguration configuration =  new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);
    }

    public static void initialize_tapdaq (Activity activity){
        TapdaqConfig config = Tapdaq.getInstance().config();
        Tapdaq.getInstance().initialize(activity, mAppId, mClientKey, config, new TMInitListener());
    }
    //Banners To Show
    public static void showBanner1 (Activity activity) {



        // For Banner Code ...
        mBannerAd = activity.findViewById(R.id.adBanner1);
        TMAdSize size = TMBannerAdSizes.STANDARD;
        mBannerAd.load(activity,"admob_fan", size, new TMAdListener());
        // For Banner Code ...

    }
    //Banners To Show
    public static void showBanner2 (Activity activity) {

        // For Banner Code ...
        mBannerAd = activity.findViewById(R.id.adBanner2);
        TMAdSize size = TMBannerAdSizes.MEDIUM;
        mBannerAd.load(activity,"admob_fan", size, new TMAdListener());
        // For Banner Code ...
    }

}
