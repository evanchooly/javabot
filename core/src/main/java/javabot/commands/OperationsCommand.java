package javabot.commands;

import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.operations.BotOperation;

import java.util.stream.Collectors;

import static java.lang.String.join;

public abstract class OperationsCommand extends AdminCommand {
    protected void listCurrent(final Message event) {
        getBot().postMessageToChannel(event, Sofia.adminRunningOperations(event.getUser().getNick()));
        getBot().postMessageToChannel(event, join(",", getBot().getActiveOperations()
                                                               .stream()
                                                               .map(BotOperation::getName)
                                                               .collect(Collectors.toList())));
    }
}