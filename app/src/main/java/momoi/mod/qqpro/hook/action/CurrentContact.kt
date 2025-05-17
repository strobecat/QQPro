package momoi.mod.qqpro.hook.action

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tencent.aio.base.chat.ChatPie
import com.tencent.aio.data.AIOContact
import com.tencent.aio.main.fragment.ChatFragment
import com.tencent.qqnt.kernel.nativeinterface.Contact
import momoi.anno.mixin.Mixin

val CurrentContact = Contact(0, "", "")

@Mixin
class Hook : ChatPie() {
    override fun a(
        fragment: ChatFragment,
        inflater: LayoutInflater,
        container: ViewGroup,
        isPreload: Boolean
    ): View {
        e?.b?.b?.let {
            CurrentContact.chatType = it.b
            CurrentContact.peerUid = it.c
            CurrentContact.guildId = it.d
        }
        return super.a(fragment, inflater, container, isPreload)
    }
}