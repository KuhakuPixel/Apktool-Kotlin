/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package apktool.kotlin.app


import apktool.kotlin.lib.Apktool
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.required
import java.io.File

fun main(args: Array<String>) {
    // arg parsing
    val parser = ArgParser("patcher :)")
    val input by parser.option(ArgType.String, shortName = "i", description = "Input file").required()
    val redirectToLuckyPatcher by parser.option(ArgType.Boolean, shortName = "lp", description = "redirect to lucky patcher").default(false)
    parser.parse(args)
    //

    // replace the string
    val originalPackageName = "\"com.android.vending\""
    var newPackageName = ""

    val originalServiceToConnectToName = "\"com.android.vending.billing.InAppBillingService.BIND\""
    var newServiceToConnectToName = ""

    if (redirectToLuckyPatcher) {
        println("redirecting purchases to lucky patcher")
        newPackageName = "\"com.android.vending.billing.InAppBillingService.BINN\""
        newServiceToConnectToName = "\"com.android.vending.billing.InAppBillingService.BINN\""
    } else {
        println("redirecting purchases to own service")
        // redirect to our own
        newPackageName = "\"org.billinghack\""
        newServiceToConnectToName = "\"org.billinghack.BillingService.BIND\""

    }

    // =========================================== begin patch =============================
    Apktool(
            apkFile = input,
            decodeResource = true,
            cleanDecompilationFolder = true
    ).use {
        val decompiledFiles: Array<File> = it.decompilationFolder!!.listFiles()!!

        for (f: File in decompiledFiles) {
            println(f.absolutePath)
        }
        // ============== begin the patch process ================
        val apiFolderPath = File(it.decompilationFolder.toString(), "smali/com/android/billingclient/api/")
        val billingClientFiles: Array<File> = apiFolderPath.listFiles()

        // for folder, find the exact and replace
        for (f in billingClientFiles) {
            // write

            val text: String = f.readText()

            if (text.contains(originalPackageName) && text.contains(originalServiceToConnectToName)) {
                var newText = text.replace(originalPackageName, newPackageName)
                newText = newText.replace(originalServiceToConnectToName, newServiceToConnectToName)

                // replace with normal log
                val logSmaliInstruction = "Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I"
                // println("New text: ${newText}")
                newText = newText.replace("Lcom/google/android/gms/internal/play_billing/zzb;->zzo(Ljava/lang/String;Ljava/lang/String;)V", logSmaliInstruction)
                newText = newText.replace("Lcom/google/android/gms/internal/play_billing/zzb;->zzn(Ljava/lang/String;Ljava/lang/String;)V", logSmaliInstruction)
                //
                println("writing to ${f.absolutePath}")
                f.printWriter().use { out ->
                    out.print(newText)
                }
            }
        }
        println("injecting permission")
        // inject permission for querying other package's services
        it.injectPermissionName("android.permission.QUERY_ALL_PACKAGES")
        //
        it.export("Recompiled.apk", signApk = true)
    }


}
