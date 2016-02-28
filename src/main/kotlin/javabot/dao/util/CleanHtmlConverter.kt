package javabot.dao.util

import java.util.StringJoiner

object CleanHtmlConverter {
    fun convert(message: String, converter: (String) -> String): String {
        var content = message.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
        if (content.contains("http://") || content.contains("https://")) {
            val joiner = StringJoiner(" ")
            content.split(" ").map({ token ->
                var value = token
                if (token.startsWith("http://") || token.startsWith("https://")) {
                    value = converter(token)
                }
                value
            }).forEach({ joiner.add(it) })

            content = joiner.toString()
        }
        return content
    }
}
