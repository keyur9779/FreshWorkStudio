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


-keep class com.app.freshworkstudio.model**{*;}
-keepclassmembers class com.app.freshworkstudio.model**{*;}

-keep class com.skydoves**{*;}
-keepclassmembers class com.skydoves**{*;}

-assumenosideeffects class android.util.Log {
  public static *** v(...);
  public static *** d(...);
  public static *** i(...);
  public static *** w(...);
  public static *** e(...);
}

-keepclassmembers class com.infoblox.bloxone.model**{*;}
-keep class kotlin.reflect.**{*;}
-keepclassmembers class kotlin.reflect.**{*;}

-dontwarn kotlin.reflect.jvm.internal.**
-keep class kotlin.reflect.jvm.internal.** { *; }
-keepclassmembers class kotlin.reflect.jvm.internal.** { *; }




-keep class kotlin.Metadata { *; }
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}



-keepnames @kotlin.Metadata class com.yourpackage.model.**
-keep class com.yourpackage.model.** { *; }
-keepclassmembers class com.yourpackage.model.** { *; }

# data binding
-dontwarn androidx.databinding.**
-keep class androidx.databinding.** { *; }
-keep class * extends androidx.databinding.DataBinderMapper
-keep class ** { @androidx.databinding.Bindable <methods>; }
-keep class ** { @androidx.databinding.Bindable <fields>; }

# bindables
-dontwarn com.skydoves.bindables.**
-dontwarn androidx.databinding.Bindable
-keep class com.skydoves.bindables.** { *; }
-keep class ** extends com.skydoves.bindables.**
-keep @androidx.databinding.Bindable interface *