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
import momoi.mod.qqpro.Settings
import momoi.mod.qqpro.Utils
import momoi.mod.qqpro.asGroup
import momoi.mod.qqpro.forEachAll
import momoi.mod.qqpro.lib.*
import moye.wearqq.SettingsActivity

@Mixin
class 设置页 : SettingsActivity() {
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val linear = findViewById<View>(2114521834).parent.parent.asGroup()
        (linear.getChildAt(linear.childCount-1) as? TextView)?.let {
            it.text = "禁止删除\"爅峫\"署名或进行商用,否则将会追究\n下面是QQPro的设置 by java30433\n不准你们骂才羽桃井😭😭"
            (it.layoutParams as? MarginLayoutParams)?.setMargins(0, 0, 0, 0)
        }
        GroupScope(linear).apply {
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
                                .text("缩放倍数")
                                .textColor(0xFF_FFFFFF)
                            add<TextView>()
                                .text("重启后生效")
                                .textColor(0xFF_a1a1a1)
                        }
                    add<EditText>()
                        .text(Settings.scale.toString())
                        .textColor(0xFF_FFFFFF)
                        .weight(1f)
                        .doAfterTextChanged {
                            Settings.sp.edit {
                                putFloat("scale", it.toString().toFloatOrNull() ?: 0.9f)
                            }
                        }
                }
            add<View>()
                .height(24.dp)
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
                                .text("表冠适配修复")
                                .textColor(0xFF_FFFFFF)
                            add<TextView>()
                                .text("表冠划起来没动画开这个")
                                .textColor(0xFF_a1a1a1)
                        }
                    add<Switch>()
                        .weight(1f)
                        .checked(Settings.enableSmoothScroll)
                        .doAfterSwitch {
                            Settings.sp.edit {
                                putBoolean("enableSmoothScroll", it)
                            }
                        }
                }
            add<View>()
                .height(24.dp)
        }
    }
}