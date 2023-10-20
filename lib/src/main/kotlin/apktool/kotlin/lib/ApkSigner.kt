package apktool.kotlin.lib

import at.favre.tools.apksigner.SignTool
import java.io.File

class ApkSigner {
    companion object {
        fun sign(apkPath: File) {
            val args = arrayOf("--apks", apkPath.absolutePath, "--allowResign", "--overwrite")
            SignTool.main(args)
        }
    }
}
