package momoi.mod.qqpro

import com.tencent.qqnt.kernel.nativeinterface.MsgRecord
import com.tencent.qqnt.msg.api.impl.MsgServiceImpl
import com.tencent.qqnt.msg.api.impl.MsgUtilApiImpl

object MsgUtil {
    val msgService = MsgServiceImpl()
    val msgUtilApi = MsgUtilApiImpl()

    fun summary(record: MsgRecord) = msgUtilApi.getElementSummary(record)
}