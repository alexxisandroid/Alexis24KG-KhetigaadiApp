package com.khetigaadi.commons.utils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;
import com.khetigaadi.R;

import org.jetbrains.annotations.NotNull;
import org.ocpsoft.prettytime.PrettyTime;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;


/**
 * Created by Irfan on 1/14/2016.
 */
public class Constants {
    public static final int DEALER_DETAILS_REQUESRT = 58;
    public static final String APP_BANNER_KEY = "allBanner";
    public static final String TOKEN_ID = "TOKEN_ID";
    public static final String IS_FIRST_TIME_LAUNCH = "isFirstLaunch";
    public static final String POPULAR_TRACTORS = "popularTractor";
    public static final String CURRENT_LANGUAGE = "currentLanguage";
    public static final int IMAGE_COMPRESS_QUALITY = 100;//50% imamge compress
    public static final int NOTIFICATION_REQUEST_CODE = 0;
    public static final String BRAND_BASE_URL = "brandBaseUrl";

    public static final String NOTIFICATION_INTENT_KEY = "notification";

    public static final long ANIM_VIEWPAGER_DELAY = 6000;
    public static final String NO_OF_LAUNCHED = "noOfLaunched";
    public static final @NonNull String  PROFILE_UPDATED = "profileUpdated";


    public static boolean REDIRECT_TO_EDIT_PROFILE_COUNT = false;
    private static final String DATE_SERVER_FORMAT = "yyyy-MM-dd hh:mm:ss";
    private static final String DATE_REQUERED_FORMAT = "dd MMM yyyy hh:mm a";
    //    public static String compareTractors = "";
    public static Pattern PHONE = Pattern.compile("^(\\+\\d{1,3}[- ]?)?\\d{10}$");
    @NotNull
    public static final String TYPES_BASE_URL = "typeBaseUrl";
    @NotNull
    public static final String STATE_BASE_URL = "stateBaseUrl";

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable();
        }
        return false;
    }


    public static String decodeURL(String str) {
        if (str == null) {
            return "";
        }
        try {
//            Log.e("ImageUrl",URLDecoder.decode(str, "UTF-8"));
            return URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static LatLng getLatLong(Activity activity, String city) {
        Geocoder coder = new Geocoder(activity);
        List<Address> address;
        try {
            address = coder.getFromLocationName(city, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            Timber.i(location.toString());
            return new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String convertTimeAgo(String time) {
        Date date = null;
        try {
            date = new SimpleDateFormat(DATE_SERVER_FORMAT).parse(time);
            return new PrettyTime().format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static String getRealPath(Context context, Intent data) {
        Uri uri = data.getData();
        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(projection[0]);
        String picturePath = cursor.getString(columnIndex); // returns null
        cursor.close();
        return picturePath;
    }

    public static boolean isValidName(String name) {
        final String USERNAME_PATTERN = "[a-zA-Z\\\\._\\\\-]{3,}";
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public static List<Year> getYears() {
        List<Year> stateList = new ArrayList<>();
        stateList.add(new Year(1, "2024"));
        stateList.add(new Year(2, "2023"));
        stateList.add(new Year(3, "2022"));
        stateList.add(new Year(4, "2021"));
        stateList.add(new Year(5, "2020"));
        stateList.add(new Year(6, "2019"));
        stateList.add(new Year(7, "2018"));
        stateList.add(new Year(8, "2017"));
        stateList.add(new Year(9, "2016"));
        stateList.add(new Year(10, "2015"));
        stateList.add(new Year(11, "2014"));
        stateList.add(new Year(12, "2013"));
        stateList.add(new Year(13, "2012"));
        stateList.add(new Year(14, "2011"));
        stateList.add(new Year(15, "2010"));
        stateList.add(new Year(16, "2009"));
        stateList.add(new Year(17, "2008"));
        stateList.add(new Year(18, "2007"));
        stateList.add(new Year(19, "2006"));
        stateList.add(new Year(20, "2005"));
        return stateList;
    }

    public void noInternetConnection(final Activity activity, View view) {
        try {
            Snackbar snackbar = Snackbar
                    .make(view, activity.getResources().getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG)
                    .setAction(activity.getString(R.string.action_settings), v -> activity.startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0));
            snackbar.setActionTextColor(ContextCompat.getColor(activity, R.color.white));
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimary));//change Snackbar's background color;
            TextView textView = (TextView) snackbarView.findViewById(R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(activity, R.color.white));//change Snackbar's text color;
            snackbar.show();
        } catch (Exception e) {
//            Log.i(TAG, e.getMessage());
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = activity.getCurrentFocus();
        if (v != null)
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


}
