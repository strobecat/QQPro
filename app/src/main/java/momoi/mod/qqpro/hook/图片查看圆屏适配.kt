package momoi.mod.qqpro.hook

import android.content.Context
import android.util.AttributeSet
import com.tencent.richframework.widget.matrix.RFWMatrixImageView
import momoi.anno.mixin.Mixin
import momoi.mod.qqpro.util.Utils
import kotlin.math.sqrt

private val size = ((Utils.heightPixels - Utils.heightPixels / sqrt(2f)) / 2).toInt()

@Mixin
class 图片查看圆屏适配(context: Context?, attributeSet: AttributeSet?) : RFWMatrixImageView(
    context,
    attributeSet
) {
    val longImgScale get() = actualHeight.toFloat() / displayRect.height() / (actualWidth.toFloat() / displayRect.width())
    override fun setMaximumScale(f: Float) {
        super.setMaximumScale(longImgScale.coerceAtLeast(f * 1.5f) * 1.1f)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (Utils.isRoundScreen) {
            setPadding(size, size, size, size)
        }
    }
}