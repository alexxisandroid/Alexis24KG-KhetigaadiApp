package com.khetigaadi.def;
import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Retention(RetentionPolicy.SOURCE)
@IntDef({DrawerSubMenu.DASHBOARD, DrawerSubMenu.PROFILE,
        DrawerSubMenu.MY_OLD_TRACTORS, DrawerSubMenu.MY_RENT_TRACTORS, DrawerSubMenu.BUY_ENQUIRY,
        DrawerSubMenu.MY_FAVORITES_SUB, DrawerSubMenu.ADD_OWNER_TRACTOR,DrawerSubMenu.ABOUT_US, DrawerSubMenu.TERMS_AND_CONDITIONS,
        DrawerSubMenu.PRIVACY_POLICY})
public @interface DrawerSubMenu {
    int DASHBOARD = 18;
    int PROFILE = 19;
    int MY_OLD_TRACTORS = 20;
    int MY_RENT_TRACTORS = 21;
    int BUY_ENQUIRY = 22;
    int MY_FAVORITES_SUB = 23;
    int ADD_OWNER_TRACTOR = 24;

    int ABOUT_US = 25;
    int TERMS_AND_CONDITIONS = 26;
    int PRIVACY_POLICY = 27;
}
