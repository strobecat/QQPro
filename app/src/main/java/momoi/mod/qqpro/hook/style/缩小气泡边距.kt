package momoi.mod.qqpro.hook.style

import android.content.Context
import com.tencent.watch.aio_impl.ui.widget.BubbleLayoutCompatPress
import momoi.anno.mixin.Mixin

@Mixin
class 缩小气泡边距(context: Context) : BubbleLayoutCompatPress(context) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredHeight - 12)
    }
}