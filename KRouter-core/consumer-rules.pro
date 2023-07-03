-keep class * implements com.ckenergy.compose.krouter.core.INavGraphProvider{*;}
-keep class com.ckenergy.compose.krouter.core.** {*;}
################################### keep ###################################

-keep,allowobfuscation @interface androidx.annotation.Keep
-keep @androidx.annotation.Keep class * {*;}
-keepclassmembers @androidx.annotation.Keep class * {*;}
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}