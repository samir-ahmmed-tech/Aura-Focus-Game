# Add project specific ProGuard rules here.
# Compose and Coroutines optimization rules
-keepattributes *Annotation*
-keepclassmembers class * {
    @androidx.compose.runtime.Composable *;
}

# Preserve Data Models & State across minification
-keep class com.example.data.** { *; }
-keep class com.example.audio.** { *; }
-keep class com.example.util.** { *; }

# Google Play Services & AdMob
-keep public class com.google.android.gms.ads.** {
   public *;
}
-keep public class com.google.ads.** {
   public *;
}

