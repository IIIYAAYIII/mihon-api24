# Types extensions implement, extend or construct (including reflected members such as headers$delegate)
-keep class eu.kanade.tachiyomi.source.** { *; }
-keepclassmembers class eu.kanade.tachiyomi.source.** { *; }

# Final classes and top-level functions extensions only call into
-keep class eu.kanade.tachiyomi.AppInfo { *; }
-keep class eu.kanade.tachiyomi.network.** { *; }
-keepclassmembers class eu.kanade.tachiyomi.network.** { *; }
-keep class eu.kanade.tachiyomi.util.JsoupExtensionsKt { *; }
-keep class j$.** { *; }
-keep interface j$.** { *; }

