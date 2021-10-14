package com.sausageman.firstguide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SharedElementCallback;
import android.app.VoiceInteractor;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sausageman.firstguide.Models.Apps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import static android.webkit.URLUtil.isValidUrl;

public class AppsActivity extends AppCompatActivity {
    private static final String TAG = "AppsActivity";

    ArrayList<Apps> appsArrayList;
    RecyclerView recyclerView;
    AppsAdapter appsAdapter;
    RecyclerView.LayoutManager layoutManager;
    RequestQueue requestQueue;
    Button btnSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);

        recyclerView = findViewById(R.id.recycle_view);
        btnSkip = findViewById(R.id.btn_skip);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AppsActivity.this, MainActivity.class));
                finish();
            }
        });
        createRecycleView();
        loadApps();
    }

    private void createRecycleView(){
        appsArrayList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(AppsActivity.this, 3);
        recyclerView.setLayoutManager(layoutManager);
        appsAdapter = new AppsAdapter(appsArrayList, this);
        recyclerView.setAdapter(appsAdapter);
        appsAdapter.setOnItemClickListener(new AppsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Apps apps = appsArrayList.get(position);
                String url = apps.getAppUrl();
                if (!url.isEmpty()){
                    openWebPage(url);
                }
            }
        });

    }

    private void loadApps(){
        SharedPreferences shared = getApplicationContext().getSharedPreferences("shared", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, MyApp.jsonAppsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("apps");


                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject data = jsonArray.getJSONObject(i);
                        Log.e(TAG, data.toString());

                        int appId = data.getInt("id");
                        String appName = data.getString("name");
                        String appIcon = data.getString("icon");
                        String appUrl = data.getString("url");
                        String appCountry = data.getString("country");
                        boolean appState = data.getBoolean("state");
                        String appPayout = data.getString("payout");

                         if (appState){
                            if (appCountry.equals("all")){
                                appsArrayList.add(new Apps(appId, appName, appIcon, appUrl, appState, appCountry, Double.parseDouble(appPayout)));
                            }else {
                                if (appCountry.contains(shared.getString("countryCode", "all"))){
                                    appsArrayList.add(new Apps(appId, appName, appIcon, appUrl, appState, appCountry, Double.parseDouble(appPayout)));

                                }
                            }
                           }
                        appsAdapter.notifyDataSetChanged();
                        Log.e(TAG,appName);
                    }

                    Collections.sort(appsArrayList, new Comparator<Apps>() {
                        @Override
                        public int compare(Apps apps, Apps t1) {
                            return Double.compare(t1.getAppPayout(), apps.getAppPayout());
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.e(TAG, error.getMessage());
                    }
                }
        );
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    private void openWebPage(String url) {
        try {
            if (!isValidUrl(url)) {
                Log.e(TAG, " This is not a valid link");
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " You don't have any browser to open web page", Toast.LENGTH_LONG).show();
        }
    }
}