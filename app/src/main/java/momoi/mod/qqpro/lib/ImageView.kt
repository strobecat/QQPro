package momoi.mod.qqpro.lib

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.widget.ImageView
import momoi.mod.qqpro.util.Utils
import java.io.File
import java.io.InputStream

fun <T : ImageView> T.imageResource(resId: Int) = apply {
    setImageResource(resId)
}
fun <T : ImageView> T.scaleType(scaleType: ImageView.ScaleType) = apply {
    setScaleType(scaleType)
}
fun <T : ImageView> T.adjustViewBounds(adjust: Boolean = true) = apply {
    adjustViewBounds = adjust
}

fun ImageView.bitmapDecodeFile(file: File) {
    var s: InputStream? = null
    bitmapDecodeStream {
        s?.close()
        s = file.inputStream()
        s!!
    }
    s?.close()
}

fun ImageView.bitmapDecodeAssets(path: String) =
    Utils.application.assets.open(path).use {
        bitmapDecodeStream { reread ->
            if (reread) {
                it.reset()
                it
            } else {
                it.mark(0)
                it
            }
        }
        this
    }

inline fun ImageView.bitmapDecodeStream(streamProvider: (reread: Boolean)->InputStream): Bitmap? {
    val rect = Rect()
    BitmapFactory.decodeStream(streamProvider(false), rect, BitmapFactory.Options().apply {
        inJustDecodeBounds = true
    })
    val bitmap = BitmapFactory.decodeStream(streamProvider(true), null, BitmapFactory.Options().apply {
        if (rect.height() > 300 && rect.height() > maxHeight) {
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