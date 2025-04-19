package momoi.mod.qqpro.hook.style

import android.view.View
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.tencent.watch.aio_impl.data.WatchAIOMsgItem
import com.tencent.watch.aio_impl.ui.cell.base.BaseWatchItemCell
import com.tencent.watch.aio_impl.ui.widget.AIOCellGroupWidget
import momoi.anno.mixin.Mixin
import momoi.mod.qqpro.Settings
import momoi.mod.qqpro.asGroupOrNull

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
            view.textSize = 15f * Settings.chatScale.value
        }
    }
}