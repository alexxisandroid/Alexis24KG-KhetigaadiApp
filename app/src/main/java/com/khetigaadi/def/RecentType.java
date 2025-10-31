package com.khetigaadi.def;
import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Retention(RetentionPolicy.SOURCE)
@IntDef({RecentType.NEW_TRACTOR, RecentType.OLD_TRACTOR, RecentType.RENT_TRACTOR})
public @interface RecentType {
    int NEW_TRACTOR = 1;
    int OLD_TRACTOR = 2;
    int RENT_TRACTOR = 3;
}