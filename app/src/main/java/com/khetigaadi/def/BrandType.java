package com.khetigaadi.def;
import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Retention(RetentionPolicy.SOURCE)
@IntDef({BrandType.NEW_TRACTOR, BrandType.OLD_TRACTOR, BrandType.IMPLEMENT})
public @interface BrandType {
    int NEW_TRACTOR = 1;
    int OLD_TRACTOR = 2;
    int IMPLEMENT = 3;
}