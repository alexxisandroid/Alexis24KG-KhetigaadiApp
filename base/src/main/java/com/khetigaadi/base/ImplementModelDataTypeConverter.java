package com.khetigaadi.base;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.khetigaadi.base.model.ImplementModel;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class ImplementModelDataTypeConverter {
    private static final Gson gson = new Gson();

    @TypeConverter
    public static List<ImplementModel> stringToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<ImplementModel>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String ListToString(List<ImplementModel> someObjects) {
        return gson.toJson(someObjects);
    }
}
