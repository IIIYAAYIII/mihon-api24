package eu.kanade.tachiyomi.util.system

import android.content.Context
import android.os.Build
import logcat.LogPriority
import tachiyomi.core.common.util.system.logcat
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest
import java.util.zip.Adler32
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

object DexPatcher {

    /**
     * Clears cached patched APKs and odex files if the host app has been updated.
     */
    fun cleanIfAppUpdated(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) return
        try {
            val prefs = context.getSharedPreferences("dex_patcher_state", Context.MODE_PRIVATE)
            @Suppress("DEPRECATION")
            val currentVersionCode = try {
                context.packageManager.getPackageInfo(context.packageName, 0).versionCode
            } catch (_: Exception) {
                0
            }
            val lastVersionCode = prefs.getInt("last_version_code", -1)
            if (lastVersionCode != currentVersionCode) {
                logcat(LogPriority.INFO) { "App updated (code $lastVersionCode -> $currentVersionCode), clearing extension DEX caches" }
                File(context.codeCacheDir, PATCHED_DIR_NAME).deleteRecursively()
                File(context.codeCacheDir, "ext_opt").deleteRecursively()
                prefs.edit().putInt("last_version_code", currentVersionCode).apply()
            }
        } catch (e: Exception) {
            logcat(LogPriority.WARN, e) { "Failed to clean DEX patcher cache on app update" }
        }
    }

    /**
     * Checks if the given APK file requires DEX version downgrading (e.g. from 038/039 to 035)
     * so that Android 7.0/7.1 (API 24/25) ART runtime can load it without throwing:
     * "ClassNotFoundException: Didn't find class ... on path: DexPathList[...]"
     */
    fun getPatchedApk(context: Context, sourceApk: File): File {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return sourceApk
        }
        if (!sourceApk.exists()) {
            return sourceApk
        }

        try {
            val patchDir = File(context.codeCacheDir, PATCHED_DIR_NAME).apply { mkdirs() }
            val patchedApk = File(patchDir, "${sourceApk.nameWithoutExtension}_api24.apk")

            val baseApk = File(context.applicationInfo.sourceDir)
            val baseModified = if (baseApk.exists()) baseApk.lastModified() else 0L

            // If already patched and up to date relative to both source and base APK, reuse it
            if (patchedApk.exists() &&
                patchedApk.lastModified() >= sourceApk.lastModified() &&
                patchedApk.lastModified() >= baseModified
            ) {
                return patchedApk
            }

            // Quick check if any dex in the APK actually has an unsupported version
            if (!hasUnsupportedDexVersion(sourceApk)) {
                return sourceApk
            }

            logcat(LogPriority.INFO) { "Patching DEX version for Android 7.x compatibility: ${sourceApk.name}" }
            val tempFile = File.createTempFile("dex_patch_", ".tmp", patchDir)
            try {
                patchApk(sourceApk, tempFile)
                if (tempFile.exists() && tempFile.length() > 0) {
                    if (patchedApk.exists()) {
                        patchedApk.delete()
                    }
                    if (tempFile.renameTo(patchedApk)) {
                        patchedApk.setLastModified(sourceApk.lastModified())
                        return patchedApk
                    }
                }
            } catch (e: Exception) {
                logcat(LogPriority.ERROR, e) { "Failed to patch DEX for ${sourceApk.name}" }
                tempFile.delete()
            }
        } catch (e: Exception) {
            logcat(LogPriority.ERROR, e) { "Error in DexPatcher for ${sourceApk.name}" }
        }

        return sourceApk
    }

    fun deletePatchedApk(context: Context, pkgName: String) {
        try {
            val patchDir = File(context.codeCacheDir, PATCHED_DIR_NAME)
            if (patchDir.exists() && patchDir.isDirectory) {
                patchDir.listFiles { file -> file.name.startsWith(pkgName) }?.forEach {
                    it.delete()
                }
            }
        } catch (_: Exception) {}
    }

    private fun hasUnsupportedDexVersion(sourceApk: File): Boolean {
        ZipFile(sourceApk).use { zip ->
            val entries = zip.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                if (entry.name.endsWith(".dex")) {
                    zip.getInputStream(entry).use { input ->
                        val header = ByteArray(8)
                        val read = input.read(header)
                        if (read >= 8 && isDexMagic(header)) {
                            val v0 = header[4].toInt().toChar()
                            val v1 = header[5].toInt().toChar()
                            val v2 = header[6].toInt().toChar()
                            val verStr = "$v0$v1$v2"
                            if (verStr == "038" || verStr == "039") {
                                return true
                            }
                        }
                    }
                }
            }
        }
        return false
    }

    private fun patchApk(sourceApk: File, outputFile: File) {
        ZipFile(sourceApk).use { srcZip ->
            ZipOutputStream(BufferedOutputStream(FileOutputStream(outputFile))).use { outZip ->
                val entries = srcZip.entries()
                val buffer = ByteArray(16384)

                while (entries.hasMoreElements()) {
                    val entry = entries.nextElement()
                    val newEntry = ZipEntry(entry.name)
                    newEntry.method = ZipEntry.DEFLATED
                    newEntry.time = entry.time
                    outZip.putNextEntry(newEntry)

                    if (entry.name.endsWith(".dex")) {
                        val dexBytes = srcZip.getInputStream(entry).use { it.readBytes() }
                        val patchedBytes = patchDexHeader(dexBytes)
                        outZip.write(patchedBytes)
                    } else {
                        srcZip.getInputStream(entry).use { input ->
                            var len: Int
                            while (input.read(buffer).also { len = it } > 0) {
                                outZip.write(buffer, 0, len)
                            }
                        }
                    }
                    outZip.closeEntry()
                }
            }
        }
    }

    private fun isDexMagic(header: ByteArray): Boolean {
        return header.size >= 4 &&
            header[0] == 'd'.code.toByte() &&
            header[1] == 'e'.code.toByte() &&
            header[2] == 'x'.code.toByte() &&
            header[3] == '\n'.code.toByte()
    }

    private fun patchDexHeader(bytes: ByteArray): ByteArray {
        if (bytes.size < 32 || !isDexMagic(bytes)) {
            return bytes
        }
        val v0 = bytes[4].toInt().toChar()
        val v1 = bytes[5].toInt().toChar()
        val v2 = bytes[6].toInt().toChar()
        val verStr = "$v0$v1$v2"

        if (verStr == "038" || verStr == "039") {
            // Downgrade to 035
            bytes[4] = '0'.code.toByte()
            bytes[5] = '3'.code.toByte()
            bytes[6] = '5'.code.toByte()
            bytes[7] = 0

            // Recompute SHA-1 signature (bytes 32 to end) -> write at bytes 12..31
            val md = MessageDigest.getInstance("SHA-1")
            md.update(bytes, 32, bytes.size - 32)
            val sha1 = md.digest()
            System.arraycopy(sha1, 0, bytes, 12, 20)

            // Recompute Adler32 checksum (bytes 12 to end) -> write at bytes 8..11 (little-endian)
            val adler = Adler32()
            adler.update(bytes, 12, bytes.size - 12)
            val checksum = adler.value.toInt()
            bytes[8] = (checksum and 0xFF).toByte()
            bytes[9] = ((checksum ushr 8) and 0xFF).toByte()
            bytes[10] = ((checksum ushr 16) and 0xFF).toByte()
            bytes[11] = ((checksum ushr 24) and 0xFF).toByte()
        }

        return bytes
    }
}
