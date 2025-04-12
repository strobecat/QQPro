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
            appendLine("QQPro - v1.2")
            appendLine()
            appendLine("更新日志：")
            appendLine("回复消息定位")
            appendLine("修复回复显示bug")
            appendLine("消息长按菜单背景透明")
            appendLine("聊天记录图片加载失败提示")
            appendLine("移除超级表情放大动画")
            appendLine("调整气泡边距")
            appendLine("圆屏查看图片时添加边距")
            appendLine("新设置：气泡文本缩放")
            appendLine("新设置：平滑表冠滚动（by dudu）")
            appendLine()
            appendLine("交流群：260743206")
            appendLine("2025/04/12")
        }
        return result
    }
}