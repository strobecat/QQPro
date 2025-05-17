package momoi.mod.qqpro.hook.style

import android.content.Context
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.tencent.watch.aio_impl.data.WatchAIOMsgItem
import com.tencent.watch.aio_impl.ui.cell.base.BaseWatchItemCell
import com.tencent.watch.aio_impl.ui.widget.AIOCellGroupWidget
import momoi.anno.mixin.Mixin
import momoi.mod.qqpro.Utils
import momoi.mod.qqpro.hook.Mikoto10031
import momoi.mod.qqpro.hook.action.AIOMsgEx

var distanceCache = 0
val nickHeightField = AIOCellGroupWidget::class.java.getDeclaredField("g").apply {
    isAccessible = true
}

class 合并相同发送者消息 : BaseWatchItemCell() {
    override fun i(
        view: View?,
        item: WatchAIOMsgItem,
        p3: Int,
        p4: MutableList<*>?,
        p5: Lifecycle?,
        p6: LifecycleOwner?
    ) {
        val widget = view as? AIOCellGroupWidget ?: return
        val msg = item as AIOMsgEx
        if (msg.previousSame) {
            distanceCache = widget.g
            nickHeightField.set(widget, 0)
            widget.nickWidget?.visibility = View.GONE
        } else {
            nickHeightField.set(widget, distanceCache)
            widget.nickWidget?.visibility = View.VISIBLE
        }
        super.i(view, item, p3, p4, p5, p6)
    }
}