package com.sausageman.firstguide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.facebook.ads.AdError;
import com.facebook.ads.NativeAdsManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.sausageman.firstguide.Ads.Banner_Ads;
import com.sausageman.firstguide.JsonReader.AsyncItems;


import java.util.ArrayList;
import java.util.List;

public class GuideList extends AppCompatActivity {

    RecyclerView itemList;
    CustomAdapter customAdapter;

    List<Object> itemsModelList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_list);

        itemList = findViewById(R.id.itemList);
        itemList.setHasFixedSize(true);
        itemList.setLayoutManager(new LinearLayoutManager(this));
        getData();
    }

    @SuppressLint("StaticFieldLeak")
    private void getData() {
        new AsyncItems(GuideList.this, CustomUtils.JSON_LINK) {
            @Override
            protected void onDataFirst() {

            }

            @Override
            protected void onDataFinished(List<Object> modelItems, String status) {
                itemsModelList = modelItems;
                if (MyApp.NetworkAds.equalsIgnoreCase("admob")) {
                    loadNatAdmob();
                }
                else {
                    getAdapterData();
                }
            }
        }.execute();
    }

    private List<NativeAd> mNativeAds = new ArrayList<>();
    private AdLoader adLoader;
    private void loadNatAdmob() {

        mNativeAds.clear();
        getAdapterData();
        AdLoader.Builder builder = new AdLoader.Builder(getApplicationContext(), MyApp.NativeAdmob);
        adLoader = builder.forNativeAd (new NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(NativeAd NativeAd) {
                mNativeAds.add(NativeAd);
                if (!adLoader.isLoading()) {
                    insertAdsInMenuItems();
                }
            }
            }).withAdListener(
                new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adErro) {
                        if (!adLoader.isLoading()) {
                            insertAdsInMenuItems();
                        }
                    }
                }).build();

        adLoader.loadAds(new AdRequest.Builder().build(), itemsModelList.size());
    }

    private void insertAdsInMenuItems() {
        if (mNativeAds.size() <= 0) {
            return;
        }

        try {
            int offset = (itemsModelList.size() / mNativeAds.size()) + 1;
            int index = 1;
            for (NativeAd ad : mNativeAds) {
                itemsModelList.add(index, ad);
                index = index + offset;
            }
            customAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.d("motya", "Native not replace correct : " + e);
            }
        }
    }

    private void getAdapterData() {
        customAdapter = new CustomAdapter(GuideList.this, itemsModelList);
        itemList.setAdapter(customAdapter);
    }
}