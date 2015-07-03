package javabot.dao.util;

import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Stream;

public class CleanHtmlConverter {
    public static String convert(String message, Function<String, String> converter) {
        message = message.replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;");
        if (message.contains("http://") || message.contains("https://")) {
            StringJoiner joiner = new StringJoiner(" ");
            Stream.of(message.split(" ")).map(token -> {
                if (token.startsWith("http://") || token.startsWith("https://")) {
                    token = converter.apply(token);
                }
                return token;
            }).forEach(joiner::add);

            message = joiner.toString();
        }
        return message;
    }
}
