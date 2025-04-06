package momoi.mod.qqpro.api

import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import kotlin.concurrent.thread

object Http {
    inline fun get(
        url: String,
        charset: String = "UTF-8",
        crossinline callback: (String) -> Unit
    ) {
        thread {
            var connection: HttpURLConnection? = null
            try {
                connection = URL(url).openConnection() as HttpURLConnection
                with(connection) {
                    requestMethod = "GET"
                    connectTimeout = 5000
                    readTimeout = 5000
                }
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val charsetObj = Charset.forName(charset)
                    val response = connection.inputStream
                        .bufferedReader(charsetObj)
                        .use { it.readText() }
                    callback(response)
                } else {
                    runCatching {
                        callback("HTTP error: $responseCode")
                    }
                }
            } catch (e: Exception) {
                runCatching {
                    callback("Error: ${e.message ?: "Unknown error"}")
                }
            } finally {
                connection?.disconnect()
            }
        }
    }
}