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
            appendLine("QQPro - v1.3.1")
            appendLine()
            appendLine("更新日志：")
            appendLine("修复跳转首条未读消息的一些问题")
            appendLine("修复原版就有的底部消息消失的bug")
            appendLine("提升了看见聊天记录中图片的概率(?)")
            appendLine("新设置：输入键居中")
            appendLine("The first step for New-UI(designed by WD2345)")
            appendLine()
            appendLine("交流群：392106734")
            appendLine("2025/04/20")
        }
        return result
    }
}