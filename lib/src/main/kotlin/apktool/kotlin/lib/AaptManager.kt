package apktool.kotlin.lib

import java.io.File

class AaptManager {
    companion object {
        fun getAapt2(): File {
            return brut.util.AaptManager.getAapt2()
        }
    }
}