package momoi.mod.qqpro.hook.aio_cell

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.tencent.qqnt.kernel.nativeinterface.Contact
import momoi.mod.qqpro.lib.FILL
import momoi.mod.qqpro.lib.background
import momoi.mod.qqpro.lib.clickable
import momoi.mod.qqpro.lib.content
import momoi.mod.qqpro.lib.dp
import momoi.mod.qqpro.lib.marginVertical
import momoi.mod.qqpro.lib.padding
import momoi.mod.qqpro.lib.size
import momoi.mod.qqpro.lib.textColor
import momoi.mod.qqpro.lib.textSize
import momoi.mod.qqpro.lib.vertical
import momoi.mod.qqpro.showDialog

class ForwardMsgView(context: Context) : LinearLayout(context) {
    fun loadData(contact: Contact, data: ForwardMsgData) {
        clickable {
            showDialog(DetailFragment(contact, data))
        }
        mTvTitle.text = data.title
        mTvPreview.text = data.previewLines.dropLast(1).joinToString(separator = "\n")
        mTvSummary.text = data.summary
    }
    private lateinit var mTvTitle: TextView
    private lateinit var mTvSummary: TextView
    private lateinit var mTvPreview: TextView
    init {
        this.vertical()
            .padding(2.dp)
            .content {
                mTvTitle = add<TextView>()
                    .textSize(13f)
                    .textColor(0xFF_FFFFFF.toInt())
                mTvPreview = add<TextView>()
                    .textSize(12f)
                    .textColor(0xFF_CCCCCC.toInt())
                add<View>()
                    .size(width = FILL, height = 1)
                    .background(0xFF_AAAAAA.toInt())
                    .marginVertical(1.dp)
                mTvSummary = add<TextView>()
                    .textSize(10f)
                    .textColor(0xFF_CCCCCC.toInt())
            }
    }
}