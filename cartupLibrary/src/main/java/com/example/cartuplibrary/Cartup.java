package com.example.cartuplibrary;

import android.content.Context;

public class Cartup {

    CartupEvent event;
    CartupRecommendation recommendation;

    String orgId;
    String orgName;
    String domain;
    String secretKey;
    Context context;

    public Cartup(Context context, String orgName, String orgId, String secretKey, String domain) {
        this.orgId = orgId;
        this.orgName = orgName;
        this.domain = domain;
        this.context = context;
        this.secretKey = secretKey;
    }

    public CartupEvent GetCartupEvent() {
        return new CartupEvent(this.context, this.orgName, this.orgId, this.secretKey, this.domain);
    }

    public CartupRecommendation GetCartupRecommendation() {
        return new CartupRecommendation(this.context, this.orgName, this.orgId, this.secretKey, this.domain);
    }

}
