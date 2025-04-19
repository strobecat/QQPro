package momoi.mod.qqpro.hook

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.core.content.edit
import androidx.core.view.forEach
import androidx.core.widget.doAfterTextChanged
import momoi.anno.mixin.Mixin
import momoi.mod.qqpro.Pref
import momoi.mod.qqpro.Settings
import momoi.mod.qqpro.Utils
import momoi.mod.qqpro.asGroup
import momoi.mod.qqpro.forEachAll
import momoi.mod.qqpro.lib.FILL
import momoi.mod.qqpro.lib.GroupScope
import momoi.mod.qqpro.lib.LinearScope
import momoi.mod.qqpro.lib.background
import momoi.mod.qqpro.lib.checked
import momoi.mod.qqpro.lib.content
import momoi.mod.qqpro.lib.doAfterSwitch
import momoi.mod.qqpro.lib.dp
import momoi.mod.qqpro.lib.height
import momoi.mod.qqpro.lib.padding
import momoi.mod.qqpro.lib.text
import momoi.mod.qqpro.lib.textColor
import momoi.mod.qqpro.lib.textSize
import momoi.mod.qqpro.lib.vertical
import momoi.mod.qqpro.lib.width
import moye.wearqq.SettingsActivity

@Mixin
class 设置页 : SettingsActivity() {
    @SuppressLint("ResourceType", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val linear = findViewById<View>(2114521834).parent.parent.asGroup()
        linear.parent.asGroup().requestFocus()
        (linear.getChildAt(linear.childCount - 1) as? TextView)?.let {
            it.text =
                "禁止删除\"爅峫\"署名或进行商用,否则将会追究\n下面是QQPro的设置 by java30433\n不准你们骂才羽桃井😭😭"
            (it.layoutParams as? MarginLayoutParams)?.setMargins(0, 0, 0, 0)
        }
        GroupScope(linear).apply {
            floatInput(
                "缩放倍数",
                "重启后生效",
                Settings.scale
            )
            floatInput(
                "聊天文本缩放",
                "",
                Settings.chatScale
            )
            switch(
                "平滑表冠滚动",
                "表冠划起来没动画开这个",
                Settings.enableSmoothScroll
            )
            add<View>()
                .height(64.dp)
        }
    }

    private fun GroupScope.switch(
        title: String,
        desc: String = "",
        pref: Pref<Boolean>
    ) {
        baseEntry(title, desc) {
            add<Switch>()
                .checked(pref.value)
                .weight(0.6f)
                .doAfterSwitch {
                    pref.value = it
                }
        }
    }
    private fun GroupScope.floatInput(
        title: String,
        desc: String = "",
        pref: Pref<Float>
    ) {
        baseEntry(title, desc) {
            add<EditText>()
                .text(pref.value.toString())
                .textSize(13f)
                .textColor(0xFF_FFFFFF)
                .weight(1f)
                .doAfterTextChanged {
                    pref.value = it.toString().toFloatOrNull() ?: pref.value
                }
        }
    }

    private fun GroupScope.baseEntry(
        title: String,
        desc: String = "",
        content: LinearScope.() -> Unit
    ) {
        add<LinearLayout>()
            .width(FILL)
            .background(0xFF_242424)
            .padding(4.dp)
            .content {
                add<LinearLayout>()
                    .vertical()
                    .weight(1f)
                    .content {
                        add<TextView>()
                            .text(title)
                            .textSize(13f)
                            .textColor(0xFF_FFFFFF)
                        add<TextView>()
                            .text(desc)
                            .textSize(11f)
                            .textColor(0xFF_a1a1a1)
                    }
                content.invoke(this)
            }
    }
}