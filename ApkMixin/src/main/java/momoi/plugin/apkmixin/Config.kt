package momoi.plugin.apkmixin

import org.gradle.api.Project

inline fun Project.apkMixin(block: Config.()->Unit) {
    Config.block()
}

object Config {
    var versionName = ""

    var targetApk: String? = null
}