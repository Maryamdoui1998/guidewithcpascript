package com.sausageman.firstguide.Models;

public class Apps {
    private int appId;
    private String appName;
    private String appIcon;
    private String appUrl;
    private boolean appState;
    private String appCountry;
    private double appPayout;

    public Apps(int appId, String appName, String appIcon, String appUrl, boolean appState, String appCountry, double appPayout) {
        this.appId = appId;
        this.appName = appName;
        this.appIcon = appIcon;
        this.appUrl = appUrl;
        this.appState = appState;
        this.appCountry = appCountry;
        this.appPayout = appPayout;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public boolean isAppState() {
        return appState;
    }

    public void setAppState(boolean appState) {
        this.appState = appState;
    }

    public String getAppCountry() {
        return appCountry;
    }

    public void setAppCountry(String appCountry) {
        this.appCountry = appCountry;
    }

    public double getAppPayout() {
        return appPayout;
    }

    public void setAppPayout(double appPayout) {
        this.appPayout = appPayout;
    }
}
