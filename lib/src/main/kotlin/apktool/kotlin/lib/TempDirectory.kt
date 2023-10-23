package apktool.kotlin.lib

import java.nio.file.Path
import kotlin.io.path.createTempDirectory

/**
 * class to create Temporary directory that will clean it self
 * */
class TempDirectory(val prefix: String) : AutoCloseable {
    val path: Path

    init {
        path = createTempDirectory(prefix = prefix)
    }

    override fun close() {
        path.toFile().deleteRecursively()
    }
}