package momoi.mod.qqpro

import android.view.View
import com.tencent.qqnt.kernel.nativeinterface.MsgRecord
import com.tencent.qqnt.msg.api.impl.MsgServiceImpl
import com.tencent.qqnt.msg.api.impl.MsgUtilApiImpl
import com.tencent.watch.aio_impl.ext.MsgListUtilKt
import momoi.mod.qqpro.hook.view.MyDialogFragment
import java.util.UUID
import kotlin.random.Random

object MsgUtil {
    val msgService = MsgServiceImpl()
    val msgUtilApi = MsgUtilApiImpl()

    fun summary(record: MsgRecord): CharSequence {
        return MsgListUtilKt.a(record.elements)
    }
}

fun View.showDialog(dialog: MyDialogFragment) {
    dialog.show(
        WatchPicElementExtKt.W(this).childFragmentManager,
        Random.nextInt().toString()
    )
}