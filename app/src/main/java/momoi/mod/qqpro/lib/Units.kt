package momoi.mod.qqpro.lib

import android.annotation.SuppressLint
import android.content.res.Resources
import android.util.TypedValue

val Int.dp @SuppressLint("WrongConstant")
get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toFloat(), Resources.getSystem().displayMetrics).toInt()