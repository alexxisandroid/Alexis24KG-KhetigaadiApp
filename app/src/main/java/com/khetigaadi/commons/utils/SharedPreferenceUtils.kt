package com.example.myapplication.commons.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceUtils(context: Context) {
    var context: Context?
        protected set
    private val pref: SharedPreferences
    private val editor: SharedPreferences.Editor?

    init {
        this.context = context
        pref = context.getSharedPreferences(DATA_BASE_NAME, Context.MODE_PRIVATE)
        editor = pref.edit()
    }

    fun setValue(key: String?, value: String?) {
        editor!!.putString(key, value)
        editor.commit()
    }

    //    public void setValue(String key,int value) {
    //        editor.putInt(key,value);
    //        editor.commit();
    //    }
    //    public void setValue(String key, double value) {
    //        setValue(key,Double.toString(value));
    //    }
    //    public void setValue(String key,long value) {
    //        editor.putLong(key, value);
    //        editor.commit();
    //    }
    //
    fun setValue(key: String?, value: Boolean) {
        editor!!.putBoolean(key, value)
        editor.commit()
    }

    fun getStringValue(key: String?, defaultValue: String?): String? {
        return pref.getString(key, defaultValue)
    }

    //    public int getIntValue(String key,int defaultValue) {
    //        return pref.getInt(key,defaultValue);
    //    }
    //    public long getLongValue(String key,long defaultValue) {
    //        return pref.getLong(key,defaultValue);
    //    }
    fun getBooleanValue(keyFlag: String?, defaultValue: Boolean): Boolean {
        return pref.getBoolean(keyFlag, defaultValue)
    }

    fun removeKey(key: String?) {
        if (editor != null) {
            editor.remove(key)
            editor.commit()
        }
    }

    fun clear() {
        editor!!.clear().commit()
    }

    companion object {
        //  private static final String TAG = "SharedPreferenceUtils";
        var DATA_BASE_NAME: String = "Card Scanner" // BuildConfig.APP_NAME;

        @SuppressLint("StaticFieldLeak")
        private var sInstance: SharedPreferenceUtils? = null

        @Synchronized
        fun getInstance(context: Context): SharedPreferenceUtils {
            if (sInstance == null) {
                sInstance = SharedPreferenceUtils(context.getApplicationContext())
            }
            return sInstance!!
        }
    }
}