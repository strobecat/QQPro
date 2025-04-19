package momoi.mod.qqpro.hook.style

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.forEach
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.tencent.qqnt.kernel.nativeinterface.MsgRecord
import com.tencent.watch.aio_impl.data.WatchAIOMsgItem
import com.tencent.watch.aio_impl.ext.MsgListUtilKt
import com.tencent.watch.aio_impl.ui.cell.base.BaseWatchItemCell
import com.tencent.watch.aio_impl.ui.cell.superface.WatchAniStickerGroupWidget
import com.tencent.watch.aio_impl.ui.cell.superface.WatchAniStickerItemCell
import com.tencent.watch.aio_impl.ui.cell.superface.WatchAniStickerMsgItem
import com.tencent.watch.aio_impl.ui.menu.AIOLongClickMenuFragment
import com.tencent.watch.aio_impl.ui.widget.AIOCellGroupWidget
import com.tencent.watch.aio_impl.ui.widget.AIOItemTextView
import com.tencent.watch.aio_impl.ui.widget.BubbleLayoutCompatPress
import com.tencent.watch.aio_impl.ui.widget.RoundBubbleImageView
import me.jessyan.autosize.AutoSizeConfig
import momoi.anno.mixin.Mixin
import momoi.anno.mixin.StaticHook
import momoi.mod.qqpro.Settings
import momoi.mod.qqpro.asGroup
import momoi.mod.qqpro.asGroupOrNull
import momoi.mod.qqpro.forEachAll
import momoi.mod.qqpro.lib.background

@Mixin
class 缩放 : AutoSizeConfig() {
    override fun getDesignHeightInDp(): Int {
        return (super.getDesignHeightInDp() / Settings.scale.value).toInt()
    }

    override fun getDesignWidthInDp(): Int {
        return (super.getDesignWidthInDp() / Settings.scale.value).toInt()
    }
}