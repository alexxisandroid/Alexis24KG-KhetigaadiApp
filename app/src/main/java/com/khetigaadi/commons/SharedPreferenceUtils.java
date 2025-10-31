package com.khetigaadi.commons;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.khetigaadi.R;
public class SharedPreferenceUtils {
    protected Context mContext;
    private final SharedPreferences pref;
    private Editor editor;
    private static SharedPreferenceUtils sInstance;
    private SharedPreferenceUtils(Context context) {
        mContext = context;
        pref = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        editor = pref.edit();
    }
    public static synchronized SharedPreferenceUtils getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SharedPreferenceUtils(context);
        }
        return sInstance;
    }
    public void setValue(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }
    public void setValue(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }
    public void setValue(String key, double value) {
        setValue(key, Double.toString(value));
    }
    public void setValue(String key, long value) {
        editor.putLong(key, value);
        editor.apply();
    }
    public void setValue(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }
    public String getStringValue(String key, String defaultValue) {
        return pref.getString(key, defaultValue);
    }
    public int getIntValue(String key, int defaultValue) {
        return pref.getInt(key, defaultValue);
    }
    public long getLongValue(String key, long defaultValue) {
        return pref.getLong(key, defaultValue);
    }
    public boolean getBooleanValue(String keyFlag, boolean defaultValue) {
        return pref.getBoolean(keyFlag, defaultValue);
    }
    public String getRentMonth() {
        return pref.getString("RENTMONTH", "");
    }
    public void setRentMonth(String rentMonth) {
        editor.putString("RENTMONTH", rentMonth);
        editor.commit();
    }
    public void setRentHour(String rentHour) {
        editor.putString("RENTHOUR", rentHour);
        editor.commit();
    }
    public String getRentHour() {
        return pref.getString("RENTHOUR", "");
    }

    public void setRentBrandId(String rentBrandId) {
        editor.putString("RENTBRANDID", rentBrandId);
        editor.commit();
    }
    public String getRentBrandId() {
        return pref.getString("RENTBRANDID", "");
    }
    public void setRentYear(String rentYear) {
        editor.putString("RENTYEAR", rentYear);
        editor.commit();
    }
    public String getRentYear() {
        return pref.getString("RENTYEAR", "");
    }
    public void setRentModelId(String rentModelId) {
        editor.putString("RENTMODELID", rentModelId);
        editor.commit();
    }
    public String getRentModelId() {
        return pref.getString("RENTMODELID", "");
    }
    public void setIsOwnerBrandId(String isOwnerBrandId) {
        editor.putString("ISOWNERBRANDID", isOwnerBrandId);
        editor.commit();
    }
    public String getIsOwnerBrandId() {
        return pref.getString("ISOWNERBRANDID", "");
    }
    public void setIsOwnerModelId(String isOwnerModelId) {
        editor.putString("ISOWNERMODELID", isOwnerModelId);
        editor.commit();
    }
    public String getIsOwnerModelId() {
        return pref.getString("ISOWNERMODELID", "");
    }
    public void setIsOwnerYearId(String isOwnerYearId) {
        editor.putString("ISOWNERYEARID", isOwnerYearId);
        editor.commit();
    }
    public String getIsOwnerYearId() {
        return pref.getString("ISOWNERYEARID", "");
    }
    public String getIsOwnerMonthId() {
        return pref.getString("ISOWNERMONTHID", "");
    }
    public void setIsOwnerMonthId(String isOwnerMonthId) {
        editor.putString("ISOWNERMONTHID", isOwnerMonthId);
        editor.commit();
    }
    public String getImplementTypeImagePath() {
        return pref.getString("IMPLEMENTTYPEIMAGEPATH", "");
    }
    public void setImplementTypeImagePath(String implementTypeImagePath) {
        editor.putString("IMPLEMENTTYPEIMAGEPATH", implementTypeImagePath);
        editor.commit();
    }
    public String getTractorImagePath() {
        return pref.getString("TRACTORIMAGEPATH", "");
    }
    public void setTractorImagePath(String tractorImagePath) {
        editor.putString("TRACTORIMAGEPATH", tractorImagePath);
        editor.commit();
    }
    public void setTractorID(String tractorID) {
        editor.putString("TRACTORID", tractorID);
        editor.commit();
    }
    public void setImplementImageList(String implementImageList) {
        editor.putString("IMPLEMENTIMAGELIST", implementImageList);
        editor.commit();
    }
    public String getImplementImageList() {
        return pref.getString("IMPLEMENTIMAGELIST", "");
    }
    public void removeKey(String key) {
        if (editor != null) {
            editor.remove(key);
            editor.commit();
        }
    }
    public void clear() {
        editor.clear().commit();
    }
}
