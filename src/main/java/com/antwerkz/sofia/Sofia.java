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

    public static String loggingInUser(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("@debug.logging.in.user", locale), arg0);
    }

    public static void logLoggingInUser(Object arg0, Locale... locale) {
        if(logger.isDebugEnabled()) {
            logger.debug(loggingInUser(arg0));
        }
    }
    public static String factoidInvalidSearchValue(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("@info.factoid.invalid.search.value", locale), arg0);
    }

    public static void logFactoidInvalidSearchValue(Object arg0, Locale... locale) {
        if(logger.isInfoEnabled()) {
            logger.info(factoidInvalidSearchValue(arg0));
        }
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
    public static String webappNotStarting(Locale... locale) {
        return getMessageValue("@info.webapp.not.starting", locale);
    }

    public static void logWebappNotStarting(Locale... locale) {
        if(logger.isInfoEnabled()) {
            logger.info(webappNotStarting());
        }
    }
    public static String webappStarting(Locale... locale) {
        return getMessageValue("@info.webapp.starting", locale);
    }

    public static void logWebappStarting(Locale... locale) {
        if(logger.isInfoEnabled()) {
            logger.info(webappStarting());
        }
    }
    public static String accountTooNew(Locale... locale) {
        return getMessageValue("account.too.new", locale);
    }

    public static String action(Locale... locale) {
        return getMessageValue("action", locale);
    }

    public static String addedBy(Locale... locale) {
        return getMessageValue("addedBy", locale);
    }

    public static String addedOn(Locale... locale) {
        return getMessageValue("addedOn", locale);
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

    public static String channelDeleted(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("channel.deleted", locale), arg0);
    }

    public static String channelUnknown(Object arg0, Object arg1, Locale... locale) {
        return MessageFormat.format(getMessageValue("channel.unknown", locale), arg0, arg1);
    }

    public static String channelUpdated(Locale... locale) {
        return getMessageValue("channel.updated", locale);
    }

    public static String configIrcHistory(Locale... locale) {
        return getMessageValue("config.irc.history", locale);
    }

    public static String configIrcNick(Locale... locale) {
        return getMessageValue("config.irc.nick", locale);
    }

    public static String configIrcPassword(Locale... locale) {
        return getMessageValue("config.irc.password", locale);
    }

    public static String configIrcPort(Locale... locale) {
        return getMessageValue("config.irc.port", locale);
    }

    public static String configIrcServer(Locale... locale) {
        return getMessageValue("config.irc.server", locale);
    }

    public static String configIrcTrigger(Locale... locale) {
        return getMessageValue("config.irc.trigger", locale);
    }

    public static String configMinimumNickservAge(Locale... locale) {
        return getMessageValue("config.minimum.nickserv.age", locale);
    }

    public static String configThrottleThreshold(Locale... locale) {
        return getMessageValue("config.throttle.threshold", locale);
    }

    public static String configWebUrl(Locale... locale) {
        return getMessageValue("config.web.url", locale);
    }

    public static String configurationMissingProperties(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("configuration.missing.properties", locale), arg0);
    }

    public static String configurationSetProperty(Object arg0, Object arg1, Locale... locale) {
        return MessageFormat.format(getMessageValue("configuration.set.property", locale), arg0, arg1);
    }

    public static String configurationUnknownProperty(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("configuration.unknown.property", locale), arg0);
    }

    public static String configurationWebMissingFile(Locale... locale) {
        return getMessageValue("configuration.web.missing.file", locale);
    }

    public static String daysUntil(Object arg0, Number arg1, java.util.Date arg2, Locale... locale) {
        return MessageFormat.format(getMessageValue("days.until", locale), arg0, arg1, arg2);
    }

    public static String email(Locale... locale) {
        return getMessageValue("email", locale);
    }

    public static String factoidAdded(Object arg0, Object arg1, Object arg2, Object arg3, Locale... locale) {
        return MessageFormat.format(getMessageValue("factoid.added", locale), arg0, arg1, arg2, arg3);
    }

    public static String factoidCantBeBlank(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("factoid.cantBeBlank", locale), arg0);
    }

    public static String factoidChanged(Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Locale... locale) {
        return MessageFormat.format(getMessageValue("factoid.changed", locale), arg0, arg1, arg2, arg3, arg4);
    }

    public static String factoidChangingLocked(Object arg0, Object arg1, Object arg2, Locale... locale) {
        return MessageFormat.format(getMessageValue("factoid.changing.locked", locale), arg0, arg1, arg2);
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

    public static String factoidRemoved(Object arg0, Object arg1, Object arg2, Object arg3, Locale... locale) {
        return MessageFormat.format(getMessageValue("factoid.removed", locale), arg0, arg1, arg2, arg3);
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

    public static String ircName(Locale... locale) {
        return getMessageValue("ircName", locale);
    }

    public static String javadocApiList(Object arg0, Object arg1, Locale... locale) {
        return MessageFormat.format(getMessageValue("javadoc.api.list", locale), arg0, arg1);
    }

    public static String javadocApiName(Locale... locale) {
        return getMessageValue("javadoc.api.name", locale);
    }

    public static String javadocDownloadUrl(Locale... locale) {
        return getMessageValue("javadoc.download.url", locale);
    }

    public static String javadocUrl(Locale... locale) {
        return getMessageValue("javadoc.url", locale);
    }

    public static String jepInvalid(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("jep.invalid", locale), arg0);
    }

    public static String jepMissing(Locale... locale) {
        return getMessageValue("jep.missing", locale);
    }

    public static String jepSucceed(Object arg0, Object arg1, Locale... locale) {
        return MessageFormat.format(getMessageValue("jep.succeed", locale), arg0, arg1);
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

    public static String karmaChanged(Object arg0, Object arg1, Number arg2, Object arg3, Locale... locale) {
        return MessageFormat.format(getMessageValue("karma.changed", locale), arg0, arg1, arg2, arg3);
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

    public static String logsAnchorFormat(Object arg0, Object arg1, Locale... locale) {
        return MessageFormat.format(getMessageValue("logs.anchorFormat", locale), arg0, arg1);
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

    public static String nickservNotResponding(Locale... locale) {
        return getMessageValue("nickserv.not.responding", locale);
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

    public static String rfcFail(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("rfc.fail", locale), arg0);
    }

    public static String rfcInvalid(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("rfc.invalid", locale), arg0);
    }

    public static String rfcMissing(Locale... locale) {
        return getMessageValue("rfc.missing", locale);
    }

    public static String rfcSucceed(Object arg0, Object arg1, Locale... locale) {
        return MessageFormat.format(getMessageValue("rfc.succeed", locale), arg0, arg1);
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

    public static String submit(Locale... locale) {
        return getMessageValue("submit", locale);
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

    public static String userJoined(Object arg0, Object arg1, Object arg2, Locale... locale) {
        return MessageFormat.format(getMessageValue("user.joined", locale), arg0, arg1, arg2);
    }

    public static String userNickChanged(Object arg0, Object arg1, Locale... locale) {
        return MessageFormat.format(getMessageValue("user.nickChanged", locale), arg0, arg1);
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

    public static String userParted(Object arg0, Object arg1, Locale... locale) {
        return MessageFormat.format(getMessageValue("user.parted", locale), arg0, arg1);
    }

    public static String userQuit(Object arg0, Object arg1, Locale... locale) {
        return MessageFormat.format(getMessageValue("user.quit", locale), arg0, arg1);
    }

    public static String weatherUnknown(Object arg0, Locale... locale) {
        return MessageFormat.format(getMessageValue("weather.unknown", locale), arg0);
    }


}
