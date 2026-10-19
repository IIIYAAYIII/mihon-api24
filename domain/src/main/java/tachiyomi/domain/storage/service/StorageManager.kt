package tachiyomi.domain.storage.service

import android.content.Context
import android.os.Build
import androidx.core.net.toUri
import com.hippo.unifile.UniFile
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import eu.kanade.tachiyomi.util.storage.DiskUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import mihon.core.metro.AppCoroutineScope
import java.io.File
import java.util.concurrent.ConcurrentHashMap

@Inject
@SingleIn(AppScope::class)
class StorageManager(
    @AppCoroutineScope private val scope: CoroutineScope,
    private val context: Context,
    storagePreferences: StoragePreferences,
) {

    @Volatile
    private var baseDirLazy: Lazy<UniFile?> = lazy { getBaseDir(storagePreferences.baseStorageDirectory.get()) }
    private val baseDir: UniFile?
        get() = baseDirLazy.value

    // On SAF, createDirectory lists the whole base directory to find an existing child, so a resolved child
    // is reused while it's under the current base directory and still exists; it can be deleted outside the app
    private val childDirectories = ConcurrentHashMap<String, Pair<UniFile, UniFile>>()

    private val _changes: Channel<Unit> = Channel(Channel.UNLIMITED)
    val changes = _changes.receiveAsFlow()
        .shareIn(scope, SharingStarted.Lazily, 1)

    init {
        storagePreferences.baseStorageDirectory.changes()
            .drop(1)
            .distinctUntilChanged()
            .onEach { uri ->
                baseDirLazy = lazyOf(getBaseDir(uri))
                baseDir?.let { parent ->
                    parent.createDirectory(AUTOMATIC_BACKUPS_PATH)
                    parent.createDirectory(LOCAL_SOURCE_PATH)
                    parent.createDirectory(DOWNLOADS_PATH).also {
                        DiskUtil.createNoMediaFile(it, context)
                    }
                }
                _changes.send(Unit)
            }
            .launchIn(scope)
    }

    private fun getBaseDir(uri: String): UniFile? {
        val file = UniFile.fromUri(context, uri.toUri())
        if (file?.exists() == true) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                file.filePath?.let { rawPath ->
                    val rawFile = File(rawPath)
                    if (!rawFile.exists()) {
                        rawFile.mkdirs()
                    }
                    if (rawFile.exists()) {
                        UniFile.fromFile(rawFile)?.let { return it }
                    }
                }
            }
            return file
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            file?.filePath?.let { rawPath ->
                val rawFile = File(rawPath)
                if (rawFile.mkdirs() || rawFile.exists()) {
                    UniFile.fromFile(rawFile)?.let { return it }
                }
            }
        }
        return file?.takeIf { it.exists() }
    }

    fun getAutomaticBackupsDirectory(): UniFile? {
        return getChildDirectory(AUTOMATIC_BACKUPS_PATH)
    }

    fun getDownloadsDirectory(): UniFile? {
        return getChildDirectory(DOWNLOADS_PATH)
    }

    fun getLocalSourceDirectory(): UniFile? {
        return getChildDirectory(LOCAL_SOURCE_PATH)
    }

    private fun getChildDirectory(name: String): UniFile? {
        val parent = baseDir ?: return null
        childDirectories[name]
            ?.takeIf { (cachedParent, directory) -> cachedParent === parent && directory.exists() }
            ?.let { (_, directory) -> return directory }
        var dir = parent.createDirectory(name)
        if (dir == null && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            parent.filePath?.let { parentPath ->
                val childFile = File(parentPath, name)
                if (childFile.exists() || childFile.mkdirs()) {
                    dir = UniFile.fromFile(childFile)
                }
            }
        }
        return dir?.also { childDirectories[name] = parent to it }
    }
}

private const val AUTOMATIC_BACKUPS_PATH = "autobackup"
private const val DOWNLOADS_PATH = "downloads"
private const val LOCAL_SOURCE_PATH = "local"
