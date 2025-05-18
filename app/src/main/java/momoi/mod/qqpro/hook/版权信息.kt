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
            appendLine("QQPro - v1.4.1")
            appendLine()
            appendLine("更新日志：")
            appendLine("现在可以展示卡片信息和跳转链接")
            appendLine("大部分文本都兼容了缩放设置")
            appendLine("优化了长图缩放效果")
            appendLine("修复了回复消息内容显示问题")
            appendLine("移除了跳转未读消息的滚动动画")
            appendLine("修复圆屏长按菜单错位问题")
            appendLine("对Hook代码层进行了一些调整")
            appendLine()
            appendLine("交流群：392106734")
            appendLine("2025/05/17")
        }
        return result
    }
}