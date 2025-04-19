package momoi.mod.qqpro.hook.action

import android.content.Context
import com.tencent.qqnt.chats.api.impl.ChatsUtilImpl
import com.tencent.qqnt.chats.core.adapter.holder.RecentContactItemHolder
import com.tencent.qqnt.chats.core.adapter.itemdata.BaseChatItem
import com.tencent.qqnt.chats.core.adapter.itemdata.RecentContactChatItem
import com.tencent.qqnt.chats.data.converter.ChatsItemConverter
import com.tencent.qqnt.kernel.nativeinterface.RecentContactInfo
import com.tencent.qqnt.watch.chat.list.WatchRecentContactHolder
import com.tencent.qqnt.watch.chat.list.WatchRecentItemBuilder
import momoi.anno.mixin.Mixin
import momoi.mod.qqpro.Utils
import mqq.app.AppRuntime

object RecentContacts {
    val map = mutableMapOf<String, Data>()
    fun get(peerUin: String?) = map[peerUin]
    class Data(
        val raw: RecentContactInfo,
        val unreadCntCached: Int
    )

    @Mixin
    class Hook : WatchRecentItemBuilder() {
        override fun t(item: RecentContactChatItem, holder: WatchRecentContactHolder) {
            Utils.log("load recent contact: ${item.a.peerName}, unreadCnt: ${item.a.unreadCnt}, chatCnt: ${item.a.unreadChatCnt}, peerUid: ${item.a.peerUid}")
            map[item.a.peerUid] = Data(
                item.a,
                item.a.unreadCnt.toInt()
            )
            super.t(item, holder)
        }
    }
}