package momoi.mod.qqpro.hook.action

import android.view.View
import com.tencent.aio.api.list.IListUIOperationApi
import com.tencent.aio.base.mvi.part.MsgListUiState
import com.tencent.aio.part.root.panel.content.firstLevel.msglist.mvx.intent.`MsgListDataIntent$LoadTopPage`
import com.tencent.mvi.api.help.CreateViewParams
import com.tencent.watch.aio_impl.coreImpl.vb.WatchAIOListVB
import com.tencent.watch.aio_impl.data.WatchAIOMsgItem
import momoi.anno.mixin.Mixin
import momoi.mod.qqpro.Utils
import momoi.mod.qqpro.lib.Observable

object CurrentMsgList{
    lateinit var vb: WatchAIOListVB
        private set
    var msgList = Observable(listOf<WatchAIOMsgItem>())
        private set

    fun getMsgIndex(msg: WatchAIOMsgItem): Int {
        return msgList.value.indexOf(msg)
    }

    private fun loadMoreMsg() {
        vb.L(`MsgListDataIntent$LoadTopPage`("WatchAIOListVB"))
    }

    fun findMsg(
        seq: Long,
        result: (WatchAIOMsgItem?) -> Unit,
        repeatCount: Int = 10
    ) {
        val msg = msgList.value.find { it.d.msgSeq == seq }
        if (msg == null) {
            if (repeatCount > 0) {
                msgList.observeOnce {
                    findMsg(seq, result, repeatCount - 1)
                }
                loadMoreMsg()
                Utils.log("Msg not found, try to load more")
            } else result(null)
        } else {
            result(msg)
        }
    }

    @Mixin
    class Hook : WatchAIOListVB() {
        @Suppress("UNCHECKED_CAST")
        override fun n(state: MsgListUiState, uiHelper: IListUIOperationApi) {
            super.n(state, uiHelper)
            vb = this
            msgList.update(state as List<WatchAIOMsgItem>)
        }
    }

}