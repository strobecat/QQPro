package momoi.mod.qqpro.api

data class QQInfo(
    val nick: String,
    val avatarUrl: String
) {
    companion object {
        fun get(uin: String, charset: String, callback: (QQInfo)->Unit) {
            Http.get("https://users.qzone.qq.com/fcg-bin/cgi_get_portrait.fcg?uins=$uin", charset) {
                val split = it.split("\"")
                callback(QQInfo(
                    nick = split[5],
                    avatarUrl = split[3]
                ))
            }
        }
    }
}