package momoi.mod.qqpro.hook.action

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tencent.aio.api.list.IListUIOperationApi
import com.tencent.aio.base.chat.ChatPie
import com.tencent.aio.base.mvi.part.MsgListUiState
import com.tencent.aio.main.fragment.ChatFragment
import com.tencent.aio.part.root.panel.content.firstLevel.msglist.mvx.intent.`MsgListDataIntent$LoadTopPage`
import com.tencent.aio.part.root.panel.content.firstLevel.msglist.mvx.state.MsgListState
import com.tencent.mvi.api.help.CreateViewParams
import com.tencent.watch.aio_impl.coreImpl.vb.WatchAIOListVB
import com.tencent.watch.aio_impl.data.WatchAIOMsgItem
import momoi.anno.mixin.Mixin
import momoi.mod.qqpro.Utils
import momoi.mod.qqpro.lib.Observable

object CurrentMsgList {
    lateinit var vb: WatchAIOListVB
        private set
    var msgList = Observable(MsgListState())
        private set

    fun getMsgIndex(msg: WatchAIOMsgItem): Int {
        return msgList.value.indexOf(msg)
    }

    private var isLoadingMsg = false
    private fun loadMoreMsg() {
        if (!isLoadingMsg) {
            msgList.observeOnce {
                isLoadingMsg = false
            }
            isLoadingMsg = true
            Utils.log("Load more msg. currentSize: ${msgList.value.size}")
            vb.L(`MsgListDataIntent$LoadTopPage`("WatchAIOListVB"))
        }
    }

    fun upwardMsg(current: Int, count: Int, callback: (Int) -> Unit) {
        val target = msgList.value.size - 1 - current + count
        upwardMsgInternal(target, callback)
    }

    private fun upwardMsgInternal(target: Int, callback: (Int) -> Unit) {
        if (msgList.value.size < target) {
            msgList.observeOnce {
                upwardMsgInternal(target, callback)
            }
            loadMoreMsg()
        } else {
            callback(msgList.value.size - target - 1)
        }
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
            } else result(null)
        } else {
            result(msg)
        }
    }

    @Mixin
    class Hook : WatchAIOListVB() {
        @Suppress("UNCHECKED_CAST")
        override fun n(state: MsgListUiState, uiHelper: IListUIOperationApi) {
            vb = this
            val msg = msgList.value
            val list = state as MsgListState
            val lastIndex = list.indexOfFirst { it.d.msgSeq == msg.getOrNull(0)?.d?.msgSeq }
            msgList.update(
                MsgListState(
                    state.b,
                    if (lastIndex != -1) {
                        list.subList(0, lastIndex) + msgList.value
                    } else {
                        list + msgList.value
                    },
                    state.c, state.d
                )
            )
            Utils.log("Msg lists updated. currentSize: ${msgList.value.size}")
            super.n(msgList.value, uiHelper)
        }
    }

    @Mixin
    class Clear : ChatPie() {
        override fun a(
            fragment: ChatFragment,
            inflater: LayoutInflater,
            container: ViewGroup,
            isPreload: Boolean
        ): View {
            msgList = Observable(MsgListState())
            return super.a(fragment, inflater, container, isPreload)
        }
    }

}