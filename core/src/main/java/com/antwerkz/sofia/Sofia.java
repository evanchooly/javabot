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

    public static String adminAdded(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("admin.added", locale), arg0);
    }

    public static String adminAlready(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("admin.already", locale), arg0);
    }

    public static String adminBadChannelName(Locale... locale) {
        return getMessageValue("admin.badChannelName", locale);
    }

    public static String adminDoneRemovingOldJavadoc(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("admin.done.removing.old.javadoc", locale), arg0);
    }

    public static String adminJoinedChannel(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("admin.joinedChannel", locale), arg0);
    }

    public static String adminJoiningChannel(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("admin.joiningChannel", locale), arg0);
    }

    public static String adminJoiningLoggedChannel(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("admin.joiningLoggedChannel", locale), arg0);
    }

    public static String adminKnownCommands(Object arg0, Object arg1, Locale... locale) {
        return MessageFormat.format(getMessageValue("admin.known.commands", locale), arg0, arg1);
    }

    public static String adminKnownOperations(Object arg0, Object arg1, Locale... locale) {
        return MessageFormat.format(getMessageValue("admin.known.operations", locale), arg0, arg1);
    }

    public static String adminListChannelsPreamble(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("admin.list.channels.preamble", locale), arg0);
    }

    public static String adminOperationDisabled(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("admin.operation.disabled", locale), arg0);
    }

    public static String adminOperationEnabled(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("admin.operation.enabled", locale), arg0);
    }

    public static String adminOperationInstructions(Locale... locale) {
        return getMessageValue("admin.operation.instructions", locale);
    }

    public static String adminOperationNotDisabled(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("admin.operation.not.disabled", locale), arg0);
    }

    public static String adminOperationNotEnabled(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("admin.operation.not.enabled", locale), arg0);
    }

    public static String adminParseFailure(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("admin.parseFailure", locale), arg0);
    }

    public static String adminRemovingOldJavadoc(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("admin.removing.old.javadoc", locale), arg0);
    }

    public static String adminRunningOperations(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("admin.running.operations", locale), arg0);
    }

    public static String alreadyShunned(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("alreadyShunned", locale), arg0);
    }

    public static String apiLocation(Object arg0, Object arg1, Locale... locale) {
        return MessageFormat.format(getMessageValue("api.location", locale), arg0, arg1);
    }

    public static String botAolbonics(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("bot.aolbonics", locale), arg0);
    }

    public static String botIgnoring(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("bot.ignoring", locale), arg0);
    }

    public static String botSelfTalk(Locale... locale) {
        return getMessageValue("bot.selfTalk", locale);
    }

    public static String botStats(Number arg0, Number arg1, Number arg2, Locale... locale) {
        return MessageFormat.format(getMessageValue("bot.stats", locale), arg0, arg1, arg2);
    }

    public static String botUnixCommand(Object arg0, Object arg1, Locale... locale) {
        return MessageFormat.format(getMessageValue("bot.unixCommand", locale), arg0, arg1);
    }

    public static String botVersion(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("bot.version", locale), arg0);
    }

    public static String changingLockedFactoid(Object arg0, Object arg1, Locale... locale) {
        return MessageFormat.format(getMessageValue("changing.locked.factoid", locale), arg0, arg1);
    }

    public static String channelDeleted(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("channel.deleted", locale), arg0);
    }

    public static String channelUnknown(Object arg0, Object arg1, Locale... locale) {
        return MessageFormat.format(getMessageValue("channel.unknown", locale), arg0, arg1);
    }

    public static String channelUpdated(Locale... locale) {
        return getMessageValue("channel.updated", locale);
    }

    public static String configurationSetProperty(Object arg0, Object arg1, Locale... locale) {
        return MessageFormat.format(getMessageValue("configuration.set.property", locale), arg0, arg1);
    }

    public static String configurationUnknownProperty(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("configuration.unknown.property", locale), arg0);
    }

    public static String daysUntil(Object arg0, Number arg1, java.util.Date arg2, Locale... locale) {
        return MessageFormat.format(getMessageValue("days.until", locale), arg0, arg1, arg2);
    }

    public static String email(Locale... locale) {
        return getMessageValue("email", locale);
    }

    public static String factoidAdded(Object arg0, Object arg1, Object arg2, Locale... locale) {
        return MessageFormat.format(getMessageValue("factoid.added", locale), arg0, arg1, arg2);
    }

    public static String factoidChanged(Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Locale... locale) {
        return MessageFormat.format(getMessageValue("factoid.changed", locale), arg0, arg1, arg2, arg3, arg4);
    }

    public static String factoidDeleteLocked(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("factoid.delete.locked", locale), arg0);
    }

    public static String factoidDeleteUnknown(Object arg0, Object arg1, Locale... locale) {
        return MessageFormat.format(getMessageValue("factoid.delete.unknown", locale), arg0, arg1);
    }

    public static String factoidExists(Object arg0, Object arg1, Locale... locale) {
        return MessageFormat.format(getMessageValue("factoid.exists", locale), arg0, arg1);
    }

    public static String factoidForgotten(Object arg0, Object arg1, Locale... locale) {
        return MessageFormat.format(getMessageValue("factoid.forgotten", locale), arg0, arg1);
    }

    public static String factoidInfo(Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Locale... locale) {
        return MessageFormat.format(getMessageValue("factoid.info", locale), arg0, arg1, arg2, arg3, arg4);
    }

    public static String factoidInvalidName(Locale... locale) {
        return getMessageValue("factoid.invalid.name", locale);
    }

    public static String factoidInvalidValue(Locale... locale) {
        return getMessageValue("factoid.invalid.value", locale);
    }

    public static String factoidLocked(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("factoid.locked", locale), arg0);
    }

    public static String factoidLoop(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("factoid.loop", locale), arg0);
    }

    public static String factoidTellSyntax(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("factoid.tell.syntax", locale), arg0);
    }

    public static String factoidUnknown(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("factoid.unknown", locale), arg0);
    }

    public static String historyLength(Locale... locale) {
        return getMessageValue("history.length", locale);
    }

    public static String hostName(Locale... locale) {
        return getMessageValue("hostName", locale);
    }

    public static String invalidDateFormat(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("invalid.date.format", locale), arg0);
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

    public static String javadocApiList(Object arg0, Object arg1, Locale... locale) {
        return MessageFormat.format(getMessageValue("javadoc.api.list", locale), arg0, arg1);
    }

    public static String jsrInvalid(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("jsr.invalid", locale), arg0);
    }

    public static String jsrMissing(Locale... locale) {
        return getMessageValue("jsr.missing", locale);
    }

    public static String jsrUnknown(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("jsr.unknown", locale), arg0);
    }

    public static String karmaChanged(Object arg0, Object arg1, Number arg2, Locale... locale) {
        return MessageFormat.format(getMessageValue("karma.changed", locale), arg0, arg1, arg2);
    }

    public static String karmaOthersNone(Object arg0, Object arg1, Locale... locale) {
        return MessageFormat.format(getMessageValue("karma.others.none", locale), arg0, arg1);
    }

    public static String karmaOthersValue(Object arg0, Number arg1, Object arg2, Locale... locale) {
        return MessageFormat.format(getMessageValue("karma.others.value", locale), arg0, arg1, arg2);
    }

    public static String karmaOwnIncrement(Locale... locale) {
        return getMessageValue("karma.own.increment", locale);
    }

    public static String karmaOwnNone(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("karma.own.none", locale), arg0);
    }

    public static String karmaOwnValue(Object arg0, Number arg1, Locale... locale) {
        return MessageFormat.format(getMessageValue("karma.own.value", locale), arg0, arg1);
    }

    public static String leaveChannel(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("leave.channel", locale), arg0);
    }

    public static String leavePrivmsg(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("leave.privmsg", locale), arg0);
    }

    public static String logsEntry(Object arg0, Object arg1, Object arg2, Locale... locale) {
        return MessageFormat.format(getMessageValue("logs.entry", locale), arg0, arg1, arg2);
    }

    public static String logsNone(Locale... locale) {
        return getMessageValue("logs.none", locale);
    }

    public static String logsNoneForNick(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("logs.noneForNick", locale), arg0);
    }

    public static String noDocumentation(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("no.documentation", locale), arg0);
    }

    public static String notAdmin(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("not.admin", locale), arg0);
    }

    public static String notAllowed(Locale... locale) {
        return getMessageValue("not.allowed", locale);
    }

    public static String ok(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("ok", locale), arg0);
    }

    public static String privmsgChange(Locale... locale) {
        return getMessageValue("privmsg.change", locale);
    }

    public static String registerNick(Object arg0, Object arg1, Object arg2, Locale... locale) {
        return MessageFormat.format(getMessageValue("register.nick", locale), arg0, arg1, arg2);
    }

    public static String seenLast(Object arg0, Object arg1, Object arg2, Object arg3, Locale... locale) {
        return MessageFormat.format(getMessageValue("seen.last", locale), arg0, arg1, arg2, arg3);
    }

    public static String seenUnknown(Object arg0, Object arg1, Locale... locale) {
        return MessageFormat.format(getMessageValue("seen.unknown", locale), arg0, arg1);
    }

    public static String shunUsage(Locale... locale) {
        return getMessageValue("shun.usage", locale);
    }

    public static String shunned(Object arg0, java.util.Date arg1, Locale... locale) {
        return MessageFormat.format(getMessageValue("shunned", locale), arg0, arg1);
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

    public static String unhandledMessage(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("unhandled.message", locale), arg0);
    }

    public static String unknownApi(Object arg0, Object arg1, Locale... locale) {
        return MessageFormat.format(getMessageValue("unknown.api", locale), arg0, arg1);
    }

    public static String unknownUser(Locale... locale) {
        return getMessageValue("unknown.user", locale);
    }

    public static String userNoSharedChannels(Locale... locale) {
        return getMessageValue("user.no.shared.channels", locale);
    }

    public static String userNotInChannel(Object arg0, Object arg1, Locale... locale) {
        return MessageFormat.format(getMessageValue("user.not.in.channel", locale), arg0, arg1);
    }

    public static String userNotFound(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("user.notFound", locale), arg0);
    }

    public static String weatherUnknown(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("weather.unknown", locale), arg0);
    }

    public static String webUrl(Locale... locale) {
        return getMessageValue("web.url", locale);
    }


}
