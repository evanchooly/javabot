package javabot.operations;

import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.operations.locator.JCPJSRLocator;
import org.pircbotx.Channel;

import javax.inject.Inject;

public class JSROperation extends BotOperation {
    @Inject
    JCPJSRLocator locator;

    @Override
    public boolean handleMessage(final Message event) {
        final String message = event.getValue().toLowerCase();
        final Channel channel = event.getChannel();
        if ("jsr".equals(message)) {
            getBot().postMessage(channel, event.getUser(), Sofia.jsrMissing(), event.isTell());
            return true;
        } else {
            if (message.startsWith("jsr ")) {
                final String jsrString = message.substring("jsr ".length());

                try {
                    final int jsr = Integer.parseInt(jsrString);
                    String response = locator.findInformation(jsr);
                    if (response != null && !response.isEmpty()) {
                        getBot().postMessage(channel, event.getUser(), response, event.isTell());
                    } else {
                        getBot().postMessage(channel, event.getUser(), Sofia.jsrUnknown(jsrString), event.isTell());
                    }
                } catch (NumberFormatException nfe) {
                    getBot().postMessage(channel, event.getUser(), Sofia.jsrInvalid(jsrString), event.isTell());
                }
                return true;
            }
        }
        return false;
    }
}

