import android.net.Uri
import android.widget.ImageView
import com.tencent.qqnt.kernel.nativeinterface.PicElement
import momoi.mod.qqpro.util.Utils
import momoi.mod.qqpro.child
import momoi.mod.qqpro.lib.bitmapDecodeFile
import momoi.mod.qqpro.msg.getImageUrl
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread
import androidx.core.net.toUri

// 抽取的加载图片 URL 的函数
fun ImageView.loadPicUrl(url: String?, cacheFileName: String = "${System.currentTimeMillis() / 1000}${url.hashCode()}") = apply {
    if (url.isNullOrEmpty()) {
        return@apply
    }
    require(maxHeight != 0)
    val cacheFile = context.externalCacheDir!!.child("$cacheFileName.jpg")
    cacheFile.parentFile?.mkdirs()
    if (cacheFile.exists()) {
        Utils.log("Load Image from disk ${cacheFile.path}")
        bitmapDecodeFile(cacheFile)
    } else {
        download(url, cacheFile) { succeed ->
            if (!succeed) {
                val error = context.externalCacheDir!!.child("error.jpg")
                if (error.exists()) {
                    bitmapDecodeFile(error)
                } else {
                    download("https://i0.hdslb.com/bfs/new_dyn/e8907352f1c8be0ea696c1447723f6091769278028.png", error) {
                        if (it) {
                            bitmapDecodeFile(error)
                        }
                    }
                }
            }
        }
    }
}

fun ImageView.loadPicElement(pic: PicElement) = apply {
    // 调用抽取的函数
    loadPicUrl(pic.getImageUrl(), pic.md5HexStr)
}

inline fun download(rawUrl: String, file: File, crossinline callback: (Boolean) -> Unit) {
    thread {
        var connection: HttpURLConnection? = null
        val uri = rawUrl.toUri()
        val url = if (uri.scheme.isNullOrEmpty()) {
            URL("https://$rawUrl")
        } else URL(rawUrl.replace("http://", "https://"))
        try {
            connection = url.openConnection() as HttpURLConnection
            connection.instanceFollowRedirects = true
            connection.connectTimeout = 60_000 // 60秒超时
            connection.readTimeout = 10_000
            connection.requestMethod = "GET"
            connection.doInput = true
            Utils.log("Download Image From: $url")
            connection.connect()
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                if (!file.exists()) {
                    file.createNewFile()
                }
                connection.inputStream.use { input ->
                    file.outputStream().use { out ->
                        input.copyTo(out)
                    }
                }
                callback(true)
            } else {
                callback(false)
                Utils.log("Download Image Failed! ${connection.url}")
            }
        } catch (e: Exception) {
            callback(false)
            e.printStackTrace()
        } finally {
            connection?.disconnect()
        }
    }
}