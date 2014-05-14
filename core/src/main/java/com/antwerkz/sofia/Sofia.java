package com.antwerkz.sofia;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.security.*;
import java.util.ResourceBundle.Control;

import org.slf4j.*;

public class Sofia {
    private static Map<Locale, ResourceBundle> messages = new HashMap<>();
        private static final Logger logger = LoggerFactory.getLogger(Sofia.class);

    private Sofia() {}

    private static ResourceBundle getBundle(Locale... localeList) {
        Locale locale = localeList.length == 0 ? Locale.getDefault() : localeList[0];
        ResourceBundle labels = loadBundle(locale);
        if(labels == null) {
            labels = loadBundle(Locale.ROOT);
        }
        return labels;
    }

    private static ResourceBundle loadBundle(Locale locale) {
        ResourceBundle bundle = messages.get(locale);
        if(bundle == null) {
            bundle = ResourceBundle.getBundle("sofia", locale );
            messages.put(locale, bundle);
        }
        return bundle;
    }

    private static String getMessageValue(String key, Locale... locale) {
        return (String) getBundle(locale).getObject(key);
    }

    public static String noNickservEntry(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("@info.no.nickserv.entry", locale), arg0);
    }

    public static void logNoNickservEntry(Object arg0, Locale... locale) {
        if(logger.isInfoEnabled()) {
            logger.info(noNickservEntry(arg0));
        }
    }
    public static String waitingForNickserv(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("@info.waiting.for.nickserv", locale), arg0);
    }

    public static void logWaitingForNickserv(Object arg0, Locale... locale) {
        if(logger.isInfoEnabled()) {
            logger.info(waitingForNickserv(arg0));
        }
    }
    public static String accountTooNew(Locale... locale) {
        return getMessageValue("account.too.new", locale);
    }

    public static String changingLockedFactoid(Object arg0, Object arg1, Locale... locale) {
        return MessageFormat.format(getMessageValue("changing.locked.factoid", locale), arg0, arg1);
    }

    public static String channelDeleted(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("channel.deleted", locale), arg0);
    }

    public static String channelUpdated(Locale... locale) {
        return getMessageValue("channel.updated", locale);
    }

    public static String email(Locale... locale) {
        return getMessageValue("email", locale);
    }

    public static String factoidChanged(Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Locale... locale) {
        return MessageFormat.format(getMessageValue("factoid.changed", locale), arg0, arg1, arg2, arg3, arg4);
    }

    public static String factoidExists(Object arg0, Object arg1, Locale... locale) {
        return MessageFormat.format(getMessageValue("factoid.exists", locale), arg0, arg1);
    }

    public static String factoidLocked(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("factoid.locked", locale), arg0);
    }

    public static String historyLength(Locale... locale) {
        return getMessageValue("history.length", locale);
    }

    public static String hostName(Locale... locale) {
        return getMessageValue("hostName", locale);
    }

    public static String invalidFactoidName(Locale... locale) {
        return getMessageValue("invalid.factoid.name", locale);
    }

    public static String invalidFactoidValue(Locale... locale) {
        return getMessageValue("invalid.factoid.value", locale);
    }

    public static String ircHistory(Locale... locale) {
        return getMessageValue("irc.history", locale);
    }

    public static String ircNick(Locale... locale) {
        return getMessageValue("irc.nick", locale);
    }

    public static String ircPassword(Locale... locale) {
        return getMessageValue("irc.password", locale);
    }

    public static String ircPort(Locale... locale) {
        return getMessageValue("irc.port", locale);
    }

    public static String ircServer(Locale... locale) {
        return getMessageValue("irc.server", locale);
    }

    public static String ircTrigger(Locale... locale) {
        return getMessageValue("irc.trigger", locale);
    }

    public static String ircName(Locale... locale) {
        return getMessageValue("ircName", locale);
    }

    public static String noDocumentation(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("no.documentation", locale), arg0);
    }

    public static String ok(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("ok", locale), arg0);
    }

    public static String registerNick(Object arg0, Object arg1, Object arg2, Locale... locale) {
        return MessageFormat.format(getMessageValue("register.nick", locale), arg0, arg1, arg2);
    }

    public static String throttleThreshold(Locale... locale) {
        return getMessageValue("throttle.threshold", locale);
    }

    public static String throttledUser(Locale... locale) {
        return getMessageValue("throttled.user", locale);
    }

    public static String tooManyResults(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("too.many.results", locale), arg0);
    }

    public static String unknownUser(Locale... locale) {
        return getMessageValue("unknown.user", locale);
    }

    public static String webUrl(Locale... locale) {
        return getMessageValue("web.url", locale);
    }


}
