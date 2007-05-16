package javabot;

import javabot.operations.JavadocOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IndependentJavadocViewer {
    private static Log log = LogFactory.getLog(IndependentJavadocViewer.class);

    public static void main(String[] args) {
        JavadocOperation operation = new JavadocOperation
            ("docref.xml", "http://java.sun.com/j2se/1.5.0/docs/api/");
        Message message = (Message)operation
            .handleMessage(new BotEvent("cmdline", "cmdline", null, null, "javadoc "
            + args[0])).get(0);
        log.debug(message.getMessage().split(": ")[1]);
    }
}
