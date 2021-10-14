package com.sausageman.firstguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.sausageman.firstguide.Ads.Banner_Ads;
import com.tapdaq.sdk.Tapdaq;
import com.tapdaq.sdk.TapdaqConfig;
import com.tapdaq.sdk.listeners.TMAdListener;

public class MainActivity extends AppCompatActivity {

    ImageButton letStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        letStart = findViewById(R.id.letStart);
        Banner_Ads.initialize_tapdaq(this);
        Banner_Ads.showBanner1(this);
        Banner_Ads.showBanner2(this);
        letStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goIntent = new Intent(MainActivity.this, SecActivity.class);
                if(Tapdaq.getInstance().isVideoReady(MainActivity.this,"admob_fan")) {
                    Tapdaq.getInstance().showVideo(MainActivity.this, "admob_fan", new TMAdListener()); {

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
        Tapdaq.getInstance().loadVideo(MainActivity.this,  "admob_fan", new TMAdListener());

    }
}