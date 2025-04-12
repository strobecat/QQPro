package momoi.mod.qqpro

object Settings {
    val sp = Utils.application.getSharedPreferences("qqpro", 0)
    const val VERSION_CODE = 3
    val scale get() = sp.getFloat("scale", 0.9f)
    val chatScale get() = sp.getFloat("chatScale", 0.93f)
    val enableSmoothScroll = sp.getBoolean("enableSmoothScroll", false)
}