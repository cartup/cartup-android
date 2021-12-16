package com.example.cartuplibrary;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CartupRecommendation {
    String orgName;
    String orgId;
    String domain;
    String widgetId;
    String pName;
    String sku;
    String categoryName;
    String userId;
    String spotDy_uid;
    String siteDomain;
    String url;
    String secretKey;

    public CartupRecommendation(Context context, String orgId, String orgName, String secretKey, String domain) {
        this.orgId = orgId;
        this.orgName = orgName;
        this.domain = domain;
        this.secretKey = secretKey;
        String ts = Context.TELEPHONY_SERVICE;
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(ts);
        String imei = orgName + mTelephonyMgr.getDeviceId();
        this.spotDy_uid = imei;
    }

    public CartupRecommendation SetWidgetId(String divisionId) {
        this.widgetId = divisionId;
        return this;
    }

    public CartupRecommendation SetProductName(String pName) {
        this.pName = pName;
        return this;
    }

    public CartupRecommendation SetSku(String sku) {
        this.sku = sku;
        return this;
    }

    public CartupRecommendation SetCategoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public CartupRecommendation SetUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public CartupRecommendation SetSiteDomain(String siteDomain) {
        this.siteDomain = siteDomain;
        return this;
    }

    public String GetUrl() {
        String url = "https://widgetapi.ecomtics.nl/v1/widgetserver/custom?divisionId=";
        if(this.widgetId != ""){
            url += this.widgetId;
        } else {
            return "Widget Id is mandatory";
        }

        if(this.orgId != ""){
            url += "&orgId=" + this.orgId;
        } else {
            return "OrgId is mandatory";
        }

        if(this.orgName != ""){
            url += "&com=" + this.orgName;
        } else {
            return "OrgName is mandatory";
        }

        if(this.domain != ""){
            url += "&sitedomain=" + this.domain;
        } else {
            return "Domain is mandatory";
        }

        if(this.spotDy_uid != "") {
            url += "&spotDy_uid=" + this.spotDy_uid;
        } else {
            return "Spotdy uid is not set";
        }

        if(this.userId != "") {
            url += "&login=" + this.userId;
        }
        if(this.sku != "") {
            url += "&sku=" + this.sku;
        }
        if(this.pName != "") {
            url += "&pName=" + this.pName;
        }
        if(this.categoryName != "") {
            url += "&catquery=" + this.categoryName;
        }

        if(this.secretKey != "") {
            url += "&secretKey=" + this.secretKey;
        }

        this.url = url;
        return url;
    }

    public String GetData() throws IOException {

        URL urlForGetRequest = new URL(this.GetUrl());
        String readLine = null;
        HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
        conection.setRequestMethod("GET");
        int responseCode = conection.getResponseCode();


        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while ((readLine = in .readLine()) != null) {
                response.append(readLine);
            } in .close();
            System.out.println("JSON String Result " + response.toString());
            return response.toString();
        }
        return String.valueOf(responseCode);
    }

}
