package momoi.mod.qqpro.hook.style

import android.content.Context
import android.view.View
import com.tencent.watch.aio_impl.ui.widget.AIOCellGroupWidget
import momoi.anno.mixin.Mixin

@Mixin
class 合并相同发送者消息(context: Context) : AIOCellGroupWidget(context) {
    override fun getNickWidget(): View? {
    }
}