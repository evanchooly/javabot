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
            getBot().postMessageToChannel(event, Sofia.jsrMissing());
            return true;
        } else {
            if (message.startsWith("jsr ")) {
                final String jsrString = message.substring("jsr ".length());

                try {
                    final int jsr = Integer.parseInt(jsrString);
                    String response = locator.findInformation(jsr);
                    if (response != null && !response.isEmpty()) {
                        getBot().postMessageToChannel(event, response);
                    } else {
                        getBot().postMessageToChannel(event, Sofia.jsrUnknown(jsrString));
                    }
                } catch (NumberFormatException nfe) {
                    getBot().postMessageToChannel(event, Sofia.jsrInvalid(jsrString));
                }
                return true;
            }
        }
        return false;
    }
}

