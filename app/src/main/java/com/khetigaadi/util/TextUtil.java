package com.khetigaadi.util;
import androidx.annotation.Nullable;
public class TextUtil {
    public static boolean isEmptyOrZero(@Nullable CharSequence str) {
        try {
            if (android.text.TextUtils.isEmpty(str))
                return true;
            else return Double.parseDouble(str.toString()) == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }
}
