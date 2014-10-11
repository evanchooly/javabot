package javabot.operations;

import com.antwerkz.maven.SPI;
import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.dao.FactoidDao;
import javabot.dao.LogsDao;
import javabot.model.Factoid;
import javabot.model.Logs.Type;
import org.pircbotx.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.time.LocalDateTime;

@SPI(StandardOperation.class)
public class AddFactoidOperation extends StandardOperation {
    public static final Logger log = LoggerFactory.getLogger(AddFactoidOperation.class);

    @Inject
    private FactoidDao factoidDao;

    @Inject
    private LogsDao logDao;

    @Override
    public boolean handleMessage(final Message event) {
        String message = event.getValue();
        final Channel channel = event.getChannel();
        boolean handled = false;
        if (message.startsWith("no ") || message.startsWith("no, ")) {
            if (!channel.getName().startsWith("#") && !isAdminUser(event.getUser())) {
                getBot().postMessage(channel, event.getUser(), Sofia.privmsgChange(), event.isTell());
                handled = true;
            } else {
                message = message.substring(2);
                if (message.startsWith(",")) {
                    message = message.substring(1);
                }
                message = message.trim();
                handled = updateFactoid(event, message);
            }
        }
        if (!handled) {
            handled = addFactoid(event, message);
        }
        return handled;
    }

    private boolean updateFactoid(final Message event, final String message) {
        final int is = message.indexOf(" is ");
        boolean handled = false;
        if (is != -1) {
            final Channel channel = event.getChannel();
            if (!channel.getName().startsWith("#") && !isAdminUser(event.getUser())) {
                getBot().postMessage(channel, event.getUser(), Sofia.privmsgChange(), event.isTell());
            } else {
                String key = message.substring(0, is);
                key = key.replaceAll("^\\s+", "");
                final Factoid factoid = factoidDao.getFactoid(key);
                boolean admin = isAdminUser(event.getUser());
                if (factoid != null) {
                    if (factoid.getLocked()) {
                        if (admin) {
                            handled = updateExistingFactoid(event, message, factoid, is, key);
                        } else {
                            logDao.logMessage(Type.MESSAGE, event.getChannel(), event.getUser(),
                                              Sofia.changingLockedFactoid(event.getUser(), key));
                            getBot().postMessage(event.getChannel(), event.getUser(), Sofia.factoidLocked(event.getUser()),
                                                 event.isTell());
                            handled = true;
                        }
                    } else {
                        handled = updateExistingFactoid(event, message, factoid, is, key);
                    }
                }
            }
        }
        return handled;
    }

    private boolean addFactoid(final Message event, final String message) {
        boolean handled = false;
        if (message.toLowerCase().contains(" is ")) {
            if (!event.getChannel().getName().startsWith("#") && !isAdminUser(event.getUser())) {
                getBot().postMessage(event.getChannel(), event.getUser(), Sofia.privmsgChange(), event.isTell());
                handled = true;
            } else {
                String key = message.substring(0, message.indexOf(" is "));
                key = key.toLowerCase();
                while (key.endsWith(".") || key.endsWith("?") || key.endsWith("!")) {
                    key = key.substring(0, key.length() - 1);
                }
                final int index = message.indexOf(" is ");
                String value = null;
                if (index != -1) {
                    value = message.substring(index + 4, message.length());
                }
                if (key.trim().isEmpty()) {
                    getBot().postMessage(event.getChannel(), event.getUser(), Sofia.factoidInvalidName(), event.isTell());
                    handled = true;
                } else if (value == null || value.trim().isEmpty()) {
                    getBot().postMessage(event.getChannel(), event.getUser(), Sofia.factoidInvalidValue(), event.isTell());
                    handled = true;
                } else if (factoidDao.hasFactoid(key)) {
                    getBot().postMessage(event.getChannel(), event.getUser(), Sofia.factoidExists(key, event.getUser().getNick()),
                                         event.isTell());
                    handled = true;
                } else {
                    if (value.startsWith("<see>")) {
                        value = value.toLowerCase();
                    }
                    factoidDao.addFactoid(event.getUser().getNick(), key, value);
                    getBot().postMessage(event.getChannel(), event.getUser(), Sofia.ok(event.getUser().getNick()), event.isTell());
                    handled = true;
                }
            }
        }
        return handled;
    }

    private boolean updateExistingFactoid(final Message event, final String message, final Factoid factoid,
                                          final int is, final String key) {
        final String newValue = message.substring(is + 4);
        logDao.logMessage(Type.MESSAGE, event.getChannel(), event.getUser(),
                          Sofia.factoidChanged(event.getUser(), key, factoid.getValue(), newValue, event.getChannel()));
        factoid.setValue(newValue);
        factoid.setUpdated(LocalDateTime.now());
        factoid.setUserName(event.getUser().getNick());
        factoidDao.save(factoid);
        getBot().postMessage(event.getChannel(), event.getUser(), Sofia.ok(event.getUser()), event.isTell());
        return true;
    }
}
