import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.widget.ImageView
import com.tencent.qqnt.kernel.nativeinterface.PicElement
import momoi.mod.qqpro.Utils
import momoi.mod.qqpro.child
import momoi.mod.qqpro.msg.getImageUrl
import java.io.BufferedOutputStream
import java.io.File
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

//TODO 优化逻辑
fun ImageView.loadPicElement(pic: PicElement) {
    require(maxHeight != 0)
    val cacheFile = context.externalCacheDir!!.child("${pic.md5HexStr}.jpg")
    cacheFile.parentFile?.mkdirs()
    if (cacheFile.exists()) {
        Utils.log("Load Image from disk ${cacheFile.path}")
        setImageBitmap(BitmapFactory.decodeFile(cacheFile.absolutePath))
    } else {
        download(pic.getImageUrl()) { stream ->
            if (stream == null) {
                val error = context.externalCacheDir!!.child("error.jpg")
                if (error.exists()) {
                    bitmapDecodeFile(error)
                } else {
                    download("https://i0.hdslb.com/bfs/new_dyn/e8907352f1c8be0ea696c1447723f6091769278028.png") { errorStream ->
                        val bitmap = bitmapDecodeStream(errorStream!!)
                        BufferedOutputStream(error.outputStream()).use {
                            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
                        }
                    }
                }
            } else {
                val bitmap = bitmapDecodeStream(stream)
                BufferedOutputStream(cacheFile.outputStream()).use {
                    bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, it)
                }
            }
        }
    }
}

inline fun download(url: String, crossinline callback: (InputStream?) -> Unit) {
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
                callback(connection.inputStream)
            } else {
                callback(null)
                Utils.log("Download Image Failed! $url")
            }
        } catch (e: Exception) {
            callback(null)
            e.printStackTrace()
        } finally {
            connection?.disconnect()
        }
    }
}

fun ImageView.bitmapDecodeFile(file: File): Bitmap? =
    file.inputStream().use {
        bitmapDecodeStream(it)
    }
fun ImageView.bitmapDecodeStream(inputStream: InputStream): Bitmap? {
    val rect = Rect()
    BitmapFactory.decodeStream(inputStream, rect, BitmapFactory.Options().apply {
        inJustDecodeBounds = true
    })
    val bitmap = BitmapFactory.decodeStream(inputStream, null, BitmapFactory.Options().apply {
        if (rect.height() > maxHeight) {
            outHeight = maxHeight
            outWidth = (rect.width().toFloat() / rect.height().toFloat() * maxHeight.toFloat()).toInt()
            inSampleSize = rect.height() / maxHeight
        }
    })
    post {
        setImageBitmap(bitmap)
    }
    return bitmap
}