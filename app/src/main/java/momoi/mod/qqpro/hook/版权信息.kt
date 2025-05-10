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
            appendLine("QQPro - v1.4")
            appendLine()
            appendLine("更新日志：")
            appendLine("支持查看嵌套的聊天记录（最多三层，QQ限制）")
            appendLine("聊天记录和回复可以看见黄脸表情")
            appendLine("调整了消息长按菜单的位置")
            appendLine("聊天记录查看大图可以点击屏幕顶部返回")
            appendLine("也许彻底修复了消息列表的闪退bug")
            appendLine()
            appendLine("交流群：392106734")
            appendLine("2025/05/10")
        }
        return result
    }
}