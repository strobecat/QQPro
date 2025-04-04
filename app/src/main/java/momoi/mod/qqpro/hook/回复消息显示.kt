package momoi.mod.qqpro.hook

import android.content.Context
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.tencent.qqnt.kernel.nativeinterface.ReplyElement
import com.tencent.watch.aio_impl.data.WatchAIOMsgItem
import com.tencent.watch.aio_impl.ui.cell.base.BaseWatchItemCell
import com.tencent.watch.aio_impl.ui.widget.AIOCellGroupWidget
import momoi.anno.mixin.Mixin
import momoi.mod.qqpro.Colors
import momoi.mod.qqpro.MsgUtil
import momoi.mod.qqpro.Settings
import momoi.mod.qqpro.Utils
import momoi.mod.qqpro.asGroup
import momoi.mod.qqpro.forEachAll
import momoi.mod.qqpro.join
import momoi.mod.qqpro.lib.FILL
import momoi.mod.qqpro.lib.MutableState
import momoi.mod.qqpro.lib.ReplyView
import momoi.mod.qqpro.lib.WRAP
import momoi.mod.qqpro.lib.background
import momoi.mod.qqpro.lib.content
import momoi.mod.qqpro.lib.create
import momoi.mod.qqpro.lib.dp
import momoi.mod.qqpro.lib.layoutParams
import momoi.mod.qqpro.lib.marginHorizontal
import momoi.mod.qqpro.lib.paddingHorizontal
import momoi.mod.qqpro.lib.text
import momoi.mod.qqpro.lib.textColor
import momoi.mod.qqpro.lib.textSize
import momoi.mod.qqpro.lib.vertical
import momoi.mod.qqpro.warp

@Mixin
class Mikoto10031(context: Context) : AIOCellGroupWidget(context) {
    var replyView: ReplyView? = null

    fun initReplyView() {
        if (replyView == null) {
            replyView = create(context)
            val warp = contentWidget.warp()
            warp.addView(replyView, 0)
        }
    }
}

@Mixin
class Mikoto10032 : BaseWatchItemCell() {

    override fun reply_item(view: View?, watchAIOMsgItem: WatchAIOMsgItem) {

    }

    override fun i(
        view: View?,
        item: WatchAIOMsgItem,
        p3: Int,
        p4: MutableList<*>?,
        p5: Lifecycle?,
        p6: LifecycleOwner?
    ) {
        super.i(view, item, p3, p4, p5, p6)
        val widget = view as? Mikoto10031 ?: return
        (item.d.elements.find { it.replyElement != null }?.replyElement)?.let { reply ->
            widget.initReplyView()
            widget.replyView?.loadData(this.f().l(), reply)
        } ?: run {
            widget.replyView?.visibility = View.GONE
        }
        (widget.contentWidget as? TextView)?.layoutParams?.let {
            it.width = WRAP
            it.height = WRAP
        }
    }
}