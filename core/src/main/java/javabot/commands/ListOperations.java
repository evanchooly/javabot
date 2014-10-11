package javabot.commands;

import com.antwerkz.maven.SPI;
import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.operations.BotOperation;
import org.apache.commons.lang.StringUtils;

@SPI({AdminCommand.class})
public class ListOperations extends OperationsCommand {
    @Override
    public void execute(final Message event) {
        getBot().postMessage(event.getChannel(), event.getUser(),
                             Sofia.adminKnownOperations(event.getUser().getNick(), StringUtils.join(BotOperation.list().iterator(), ",")),
                             event.isTell());

        listCurrent(event);
        getBot().postMessage(event.getChannel(), event.getUser(), Sofia.adminOperationInstructions(), event.isTell());
    }
}