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

public class JavadocOperation extends BotOperation {
    @Autowired
    private ApiDao apiDao;
    private ClazzDao dao;

    public JavadocOperation(Javabot bot, ApiDao aDao, ClazzDao clazzDao) {
        super(bot);
//        apiDao = aDao;
        dao = clazzDao;
    }

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    @Override
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        if(message.toLowerCase().startsWith("javadoc ")) {
            String[] urls;
            String key = message.substring("javadoc ".length()).trim();
            if(key.startsWith("-list") || "".equals(key)) {
                List<String> names = apiDao.listNames();
                StringBuilder builder = new StringBuilder();
                for(String name : names) {
                    builder.append(name).append(" ");
                }
                messages.add(new Message(event.getChannel(), event, event.getSender()
                    + ", I know of the following APIs: " + builder));
            } else {
                int openIndex = key.indexOf('(');
                if(openIndex == -1) {
                    Clazz[] classes = dao.getClass(key);
                    urls = new String[classes.length];
                    int index = 0;
                    for(Clazz clazz : classes) {
                        urls[index++] = clazz.getClassUrl();
                    }
                } else {
                    int finalIndex = key.lastIndexOf('.', openIndex);
                    int closeIndex = key.indexOf(')');
                    if(closeIndex == -1 || finalIndex == -1) {
                        urls = new String[0];
                    }
                    String className = key.substring(0, finalIndex);
                    String methodName = key.substring(finalIndex + 1, openIndex);
                    String signatureTypes = key.substring(openIndex + 1, closeIndex);
                    List<Method> list = dao.getMethods(className, methodName, signatureTypes);
                    urls = new String[list.size()];
                    int index = 0;
                    for(Method method : list) {
                        urls[index++] = method.getMethodUrl();
                    }
                }
                if(urls.length != 0) {
                    StringBuilder urlMessage = new StringBuilder();
                    for(int index = 0;  index < urls.length; index++) {
                        urlMessage.append(index != 0 ? ", " : "")
                            .append(urls[index]);
                    }
                    if(urls.length > 3) {
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

    @Override
    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}