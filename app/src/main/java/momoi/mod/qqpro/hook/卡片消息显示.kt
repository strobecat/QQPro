package momoi.mod.qqpro.hook

import android.content.Context
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.tencent.watch.aio_impl.data.WatchAIOMsgItem
import com.tencent.watch.aio_impl.ui.cell.base.BaseWatchItemCell
import com.tencent.watch.aio_impl.ui.widget.AIOCellGroupWidget
import momoi.anno.mixin.Mixin

@Mixin
class CardWidget(context: Context) : AIOCellGroupWidget(context) {
}

@Mixin
class 呃呃呃 : BaseWatchItemCell() {

    override fun i(
        view: View?,
        item: WatchAIOMsgItem,
        p3: Int,
        p4: MutableList<*>?,
        p5: Lifecycle?,
        p6: LifecycleOwner?
    ) {
        super.i(view, item, p3, p4, p5, p6)

    }
}