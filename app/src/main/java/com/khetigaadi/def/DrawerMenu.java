package com.khetigaadi.def;
import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Retention(RetentionPolicy.SOURCE)
@IntDef({DrawerMenu.Home, DrawerMenu.TRACTORS, DrawerMenu.IMPLEMENTS, DrawerMenu.BUY_OLD_TRACTOR,
        DrawerMenu.SELL_OLD_TRACTOR, DrawerMenu.RENT, DrawerMenu.COMPARE, DrawerMenu.SHOP,
        DrawerMenu.MY_FAVORITES, DrawerMenu.LANGUAGE, DrawerMenu.NEWS, DrawerMenu.DEALERS,
        DrawerMenu.SERVICE_CENTER, DrawerMenu.WEATHER, DrawerMenu.SAFETY_TIPS,DrawerMenu.SHARE,DrawerMenu.FEEDBACK,DrawerMenu.ABOUT_KHETIGAADI,
        DrawerMenu.LOGOUT, DrawerMenu.KHETIGURU, DrawerMenu.HARVESTER, DrawerMenu.MANDI_PRICE})
public @interface DrawerMenu {
    int Home = 1;
    int TRACTORS = 2;
    int IMPLEMENTS = 3;
    int BUY_OLD_TRACTOR = 4;
    int SELL_OLD_TRACTOR = 5;
    int RENT = 6;
    int COMPARE = 7;
    int SHOP = 8;
    int MY_FAVORITES = 9;
    int LANGUAGE = 10;
    int NEWS = 11;
    int DEALERS = 12;
    int SERVICE_CENTER = 13;
    int WEATHER = 14;
    int SAFETY_TIPS = 15;

    int SHARE = 16;
    int FEEDBACK = 17;

   // int ABOUT_KHETIGAADI = 16;
    int ABOUT_KHETIGAADI = 18;

   // int LOGOUT = 17;
    int LOGOUT = 19;
   // int LOGOUT = 20;

   // int HARVESTER = 19;
    int HARVESTER = 21;
   // int MANDI_PRICE = 20;
    int MANDI_PRICE = 22;
  //  int KHETIGURU = 18;
    int KHETIGURU = 20;
}