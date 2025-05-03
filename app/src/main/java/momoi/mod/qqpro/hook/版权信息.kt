package momoi.mod.qqpro.hook

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tencent.qqnt.watch.selftab.ui.SelfFragment
import momoi.anno.mixin.Mixin

@Mixin
class 版权信息 : SelfFragment() {
    @SuppressLint("ResourceType", "SetTextI18n")
    override fun Y(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val result = super.Y(inflater, container, savedInstanceState)
        val tv = result.findViewById<TextView>(2114521808)
        tv.text = buildString {
            appendLine("QQPro - v1.3.1-fix3")
            appendLine()
            appendLine("更新日志：")
            appendLine("修复了消息列表可能重复的bug")
            appendLine("优化了性能，减小了消息延迟")
            appendLine()
            appendLine("交流群：392106734")
            appendLine("2025/04/20")
        }
        return result
    }
}