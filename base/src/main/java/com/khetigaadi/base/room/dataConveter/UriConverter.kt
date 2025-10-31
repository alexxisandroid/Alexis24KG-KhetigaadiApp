package com.khetigaadi.base.room.dataConveter
import android.net.Uri
import androidx.room.TypeConverter
class UriConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromString(value: String?):Uri? {
            return if (value == null) null else Uri.parse(value)
        }
        @TypeConverter
        @JvmStatic
        fun toString(uri: Uri?):String {
            return uri.toString()
        }
    }
}