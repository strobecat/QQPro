package momoi.mod.qqpro.hook.style

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.forEach
import com.tencent.watch.aio_impl.ui.menu.AIOLongClickMenuFragment
import com.tencent.watch.aio_impl.ui.menu.MenuItemFactory
import momoi.anno.mixin.Mixin
import momoi.mod.qqpro.util.Utils
import momoi.mod.qqpro.asGroup
import momoi.mod.qqpro.forEachAll
import momoi.mod.qqpro.lib.FILL
import momoi.mod.qqpro.lib.LinearScope
import momoi.mod.qqpro.lib.background
import momoi.mod.qqpro.lib.create
import momoi.mod.qqpro.lib.dp
import momoi.mod.qqpro.lib.height
import momoi.mod.qqpro.lib.paddingHorizontal
import momoi.mod.qqpro.lib.vh
import momoi.mod.qqpro.lib.width

val menuSort = arrayOf(
    "回复",
    "@Ta",
    "复制文本",
    "复读文本",
    "去聊天",
    "加好友",
    "删除",
)

private fun process(group: ViewGroup) {
    group.removeViewAt(0)
    val linear = group.getChildAt(0).asGroup()
        .getChildAt(0).asGroup()
        .getChildAt(0) as LinearLayout
    linear.background(0x44_000000)
    val items = mutableMapOf<String, View>()
    linear.forEach { item ->
        item.asGroup().forEachAll {
            if (it is AppCompatTextView) {
                items[it.text.toString()] = item
            }
        }
    }
    linear.removeAllViews()
    if (Utils.isRoundScreen) {
        linear.paddingHorizontal(0.1f.vh)
        LinearScope(linear).add<View>()
            .width(FILL)
            .height(0.18f.vh)
    }
    menuSort.forEach {
        items[it]?.let { item ->
            linear.addView(item)
        }
    }
    items.values.forEach {
        if (it.parent == null) {
            linear.addView(it, 1)
        }
    }
    if (Utils.isRoundScreen) {
        LinearScope(linear).add<View>()
            .width(FILL)
            .height(0.18f.vh)
    }
}

@Mixin
class 长按菜单透明(p0: (MenuItemFactory.ItemEnum) -> Unit, p1: String?) :
    AIOLongClickMenuFragment(p0, p1) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return super.onCreateView(inflater, container, savedInstanceState).apply {
            this.asGroup().getChildAt(0).asGroup().let { group ->
                process(group)
            }
        }
    }
}