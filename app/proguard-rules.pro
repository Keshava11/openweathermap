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

###---------------Begin: proguard configuration for OkHttp-Retrofit  ----------

-keep class okio.**{*;}
-dontwarn okio.**
-dontwarn okhttp3.**
-dontwarn retrofit2.Platform$Java8
-dontwarn javax.annotation.** # For Warning: retrofit2.CallAdapter$Factory: can't find referenced class javax.annotation.Nullable

-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

-dontwarn com.squareup.picasso.**
-keep class com.squareup.picasso.* { *; }

###---------------End: proguard configuration for Picasso  ----------



###---------------Begin: proguard configuration for Model Classes  ----------

-keep class android.support.** {*;}
-keep interface android.support.** {*;}

-keep class com.talkcharge.weather.common.model.** {*;}
-keep class com.talkcharge.weather.util.** {*;}

-keep class com.facebook.** {*;}

-keepclassmembers class * implements android.os.Parcelable { static ** CREATOR; }
-keepattributes InnerClasses
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

##---------------End : proguard configuration for Model Classes  ----------


###---------------Begin: proguard configuration for Gson  ----------

 # Gson uses generic type information stored in a class file when working with fields. Proguard
 # removes such information by default, so configure it to keep all of it.
-keepattributes Signature, Exception

 # For using GSON @Expose annotation
-keepattributes *Annotation*

 # Gson specific classes
#-keep class sun.misc.Unsafe { *; }
-dontwarn sun.misc.**
 #-keep class com.google.gson.stream.** { *; }

 # Application classes that will be serialized/deserialized over Gson
 -keep class com.google.gson.examples.android.model.** { *; }

 # Prevent proguard from stripping interface information from TypeAdapterFactory,
 # JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

-keepclassmembers class * implements android.os.Parcelable { static ** CREATOR; }
-keepattributes InnerClasses
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

 ##---------------End: proguard configuration for Gson  ----------


-renamesourcefileattribute SourceFile
-keepattributes SourceFile, LineNumberTable
-keepattributes LocalVariableTable, LocalVariableTypeTable
-printmapping build\outputs\mapping\release\mapping.txt