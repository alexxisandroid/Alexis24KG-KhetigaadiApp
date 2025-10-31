package com.khetigaadi.shop.def;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Retention(RetentionPolicy.SOURCE)
@StringDef({AddressType.ADDRESS_HOME, AddressType.ADDRESS_OFFICE})
public @interface AddressType {
    String ADDRESS_OFFICE = "Office";
    String ADDRESS_HOME = "Home";
}