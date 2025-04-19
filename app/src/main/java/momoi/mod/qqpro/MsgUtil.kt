package momoi.mod.qqpro

import android.view.View
import com.tencent.qqnt.kernel.nativeinterface.MsgRecord
import com.tencent.qqnt.msg.api.impl.MsgServiceImpl
import com.tencent.qqnt.msg.api.impl.MsgUtilApiImpl
import momoi.mod.qqpro.hook.view.MyDialogFragment
import java.util.UUID
import kotlin.random.Random

object MsgUtil {
    val msgService = MsgServiceImpl()
    val msgUtilApi = MsgUtilApiImpl()

    fun summary(record: MsgRecord) = msgUtilApi.getElementSummary(record)
}

fun View.showDialog(dialog: MyDialogFragment) {
    dialog.show(
        WatchPicElementExtKt.W(this).childFragmentManager,
        Random.nextInt().toString()
    )
}