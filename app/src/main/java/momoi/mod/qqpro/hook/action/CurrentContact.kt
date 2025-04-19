package momoi.mod.qqpro.hook.action

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tencent.aio.base.chat.ChatPie
import com.tencent.aio.data.AIOContact
import com.tencent.aio.main.fragment.ChatFragment
import com.tencent.qqnt.kernel.nativeinterface.Contact
import momoi.anno.mixin.Mixin

object CurrentContact {
    var value: Contact? = null
        private set

    @Mixin
    class Hook : ChatPie() {
        override fun a(
            fragment: ChatFragment,
            inflater: LayoutInflater,
            container: ViewGroup,
            isPreload: Boolean
        ): View {
            e?.b?.b?.let {
                value = Contact(it.b, it.c, it.d)
            }
            return super.a(fragment, inflater, container, isPreload)
        }
    }
}