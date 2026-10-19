package mihon.data.dalvik

import android.os.Build
import dalvik.system.BaseDexClassLoader
import dalvik.system.DelegateLastClassLoader
import java.io.File
import java.net.URL
import java.util.Collections
import java.util.Enumeration

@Suppress("FunctionName")
fun DelegateLastClassLoaderCompat(
    dexPath: String,
    librarySearchPath: String?,
    parent: ClassLoader,
    optimizedDirectory: String? = null,
): ClassLoader {
    // ART recognizes the built-in loader and maps app images compiled for this app's class loader chain, and before
    // Android 17 a damaged one crashes every launch until the app is updated. ART doesn't recognize the backport.
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CINNAMON_BUN) {
        DelegateLastClassLoader(dexPath, librarySearchPath, parent)
    } else {
        DelegateLastPathClassLoader(dexPath, optimizedDirectory, librarySearchPath, parent)
    }
}

/**
 * A [BaseDexClassLoader] that looks up classes like [DelegateLastClassLoader]: the boot class loader first, then its own
 * dex path, then the parent.
 */
private class DelegateLastPathClassLoader(
    dexPath: String,
    optimizedDirectory: String?,
    librarySearchPath: String?,
    parent: ClassLoader,
) : BaseDexClassLoader(dexPath, optimizedDirectory?.let(::File), librarySearchPath, parent) {

    private val bootClassLoader: ClassLoader? = Any::class.java.classLoader

    override fun loadClass(name: String?, resolve: Boolean): Class<*> {
        findLoadedClass(name)?.let { return it }

        if (bootClassLoader != null) {
            try {
                return bootClassLoader.loadClass(name)
            } catch (_: ClassNotFoundException) {}
        }

        val fromSuper = try {
            return findClass(name)
        } catch (e: ClassNotFoundException) {
            e
        }

        return try {
            parent.loadClass(name)
        } catch (_: ClassNotFoundException) {
            throw fromSuper
        }
    }

    override fun getResource(name: String?): URL? {
        return bootClassLoader?.getResource(name)
            ?: findResource(name)
            ?: parent?.getResource(name)
    }

    override fun getResources(name: String?): Enumeration<URL> {
        val resources = buildList {
            bootClassLoader?.getResources(name)?.let { addAll(it.toList()) }
            findResources(name)?.let { addAll(it.toList()) }
            parent?.getResources(name)?.let { addAll(it.toList()) }
        }
        return Collections.enumeration(resources)
    }
}
