package apktool.kotlin.lib

import java.io.File
import java.lang.IllegalStateException
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

    // need to throw exception when decodeResource is false
    fun injectPermissionName(permissionName: String) {

        // need to decode resource in order to have readable text of the manifest
        if (!this.decodeResource) {
            throw IllegalStateException("decodeResource must be true, when injecting permission to AndroidManifest.xml")
        }

        val currentContent: MutableList<String> = this.manifestFile.readLines().toMutableList()

        // find injection point
        for (i in currentContent.indices) {

            if (currentContent[i].contains("<manifest xmlns:android")) {
                // insert the permission after  <manifest xmlns:android ...
                currentContent.add(i + 1, "<uses-permission android:name=\"${permissionName}\"/>")
            }

        }

        // rewrite the content back
        // not so efficient but will do for now
        this.manifestFile.printWriter().use { out ->
            out.print(currentContent.joinToString(separator = "\n"))
        }

    }

    override fun close() {

        // clean up
        if (cleanDecompilationFolder)
            decompilationFolder!!.deleteRecursively()
    }

}