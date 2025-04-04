package momoi.mod.qqpro.hook

import android.content.Context
import android.content.res.Resources
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.ScrollView
import androidx.core.view.MotionEventCompat
import androidx.core.view.ViewConfigurationCompat
import androidx.recyclerview.widget.RecyclerView
import com.tencent.qqlive.module.videoreport.inject.dialog.ReportDialog
import com.tencent.qqnt.watch.mainframe.MainActivity
import momoi.anno.mixin.Mixin
import momoi.mod.qqpro.Utils
import momoi.mod.qqpro.asGroup
import momoi.mod.qqpro.forEachAll
import kotlin.math.roundToInt

private val screenCenterX = Resources.getSystem().displayMetrics.widthPixels / 2
private val point = intArrayOf(Int.MIN_VALUE, 0)

//TODO: 代码复用
@Mixin
class 滚轮适配配(context: Context) : ReportDialog(context) {
    private var targetView: View? = null
    override fun dispatchGenericMotionEvent(ev: MotionEvent): Boolean {
        if (targetView?.isInCenter() != true) {
            targetView = window?.decorView?.let { findTarget(it) }
        }
        val delta =
            -ev.getAxisValue(MotionEventCompat.AXIS_SCROLL) * ViewConfigurationCompat.getScaledVerticalScrollFactor(
                ViewConfiguration.get(context), context
            )
        targetView?.scrollBy(0, delta.roundToInt())
        return super.dispatchGenericMotionEvent(ev)
    }
}

@Mixin
class 滚轮适配 : MainActivity() {
    private var targetView: View? = null
    override fun dispatchGenericMotionEvent(ev: MotionEvent): Boolean {
        if (targetView?.isInCenter() != true) {
            targetView = findTarget(window.decorView)
        }
        val delta =
            -ev.getAxisValue(MotionEventCompat.AXIS_SCROLL) * ViewConfigurationCompat.getScaledVerticalScrollFactor(
                ViewConfiguration.get(this), this
            )
        targetView?.scrollBy(0, delta.roundToInt())
        return super.dispatchGenericMotionEvent(ev)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        findTarget(window.decorView)
        return super.dispatchTouchEvent(ev)
    }
}

private fun findTarget(rootView: View): View? {
    var target: View? = null
    rootView.asGroup().forEachAll {
        if (target != null) return@forEachAll
        val rv = (it as? RecyclerView)?.layoutManager?.canScrollVertically() == true
        val lv = it is ScrollView
        if ((rv || lv) && it.isInCenter()) {
            target = it
        }
    }
    return target
}

fun View.isInCenter(): Boolean {
    if (!isAttachedToWindow) return false
    getLocationOnScreen(point)
    return point[0] <= screenCenterX && point[0] + width > screenCenterX
}