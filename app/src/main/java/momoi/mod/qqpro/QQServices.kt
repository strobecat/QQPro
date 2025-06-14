package momoi.mod.qqpro

import com.tencent.qqnt.kernel.api.impl.GroupService
import com.tencent.qqnt.msg.KernelServiceUtil

object QQServices {
    val group get() = KernelServiceUtil.f()!!.wrapperSession!!.groupService
}