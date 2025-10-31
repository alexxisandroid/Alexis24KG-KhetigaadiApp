package com.khetigaadi.room.db;
import android.content.Context;
import android.util.Log;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.khetigaadi.BuildConfig;
//import com.khetigaadi.base.model.Brand;
import com.khetigaadi.implement.newImplement.util.Brand;
import com.khetigaadi.base.model.City;
//import com.khetigaadi.base.model.Model;
//import com.khetigaadi.shop.room.dao.DaoCategory;
import com.khetigaadi.room.dao.DaoCategory;
import com.khetigaadi.shop.model.Category;
//import com.khetigaadi.shop.room.dao.DaoCart;
import com.khetigaadi.room.dao.DaoCart;
import com.khetigaadi.tractor.util.Model;
import com.khetigaadi.base.model.State;
import com.khetigaadi.base.model.Taluka;
import com.khetigaadi.base.room.dataConveter.UriConverter;
import com.khetigaadi.commons.model.Hp;
import com.khetigaadi.commons.model.Recent;
import com.khetigaadi.implement.newImplement.util.BrandTypeMaster;
import com.khetigaadi.implement.newImplement.util.Type;
import com.khetigaadi.room.dao.DaoBrand;
import com.khetigaadi.room.dao.DaoHp;
import com.khetigaadi.room.dao.DaoModel;
import com.khetigaadi.room.dao.DaoRecent;
import com.khetigaadi.room.dao.DaoRentTractor;
import com.khetigaadi.room.dao.DaoType;
import com.khetigaadi.room.dao.DaoUsedTractor;
//import com.khetigaadi.shop.model.Category;
import com.khetigaadi.shop.model.OrderProduct;
import com.khetigaadi.shop.model.Product;
//import com.khetigaadi.shop.room.dao.DaoCart;
import com.khetigaadi.tractor.rent.rentTractorNewUi.model.RentImplement;
import com.khetigaadi.tractor.sellTractorNewUi.model.TractorImages;
import com.khetigaadi.user.room.dao.DaoCity;
import com.khetigaadi.user.room.dao.DaoState;
import com.khetigaadi.user.room.dao.DaoTaluka;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Database(entities = {Brand.class,Type.class,Model.class,Hp.class,State.class,City.class,Taluka.class,BrandTypeMaster.class,Recent.class,Product.class,OrderProduct.class,Category.class,TractorImages.class,RentImplement.class},version = BuildConfig.APP_DATA_VERSION, exportSchema = false)
@TypeConverters(UriConverter.class)
public abstract class MyDatabase extends RoomDatabase {
    private static volatile MyDatabase INSTANCE;
    private static final int NUMBERS_OF_THREADS = 1;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBERS_OF_THREADS);
    public static MyDatabase getDatabase(final Context context) {
        Log.d("dfgdgdf",""+INSTANCE);
        if (INSTANCE == null) {
            synchronized (MyDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    MyDatabase.class,"KhetiGaadi").allowMainThreadQueries().fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        Log.d("dfgdgdf22",""+INSTANCE+"=="+(INSTANCE instanceof MyDatabase));
        return INSTANCE;
    }
    public abstract DaoBrand daoBrand();
    public abstract DaoType daoType();
    public abstract DaoModel daoModel();
    public abstract DaoHp daoHp();
    public abstract DaoState daoState();
    public abstract DaoCity daoCity();
    public abstract DaoTaluka daoTaluka();
    public abstract DaoRecent daoRecent();
    public abstract DaoUsedTractor daoUsedTractor();
    public abstract DaoCart daoCart();
    public abstract DaoCategory daoCategory();
    public abstract DaoRentTractor daoRentTractor();
}
