package momoi.mod.qqpro.hook

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.widget.ImageView
import androidx.core.view.isInvisible
import com.tencent.qqnt.kernel.nativeinterface.MsgRecord
import com.tencent.watch.aio_impl.data.WatchAIOMsgItem
import com.tencent.watch.aio_impl.ext.MsgListUtilKt
import com.tencent.watch.aio_impl.ui.widget.RoundBubbleImageView
import me.jessyan.autosize.AutoSizeConfig
import momoi.anno.mixin.Mixin
import momoi.anno.mixin.StaticHook
import momoi.mod.qqpro.Settings
import momoi.mod.qqpro.lib.WRAP

@Mixin
class 缩放 : AutoSizeConfig() {
    override fun getDesignHeightInDp(): Int {
        return (super.getDesignHeightInDp() / Settings.scale).toInt()
    }

    override fun getDesignWidthInDp(): Int {
        return (super.getDesignWidthInDp() / Settings.scale).toInt()
    }
}

private val heightLimit = Resources.getSystem().displayMetrics.heightPixels * 0.5f

//TODO 这里还不是很完善
class MyImageView(context: Context) : ImageView(context) {
    init {
        adjustViewBounds = true
        scaleType = ScaleType.FIT_CENTER
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        maxHeight = (heightLimit * 1.5f).toInt()
        maxWidth = drawable?.bounds?.let {
            (heightLimit / it.height() * it.width()).toInt()
        } ?: Int.MAX_VALUE
        if (maxWidth < maxHeight) maxWidth = maxHeight
        super.onMeasure(
            MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST)
        )
    }
}

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

@Mixin
object 修复回复带图显示 : MsgListUtilKt() {
    @StaticHook
    @JvmStatic
    fun c_(msg: MsgRecord): WatchAIOMsgItem {
        val reply = msg.elements.firstOrNull { it.replyElement != null } ?: return c(msg)
        msg.elements.remove(reply)
        val rawType = msg.msgType
        msg.msgType = 2
        val result = c(msg)
        msg.elements.add(0, reply)
        msg.msgType = rawType
        return result
    }
}