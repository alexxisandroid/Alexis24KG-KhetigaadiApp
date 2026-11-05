# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\android\AndroidSoftware\adt-bundle-windows-x86-20131030\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
# Gson


##############################################
# General
##############################################
#
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes EnclosingMethod
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault

##############################################
# Remove Log statements in Release
##############################################

-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
}

##############################################
# Retrofit
##############################################

-keepattributes Signature
-keepattributes Exceptions

#noinspection ExpensiveKeepRuleInspection
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }

##############################################
# OkHttp
##############################################

-dontwarn okhttp3.**
-dontwarn okio.**

##############################################
# Gson
##############################################

#noinspection ExpensiveKeepRuleInspection
-keep class com.google.gson.** { *; }

-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

##############################################
# Kotlin
##############################################

-keep class kotlin.Metadata { *; }

-dontwarn kotlin.**
-dontwarn kotlinx.**

##############################################
# Coroutines
##############################################

-keep class kotlinx.coroutines.** { *; }

##############################################
# ViewBinding
##############################################

-keep class **Binding {
    *;
}

##############################################
# Android Parcelable
##############################################

-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator CREATOR;
}

##############################################
# Room Database
##############################################

-keep class androidx.room.** { *; }

-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *
-keep @androidx.room.Database class *

##############################################
# Firebase
##############################################

-keep class com.google.firebase.** { *; }

-dontwarn com.google.firebase.**

##############################################
# ML Kit
##############################################

-keep class com.google.mlkit.** { *; }

-dontwarn com.google.mlkit.**

##############################################
# Glide
##############################################

-keep public class * extends com.bumptech.glide.module.AppGlideModule
#noinspection ShrinkerUnresolvedReference
-keep class com.bumptech.glide.GeneratedAppGlideModuleImpl

##############################################
# Picasso
##############################################

-dontwarn com.squareup.picasso.**

##############################################
# DataBinding
##############################################

-keep class androidx.databinding.** { *; }

##############################################
# WebView JS Interface
##############################################

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

##############################################
# Enums
##############################################

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

##############################################
# Custom Views
##############################################

-keep public class * extends android.view.View

##############################################
# Native Libraries
##############################################

-keepclasseswithmembernames class * {
    native <methods>;
}

##############################################
# Keep Application
##############################################

-keep class * extends android.app.Application {
    *;
}

##############################################
# Activities
##############################################

-keep public class * extends androidx.appcompat.app.AppCompatActivity

##############################################
# Fragments
##############################################

-keep public class * extends androidx.fragment.app.Fragment

##############################################
# Services
##############################################

-keep public class * extends android.app.Service

##############################################
# BroadcastReceiver
##############################################

-keep public class * extends android.content.BroadcastReceiver

##############################################
# WorkManager
##############################################

-keep class * extends androidx.work.ListenableWorker {
    *;
}

##############################################
# Ignore Warnings
##############################################

-ignorewarnings



-keepattributes Signature

-keepattributes *Annotation*

# Retrofit

-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }

-keep class com.google.gson.** { *; }

-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
# Room

-keep class androidx.room.** { *; }

-keep class com.google.mlkit.** { *; }

-keep class **Binding {
    *;
}

-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

##################################
# ATTRIBUTES
##################################

-keepattributes Signature
-keepattributes *Annotation*
-keepattributes RuntimeVisibleAnnotations

##################################
# GSON
##################################

-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

##################################
# PARCELABLE
##################################

-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator CREATOR;
}

##################################
# WEBVIEW
##################################

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

##################################
# HILT
##################################

#noinspection ExpensiveKeepRuleInspection
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

##################################
# WORKMANAGER
##################################

-keep class * extends androidx.work.ListenableWorker

##################################
# GLIDE
##################################

-keep public class * extends com.bumptech.glide.module.AppGlideModule
#noinspection ShrinkerUnresolvedReference
-keep class com.bumptech.glide.GeneratedAppGlideModuleImpl

##################################
# LOGS
##################################

-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** i(...);
    public static *** v(...);
    public static *** w(...);
}

-assumenosideeffects class timber.log.Timber {
    public static *** d(...);
    public static *** i(...);
    public static *** v(...);
    public static *** w(...);
}

##################################
# WARNINGS
##################################

-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn kotlin.**
-dontwarn kotlinx.**

# Enable general optimizations
-optimizationpasses 5

# Remove log calls in release
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# Remove Timber logs
-assumenosideeffects class timber.log.Timber {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# Keep annotations
#-keepattributes *Annotation*

# Retrofit
#-keepattributes Signature
-keepattributes RuntimeVisibleAnnotations

# Gson
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Kotlin metadata
-keep class kotlin.Metadata { *; }

# Remove source information
-renamesourcefileattribute SourceFile

# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn com.google.android.apps.nbu.paisa.inapp.client.api.PaymentsClient
-dontwarn com.google.android.apps.nbu.paisa.inapp.client.api.Wallet
-dontwarn com.google.android.apps.nbu.paisa.inapp.client.api.WalletUtils
-dontwarn com.google.android.gms.auth.api.credentials.Credential
-dontwarn com.google.android.gms.auth.api.credentials.Credentials
-dontwarn com.google.android.gms.auth.api.credentials.CredentialsClient
-dontwarn com.google.android.gms.auth.api.credentials.CredentialsOptions$Builder
-dontwarn com.google.android.gms.auth.api.credentials.CredentialsOptions
-dontwarn com.google.android.gms.auth.api.credentials.HintRequest$Builder
-dontwarn com.google.android.gms.auth.api.credentials.HintRequest

