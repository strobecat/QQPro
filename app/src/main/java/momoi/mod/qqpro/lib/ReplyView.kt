package momoi.mod.qqpro.lib

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import com.tencent.qqnt.kernel.nativeinterface.Contact
import com.tencent.qqnt.kernel.nativeinterface.ReplyElement
import momoi.anno.mixin.Mixin
import momoi.mod.qqpro.Colors
import momoi.mod.qqpro.MsgUtil
import momoi.mod.qqpro.Utils
import momoi.mod.qqpro.join

@Mixin
class ReplyElementEx : ReplyElement() {
    var senderName: String? = null
}

class ReplyView(context: Context) : LinearLayout(context) {
    val stateReplySender = MutableState("")
    val stateReplyContent = MutableState("")
    init {
        this.vertical()
            .layoutParams(LayoutParams(WRAP, WRAP))
            .marginHorizontal(2.dp)
            .background(Colors.replyBackground)
            .paddingHorizontal(2.dp)
            .content {
                add<TextView>()
                    .textSize(10f)
                    .textColor(Colors.replyText)
                    .text(stateReplySender)
                add<TextView>()
                    .textSize(12f)
                    .textColor(Colors.replyText)
                    .text(stateReplyContent)
            }
    }

    fun loadData(contact: Contact, replyElement: ReplyElement) {
        val reply = replyElement as ReplyElementEx
        val sendTime = Utils.formatTime(reply.replyMsgTime * 1000)
        val senderName = reply.senderName ?: reply.senderUid.toString().also {
            MsgUtil.msgService.getSingleMsg(contact, reply.replayMsgSeq
            ) { _, _, msgRecords ->
                msgRecords?.getOrNull(0)?.let {
                    reply.senderName =
                        it.sendMemberName.ifEmpty { it.sendNickName }
                    stateReplySender.update("${reply.senderName} $sendTime")
                    stateReplyContent.update(MsgUtil.summary(it))
                }
            }
        }
        val content = reply.sourceMsgTextElems.join { it.textElemContent ?: " "  }
        stateReplySender.update("$senderName $sendTime")
        stateReplyContent.update(content)
    }
}