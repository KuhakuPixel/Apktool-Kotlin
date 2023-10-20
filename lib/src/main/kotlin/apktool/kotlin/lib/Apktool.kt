package apktool.kotlin.lib

import java.nio.file.Path
import kotlin.io.path.createTempDirectory

class Apktool(val apkFile: String, val decodeResource: Boolean, val useAapt2: Boolean = false, val cleanDecompilationFolder: Boolean = true) : AutoCloseable {

    val decompilationFolder: Path

    init {

        decompilationFolder = createTempDirectory(prefix = apkFile)
        println("Apk ${apkFile} decompiled at ${decompilationFolder}")
        val cmd = mutableListOf("d", apkFile, "--output", decompilationFolder.toString(), "--force")

        // add --no-resource, because apktool by default decodes resource
        if (!decodeResource)
            cmd.add("--no-res")

        brut.apktool.Main.main(cmd.toTypedArray())

    }

    fun export(apkOutFile: String) {
        val cmd = mutableListOf("b", decompilationFolder.toString(), "--output", apkOutFile)

        if (useAapt2)
            cmd.add("--use-aapt2")

        brut.apktool.Main.main(cmd.toTypedArray())

    }

    override fun close() {

        // clean up
        if (cleanDecompilationFolder)
            decompilationFolder.toFile().deleteRecursively()
    }

}