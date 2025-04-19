package momoi.mod.qqpro.hook.style

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tencent.watch.aio_impl.ui.menu.AIOLongClickMenuFragment
import momoi.anno.mixin.Mixin
import momoi.mod.qqpro.asGroup
import momoi.mod.qqpro.forEachAll
import momoi.mod.qqpro.lib.background

@Mixin
class 长按菜单透明 : AIOLongClickMenuFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)?.apply {
            this.asGroup().getChildAt(0).asGroup().let { group ->
                group.removeViewAt(0)
                group.forEachAll {
                    it.background(0x22_000000)
                }
            }
        }
    }
}