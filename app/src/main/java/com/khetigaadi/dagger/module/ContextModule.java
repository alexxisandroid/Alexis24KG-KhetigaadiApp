package com.khetigaadi.dagger.module;
import android.content.Context;
import com.khetigaadi.dagger.qualifier.ApplicationContext;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
@Module
@InstallIn(SingletonComponent.class)
public class ContextModule {
    private Context context;
    @Provides
    @Singleton
    public Context provideContext(@ApplicationContext Context context) {
        return context;
    }
}
