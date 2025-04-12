package momoi.mod.qqpro.hook

import android.content.Context
import com.tencent.richframework.widget.matrix.RFWMatrixImageView
import momoi.anno.mixin.Mixin
import momoi.mod.qqpro.Utils
import kotlin.math.sqrt

private val size = ((Utils.heightPixels - Utils.heightPixels / sqrt(2f)) / 2).toInt()
@Mixin
class 图片查看圆屏适配(context: Context?) : RFWMatrixImageView(context) {
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (Utils.isRoundScreen) {
            setPadding(size, size, size, size)
        }
    }
}