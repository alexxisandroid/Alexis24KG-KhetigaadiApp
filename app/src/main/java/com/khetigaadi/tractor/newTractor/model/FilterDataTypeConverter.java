package com.khetigaadi.tractor.newTractor.model;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.khetigaadi.shop.model.Filter;
import com.khetigaadi.shop.model.ProductImage;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
public class FilterDataTypeConverter {
    private static final Gson gson = new Gson();
    @TypeConverter
    public static List<Filter> stringToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<Filter>>() {
        }.getType();
        return gson.fromJson(data, listType);
    }
    @TypeConverter
    public static String ListToString(List<Filter> someObjects) {
        return gson.toJson(someObjects);
    }
}
