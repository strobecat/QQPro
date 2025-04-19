package momoi.mod.qqpro.hook.style

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tencent.watch.ime.InputMethodFragment
import momoi.anno.mixin.Mixin

@Mixin
class 输入页透明 : InputMethodFragment() {
    override fun Y(p1: LayoutInflater?, p2: ViewGroup?, p3: Bundle?): View {
        return super.Y(p1, p2, p3).apply {
            setBackgroundColor(0x77_000000)
        }
    }
}