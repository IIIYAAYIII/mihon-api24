# Preserve all desugared library classes, methods, and fields so dynamically loaded
# Keiyoushi extensions running on Android < 8.0 (API < 26) can resolve j$.** symbols at runtime.
-keep class j$.** { *; }
-keep interface j$.** { *; }
-dontobfuscate
