# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#-keep class com.example.myapplicationtest.ObjectDetection.**{ *; }
#-keep class com.example.myapplicationtest.**{ *; }
#

#-------------- okhttp3 start-------------
# OkHttp3
# https://github.com/square/okhttp
# okhttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.* { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

# okhttp 3
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}
-keep interface okhttp3.**{*;}

# Okio
-dontwarn com.squareup.**
-dontwarn okio.**
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }

#Gson
-keep class com.google.gson.stream.** { *; }

#----------okhttp end--------------
-dontwarn com.example.utils.HttpUtilForYuanShenCKLink.**
-dontwarn com.example.myapplicationtest.MainActivityYuanShenHuoQuChouKaLink.**
-keep class com.example.utils.HttpUtilForYuanShenCKLink.**{ *; }
-keep class com.example.utils.HttpUtilForYuanShenCKLink
-keep class com.example.myapplicationtest.MainActivityYuanShenHuoQuChouKaLink.**{ *; }
-keep class com.example.myapplicationtest.MainActivityYuanShenHuoQuChouKaLink
-dontwarn kotlin.**
-keep class kotlin.**{*;}
-keep interface kotlin.**{*;}
-keep class com.example.myapplicationtest.dto.**{*;}