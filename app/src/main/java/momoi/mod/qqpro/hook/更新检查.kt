package momoi.mod.qqpro.hook

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.tencent.qqnt.watch.app.JumpActivity
import com.tencent.qqnt.watch.mainframe.MainActivity
import momoi.anno.mixin.Mixin
import momoi.mod.qqpro.Settings
import momoi.mod.qqpro.Utils
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
        thread {
            var connection: HttpsURLConnection? = null
            try {
                connection = URL("https://pastebin.com/raw/CsFwY6ZC").openConnection() as HttpsURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 10_000
                connection.readTimeout = 10_000
                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    return@thread
                }
                val body = connection.inputStream.use { inputStream ->
                    BufferedReader(InputStreamReader(inputStream)).use { reader ->
                        reader.readText()
                    }
                }
                Utils.log(body)
                val split = body.split("@", limit = 2)
                if (split[0].toInt() > Settings.VERSION_CODE) {
                    runOnUiThread {
                        Toast.makeText(this, split[1], Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }
        }
    }
}