package momoi.mod.qqpro

import android.annotation.SuppressLint
import android.app.Application
import android.content.res.Resources
import android.util.Log
import com.tencent.mobileqq.utils.TimeFormatterUtils


object Utils {
    @SuppressLint("PrivateApi")
    val application = Class.forName("android.app.ActivityThread").getMethod("currentApplication")
        .invoke(null) as Application
    val assets = application.assets

    fun formatTime(timestamp: Long): CharSequence =
        TimeFormatterUtils.a(application, 3, timestamp, true, true)!!

    private var debugWatcher: Any? = null
    fun debugger(catch: Any?) {
        debugWatcher = catch
        Log.e("QQQQQQQQQQ", "debugger!")
    }

    fun log(msg: String) {
        Log.e("QQPro", msg)
    }
    val heightPixels = Resources.getSystem().displayMetrics.heightPixels
}