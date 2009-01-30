package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.ApiDao;
import javabot.dao.ClazzDao;
import javabot.javadoc.Clazz;
import javabot.javadoc.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class JavadocOperation extends BotOperation {
    @Autowired
    private ApiDao apiDao;
    @Autowired
    private ClazzDao dao;
    private static final int RESULT_LIMIT = 8;

    public JavadocOperation(final Javabot bot) {
        super(bot);
    }

    @Override
    @Transactional
    public boolean handleMessage(final BotEvent event) {
        final String message = event.getMessage();
        boolean handled = false;
        if(message.toLowerCase().startsWith("javadoc ")) {
            final List<String> urls = new ArrayList<String>();
            final String key = message.substring("javadoc ".length()).trim();
            if(key.startsWith("-list") || "".equals(key)) {
                final List<String> names = apiDao.listNames();
                final StringBuilder builder = new StringBuilder();
                for(final String name : names) {
                    builder.append(name).append(" ");
                }
                getBot().postMessage(new Message(event.getChannel(), event, event.getSender()
                    + ", I know of the following APIs: " + builder));
            } else {
                final int openIndex = key.indexOf('(');
                if(openIndex == -1) {
                    for(final Clazz clazz : dao.getClass(key)) {
                        urls.add(clazz.getDisplayUrl(clazz.getApi().getName(), dao));
                    }
                } else {
                    final int finalIndex = key.lastIndexOf('.', openIndex);
                    final int closeIndex = key.indexOf(')');
                    
                    final String className = key.substring(0, finalIndex);
                    final String methodName = key.substring(finalIndex + 1, openIndex);
                    final String signatureTypes = key.substring(openIndex + 1, closeIndex);
                    final List<Method> list = dao.getMethods(className, methodName, signatureTypes);
                    for(final Method method : list) {
                        urls.add(method.getDisplayUrl(method.getClazz().getApi().getName(), dao));
                    }
                }
                if(!urls.isEmpty()) {
                    final StringBuilder urlMessage = new StringBuilder();
                    for(int index = 0;  index < urls.size(); index++) {
                        urlMessage.append(index != 0 ? ", " : "")
                            .append(urls.get(index));
                    }
                    if(urls.size() > RESULT_LIMIT) {
                        getBot().postMessage(new Message(event.getChannel(), event,
                            String.format("%s, too many results found.  Please see your private messages for results",
                                event.getSender())));
                        getBot().postMessage(new Message(event.getSender(), event,
                            String.format("%s, please see %s", event.getSender(), urlMessage)));
                    } else {
                        getBot().postMessage(new Message(event.getChannel(), event, urlMessage.toString()));
                    }
                }
                if(urls.isEmpty()) {
                    getBot().postMessage(new Message(event.getChannel(), event,
                            "I don't know of any documentation for " + key));
                }
            }
            handled = true;
        }
        return handled;
    }
}