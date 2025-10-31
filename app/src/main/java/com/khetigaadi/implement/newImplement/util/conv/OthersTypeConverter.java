package com.khetigaadi.implement.newImplement.util.conv;
import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.khetigaadi.implement.newImplement.util.Others;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
public class OthersTypeConverter {
    private static final Gson gson = new Gson();
    @TypeConverter
    public static List<Others> stringToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<Others>>() {
        }.getType();
        return gson.fromJson(data, listType);
    }
    @TypeConverter
    public static String ListToString(List<Others> someObjects) {
        return gson.toJson(someObjects);
    }
}