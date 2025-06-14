package momoi.mod.qqpro.hook.style

import com.tencent.qqnt.kernel.nativeinterface.MsgRecord
import com.tencent.watch.aio_impl.data.WatchAIOMsgItem
import com.tencent.watch.aio_impl.ext.MsgListUtilKt
import momoi.anno.mixin.Mixin
import momoi.anno.mixin.StaticHook

@StaticHook(MsgListUtilKt::class)
fun c(msg: MsgRecord): WatchAIOMsgItem {
    val reply = msg.elements.firstOrNull { it.replyElement != null } ?: return MsgListUtilKt.c(msg)
    msg.elements.remove(reply)
    val rawType = msg.msgType
    msg.msgType = 2
    val result = MsgListUtilKt.c(msg)
    msg.elements.add(0, reply)
    msg.msgType = rawType
    return result
}