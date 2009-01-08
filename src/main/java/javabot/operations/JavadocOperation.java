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

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    @Override
    @Transactional
    public List<Message> handleMessage(final BotEvent event) {
        final List<Message> messages = new ArrayList<Message>();
        final String message = event.getMessage();
        if(message.toLowerCase().startsWith("javadoc ")) {
            String[] urls;
            final String key = message.substring("javadoc ".length()).trim();
            if(key.startsWith("-list") || "".equals(key)) {
                final List<String> names = apiDao.listNames();
                final StringBuilder builder = new StringBuilder();
                for(final String name : names) {
                    builder.append(name).append(" ");
                }
                messages.add(new Message(event.getChannel(), event, event.getSender()
                    + ", I know of the following APIs: " + builder));
            } else {
                final int openIndex = key.indexOf('(');
                if(openIndex == -1) {
                    final Clazz[] classes = dao.getClass(key);
                    urls = new String[classes.length];
                    int index = 0;
                    for(final Clazz clazz : classes) {
                        urls[index++] = clazz.getDisplayUrl(clazz.getApi().getName(), dao);
                    }
                } else {
                    final int finalIndex = key.lastIndexOf('.', openIndex);
                    final int closeIndex = key.indexOf(')');
                    if(closeIndex == -1 || finalIndex == -1) {
                        urls = new String[0];
                    }
                    final String className = key.substring(0, finalIndex);
                    final String methodName = key.substring(finalIndex + 1, openIndex);
                    final String signatureTypes = key.substring(openIndex + 1, closeIndex);
                    final List<Method> list = dao.getMethods(className, methodName, signatureTypes);
                    urls = new String[list.size()];
                    int index = 0;
                    for(final Method method : list) {
                        urls[index++] = method.getDisplayUrl(method.getClazz().getApi().getName(), dao);
                    }
                }
                if(urls.length != 0) {
                    final StringBuilder urlMessage = new StringBuilder();
                    for(int index = 0;  index < urls.length; index++) {
                        urlMessage.append(index != 0 ? ", " : "")
                            .append(urls[index]);
                    }
                    if(urls.length > RESULT_LIMIT) {
                        messages.add(new Message(event.getChannel(), event, event.getSender() + ", too many results found."
                            + "  Please see your private messages for results"));
                        messages.add(new Message(event.getSender(), event,
                            event.getSender() + ", please see " + urlMessage));
                    } else {
                        messages.add(new Message(event.getChannel(), event, urlMessage.toString()));
                    }
                }
                if(messages.isEmpty()) {
                    messages
                        .add(new Message(event.getChannel(), event,
                            "I don't know of any documentation for " + key));
                }
            }
        }
        return messages;
    }
}