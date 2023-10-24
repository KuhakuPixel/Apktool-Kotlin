package apktool.kotlin.lib

import java.io.File
import kotlin.io.path.createTempDirectory

class Apktool(
        val apkFile: String,
        val decodeResource: Boolean,
        val useAapt2: Boolean = true,
        var decompilationFolder: File? = null,
        val cleanDecompilationFolder: Boolean = true,
) : AutoCloseable {

    val manifestFile: File
    val resourceFolder: File

    // only exist if [decodeResource] == true
    val resourceArscFile: File

    init {

        if (decompilationFolder == null)
            decompilationFolder = createTempDirectory(prefix = "Apktool-Kotlin").toFile()
        // init some predefined path
        manifestFile = File(decompilationFolder.toString(), "AndroidManifest.xml")
        resourceFolder = File(decompilationFolder.toString(), "res")
        resourceArscFile = File(decompilationFolder.toString(), "resources.arsc")
        // ============== do the decompilation ========================
        println("Apk ${apkFile} decompiled at ${decompilationFolder}")
        val cmd = mutableListOf("d", apkFile, "--output", decompilationFolder.toString(), "--force")

        // add --no-resource, because apktool by default decodes resource
        if (!decodeResource)
            cmd.add("--no-res")

        brut.apktool.Main.main(cmd.toTypedArray())

    }

    fun export(apkOutFile: String, signApk: Boolean) {
        val cmd = mutableListOf("b", decompilationFolder.toString(), "--output", apkOutFile)

        if (useAapt2)
            cmd.add("--use-aapt2")

        brut.apktool.Main.main(cmd.toTypedArray())
        // sign
        if (signApk)
            ApkSigner.sign(File(apkOutFile))


    }

    fun export(apkOutFile: File, signApk: Boolean) {
        export(apkOutFile = apkOutFile.absoluteFile.toString(), signApk = signApk)
    }

    override fun close() {

        // clean up
        if (cleanDecompilationFolder)
            decompilationFolder!!.deleteRecursively()
    }

}