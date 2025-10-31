package com.khetigaadi.dagger.module;
import android.content.Context;
import com.khetigaadi.base.room.repository.utils.AppExecutors;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.harvester.HarvesterRepository;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.retrofit.ShopAPIs;
import com.khetigaadi.room.repository.BrandRepository;
import com.khetigaadi.room.repository.CartRepository;
import com.khetigaadi.room.repository.CategoryRepository;
import com.khetigaadi.room.repository.CityRepository;
import com.khetigaadi.room.repository.HpRepository;
import com.khetigaadi.room.repository.ModelRepository;
import com.khetigaadi.room.repository.NewsRepository;
import com.khetigaadi.room.repository.RecentRepo;
import com.khetigaadi.room.repository.RentTractorRepository;
import com.khetigaadi.room.repository.StateRepository;
import com.khetigaadi.room.repository.TalukaRepository;
import com.khetigaadi.room.repository.TypeRepository;
import com.khetigaadi.room.repository.UsedTractorRepository;
import com.khetigaadi.user.SessionManager;
import com.khetigaadi.user.retrofit.UserAPIs;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
@Module
@InstallIn(SingletonComponent.class)
public class RoomModule {
    @Provides
    @Singleton
    BrandRepository providesBrandRepository(@ApplicationContext Context application,KhetiAPIs khetiAPIs,SharedPreferenceUtils sharedPreferenceUtils,AppExecutors appExecutors)
    {
        return new BrandRepository(application, khetiAPIs, sharedPreferenceUtils, appExecutors);
    }
    @Provides
    @Singleton
    TypeRepository providesTypeRepository(@ApplicationContext Context application,KhetiAPIs khetiAPIs,AppExecutors appExecutors,SharedPreferenceUtils sharedPreferenceUtils)
    {
        return new TypeRepository(application, khetiAPIs, appExecutors, sharedPreferenceUtils);
    }
    @Provides
    @Singleton
    ModelRepository providesModelRepository(@ApplicationContext Context application,KhetiAPIs khetiAPIs,SharedPreferenceUtils sharedPreferenceUtils,AppExecutors appExecutors)
    {
        return new ModelRepository(application, khetiAPIs, sharedPreferenceUtils, appExecutors);
    }
    @Provides
    @Singleton
    HpRepository providesHpRepository(@ApplicationContext Context application,KhetiAPIs khetiAPIs,AppExecutors appExecutors)
    {
        return new HpRepository(application, khetiAPIs, appExecutors);
    }
    @Provides
    @Singleton
    StateRepository providesStateRepository(@ApplicationContext Context application,UserAPIs userAPIs,SharedPreferenceUtils sharedPreferenceUtils,AppExecutors appExecutors)
    {
        return new StateRepository(application,userAPIs,sharedPreferenceUtils,appExecutors);
    }
    @Provides
    @Singleton
    CityRepository providesCityRepository(@ApplicationContext Context application,UserAPIs userAPIs,AppExecutors appExecutors)
    {
        return new CityRepository(application,userAPIs,appExecutors);
    }
    @Provides
    @Singleton
    TalukaRepository providesTalukaRepository(@ApplicationContext Context application,UserAPIs userAPIs,AppExecutors appExecutors)
    {
        return new TalukaRepository(application,userAPIs,appExecutors);
    }
    @Provides
    @Singleton
    HarvesterRepository providesHarvesterRepository(KhetiAPIs khetiAPIs)
    {
        return new HarvesterRepository(khetiAPIs);
    }
    @Provides
    @Singleton
    RecentRepo provideRecentRepo(@ApplicationContext Context application) {
        return new RecentRepo(application);
    }
    @Provides
    @Singleton
    UsedTractorRepository provideUsedTractorRepo(@ApplicationContext Context application)
    {
        return new UsedTractorRepository(application);
    }
    @Provides
    @Singleton
    RentTractorRepository provideRentTractorRepo(@ApplicationContext Context context)
    {
        return new RentTractorRepository(context);
    }
    @Provides
    @Singleton
    CartRepository providesCartRepository(@ApplicationContext Context application,SessionManager sessionManager)
    {
        return new CartRepository(application,sessionManager);
    }
    @Provides
    @Singleton
    CategoryRepository providesCategoryRepository(@ApplicationContext Context application,ShopAPIs shopAPIs,AppExecutors appExecutors,SessionManager sessionManager)
    {
        return new CategoryRepository(application,shopAPIs,appExecutors,sessionManager);
    }
    @Provides
    @Singleton
    NewsRepository providesNewsRepository(KhetiAPIs khetiAPIs)
    {
        return new NewsRepository(khetiAPIs);
    }
}
