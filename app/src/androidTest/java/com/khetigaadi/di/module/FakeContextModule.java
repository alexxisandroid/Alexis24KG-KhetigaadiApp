package com.khetigaadi.di.module;

import android.content.Context;

import com.khetigaadi.dagger.module.ContextModule;
import com.khetigaadi.dagger.qualifier.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.testing.TestInstallIn;

@Module
@TestInstallIn(components = SingletonComponent.class,replaces = ContextModule.class)
public class FakeContextModule {
    private Context context;

    @Provides
    @Singleton
    public Context provideContext(@ApplicationContext Context context) {
        return context;
    }
}
