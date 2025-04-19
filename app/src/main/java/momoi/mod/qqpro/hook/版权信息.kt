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
            appendLine("QQPro - v1.3")
            appendLine()
            appendLine("更新日志：")
            appendLine("跳转第一条未读消息")
            appendLine("显示完整未读消息数")
            appendLine("调整聊天页按钮布局")
            appendLine("滚轮支持缩放图片")
            appendLine("新设置：米兔屏蔽返回键")
            appendLine("修复了聊天记录闪退的bug")
            appendLine("修改了获取聊天记录图片的一些逻辑")
            appendLine("调整了查看大图的最大缩放")
            appendLine("回退了对气泡边距裁剪的修改")
            appendLine()
            appendLine("交流群：392106734")
            appendLine("2025/04/19")
        }
        return result
    }
}