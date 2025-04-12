package momoi.mod.qqpro

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.res.Resources
import android.os.Build
import android.util.Log
import com.tencent.mobileqq.utils.TimeFormatterUtils


object Utils {
    @SuppressLint("PrivateApi")
    val application = Class.forName("android.app.ActivityThread").getMethod("currentApplication")
        .invoke(null) as Application
    val isDebug =
        try {
            val info = application.applicationInfo
            (info.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        } catch (e: Exception) {
            false
        }

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
    val isRoundScreen = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        Resources.getSystem().configuration.isScreenRound
    } else {
        isDebug
    }
}