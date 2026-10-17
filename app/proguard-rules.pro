# ProGuard / R8 Keep Rules — Distinct Routes

# Moshi JSON Adapters & Models
-keepclassmembers class * {
    @com.squareup.moshi.Json *;
}
-keep class com.distinctroutes.app.data.model.** { *; }
-keep class com.distinctroutes.app.domain.model.** { *; }

# Retrofit
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers enum * { *; }

# osmdroid
-keep class org.osmdroid.** { *; }
-dontwarn org.osmdroid.**

# Hilt & Dagger
-keep class dagger.hilt.** { *; }
-keepclassmembers class * {
    @javax.inject.Inject *;
}