package com.sausageman.firstguide;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.sausageman.firstguide.JsonReader.AsyncItemsFull;
import com.sausageman.firstguide.Models.ModelItems;

import java.util.ArrayList;
import java.util.List;

public class Details extends AppCompatActivity {

    RelativeLayout detailsRelative;
    TextView titleTV, textTV;
    ImageView imageIV;
    Button linkBTN;
    int position = 0;
    private final String TAG = Details.class.getSimpleName();
    LinearLayout nativeLayout;
    TemplateView templateView;
    NativeAdLayout nativeAdLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        detailsRelative = findViewById(R.id.detailsRelative);
        titleTV = findViewById(R.id.titleTV);
        textTV = findViewById(R.id.textTV);
        imageIV = findViewById(R.id.imageIV);
        linkBTN = findViewById(R.id.linkBTN);
        nativeLayout = findViewById(R.id.nativeLayout);
        templateView = findViewById(R.id.templateView);
        nativeAdLayout = findViewById(R.id.nativeAdLayout);

        position = getIntent().getIntExtra("position", 0);


        if(MyApp.NetworkAds.equalsIgnoreCase("admob")) {
            templateView.setVisibility(View.VISIBLE);
            nativeAdLayout.setVisibility(View.GONE);
            AdLoader adLoader = new AdLoader.Builder(Details.this, MyApp.NativeAdmob)
                    .forNativeAd(NativeAd -> templateView.setNativeAd(NativeAd))
                    .build();

            adLoader.loadAd(new AdRequest.Builder().build());
        }
        else {
            templateView.setVisibility(View.GONE);
            nativeAdLayout.setVisibility(View.VISIBLE);
            NativeBannerAd nativeBannerAd = new NativeBannerAd(this, MyApp.NativeFacebookSingle);
            NativeAdListener nativeAdListener = new NativeAdListener() {

                @Override
                public void onMediaDownloaded(Ad ad) {
                    // Native ad finished downloading all assets
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    // Native ad failed to load
                    Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Native ad is loaded and ready to be displayed
                    if (nativeBannerAd == null || nativeBannerAd != ad) {
                        return;
                    }
                    // Inflate Native Banner Ad into Container
                    inflateAd(nativeBannerAd);
                }

                @Override
                public void onAdClicked(Ad ad) {
                    // Native ad clicked
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    // Native ad impression
                }
            };
            // load the ad
            nativeBannerAd.loadAd(
                    nativeBannerAd.buildLoadAdConfig()
                            .withAdListener(nativeAdListener)
                            .build());
        }

        getData();
    }

    private void inflateAd(NativeBannerAd nativeBannerAd) {
        // Unregister last ad
        nativeBannerAd.unregisterView();

        // Add the Ad view into the ad container.
        LayoutInflater inflater = LayoutInflater.from(Details.this);
        // Inflate the Ad view.  The layout referenced is the one you created in the last step.
        LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.native_banner_ad_unit, nativeAdLayout, false);
        nativeAdLayout.addView(adView);

        // Add the AdChoices icon
        RelativeLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(Details.this, nativeBannerAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        MediaView nativeAdIconView = adView.findViewById(R.id.native_icon_view);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(
                nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
        nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());
        sponsoredLabel.setText(nativeBannerAd.getSponsoredTranslation());

        // Register the Title and CTA button to listen for clicks.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        nativeBannerAd.registerViewForInteraction(adView, nativeAdIconView, clickableViews);
    }

    @SuppressLint("StaticFieldLeak")
    private void getData() {
        new AsyncItemsFull(Details.this, CustomUtils.JSON_LINK) {
            @Override
            protected void onDataFirst() {

            }

            @Override
            protected void onDataFinished(List<ModelItems> modelItems, String status) {
                    final ModelItems modelItems1 = modelItems.get(position);

                titleTV.setText(modelItems1.getTitle());

                Glide.with(Details.this).load(modelItems1.getImage()).error(R.mipmap.ic_launcher).into(imageIV);

                imageIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!modelItems1.getImage_link().isEmpty()) {
                            String url = modelItems1.getImage_link();
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                        }
                    }
                });

                textTV.setText(modelItems1.getText());

                if(!modelItems1.getColor().isEmpty()) {
                    titleTV.setTextColor(Color.parseColor(modelItems1.getColor()));
                    textTV.setTextColor(Color.parseColor(modelItems1.getColor()));
                }

                if(!modelItems1.getText_size().isEmpty()) {
                    textTV.setTextSize(Integer.parseInt(modelItems1.getText_size()));
                }

                if(modelItems1.getIsLink()) {
                    linkBTN.setVisibility(View.VISIBLE);
                    if(!modelItems1.getLink_title().isEmpty()) {
                        linkBTN.setText(modelItems1.getLink_title());
                    }

                    linkBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!modelItems1.getSetLink().isEmpty()) {
                                String url = modelItems1.getSetLink();
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            }
                        }
                    });
                }
                else {
                    linkBTN.setVisibility(View.GONE);
                }

                if(!modelItems1.getBackground().isEmpty()) {
                    detailsRelative.setBackgroundColor(Color.parseColor(modelItems1.getBackground()));
                }

            }
        }.execute();
    }
}