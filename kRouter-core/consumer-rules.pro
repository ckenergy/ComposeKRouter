-keep class * implements com.ckenergy.compose.plugin.core.INavGraphProvider{*;}
################################### keep ###################################

-keep,allowobfuscation @interface androidx.annotation.Keep
-keep @androidx.annotation.Keep class * {*;}
-keepclassmembers @androidx.annotation.Keep class * {*;}
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}