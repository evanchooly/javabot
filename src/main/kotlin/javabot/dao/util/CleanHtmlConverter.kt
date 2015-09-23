package javabot.dao.util

import java.util.StringJoiner
import java.util.function.Function
import java.util.stream.Stream

public object CleanHtmlConverter {
    public fun convert(message: String, converter: Function<String, String>): String {
        var message = message
        message = message.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;")
        if (message.contains("http://") || message.contains("https://")) {
            val joiner = StringJoiner(" ")
            Stream.of<String>(*message.split(" ")).map<String>({ token ->
                if (token.startsWith("http://") || token.startsWith("https://")) {
                    token = converter.apply(token)
                }
                token
            }).forEach(Consumer<String> { joiner.add(it) })

            message = joiner.toString()
        }
        return message
    }
}
