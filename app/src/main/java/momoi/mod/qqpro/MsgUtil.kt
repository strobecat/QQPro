package momoi.mod.qqpro

import android.view.View
import com.tencent.qqnt.kernel.nativeinterface.MsgElement
import com.tencent.qqnt.kernel.nativeinterface.MsgRecord
import com.tencent.qqnt.kernel.nativeinterface.TextElement
import com.tencent.qqnt.msg.api.impl.MsgServiceImpl
import com.tencent.qqnt.msg.api.impl.MsgUtilApiImpl
import com.tencent.watch.aio_impl.ext.MsgListUtilKt
import momoi.mod.qqpro.hook.view.MyDialogFragment
import java.util.UUID
import kotlin.random.Random

object MsgUtil {
    val msgService = MsgServiceImpl()
    val msgUtilApi = MsgUtilApiImpl()

    fun summary(elements: List<MsgElement>): CharSequence {
        elements.forEach { ele ->
            ele.picElement?.let {
                ele.picElement = null
                ele.textElement = TextElement().apply {
                    content = "[图片]"
                }
            }
            ele.multiForwardMsgElement?.let {
                ele.multiForwardMsgElement = null
                ele.textElement = TextElement().apply {
                    content = "[聊天记录]"
                }
            }
            ele.videoElement?.let {
                ele.videoElement = null
                ele.textElement = TextElement().apply {
                    content = "[视频]"
                }
            }
            ele.fileElement?.let {
                ele.fileElement = null
                ele.textElement = TextElement().apply {
                    content = "[文件]"
                }
            }
            ele.pttElement?.let {
                ele.pttElement = null
                ele.textElement = TextElement().apply {
                    content = "[语音]"
                }
            }
        }
        return MsgListUtilKt.a(elements)
    }
    fun summary(record: MsgRecord) = summary(record.elements)
}

fun View.showDialog(dialog: MyDialogFragment) {
    dialog.show(
        WatchPicElementExtKt.W(this).childFragmentManager,
        Random.nextInt().toString()
    )
}