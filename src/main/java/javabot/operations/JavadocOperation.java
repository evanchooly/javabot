package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.dao.ClazzDao;
import javabot.javadoc.Clazz;
import javabot.javadoc.Method;

public class JavadocOperation implements BotOperation {
    private ClazzDao dao;
    private static final String BASE_URL = "http://java.sun.com/javase/6/docs/api/";

    public JavadocOperation(ClazzDao clazzDao) {
        dao = clazzDao;
    }

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        if(message.toLowerCase().startsWith("javadoc ")) {
            String[] urls;
            String key = message.substring("javadoc ".length()).trim();
            int openIndex = key.indexOf('(');
            if(openIndex == -1) {
                Clazz[] classes = dao.getClass(key);
                urls = new String[classes.length];
                int index = 0;
                for(Clazz clazz : classes) {
                    urls[index++] = clazz.getClassUrl(BASE_URL);
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
                List<Method> list = dao.getMethods(className, methodName, signatureTypes, BASE_URL);
                urls = new String[list.size()];
                int index = 0;
                for(Method method : list) {
                    urls[index++] = method.getMethodUrl(BASE_URL);
                }
            }
//            String[] urls = javadocParser.javadoc(key);
            String sender = event.getSender();
            for(String url : urls) {
                messages.add(new Message(event.getChannel(), sender + ", please see " + url, false));
            }
            if(messages.isEmpty()) {
                messages.add(new Message(event.getChannel(), "I don't know of any documentation for " + key, false));
            }
        }
        return messages;
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}