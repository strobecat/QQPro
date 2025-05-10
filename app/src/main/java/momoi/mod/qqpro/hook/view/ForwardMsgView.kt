package momoi.mod.qqpro.hook.view

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.tencent.qqnt.kernel.nativeinterface.Contact
import momoi.mod.qqpro.hook.DetailFragment
import momoi.mod.qqpro.hook.MultiMsgData
import momoi.mod.qqpro.lib.FILL
import momoi.mod.qqpro.lib.background
import momoi.mod.qqpro.lib.clickable
import momoi.mod.qqpro.lib.content
import momoi.mod.qqpro.lib.dp
import momoi.mod.qqpro.lib.marginVertical
import momoi.mod.qqpro.lib.padding
import momoi.mod.qqpro.lib.size
import momoi.mod.qqpro.lib.text
import momoi.mod.qqpro.lib.textColor
import momoi.mod.qqpro.lib.textSize
import momoi.mod.qqpro.lib.vertical
import momoi.mod.qqpro.showDialog

class ForwardMsgView(context: Context) : LinearLayout(context) {
    fun init(contact: Contact, data: MultiMsgData) = apply {
        this.vertical()
            .padding(2.dp)
            .clickable {
                showDialog(DetailFragment(contact, data))
            }
            .content {
                add<TextView>()
                    .textSize(13f)
                    .textColor(0xFF_FFFFFF.toInt())
                    .text(data.title)
                add<TextView>()
                    .textSize(12f)
                    .textColor(0xFF_CCCCCC.toInt())
                    .text(data.previewLines.dropLast(1).joinToString(separator = "\n"))
                add<View>()
                    .size(width = FILL, height = 1)
                    .background(0xFF_AAAAAA.toInt())
                    .marginVertical(1.dp)
                add<TextView>()
                    .textSize(10f)
                    .textColor(0xFF_CCCCCC.toInt())
                    .text(data.summary)
            }
    }
}