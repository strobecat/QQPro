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
            appendLine("QQPro - v1.5")
            appendLine()
            appendLine("更新日志：")
            appendLine("修复回复有关的闪退bug")
            appendLine("现在有概率看见卡片消息的图片了")
            appendLine("聊天信息里的链接可以点开")
            appendLine("管理可以撤回群成员消息")
            appendLine("聊天中现在显示群头衔和等级")
            appendLine()
            appendLine("交流群：392106734")
            appendLine("2025/06/14")
        }
        return result
    }
}