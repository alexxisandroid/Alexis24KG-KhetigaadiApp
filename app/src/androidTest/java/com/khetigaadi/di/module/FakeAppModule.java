package com.khetigaadi.di.module;
import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.khetigaadi.base.room.repository.utils.AppExecutors;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.dagger.module.AppModule;
import com.khetigaadi.user.SessionManager;
import com.khetigaadi.user.model.User;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.UninstallModules;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.migration.DisableInstallInCheck;
import dagger.hilt.testing.TestInstallIn;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
@Module
//@TestInstallIn(components = SingletonComponent.class, replaces = AppModule.class)
//@UninstallModules(AppModule.class)
@DisableInstallInCheck
@HiltAndroidTest
public class FakeAppModule {
    @Provides
    @Singleton
    public Context provideContext(@ApplicationContext Context context) {
        return context;
    }
    @Provides
    @Singleton
    User providesUser(SessionManager session) {
        return new User();
    }
    @Provides
    @Singleton
    SessionManager providesSession(@ApplicationContext Context application) {
        return new SessionManager(application);
    }
    @Provides
    @Singleton
    SharedPreferenceUtils providesSharedPref(@ApplicationContext Context application) {
        return SharedPreferenceUtils.getInstance(application);
    }
    @Provides
    @Singleton
    public Gson gson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }
    @Provides
    @Singleton
    public Tracker getDefaultTracker(@ApplicationContext Context application) {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(application);
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        return analytics.newTracker("UA-77564827-2");
    }
    @Provides
    @Singleton
    public AppExecutors provideAppExecutors() {
        return new AppExecutors();
    }
    @Provides
    @Singleton
    public CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }
}
