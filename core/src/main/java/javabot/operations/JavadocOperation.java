package javabot.operations;

import com.antwerkz.maven.SPI;
import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.dao.ApiDao;
import javabot.dao.JavadocClassDao;
import javabot.javadoc.JavadocApi;
import javabot.javadoc.JavadocField;
import net.swisstech.bitly.BitlyClient;
import org.pircbotx.Channel;
import org.pircbotx.User;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SPI(BotOperation.class)
public class JavadocOperation extends BotOperation {
    @Inject
    private ApiDao apiDao;

    @Inject
    private JavadocClassDao dao;

    @Inject
    private BitlyClient client;

    private static final int RESULT_LIMIT = 5;

    @Override
    public boolean handleMessage(final Message event) {
        final String message = event.getValue();
        boolean handled = false;
        if (message.toLowerCase().startsWith("javadoc")) {
            String key = message.substring("javadoc".length()).trim();
            if (key.startsWith("-list") || key.isEmpty()) {
                displayApiList(event);
                handled = true;
            } else {
                JavadocApi api;
                if (key.startsWith("-")) {
                    if (key.contains(" ")) {
                        api = apiDao.find(key.substring(1, key.indexOf(" ")));
                        key = key.substring(key.indexOf(" ") + 1).trim();
                        handled = buildResponse(event, api, key);
                    } else {
                        displayApiList(event);
                        handled = true;
                    }
                } else {
                    buildResponse(event, null, key);
                    handled = true;
                }
            }
        }
        return handled;
    }

    private boolean buildResponse(Message event, JavadocApi api, String key) {
        final List<String> urls = handle(api, key);
        if (!urls.isEmpty()) {
            Channel channel = event.getChannel();
            User user = event.getUser();

            String nick = user.getNick();
            StringBuilder urlMessage = new StringBuilder(nick + ": ");
            if (urls.size() > RESULT_LIMIT) {
                getBot().postMessage(event.getChannel(), user, Sofia.tooManyResults(nick), event.isTell());
                channel = null;
            }
            urlMessage = buildResponse(event, urls, urlMessage, channel);
            getBot().postMessage(channel, user, urlMessage.toString(), event.isTell());
        } else {
            getBot().postMessage(event.getChannel(), event.getUser(), Sofia.noDocumentation(key), event.isTell());
        }

        return true;
    }

    private StringBuilder buildResponse(Message event, List<String> urls,
                                        StringBuilder urlMessage, Channel channel) {
        for (int index = 0; index < urls.size(); index++) {
            if ((urlMessage + urls.get(index)).length() > 400) {
                getBot().postMessage(channel, event.getUser(), urlMessage.toString(), event.isTell());
                urlMessage = new StringBuilder();
            }
            urlMessage
                .append(index == 0 ? "" : "; ")
                .append(urls.get(index));
        }
        return urlMessage;
    }

    public List<String> handle(final JavadocApi api, final String key) {
        final List<String> urls = new ArrayList<>();
        final int openIndex = key.indexOf('(');
        if (openIndex == -1) {
            parseFieldOrClassRequest(urls, api, key);
        } else {
            parseMethodRequest(urls, api, key, openIndex);
        }
        return urls;
    }

    private void parseFieldOrClassRequest(final List<String> urls, JavadocApi api, final String key) {
        final int finalIndex = key.lastIndexOf('.');
        if (finalIndex == -1) {
            findClasses(api, urls, key);
        } else {
            final String className = key.substring(0, finalIndex);
            final String fieldName = key.substring(finalIndex + 1);
            if (Character.isUpperCase(fieldName.charAt(0)) && !fieldName.toUpperCase().equals(fieldName)) {
                findClasses(api, urls, key);
            } else {
                final List<JavadocField> list = dao.getField(api, className, fieldName);
                urls.addAll(list.stream()
                                .map(field -> field.getDisplayUrl(field.toString(), apiDao, client))
                                .collect(Collectors.toList()));
            }
        }
    }

    private void findClasses(JavadocApi api, final List<String> urls, final String key) {
        urls.addAll(dao.getClass(api, key)
                       .stream()
                       .map(javadocClass -> javadocClass.getDisplayUrl(javadocClass.toString(), apiDao, client))
                       .collect(Collectors.toList()));
    }

    private void parseMethodRequest(final List<String> urls, JavadocApi api, final String key, final int openIndex) {
        final int closeIndex = key.indexOf(')');
        if (closeIndex != -1) {
            int finalIndex = key.lastIndexOf('.', openIndex);
            String methodName;
            String className;
            if (finalIndex == -1) {
                methodName = key.substring(0, openIndex);
                className = methodName;
            } else {
                methodName = key.substring(finalIndex + 1, openIndex);
                className = key.substring(0, finalIndex);
            }
            final String signatureTypes = key.substring(openIndex + 1, closeIndex);
            final List<String> list = new ArrayList<>();

            //
            list.addAll(dao.getMethods(api, className, methodName, signatureTypes)
                           .stream()
                           .map(method -> method.getDisplayUrl(method.toString(), apiDao, client))
                           .collect(Collectors.toList()));
            //

            if (list.isEmpty()) {
                className = methodName;
                list.addAll(dao.getMethods(api, className, methodName, signatureTypes)
                               .stream()
                               .map(method -> method.getDisplayUrl(method.toString(), apiDao, client))
                               .collect(Collectors.toList()));
            }

            urls.addAll(list);
        }
    }

    private void displayApiList(final Message event) {
        final StringBuilder builder = new StringBuilder();
        for (final JavadocApi api : apiDao.findAll()) {
            if (builder.length() != 0) {
                builder.append("; ");
            }
            builder.append(api.getName());
        }
        getBot().postMessage(event.getChannel(), event.getUser(), Sofia.javadocApiList(event.getUser().getNick(), builder),
                             event.isTell());
    }
}