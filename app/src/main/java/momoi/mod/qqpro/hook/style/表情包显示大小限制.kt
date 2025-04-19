package momoi.mod.qqpro.hook.style

import android.content.Context
import android.content.res.Resources
import com.tencent.watch.aio_impl.ui.widget.RoundBubbleImageView
import me.jessyan.autosize.AutoSizeConfig
import momoi.anno.mixin.Mixin

val heightLimit = Resources.getSystem().displayMetrics.heightPixels * 0.5f

@Mixin
class 表情包显示大小限制(context: Context?) : RoundBubbleImageView(context) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        maxHeight = heightLimit.toInt()
        maxWidth = drawable?.bounds?.let {
            (heightLimit / it.height() * it.width()).toInt()
        } ?: Int.MAX_VALUE
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (height > maxHeight) {
            super.onMeasure(
                MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST)
            )
        }
    }
}