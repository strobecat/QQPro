package momoi.mod.qqpro.hook.aio_cell

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.tencent.qqnt.kernel.nativeinterface.ArkElement
import loadPicUrl
import momoi.mod.qqpro.Settings
import momoi.mod.qqpro.hook.style.MyImageView
import momoi.mod.qqpro.lib.FILL
import momoi.mod.qqpro.lib.WRAP
import momoi.mod.qqpro.lib.background
import momoi.mod.qqpro.lib.clickable
import momoi.mod.qqpro.lib.content
import momoi.mod.qqpro.lib.dp
import momoi.mod.qqpro.lib.margin
import momoi.mod.qqpro.lib.marginVertical
import momoi.mod.qqpro.lib.padding
import momoi.mod.qqpro.lib.scaleType
import momoi.mod.qqpro.lib.size
import momoi.mod.qqpro.lib.text
import momoi.mod.qqpro.lib.textColor
import momoi.mod.qqpro.lib.textSize
import momoi.mod.qqpro.lib.vertical
import momoi.mod.qqpro.lib.width
import momoi.mod.qqpro.util.Json
import momoi.mod.qqpro.util.Utils

class CardMsgView(context: Context) : LinearLayout(context) {
    private lateinit var mTvTitle: TextView
    private lateinit var mTvTag: TextView
    private lateinit var mTvDesc: TextView
    private lateinit var mIvIcon: ImageView
    private lateinit var mIvPreview: ImageView

    init {
        vertical()
        padding(2.dp)
        content {
            mTvTitle = add<TextView>()
                .textSize(12f * Settings.chatScale.value)
                .textColor(0xFF_FFFFFF.toInt())
            mTvDesc = add<TextView>()
                .textSize(10f * Settings.chatScale.value)
                .textColor(0xFF_CCCCCC.toInt())
            mIvPreview = add<ImageView>()
                .width(FILL)
                .scaleType(ImageView.ScaleType.FIT_XY)
            add<View>()
                .size(width = FILL, height = 1)
                .background(0xFF_AAAAAA.toInt())
                .marginVertical(1.dp)
            add<LinearLayout>()
                .width(FILL)
                .content {
                    mIvIcon = add<MyImageView>()
                        .size(WRAP, FILL)
                        .scaleType(ImageView.ScaleType.CENTER_CROP)
                    mTvTag = add<TextView>()
                        .textSize(9f * Settings.chatScale.value)
                        .textColor(0xFF_FFFFFF.toInt())
                        .text(" ")
                        .weight(1f)
                        .margin(left = 2.dp)
                }
        }
    }

    fun loadData(ark: ArkElement) {
        try {
            val json = Json(ark.bytesData)
            val meta = json.json("meta")!!
            val data = meta.json(meta.keys.first())!!
            val desc = data.str("desc")
            val title = data.str("title")!!
            mTvTitle.text = title
            mTvDesc.text = desc
            val icon = data.str("icon")
            if (!icon.isNullOrEmpty()) {
                mIvIcon.loadPicUrl(icon)
            }
            val tag = data.str("tag")
            if (!tag.isNullOrEmpty()) {
                mTvTag.visibility = VISIBLE
                mTvTag.text = tag
                mIvIcon.loadPicUrl(data.str("tagIcon"))
            } else {
                mTvTag.visibility = GONE
            }
            val preview = data.str("preview")
            if (!preview.isNullOrEmpty() && json.str("view") != "news") {
                mIvPreview.loadPicUrl(preview)
            }
            clickable {
                (data.str("jumpUrl") ?: data.str("qqdocurl"))?.let {
                    Utils.openUrl(it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}