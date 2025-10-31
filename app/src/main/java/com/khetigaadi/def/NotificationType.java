package com.khetigaadi.def;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Retention(RetentionPolicy.SOURCE)
@StringDef({NotificationType.NEW_TRACTOR_NOTIFICATION, NotificationType.IMPLEMENT_NOTIFICATION, NotificationType.NEWS_NOTIFICATION})
public @interface NotificationType {
    String NEW_TRACTOR_NOTIFICATION = "newTractor";
    String IMPLEMENT_NOTIFICATION = "implement";
    String NEWS_NOTIFICATION = "news";
}
