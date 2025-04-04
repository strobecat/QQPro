package momoi.mod.qqpro.lib

import android.view.View
import android.view.ViewGroup

fun <T : View> ViewGroup.find(id: Int) = findViewById<T>(id)
fun <T : View> ViewGroup.child(id: Int) = getChildAt(0) as T