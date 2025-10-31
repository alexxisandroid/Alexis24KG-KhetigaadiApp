package com.khetigaadi.user;
import android.content.Context;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.khetigaadi.base.model.City;
import com.khetigaadi.base.model.agri.Others;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.user.model.User;
public class SessionManager {
    private static final String TAG = "SessionManager";
    private static final String KEY_USER = "user";
    private static final String KEY_USER_OTHERS = "userOthers";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String FCM_TOKEN = "fcmToken";
    private static final String ECOM_OTHER_CATEGORY_IMAGE = "shopCategoryImagePath";
    private static final String ECOM_OTHER_PRODUCT_IMAGE = "getShopProductImagePath";
    public static final String USER_CITY = "userCity";
    private static final String CUSTOMER = "customer";
    private final SharedPreferenceUtils sharedPreference;
    private User user;
    private Others userOther;
    private String language;
    public SessionManager(Context context) {
        sharedPreference = SharedPreferenceUtils.getInstance(context);
    }
    public void setLogin(boolean isLoggedIn) {
        sharedPreference.setValue(KEY_IS_LOGGED_IN, isLoggedIn);
    }
    public boolean isLoggedIn() {
        return sharedPreference.getBooleanValue(KEY_IS_LOGGED_IN, false);
    }
    public void setUser(User user) {
        this.user = user;
        setLogin(true);
        if (user.getToken() != null && !TextUtils.isEmpty(user.getToken()))
            setToken(user.getToken());
        setCustomer(user);
        sharedPreference.setValue(KEY_USER, new Gson().toJson(user));
    }
    public User getUser() {
        if (isLoggedIn()) {
            if (user == null)
                user = new Gson().fromJson(sharedPreference.getStringValue(KEY_USER, ""), User.class);
        }
        return user;
    }
    public void clear() {
        sharedPreference.removeKey(KEY_USER);
        sharedPreference.removeKey(KEY_IS_LOGGED_IN);
        sharedPreference.removeKey(USER_CITY);
    }
    public void setUserOthers(Others others) {
        if (userOther == null)
            userOther = others;
        if (others.getUserPath() != null && !TextUtils.isEmpty(others.getUserPath()))
            userOther.setUserPath(others.getUserPath());
        if (others.getPostPath() != null && !TextUtils.isEmpty(others.getPostPath()))
            userOther.setPostPath(others.getPostPath());
        if (others.getGroupPath() != null && !TextUtils.isEmpty(others.getGroupPath()))
            userOther.setGroupPath(others.getGroupPath());
        if (others.getCropPath() != null && !TextUtils.isEmpty(others.getCropPath()))
            userOther.setCropPath(others.getCropPath());
        sharedPreference.setValue(KEY_USER_OTHERS, new Gson().toJson(userOther));
    }
    public Others getUserOthers() {
        if (userOther == null)
            userOther = new Gson().fromJson(sharedPreference.getStringValue(KEY_USER_OTHERS, ""), Others.class);
        return userOther;
    }
    public void setPushToken(String token) {
        sharedPreference.setValue(FCM_TOKEN, token);
    }
    public String getFCMToken() {
        //        Log.e(TAG, "getFCMToken: " + token);
        return sharedPreference.getStringValue(FCM_TOKEN, "");
    }
    public void setToken(String token) {
        sharedPreference.setValue(Constants.TOKEN_ID, token);
    }
    public String getToken() {
        //        Log.e(TAG, "getToken: " + token);
        return sharedPreference.getStringValue(Constants.TOKEN_ID, "");
    }
    public City getUserLocation() {
        City city = new Gson().fromJson(sharedPreference.getStringValue(USER_CITY, ""), City.class);
        if (city == null)
            if (getUser() != null)
                city = getUser().getCity();
        return city;
    }
    public void setUserLocation(City userLocation) {
        sharedPreference.setValue(USER_CITY, new Gson().toJson(userLocation));
    }
    public com.khetigaadi.model.Others getEcomOther() {
        com.khetigaadi.model.Others others = new com.khetigaadi.model.Others();
        others.setShopCategoryImagePath(sharedPreference.getStringValue(ECOM_OTHER_CATEGORY_IMAGE, ""));
        others.setShopProductImagePath(sharedPreference.getStringValue(ECOM_OTHER_PRODUCT_IMAGE, ""));
        return others;
    }
    public void setEcomOther(com.khetigaadi.shop.model.Others others) {
        if (others != null) {
            if (others.getShopCategoryImagePath() != null)
                sharedPreference.setValue(ECOM_OTHER_CATEGORY_IMAGE, others.getShopCategoryImagePath());
            if (others.getShopProductImagePath() != null)
                sharedPreference.setValue(ECOM_OTHER_PRODUCT_IMAGE, others.getShopProductImagePath());
        }
    }
    public void setCustomer(User customer) {
        if (customer != null) {
            sharedPreference.setValue(CUSTOMER, new Gson().toJson(customer));
        }
    }
    public User getCustomer() {
        return new Gson().fromJson(sharedPreference.getStringValue(CUSTOMER,"{}"),User.class);
    }
    public String getLanguage() {
        if (language != null)
            return language;
        return sharedPreference.getStringValue(Constants.CURRENT_LANGUAGE, "en_US");
    }
    public void setLanguage(String language) {
        this.language = language;
        sharedPreference.setValue(Constants.CURRENT_LANGUAGE,language);
    }
    public void Logout() {
        clear();
        setLogin(false);
    }
}