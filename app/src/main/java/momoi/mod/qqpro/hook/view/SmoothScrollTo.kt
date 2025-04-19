package momoi.mod.qqpro.hook.view

import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import momoi.mod.qqpro.hook.action.CurrentMsgList

fun RecyclerView.smoothScrollToStart(position: Int) {
    layoutManager?.startSmoothScroll(
        object : LinearSmoothScroller(context) {
            init {
                targetPosition = position
            }

            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        })
}