package momoi.mod.qqpro.hook

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.forEach
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.tencent.qqnt.kernel.nativeinterface.MsgRecord
import com.tencent.watch.aio_impl.data.WatchAIOMsgItem
import com.tencent.watch.aio_impl.ext.MsgListUtilKt
import com.tencent.watch.aio_impl.ui.cell.base.BaseWatchItemCell
import com.tencent.watch.aio_impl.ui.cell.superface.WatchAniStickerGroupWidget
import com.tencent.watch.aio_impl.ui.cell.superface.WatchAniStickerItemCell
import com.tencent.watch.aio_impl.ui.cell.superface.WatchAniStickerMsgItem
import com.tencent.watch.aio_impl.ui.menu.AIOLongClickMenuFragment
import com.tencent.watch.aio_impl.ui.widget.AIOCellGroupWidget
import com.tencent.watch.aio_impl.ui.widget.AIOItemTextView
import com.tencent.watch.aio_impl.ui.widget.BubbleLayoutCompatPress
import com.tencent.watch.aio_impl.ui.widget.RoundBubbleImageView
import me.jessyan.autosize.AutoSizeConfig
import momoi.anno.mixin.Mixin
import momoi.anno.mixin.StaticHook
import momoi.mod.qqpro.Settings
import momoi.mod.qqpro.asGroup
import momoi.mod.qqpro.asGroupOrNull
import momoi.mod.qqpro.forEachAll
import momoi.mod.qqpro.lib.background

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

@Mixin
object 移除超级表情放大动画 : WatchAniStickerItemCell()  {
    @JvmStatic
    @StaticHook
    fun n_(cell: WatchAniStickerItemCell, widget: WatchAniStickerGroupWidget, msg: WatchAniStickerMsgItem) {
        msg.r.c = true
        n(cell, widget, msg)
    }
}

@Mixin
class 长按菜单透明 : AIOLongClickMenuFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)?.apply {
            this.asGroup().getChildAt(0).asGroup().let { group ->
                group.removeViewAt(0)
                group.forEachAll {
                    it.background(0x22_000000)
                }
            }
        }
    }
}

@Mixin
class 缩小文本 : BaseWatchItemCell() {
    override fun i(
        view: View?,
        item: WatchAIOMsgItem?,
        p3: Int,
        p4: MutableList<*>?,
        p5: Lifecycle?,
        p6: LifecycleOwner?
    ) {
        super.i(view, item, p3, p4, p5, p6)
        (view as? AIOCellGroupWidget)?.contentWidget?.let { content ->
            content.asGroupOrNull()?.forEach {
                resize(it)
            } ?: resize(content)
        }
    }
    fun resize(view: View) {
        if (view is TextView && view.currentTextColor == 0xFF_FFFFFF.toInt()) {
            view.textSize = 15f * Settings.chatScale
        }
    }
}

@Mixin
class 缩小气泡边距(context: Context) : BubbleLayoutCompatPress(context) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredHeight - 12)
    }
}