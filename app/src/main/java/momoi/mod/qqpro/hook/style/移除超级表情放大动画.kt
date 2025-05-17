package momoi.mod.qqpro.hook.style

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import com.tencent.watch.aio_impl.ui.cell.superface.WatchAniStickerItemCell
import com.tencent.watch.aio_impl.ui.cell.superface.WatchAniStickerGroupWidget
import com.tencent.watch.aio_impl.ui.cell.superface.WatchAniStickerMsgItem
import momoi.anno.mixin.Mixin
import momoi.anno.mixin.StaticHook

@SuppressLint("StaticFieldLeak")
@Mixin
object 移除超级表情放大动画 : WatchAniStickerItemCell()  {
    @JvmStatic
    @StaticHook
    fun n_(cell: WatchAniStickerItemCell, widget: WatchAniStickerGroupWidget, msg: WatchAniStickerMsgItem) {
        msg.r.c = true
        n(cell, widget, msg)
    }

    override fun d(
        p0: WatchAniStickerGroupWidget,
        p1: WatchAniStickerMsgItem,
        p2: Int,
        p3: List<Any?>
    ) {
    }

    override fun e(p0: ViewGroup): WatchAniStickerGroupWidget {
        throw IllegalStateException()
    }
}