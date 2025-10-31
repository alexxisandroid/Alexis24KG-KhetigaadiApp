package com.khetigaadi.shop.dataConveter;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.khetigaadi.shop.model.ProductImage;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
public class ProductImageDataTypeConverter {
    private static final Gson gson = new Gson();
    @TypeConverter
    public static List<ProductImage> stringToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<ProductImage>>() {
        }.getType();
        return gson.fromJson(data, listType);
    }
    @TypeConverter
    public static String ListToString(List<ProductImage> someObjects) {
        return gson.toJson(someObjects);
    }

}