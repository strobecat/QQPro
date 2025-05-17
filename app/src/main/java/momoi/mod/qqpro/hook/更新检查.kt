package momoi.mod.qqpro.hook

import android.os.Bundle
import android.widget.Toast
import com.tencent.qqnt.watch.mainframe.MainActivity
import momoi.anno.mixin.Mixin
import momoi.mod.qqpro.Settings
import momoi.mod.qqpro.api.Http

@Mixin
class 更新检查 : MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Http.get("https://pastebin.com/raw/CsFwY6ZC") {
            val split = it.split("@", limit = 2)
            if (split[0].toInt() > Settings.VERSION_CODE) {
                runOnUiThread {
                    Toast.makeText(this, split[1], Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}