package momoi.mod.qqpro.lib

import android.view.View
import android.widget.LinearLayout

fun <T : LinearLayout> T.vertical(): T = apply {
    orientation = LinearLayout.VERTICAL
}
fun <T : LinearLayout> T.gravity(value: Int) = apply {
    gravity = value
}