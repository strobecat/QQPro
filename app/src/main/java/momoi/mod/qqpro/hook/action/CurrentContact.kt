package momoi.mod.qqpro.hook.action

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tencent.aio.api.factory.IAIOFactory
import com.tencent.aio.base.chat.ChatPie
import com.tencent.aio.main.fragment.ChatFragment
import com.tencent.qqnt.kernel.nativeinterface.Contact
import com.tencent.qqnt.kernel.nativeinterface.MemberInfo
import com.tencent.qqnt.msg.KernelServiceUtil
import momoi.anno.mixin.Mixin
import momoi.mod.qqpro.enums.ChatType
import momoi.mod.qqpro.util.Utils

val Contact.isGroup get() = this.chatType == ChatType.GROUP
val CurrentContact = Contact(0, "", "")

object CurrentMemberInfo {
    val map = mutableMapOf<String, MemberInfo>()
    fun get(uid: String, callback: (MemberInfo) -> Unit) {
        if (!CurrentContact.isGroup) {
            return
        }
        map[uid]?.let {
            callback(it)
        } ?: KernelServiceUtil.b()?.getMemberInfoForMqq(
            CurrentContact.peerUid.toLong(),
            arrayListOf(uid),
            false
        ) { _, _, result ->
            val info = result.infos.values.first()
            map[uid] = info
            callback(info)
        }
    }
}
@Mixin
class Hook(p0: IAIOFactory) : ChatPie(p0) {
    override fun a(
        fragment: ChatFragment,
        inflater: LayoutInflater,
        container: ViewGroup,
        isPreload: Boolean
    ): View {
        e?.b?.b?.let {
            CurrentMemberInfo.map.clear()
            CurrentContact.chatType = it.b
            CurrentContact.peerUid = it.c
            CurrentContact.guildId = it.d
        }
        return super.a(fragment, inflater, container, isPreload)
    }
}