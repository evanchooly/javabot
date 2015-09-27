package javabot.dao.util

import java.util.StringJoiner

public object CleanHtmlConverter {
    public fun convert(message: String, converter: (String) -> String): String {
        var content = message.replace("&".toRegex(), "&amp;").replace("<".toRegex(), "&lt;").replace(">".toRegex(), "&gt;")
        if (content.contains("http://") || content.contains("https://")) {
            val joiner = StringJoiner(" ")
            content.split(" ").map({ token ->
                if (token.startsWith("http://") || token.startsWith("https://")) {
                    converter(token)
                }
                token
            }).forEach({ joiner.add(it) })

            content = joiner.toString()
        }
        return content
    }
}
