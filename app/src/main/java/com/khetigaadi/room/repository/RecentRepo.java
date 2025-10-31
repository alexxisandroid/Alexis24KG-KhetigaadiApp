package com.khetigaadi.room.repository;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.khetigaadi.commons.model.Recent;
import com.khetigaadi.room.db.MyDatabase;

import java.util.List;
public class RecentRepo {
    private final MyDatabase database;
    public RecentRepo(Context context) {
        database = MyDatabase.getDatabase(context);
    }
    public void insert(final Recent recent) {
        MyDatabase.databaseWriteExecutor.execute(() -> database.daoRecent().insert(recent));
    }
    public LiveData<List<Recent>> getRecent() {
        return database.daoRecent().getAllRecent();
    }
}
