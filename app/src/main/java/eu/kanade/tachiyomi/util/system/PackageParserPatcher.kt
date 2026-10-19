package eu.kanade.tachiyomi.util.system

import android.os.Build
import logcat.LogPriority
import tachiyomi.core.common.util.system.logcat
import java.lang.reflect.Field

object PackageParserPatcher {

    @Volatile
    private var patched = false

    @Synchronized
    fun patch() {
        if (patched) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            patched = true
            return
        }

        try {
            val ppClass = Class.forName("android.content.pm.PackageParser")
            val sdkField = ppClass.getDeclaredField("SDK_VERSION")
            sdkField.isAccessible = true

            var success = false
            try {
                sdkField.setInt(null, 10000)
                success = true
            } catch (_: Throwable) {
                // Ignore and fallback to Unsafe
            }

            if (!success) {
                val unsafeClass = Class.forName("sun.misc.Unsafe")
                val theUnsafeField = unsafeClass.getDeclaredField("theUnsafe")
                theUnsafeField.isAccessible = true
                val unsafe = theUnsafeField.get(null)

                val staticFieldBaseMethod = unsafeClass.getMethod("staticFieldBase", Field::class.java)
                val staticFieldOffsetMethod = unsafeClass.getMethod("staticFieldOffset", Field::class.java)
                val putIntMethod = unsafeClass.getMethod(
                    "putInt",
                    Any::class.java,
                    Long::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType,
                )

                val base = staticFieldBaseMethod.invoke(unsafe, sdkField)
                val offset = staticFieldOffsetMethod.invoke(unsafe, sdkField) as Long
                putIntMethod.invoke(unsafe, base, offset, 10000)
                success = true
            }

            if (success) {
                logcat(LogPriority.INFO) { "Successfully patched PackageParser.SDK_VERSION to 10000" }
                patched = true
            }
        } catch (e: Throwable) {
            logcat(LogPriority.WARN, e) { "Failed to patch PackageParser.SDK_VERSION" }
        }
    }
}
