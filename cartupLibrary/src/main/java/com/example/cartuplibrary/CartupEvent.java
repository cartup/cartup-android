package com.example.cartuplibrary;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class CartupEvent {

    ParamBuilder paramBuilder;
    String eventName;
    String orgName;
    String orgId;
    String domain;
    String secretKey;

    public List<String> GetRequiredParams() {
        return paramBuilder.GetRequiredFields(this.eventName);
    }

    public List<String> GetAllEventsName() {
        return paramBuilder.GetAllEventsName();
    }

    public String Validate() {
        return paramBuilder.ValidateParams(this.eventName);
    }

    public String SendEvent() {

        try {
            if (this.eventName == "") {
                return "Eventname is  not set.";
            }
            paramBuilder.AddDefaultEvents(this.eventName, this.orgName, this.orgId, this.domain);
            String url = buildUrl();
            return SendToServer(url);
        } catch (Exception e) {
            return e.toString();
        }
    }

    public CartupEvent SetEventName(String eventName) {
        this.eventName = eventName;
        return this;
    }

    public CartupEvent(Context context, String orgName, String orgId, String secretKey, String domain) {
        this.orgId = orgId;
        this.orgName = orgName;
        this.domain = domain;
        this.secretKey = secretKey;
        paramBuilder = new ParamBuilder(context);
    }

    public CartupEvent AddParam(String name, String value) {
        paramBuilder.AddParam(this.eventName, new Param(name, value));
        return this;
    }

    public CartupEvent AddParam(String name, Boolean value) {
        paramBuilder.AddParam(this.eventName, new Param(name, value));
        return this;
    }

    public CartupEvent AddParam(String name, Double value) {
        paramBuilder.AddParam(this.eventName, new Param(name, value));
        return this;
    }

    public CartupEvent AddParam(String name, String[] value) {
        paramBuilder.AddParam(this.eventName, new Param(name, value));
        return this;
    }

    public CartupEvent AddParam(String name, Double[] value) {
        paramBuilder.AddParam(this.eventName, new Param(name, value));
        return this;
    }

    public CartupEvent AddParam(String name, Long[] value) {
        paramBuilder.AddParam(this.eventName, new Param(name, value));
        return this;
    }

    public List<String> GetAllField() {
        return paramBuilder.GetAllParameters(this.eventName);
    }

    String buildUrl() throws Exception {

        List<Param> params = paramBuilder.GetParams(this.eventName);

        String url = "https://events.ecomtics.nl/craftsvilla/events?type=clickstream&action=push&inputJson=";
        JSONObject empObject = new JSONObject();
        for (int index = 0; index < params.size(); index++) {
            JSONObject innerObj = new JSONObject();
            boolean flag = false;
            if (isKeyPresent(params.get(index).ParamName)) {
                flag = true;
                innerObj.put("type", "array");
                String paramName = params.get(index).ParamName;
                if(paramName == "price_ds"){
                    innerObj.put("value", params.get(index).DoubleValues);
                    innerObj.put("element_type", "double");
                } else if(paramName == "quantity_ls"){
                    innerObj.put("value", params.get(index).LongValues);
                    innerObj.put("element_type", "long");
                } else {
                    innerObj.put("value", params.get(index).Values);
                    innerObj.put("element_type", "string");
                }
            } else {
                if (params.get(index).Value != null && params.get(index).Value != "") {
                    flag = true;
                    innerObj.put("value", params.get(index).Value).put("type", "string");
                } else if (params.get(index).doubleValue != null && params.get(index).doubleValue != 0.0) {
                    flag = true;
                    innerObj.put("value", params.get(index).doubleValue).put("type", "double");
                } else if (params.get(index).boolValue != null) {
                    flag = true;
                    innerObj.put("value", params.get(index).boolValue).put("type", "boolean");
                }
            }
            if(flag)
                empObject.put(getKey(params.get(index).ParamName), innerObj);
        }
        url += URLEncoder.encode(empObject.toString(), "UTF-8");
        url += "&secretKey=" + this.secretKey;
        url += "&type=clickstream.events";
        return url;
    }

    Boolean isKeyPresent(String key) {
        if (key == "productname_ss" || key == "sku_ss" || key == "price_ds" || key == "quantity_ls")
            return true;
        return false;
    }

    String getKey(String key) {
        if (key == "productname_ss" || key == "productname_s") {
            return "productname";
        } else if (key == "sku_ss" || key == "sku_s") {
            return "sku";
        } else if (key == "price_ds" || key == "price_d") {
            return "price";
        } else if (key == "quantity_ls" || key == "quantity_l") {
            return "quantity";
        } else if (key == "totalamount_d") {
            return "totalamount";
        }
        return key;
    }

    String SendToServer(String url) throws IOException {
        URL urlForGetRequest = new URL(url);
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
