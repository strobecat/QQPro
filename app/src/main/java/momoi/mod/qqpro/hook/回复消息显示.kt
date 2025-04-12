package momoi.mod.qqpro.hook

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.tencent.aio.api.list.IListUIOperationApi
import com.tencent.aio.base.mvi.part.MsgListUiState
import com.tencent.aio.part.root.panel.content.firstLevel.msglist.mvx.intent.`MsgListDataIntent$LoadTopPage`
import com.tencent.aio.part.root.panel.content.firstLevel.msglist.mvx.vb.ui.adapter.AIOListAdapter
import com.tencent.qqnt.kernel.nativeinterface.Contact
import com.tencent.qqnt.kernel.nativeinterface.ReplyElement
import com.tencent.watch.aio_impl.coreImpl.vb.WatchAIOListVB
import com.tencent.watch.aio_impl.data.WatchAIOMsgItem
import com.tencent.watch.aio_impl.ui.cell.base.BaseWatchItemCell
import com.tencent.watch.aio_impl.ui.widget.AIOCellGroupWidget
import momoi.anno.mixin.Mixin
import momoi.mod.qqpro.Utils
import momoi.mod.qqpro.hook.view.ReplyView
import momoi.mod.qqpro.lib.Observable
import momoi.mod.qqpro.lib.WRAP
import momoi.mod.qqpro.lib.create
import momoi.mod.qqpro.warp

private var msgList = Observable(listOf<WatchAIOMsgItem>())
fun findMsg(
    seq: Long,
    loadMore: () -> Unit,
    result: (WatchAIOMsgItem?) -> Unit,
    maxCount: Int = 10
) {
    val msg = msgList.value.find { it.d.msgSeq == seq }
    if (msg == null) {
        if (maxCount > 0) {
            msgList.observeOnce {
                findMsg(seq, loadMore, result, maxCount - 1)
            }
            loadMore()
            Utils.log("Msg not found, try to load more")
        } else result(null)
    } else {
        result(msg)
    }
}

@Mixin
class 获取显示消息列表 : WatchAIOListVB() {
    @Suppress("UNCHECKED_CAST")
    override fun n(state: MsgListUiState, uiHelper: IListUIOperationApi) {
        super.n(state, uiHelper)
        msgList.update(state as List<WatchAIOMsgItem>)
    }
}

//不用lambda是因为lambda编译会创建private类，在hook target中无法访问
class ReplyClick(
    val widget: AIOCellGroupWidget,
    val reply: ReplyElement
) : View.OnClickListener {
    private var finding = false

    override fun onClick(v: View?) {
        val rv = widget.parent as RecyclerView
        if (finding) {
            return
        }
        finding = true
        findMsg(
            seq = reply.replayMsgSeq,
            loadMore = {
                ((rv.adapter as AIOListAdapter).c as WatchAIOListVB)
                    .L(`MsgListDataIntent$LoadTopPage`("WatchAIOListVB"))
            },
            result = { item ->
                finding = false
                if (item != null) {
                    rv.layoutManager?.startSmoothScroll(
                        object : LinearSmoothScroller(widget.context) {
                            init {
                                targetPosition = msgList.value.indexOf(item)
                            }

                            override fun getVerticalSnapPreference(): Int {
                                return SNAP_TO_START
                            }
                        }
                    )
                }
            }
        )
    }
}

@Mixin
class Mikoto10031(context: Context) : AIOCellGroupWidget(context) {
    var replyView: ReplyView? = null

    fun initReplyView(contact: Contact, replyElement: ReplyElement) {
        if (replyView == null) {
            replyView = create(context)
            val warp = contentWidget.warp()
            warp.addView(replyView, 0)
        }
        replyView!!.visibility = View.VISIBLE
        replyView!!.loadData(contact, replyElement)
        replyView!!.setOnClickListener(ReplyClick(this, replyElement))
    }

    fun recycler() {
        replyView?.visibility = View.GONE
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
            widget.initReplyView(this.f().l(), reply)
        } ?: widget.recycler()
        (widget.contentWidget as? TextView)?.layoutParams?.let {
            it.width = WRAP
            it.height = WRAP
        }
    }
}