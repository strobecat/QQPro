package momoi.mod.qqpro

object Settings {
    val sp = Utils.application.getSharedPreferences("qqpro", 0)
    const val VERSION_CODE = 1
    val scale = sp.getFloat("scale", 0.9f)
}