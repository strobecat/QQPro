package momoi.mod.qqpro.lib

import android.annotation.SuppressLint
import android.content.res.Resources
import android.util.TypedValue
import momoi.mod.qqpro.util.Utils

val Int.dp @SuppressLint("WrongConstant")
get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toFloat(), Utils.application.resources.displayMetrics).toInt()

val Float.vh get() = (this * Utils.heightPixels).toInt()