package momoi.mod.qqpro.hook.style

import com.tencent.watch.aio_impl.ui.cell.superface.WatchAniStickerItemCell
import com.tencent.watch.aio_impl.ui.cell.superface.WatchAniStickerGroupWidget
import com.tencent.watch.aio_impl.ui.cell.superface.WatchAniStickerMsgItem
import momoi.anno.mixin.Mixin
import momoi.anno.mixin.StaticHook

@Mixin
object 移除超级表情放大动画 : WatchAniStickerItemCell()  {
    @JvmStatic
    @StaticHook
    fun n_(cell: WatchAniStickerItemCell, widget: WatchAniStickerGroupWidget, msg: WatchAniStickerMsgItem) {
        msg.r.c = true
        n(cell, widget, msg)
    }
}