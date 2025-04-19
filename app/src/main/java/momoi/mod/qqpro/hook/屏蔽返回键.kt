package momoi.mod.qqpro.hook

import com.tencent.qqnt.watch.mainframe.MainActivity
import momoi.anno.mixin.Mixin
import momoi.mod.qqpro.Settings
import momoi.mod.qqpro.Utils

@Mixin
class 屏蔽返回键 : MainActivity() {

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java", replaceWith = ReplaceWith("放过我吧waring大爹"))
    override fun onBackPressed() {
        Utils.log("e")
        if (!Settings.blockBack.value) {
            Utils.log("ee")
            super.onBackPressed()
        }
    }

}