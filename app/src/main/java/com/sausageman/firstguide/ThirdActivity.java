package com.sausageman.firstguide;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.sausageman.firstguide.Ads.Banner_Ads;
import com.tapdaq.sdk.Tapdaq;
import com.tapdaq.sdk.TapdaqConfig;
import com.tapdaq.sdk.listeners.TMAdListener;

public class ThirdActivity extends AppCompatActivity {

    Button letStart;
    Button candy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        letStart = findViewById(R.id.startBtn2);
        candy = findViewById(R.id.candy);
        Banner_Ads.showBanner1(this);
        Banner_Ads.showBanner2(this);

        candy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://sausagemangenerator.xyz/candies")));
            }
        });

        letStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goIntent = new Intent(ThirdActivity.this, GuideList.class);
                if(Tapdaq.getInstance().isVideoReady(ThirdActivity.this,"admob_fan")) {
                    Tapdaq.getInstance().showVideo(ThirdActivity.this, "admob_fan", new TMAdListener()); {

                            startActivity(goIntent);
                    };
                }
                else {
                            startActivity(goIntent);
                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();

        TapdaqConfig config = new TapdaqConfig();
        config.setAutoReloadAds(true);

        //intestitial Ad video
        Tapdaq.getInstance().loadVideo(ThirdActivity.this,  "admob_fan", new TMAdListener());

    }
}