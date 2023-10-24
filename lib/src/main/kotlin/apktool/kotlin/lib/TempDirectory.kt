package apktool.kotlin.lib

import java.io.File
import kotlin.io.path.createTempDirectory

/**
 * class to create Temporary directory that will clean it self
 * */
class TempDirectory(prefix: String? = null) : AutoCloseable {
    val directory: File

    init {
        directory = createTempDirectory(prefix = prefix).toFile()
    }

    override fun close() {
        directory.deleteRecursively()
    }
}