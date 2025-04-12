import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import com.tencent.qqnt.kernel.nativeinterface.PicElement
import momoi.mod.qqpro.Utils
import momoi.mod.qqpro.child
import momoi.mod.qqpro.msg.getImageUrl
import java.io.BufferedOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

//TODO 优化逻辑
fun ImageView.loadPicElement(pic: PicElement) {
    val cacheFile = context.externalCacheDir!!.child("${pic.md5HexStr}.jpg")
    cacheFile.parentFile?.mkdirs()
    if (cacheFile.exists()) {
        Utils.log("Load Image from disk ${cacheFile.path}")
        setImageBitmap(BitmapFactory.decodeFile(cacheFile.absolutePath))
    } else {
        downloadImage(pic.getImageUrl()) { bitmap ->
            if (bitmap == null) {
                val error = context.externalCacheDir!!.child("error.jpg")
                if (error.exists()) {
                    post {
                        setImageBitmap(BitmapFactory.decodeFile(error.absolutePath))
                    }
                } else {
                    downloadImage("https://i0.hdslb.com/bfs/new_dyn/e8907352f1c8be0ea696c1447723f6091769278028.png") { errorBitmap ->
                        post {
                            setImageBitmap(errorBitmap)
                        }
                        BufferedOutputStream(error.outputStream()).use {
                            errorBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
                        }
                    }
                }
            } else {
                post {
                    setImageBitmap(bitmap)
                }
                BufferedOutputStream(cacheFile.outputStream()).use {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                }
            }
        }
    }
}

inline fun downloadImage(url: String, crossinline callback: (Bitmap?) -> Unit) {
    thread {
        var connection: HttpURLConnection? = null
        var inputStream: InputStream? = null

        try {
            connection = URL(url).openConnection() as HttpURLConnection
            connection.connectTimeout = 60_000 // 60秒超时
            connection.readTimeout = 10_000
            connection.requestMethod = "GET"
            connection.doInput = true
            Utils.log("Download Image From: $url")
            connection.connect()
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(inputStream)
                callback(bitmap)
            } else {
                callback(null)
                Utils.log("Download Image Failed! $url")
            }
        } catch (e: Exception) {
            callback(null)
            e.printStackTrace()
        } finally {
            inputStream?.close()
            connection?.disconnect()
        }
    }
}