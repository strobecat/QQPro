package momoi.mod.qqpro.hook.style

import android.content.Context
import com.tencent.mobileqq.quibadge.QUIBadge
import momoi.anno.mixin.Mixin

@Mixin
class 具体未读数 : QUIBadge(null, null) {
    override fun f(i: Int) {
        this.j = i
        this.k = this.j.toString()
    }
}