package com.khetigaadi.def;
import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Retention(RetentionPolicy.SOURCE)
@IntDef({CallFrom.NEW_TRACTOR, CallFrom.OLD_TRACTOR, CallFrom.IMPLEMENT, CallFrom.RENT_TRACTOR, CallFrom.DEALER, CallFrom.HARVESTER})
public @interface CallFrom {
    int NEW_TRACTOR = 1;
    int OLD_TRACTOR = 2;
    int IMPLEMENT = 3;
    int RENT_TRACTOR = 4;
    int DEALER = 5;
    int HARVESTER = 6;
}