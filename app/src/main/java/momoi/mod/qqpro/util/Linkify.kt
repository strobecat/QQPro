package momoi.mod.qqpro.util

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView
import androidx.core.net.toUri
import java.util.regex.Pattern

fun TextView.linkify() {
    val spannable = SpannableStringBuilder(text)
    val existingSpans = spannable.getSpans(0, spannable.length, URLSpan::class.java)
    existingSpans.forEach { spannable.removeSpan(it) }
    val urlRegex = Pattern.compile("(http(s)?://)\\w+\\S+(\\.[^\\s\\u4e00-\\u9fa5\\u3002\\uff1f\\uff01\\uff0c\\u3001\\uff1b\\uff1a\\u201c\\u201d\\u2018\\u2019\\uff08\\uff09\\u300a\\u300b\\u3008\\u3009\\u3010\\u3011\\u300e\\u300f\\u300c\\u300d\\uff43\\uff44\\u3014\\u3015\\u2026\\u2014\\uff5e\\uff4f\\uffe5]+)+")
    val matcher = urlRegex.matcher(spannable)
    val links = mutableListOf<Pair<Int, Int>>()
    while (matcher.find()) {
        links.add(matcher.start() to matcher.end())
    }
    links.reversed().forEach { (start, end) ->
        val url = spannable.substring(start, end)

        spannable.setSpan(
            object : ClickableSpan() {
                override fun onClick(widget: View) {
                    context?.startActivity(
                        android.content.Intent(
                            android.content.Intent.ACTION_VIEW,
                            url.toUri()
                        )
                    )
                }
            },
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    text = spannable
    movementMethod = LinkMovementMethod.getInstance()
}