package momoi.mod.qqpro.hook

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tencent.qqnt.watch.selftab.ui.SelfFragment
import momoi.anno.mixin.Mixin

const val VERSION_CODE = 11

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
            appendLine("QQPro - v1.6")
            appendLine()
            appendLine("更新日志：")
            appendLine("优化回复消息溯源")
            appendLine("优化等级头衔相关内容")
            appendLine("调整管理撤回按钮位置")
            appendLine("调整等级头衔位置")
            appendLine("修复卡片消息图片尺寸问题")
            appendLine()
            appendLine("交流群：392106734")
            appendLine("2025/06/14")
        }
        return result
    }
}