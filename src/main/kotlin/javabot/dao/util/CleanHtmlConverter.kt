package javabot.dao.util

object CleanHtmlConverter {
    fun convert(message: String, converter: (String) -> String): String {
        var content = message.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")

        if (content.contains("http://") || content.contains("https://")) {
            content =
                content
                    .split(" ")
                    .map({ token ->
                        var value = token
                        if (token.startsWith("http://") || token.startsWith("https://")) {
                            value = converter(token)
                        }
                        value
                    })
                    .joinToString(" ")
        }
        return content
    }
}
