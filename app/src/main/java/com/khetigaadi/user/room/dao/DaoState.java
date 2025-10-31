package com.khetigaadi.user.room.dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.khetigaadi.base.model.State;
import java.util.List;
@Dao
public interface DaoState {
    @Insert
    Long insertState(State state);
    @Query("SELECT * FROM STATE order by name")
    LiveData<List<State>> getStates();
    @Query("SELECT * FROM STATE where statePosition > 0 order by statePosition limit 8")
    LiveData<List<State>> getStatesByPosition();

    @Update
    void updateState(State state);


    @Delete
    void deleteState(State state);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<State> states);

    @Query("SELECT * FROM State WHERE id =:id")
    LiveData<State> getState(int id);


}





//public class DaoState {
//    @NotNull
//    public  LiveData<List<State>> states;
//    //@NotNull
//    //public  List<State> statesByPosition;
//    public void insertAll(@Nullable List<? extends State> types) {
//    }
//    @NotNull
//    public LiveData<State> getState(int id) {
//        return null;
//    }
//    @NotNull
//    public LiveData<List<State>> statesByPosition;
//}
