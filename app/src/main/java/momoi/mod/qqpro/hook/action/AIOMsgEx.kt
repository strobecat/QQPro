package momoi.mod.qqpro.hook.action

import com.tencent.watch.aio_impl.data.WatchAIOMsgItem
import momoi.anno.mixin.Mixin

@Mixin
class AIOMsgEx : WatchAIOMsgItem() {
    var previousSame = false
}