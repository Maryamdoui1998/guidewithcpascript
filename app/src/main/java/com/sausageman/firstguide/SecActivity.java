package com.sausageman.firstguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sausageman.firstguide.Ads.Banner_Ads;
import com.tapdaq.sdk.Tapdaq;
import com.tapdaq.sdk.TapdaqConfig;
import com.tapdaq.sdk.listeners.TMAdListener;

public class SecActivity extends AppCompatActivity {

    TextView rateBtn, shareBtn, moreBtn, Startbtn;
    //AdmobAds admobAds;
    //FbAds fbAds;
    //RelativeLayout adLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec);

        Startbtn = findViewById(R.id.Startbtn);
        rateBtn = findViewById(R.id.rateBtn);
        shareBtn = findViewById(R.id.shareBtn);
        moreBtn = findViewById(R.id.moreBtn);

        //adLayout = findViewById(R.id.adLayout);


        //admobAds = new AdmobAds(this);
        //fbAds = new FbAds(this);
        //admobAds.LoadInter();
        /*if(MyApp.NetworkAds.equalsIgnoreCase("admob")) {
            admobAds.showBanner(adLayout);
        }
        else {
            fbAds.showBanner(adLayout);
        }*/
        Banner_Ads.showBanner1(this);


        Startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goIntent = new Intent(SecActivity.this, ThirdActivity.class);
                if(Tapdaq.getInstance().isVideoReady(SecActivity.this,"admob_fan")) {
                    Tapdaq.getInstance().showVideo(SecActivity.this, "admob_fan", new TMAdListener()); {


                            startActivity(goIntent);

                    };
                }
                else {

                    startActivity(goIntent);
                }

            }
        });

        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Here is the share content body";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=6456804357929516378")));
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();

        TapdaqConfig config = new TapdaqConfig();
        config.setAutoReloadAds(true);

        //intestitial Ad video
        Tapdaq.getInstance().loadVideo(SecActivity.this,  "admob_fan", new TMAdListener());

    }
}