package momoi.mod.qqpro.hook

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.tencent.qqnt.watch.app.JumpActivity
import com.tencent.qqnt.watch.mainframe.MainActivity
import momoi.anno.mixin.Mixin
import momoi.mod.qqpro.Settings
import momoi.mod.qqpro.Utils
import momoi.mod.qqpro.api.Http
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlin.concurrent.thread

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