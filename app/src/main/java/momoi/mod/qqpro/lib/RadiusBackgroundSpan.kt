package momoi.mod.qqpro.lib

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.graphics.RectF
import android.text.TextPaint
import android.text.style.ReplacementSpan
import kotlin.math.max


class RadiusBackgroundSpan : ReplacementSpan {
    private val margin: Int
    private val radius: Int
    private val textColor: Int
    private val bgColor: Int
    private val maxHeight: Int

    constructor(
        margin: Int = 0,
        radius: Int = 12.dp,
        textColor: Int,
        bgColor: Int,
        maxHeight: Int = Int.MAX_VALUE
    ) {
        this.margin = margin
        this.radius = radius
        this.textColor = textColor
        this.bgColor = bgColor
        this.maxHeight = maxHeight
    }

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: FontMetricsInt?
    ): Int {
        val newPaint: Paint = getCustomTextPaint(paint)
        return newPaint.measureText(text, start, end).toInt() + margin * 2 + 6.dp
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        var top = top
        val newPaint: Paint = getCustomTextPaint(paint)

        val textWidth: Int =
            newPaint.measureText(text, start, end).toInt() + 6.dp

        val rect = RectF()
        top = if (bottom - top > maxHeight) max((bottom - maxHeight).toDouble(), 0.0)
            .toInt() else top
        rect.top = (top + margin).toFloat()
        rect.bottom = (bottom - margin).toFloat()
        rect.left = (x + margin).toInt().toFloat()
        rect.right = rect.left + textWidth + margin
        paint.setColor(bgColor)
        canvas.drawRoundRect(rect, radius.toFloat(), radius.toFloat(), paint)

        newPaint.setColor(textColor)
        val fontMetrics = newPaint.getFontMetrics()
        val offsetX = ((rect.right - rect.left - textWidth) / 2).toInt() + margin
        val offsetY =
            ((y + fontMetrics.ascent + y + fontMetrics.descent) / 2 - (top + bottom) / 2).toInt()
        canvas.drawText(
            text,
            start,
            end,
            x + offsetX + 3.dp,
            (y - offsetY).toFloat(),
            newPaint
        )
    }

    private fun getCustomTextPaint(srcPaint: Paint?): TextPaint {
        return TextPaint(srcPaint)
    }
}
