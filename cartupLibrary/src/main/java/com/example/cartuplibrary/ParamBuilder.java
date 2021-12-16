package com.example.cartuplibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

class EventConstants {
    static final String ProductView = "productView";
    static final String AddToCart = "addToCart";
    static final String ViewCart = "viewCart";
    static final String Transaction = "transaction";
    static final String AddToWishlist = "addToWishlist";
    static final String WidgetLoad = "widgetLoad";
    static final String WidgetClick = "widgetClick";
    static final String WidgetView = "widgetView";
    static final String SignUp = "signUp";
    static final String SignOut = "signOut";
    static final String Search = "search";
    static final String Review = "review";
}

class Param {
    String ParamName;
    String Value;
    Boolean IsRequired;
    Boolean IsDefault;
    String[] Values;
    Double[] DoubleValues;
    Long[] LongValues;
    Boolean boolValue;
    Double doubleValue = 0.0;

    public Param(String name, boolean isRequired, boolean isDefault) {
        this.ParamName = name;
        this.IsRequired = isRequired;
        this.IsDefault = isDefault;
    }

    public Param(String name, String Value, boolean isDefault) {
        this.ParamName = name;
        this.IsDefault = isDefault;
        this.Value = Value;
    }

    public Param(String name, Boolean value) {
        this.ParamName = name;
        this.boolValue = value;
    }

    public Param(String name, Double value) {
        this.ParamName = name;
        this.doubleValue = value;
    }

    public Param(String name, String value) {
        this.ParamName = name;
        this.Value = value;
    }

    public Param(String name, String[] values) {
        this.ParamName = name;
        this.Values = values;
    }

    public Param(String name, Double[] values) {
        this.ParamName = name;
        this.DoubleValues = values;
    }

    public Param(String name, Long[] values) {
        this.ParamName = name;
        this.LongValues = values;
    }
}

public class ParamBuilder {

    Map<String, List<Param>> eventMap = new HashMap<String, List<Param>>();
    Context context;
    SharedPreferences.Editor editor;

    public void AddParam(String name, Param param) {
        if (this.eventMap.get(name) == null)
            this.eventMap.put(name, new ArrayList<Param>());
        List<Param> p = this.eventMap.get(name);
        for (int i = 0; i < p.size(); i++) {
            if (p.get(i).ParamName == param.ParamName) {
                Param p1 = p.get(i);
                param.IsDefault = p1.IsDefault;
                param.IsRequired = p1.IsRequired;
                p.remove(i);
                p.add(param);
                return;
            }
        }
        this.eventMap.get(name).add(param);
    }

    public String ValidateParams(String name) {
        List<Param> paramList = eventMap.get(name);
        for (int i = 0; i < paramList.size(); i++) {
            if (paramList.get(i).IsRequired && paramList.get(i).Value == "" && !paramList.get(i).IsDefault) {
                return "Failed to get the value for param " + paramList.get(i).ParamName;
            }
        }
        return "";
    }

    public ParamBuilder(Context context) {
        this.context = context;
        addAddToCart();
        addProductView();
        addViewCart();
        addTransaction();
        addAddToWishlist();
        addWidgetLoadEvent();
        addWidgetClickEvent();
        addWidgetViewEvent();
        addSignOutEvent();
        addSignUpEvent();
        addSearchEvent();
        addReviewEvent();
    }

    public List<String> GetRequiredFields(String name) {
        List<Param> params = eventMap.get(name);

        List<String> list = new ArrayList<String>();
        for (int index = 0; index < params.size(); index++) {
            if ((params.get(index).IsRequired == null || params.get(index).IsRequired) && !params.get(index).IsDefault) {
                list.add(params.get(index).ParamName);
            }
        }

        return list;
    }

    public List<Param> GetParams(String name) {
        return eventMap.get(name);
    }

    public List<String> GetAllParameters(String name) {
        List<Param> params = eventMap.get(name);

        List<String> list = new ArrayList<String>();
        for (int index = 0; index < params.size(); index++) {
            if (!params.get(index).IsDefault) {
                list.add(params.get(index).ParamName);
            }
        }

        return list;
    }

    public List<String> GetAllEventsName() {
        List<String> list = new ArrayList<String>();

        for (String name : eventMap.keySet()) {
            list.add(name);
        }

        return list;
    }

    public void AddDefaultEvents(String name, String orgName, String orgId, String domain) {
        if (context != null) {
            String ts = Context.TELEPHONY_SERVICE;
            TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(ts);
            String imei = orgName + mTelephonyMgr.getDeviceId();
            this.AddParam(name, new Param("uid_s", imei, true));
            this.AddParam(name, new Param("spotdySessionId_s", imei, true));
        }

        this.AddParam(name, new Param("domain_s", domain, true));
        this.AddParam(name, new Param("browserInfo_s", "android_mobile_native", true));

        this.AddParam(name, new Param("is_mobile_b", true));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.UK);
        String formattedDate = sdf.format(new Date());
        this.AddParam(name, new Param("date_dt", formattedDate, true));
        this.AddParam(name, new Param("org_s", orgName, true));
        String myDeviceModel = android.os.Build.MODEL;
        this.AddParam(name, new Param("deviceInfo_s", myDeviceModel, true));
        this.AddParam(name, new Param("framework_s", "native_mobile", true));
        this.AddParam(name, new Param("spotdy_eventid_s", UUID.randomUUID().toString(), true));
        this.AddParam(name, new Param("orgId_s", orgId, true));
    }

    public void addProductView() {
        this.AddParam(EventConstants.ProductView, new Param("productname_s", true, false));
        this.AddParam(EventConstants.ProductView, new Param("sku_s", true, false));
        this.AddParam(EventConstants.ProductView, new Param("price_d", true, false));

        this.AddParam(EventConstants.ProductView, new Param("discountprice_d", false, false));

        this.AddParam(EventConstants.ProductView, new Param("eventType", "productview", true));
        this.AddParam(EventConstants.ProductView, new Param("labelName", "productview", true));
        this.AddParam(EventConstants.ProductView, new Param("spotdy_eventname", "__ecomtics_productview", true));
        this.AddParam(EventConstants.ProductView, new Param("eventAction", "load", true));
    }

    public void addAddToCart() {
        this.AddParam(EventConstants.AddToCart, new Param("productname_s", true, false));
        this.AddParam(EventConstants.AddToCart, new Param("sku_s", true, false));
        this.AddParam(EventConstants.AddToCart, new Param("price_d", true, false));

        this.AddParam(EventConstants.AddToCart, new Param("discountprice_d", false, false));

        this.AddParam(EventConstants.AddToCart, new Param("eventType", "addtocart", true));
        this.AddParam(EventConstants.AddToCart, new Param("labelName", "addtocart", true));
        this.AddParam(EventConstants.AddToCart, new Param("spotdy_eventname", "__ecomtics_addtocart", true));
        this.AddParam(EventConstants.AddToCart, new Param("eventAction", "click", true));
    }

    public void addViewCart() {
        this.AddParam(EventConstants.ViewCart, new Param("productname_ss", true, false));
        this.AddParam(EventConstants.ViewCart, new Param("sku_ss", true, false));
        this.AddParam(EventConstants.ViewCart, new Param("price_ds", true, false));
        this.AddParam(EventConstants.ViewCart, new Param("quantity_ls", true, false));
        this.AddParam(EventConstants.ViewCart, new Param("totalamount_d", true, false));

        this.AddParam(EventConstants.ViewCart, new Param("eventType", "viewcart", true));
        this.AddParam(EventConstants.ViewCart, new Param("labelName", "viewcart", true));
        this.AddParam(EventConstants.ViewCart, new Param("spotdy_eventname", "__ecomtics_cartview", true));
        this.AddParam(EventConstants.ViewCart, new Param("eventAction", "load", true));
    }

    public void addTransaction() {
        this.AddParam(EventConstants.Transaction, new Param("productname_ss", true, false));
        this.AddParam(EventConstants.Transaction, new Param("sku_ss", true, false));
        this.AddParam(EventConstants.Transaction, new Param("price_ds", true, false));
        this.AddParam(EventConstants.Transaction, new Param("quantity_ls", true, false));
        this.AddParam(EventConstants.Transaction, new Param("totalamount_d", true, false));

        this.AddParam(EventConstants.Transaction, new Param("eventType", "Transaction", true));
        this.AddParam(EventConstants.Transaction, new Param("labelName", "postcheckout", true));
        this.AddParam(EventConstants.Transaction, new Param("spotdy_eventname", "__ecomtics_transactions", true));
        this.AddParam(EventConstants.Transaction, new Param("eventAction", "load", true));
    }

    public void addAddToWishlist() {
        this.AddParam(EventConstants.AddToWishlist, new Param("productname_s", true, false));
        this.AddParam(EventConstants.AddToWishlist, new Param("sku_s", true, false));
        this.AddParam(EventConstants.AddToWishlist, new Param("price_d", true, false));

        this.AddParam(EventConstants.AddToWishlist, new Param("discountprice_d", false, false));

        this.AddParam(EventConstants.AddToWishlist, new Param("eventType", "addtowishlist", true));
        this.AddParam(EventConstants.AddToWishlist, new Param("labelName", "addtowishlist", true));
        this.AddParam(EventConstants.AddToWishlist, new Param("spotdy_eventname", "__ecomtics_wishlist", true));
        this.AddParam(EventConstants.AddToWishlist, new Param("eventAction", "click", true));

    }

    public void addWidgetLoadEvent() {
        this.AddParam(EventConstants.WidgetLoad, new Param("widgetId", true, false));

        this.AddParam(EventConstants.WidgetLoad, new Param("eventType", "widgetLoad", true));
        this.AddParam(EventConstants.WidgetLoad, new Param("labelName", "widgetLoad", true));
        this.AddParam(EventConstants.WidgetLoad, new Param("spotdy_eventname", "__ecomtics_widget_load", true));
        this.AddParam(EventConstants.WidgetLoad, new Param("eventAction", "load", true));
    }

    public void addWidgetClickEvent() {
        this.AddParam(EventConstants.WidgetClick, new Param("widgetId", true, false));
        this.AddParam(EventConstants.WidgetClick, new Param("productname_s", true, false));
        this.AddParam(EventConstants.WidgetClick, new Param("sku_s", true, false));
        this.AddParam(EventConstants.WidgetClick, new Param("price_d", true, false));

        this.AddParam(EventConstants.WidgetClick, new Param("discountprice_d", false, false));

        this.AddParam(EventConstants.WidgetClick, new Param("eventType", "widgetLoad", true));
        this.AddParam(EventConstants.WidgetClick, new Param("labelName", "widgetLoad", true));
        this.AddParam(EventConstants.WidgetClick, new Param("spotdy_eventname", "__ecomtics_widget_click", true));
        this.AddParam(EventConstants.WidgetClick, new Param("eventAction", "load", true));
    }

    public void addWidgetViewEvent() {
        this.AddParam(EventConstants.WidgetView, new Param("widgetId", true, false));

        this.AddParam(EventConstants.WidgetView, new Param("eventType", "__widget_view__", true));
        this.AddParam(EventConstants.WidgetView, new Param("eventAction", "load", true));
        this.AddParam(EventConstants.WidgetView, new Param("labelName", "widgetView", true));
        this.AddParam(EventConstants.WidgetView, new Param("spotdy_eventname", "__widget_view__", true));
    }

    public void addSignUpEvent() {
        this.AddParam(EventConstants.SignUp, new Param("userid", true, false));

        this.AddParam(EventConstants.SignUp, new Param("emailid", false, false));
        this.AddParam(EventConstants.SignUp, new Param("gender", false, false));
        this.AddParam(EventConstants.SignUp, new Param("city", false, false));

        this.AddParam(EventConstants.SignUp, new Param("eventType", "signup", true));
        this.AddParam(EventConstants.SignUp, new Param("labelName", "signup", true));
        this.AddParam(EventConstants.SignUp, new Param("spotdy_eventname", "__ecomtics_signup", true));
        this.AddParam(EventConstants.SignUp, new Param("eventAction", "click", true));
    }

    public void addSignOutEvent() {
        this.AddParam(EventConstants.SignOut, new Param("userid", true, false));

        this.AddParam(EventConstants.SignOut, new Param("eventType", "signout", true));
        this.AddParam(EventConstants.SignOut, new Param("labelName", "signout", true));
        this.AddParam(EventConstants.SignOut, new Param("spotdy_eventname", "__ecomtics_signout", true));
        this.AddParam(EventConstants.SignOut, new Param("eventAction", "click", true));
    }

    public void addSearchEvent() {
        this.AddParam(EventConstants.Search, new Param("keyword", true, false));

        this.AddParam(EventConstants.Search, new Param("eventType", "Search View", true));
        this.AddParam(EventConstants.Search, new Param("labelName", "searchview", true));
        this.AddParam(EventConstants.Search, new Param("spotdy_eventname", "__ecomtics_search", true));
        this.AddParam(EventConstants.Search, new Param("eventAction", "load", true));
    }

    public void addReviewEvent() {
        this.AddParam(EventConstants.Review, new Param("reviewtext_txt", true, false));
        this.AddParam(EventConstants.Review, new Param("productname_s", true, false));
        this.AddParam(EventConstants.Review, new Param("sku_s", true, false));
        this.AddParam(EventConstants.Review, new Param("price_d", true, false));
        this.AddParam(EventConstants.Review, new Param("userid", true, false));

        this.AddParam(EventConstants.Review, new Param("eventType", "ratingandreviews", true));
        this.AddParam(EventConstants.Review, new Param("labelName", "ratingandreviews", true));
        this.AddParam(EventConstants.Review, new Param("spotdy_eventname", "__spotdy_reviewratings", true));
        this.AddParam(EventConstants.Review, new Param("eventAction", "click", true));
    }
}
