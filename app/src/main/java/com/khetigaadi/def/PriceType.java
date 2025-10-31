package com.khetigaadi.def;
import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Retention(RetentionPolicy.SOURCE)
@IntDef({PriceType.TRACTOR, PriceType.IMPLEMENT, PriceType.HARVESTER})
public @interface PriceType {
    int TRACTOR = 1;
    int IMPLEMENT = 2;
    int HARVESTER = 3;
}