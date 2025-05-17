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
import com.tencent.qqnt.kernel.nativeinterface.MsgRecord
import com.tencent.watch.aio_impl.coreImpl.vb.WatchAIOListVB
import com.tencent.watch.aio_impl.data.WatchAIOMsgItem
import com.tencent.watch.aio_impl.ext.MsgListUtilKt
import momoi.anno.mixin.Mixin
import momoi.mod.qqpro.Utils
import momoi.mod.qqpro.lib.Observable

object CurrentMsgList {
    lateinit var vb: WatchAIOListVB
        private set
    var msgList = Observable(mutableListOf<WatchAIOMsgItem>())
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
            var insertIndex = -1
            while (true) {
                val last = list.pollLast()
                if (last == null) {
                    list.addAll(msg)
                    break
                }
                val index = msg.indexOfLast { last.d.msgId == it.d.msgId }
                if (index == -1) {
                    if (insertIndex == -1) {
                        msg.add(last)
                        insertIndex = msg.lastIndex
                    } else {
                        msg.add(insertIndex, last)
                    }
                } else {
                    msg[index] = last
                    //if (insertIndex == -1) {
                    //    insertIndex = 0
                    //}
                    //for (i in insertIndex until msg.size) {
                    //    msg[i].checkAndSetSameSender(msg.getOrNull(i-1))
                    //}
                    list.addAll(msg.subList(index, msg.size))
                    break
                }
            }
            msgList.update(list.toMutableList())
            super.n(list, uiHelper)
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
            msgList = Observable(ArrayList())
            return super.a(fragment, inflater, container, isPreload)
        }
    }

}