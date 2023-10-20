/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package apktool.kotlin.app


import apktool.kotlin.lib.Apktool
import java.io.File

fun main(args: Array<String>) {


    Apktool(
            apkFile = args[0],
            decodeResource = false,
            cleanDecompilationFolder = true
    ).use {
        val decompiledFiles: Array<File> = it.decompilationFolder!!.toFile().listFiles()!!

        for (f: File in decompiledFiles) {
            println("${f.absolutePath}")
        }

        it.export("Recompiled.apk", signApk = true)
    }


}
