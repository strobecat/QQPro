package momoi.mod.qqpro.hook.view

import android.annotation.SuppressLint
import android.os.Bundle
import com.tencent.mobileqq.activity.fling.`TopGestureLayout$OnGestureListener`
import com.tencent.qqlive.module.videoreport.inject.fragment.ReportAndroidXDialogFragment

open class MyDialogFragment :  ReportAndroidXDialogFragment(),
    `TopGestureLayout$OnGestureListener` {
    override fun f() {
        dismiss()
    }

    override fun p() {
        dismiss()
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, 2115174655)
    }
}