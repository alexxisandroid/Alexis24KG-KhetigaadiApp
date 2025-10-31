package com.khetigaadi.shop.dataConveter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.khetigaadi.shop.model.Product;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class CategoryTypeConverter {
    private static final Gson gson = new Gson();
    @TypeConverter
    public static List<Product> stringToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<Product>>() {
        }.getType();
        return gson.fromJson(data, listType);
    }
    @TypeConverter
    public static String ListToString(List<Product> someObjects) {
        return gson.toJson(someObjects);
    }
}

