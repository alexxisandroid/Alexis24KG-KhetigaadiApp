package com.khetigaadi;
import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.multidex.MultiDex;
import androidx.work.Configuration;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.language.LocaleManager;
import com.khetigaadi.shop.payu.AppEnvironment;
import com.khetigaadi.user.SessionManager;
import javax.inject.Inject;
import dagger.hilt.android.HiltAndroidApp;
import timber.log.Timber;
@HiltAndroidApp
public class AppController extends Application implements Configuration.Provider {
    public final String TAG = AppController.class.getSimpleName();
    @Inject
    SharedPreferenceUtils pref;
  //  public static final boolean DEVELOPER_MODE = false;
    private static AppController mInstance;
    private AppEnvironment appEnvironment;
    @Inject
    HiltWorkerFactory workerFactory;
    @Inject
    public AppController() {
    }
    //6LexOREuAAAAAGnn06GUB0hr3ftOM5eIDhgmMBRG
//    @Override
//    protected void attachBaseContext(Context base) {
//              SessionManager sessionManager = new SessionManager(base);
//             super.attachBaseContext(new LocaleManager(sessionManager).setLocale(base));
//              super.attachBaseContext(base);
//        //  SessionManager sessionManager = new SessionManager(base);
//           new LocaleManager(sessionManager).setLocale(this);
//        MultiDex.install(this);
//        Timber.tag(TAG).d("attachBaseContext");
//    }
    @NonNull
    @Override
    public Configuration getWorkManagerConfiguration() {
        return new Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.DEBUG)
                .setWorkerFactory(workerFactory)
                .build();
    }
    @Override
    protected void attachBaseContext(Context base) {
        SessionManager sessionManager = new SessionManager(base);
        super.attachBaseContext(new LocaleManager(sessionManager).setLocale(base));

      //  super.attachBaseContext(base);
             //SessionManager sessionManager = new SessionManager(base);
          //  new LocaleManager(sessionManager).setLocale(this);

        MultiDex.install(this);
        Timber.tag(TAG).d("attachBaseContext");



    }
    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        if(BuildConfig.DEBUG) {
            appEnvironment = AppEnvironment.DEV;
        }
        else
            appEnvironment = AppEnvironment.PRODUCTION;
        mInstance = this;
        pref.setValue(Constants.NO_OF_LAUNCHED,pref.getIntValue(Constants.NO_OF_LAUNCHED,0)+1);
    }
    public static synchronized AppController getInstance() {
        return mInstance;
    }
    public AppEnvironment getAppEnvironment() {
        return appEnvironment;
    }
}