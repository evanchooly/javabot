package com.antwerkz.sofia

import java.io.*
import java.net.*
import java.text.*
import java.util.*
import java.security.*
import java.util.ResourceBundle.Control

import org.slf4j.*

public object Sofia {
    private val messages = HashMap<Locale, ResourceBundle>()
    private val logger = LoggerFactory.getLogger(Sofia::class.java)

    private fun getBundle(vararg localeList: Locale): ResourceBundle {
        val locale = if (localeList.size() == 0) Locale.getDefault() else localeList[0]
        var labels: ResourceBundle? = loadBundle(locale)
        if (labels == null) {
            labels = loadBundle(Locale.ROOT)
        }
        return labels
    }

    private fun loadBundle(locale: Locale): ResourceBundle {
        var bundle: ResourceBundle? = messages.get(locale)
        if (bundle == null) {
            bundle = ResourceBundle.getBundle("sofia", locale)
            messages.put(locale, bundle)
        }
        return bundle!!
    }

    private fun getMessageValue(key: String, vararg locale: Locale): String {
        return getBundle(*locale).getObject(key) as String
    }

    public fun loggingInUser(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("@debug.logging.in.user", *locale), arg0)
    }

    public fun logLoggingInUser(arg0: Any, vararg locale: Locale) {
        if (logger.isDebugEnabled) {
            logger.debug(loggingInUser(arg0))
        }
    }

    public fun factoidInvalidSearchValue(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("@info.factoid.invalid.search.value", *locale), arg0)
    }

    public fun logFactoidInvalidSearchValue(arg0: Any, vararg locale: Locale) {
        if (logger.isInfoEnabled) {
            logger.info(factoidInvalidSearchValue(arg0))
        }
    }

    public fun noNickservEntry(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("@info.no.nickserv.entry", *locale), arg0)
    }

    public fun logNoNickservEntry(arg0: Any, vararg locale: Locale) {
        if (logger.isInfoEnabled) {
            logger.info(noNickservEntry(arg0))
        }
    }

    public fun waitingForNickserv(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("@info.waiting.for.nickserv", *locale), arg0)
    }

    public fun logWaitingForNickserv(arg0: Any, vararg locale: Locale) {
        if (logger.isInfoEnabled) {
            logger.info(waitingForNickserv(arg0))
        }
    }

    public fun webappNotStarting(vararg locale: Locale): String {
        return getMessageValue("@info.webapp.not.starting", *locale)
    }

    public fun logWebappNotStarting(vararg locale: Locale) {
        if (logger.isInfoEnabled) {
            logger.info(webappNotStarting())
        }
    }

    public fun webappStarting(vararg locale: Locale): String {
        return getMessageValue("@info.webapp.starting", *locale)
    }

    public fun logWebappStarting(vararg locale: Locale) {
        if (logger.isInfoEnabled) {
            logger.info(webappStarting())
        }
    }

    public fun accountTooNew(vararg locale: Locale): String {
        return getMessageValue("account.too.new", *locale)
    }

    public fun action(vararg locale: Locale): String {
        return getMessageValue("action", *locale)
    }

    public fun addedBy(vararg locale: Locale): String {
        return getMessageValue("addedBy", *locale)
    }

    public fun addedOn(vararg locale: Locale): String {
        return getMessageValue("addedOn", *locale)
    }

    public fun adminAdded(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("admin.added", *locale), arg0)
    }

    public fun adminAlready(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("admin.already", *locale), arg0)
    }

    public fun adminBadChannelName(vararg locale: Locale): String {
        return getMessageValue("admin.badChannelName", *locale)
    }

    public fun adminDoneRemovingOldJavadoc(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("admin.done.removing.old.javadoc", *locale), arg0)
    }

    public fun adminJoinedChannel(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("admin.joinedChannel", *locale), arg0)
    }

    public fun adminJoiningChannel(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("admin.joiningChannel", *locale), arg0)
    }

    public fun adminJoiningLoggedChannel(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("admin.joiningLoggedChannel", *locale), arg0)
    }

    public fun adminKnownCommands(arg0: Any, arg1: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("admin.known.commands", *locale), arg0, arg1)
    }

    public fun adminKnownOperations(arg0: Any, arg1: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("admin.known.operations", *locale), arg0, arg1)
    }

    public fun adminListChannelsPreamble(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("admin.list.channels.preamble", *locale), arg0)
    }

    public fun adminOperationDisabled(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("admin.operation.disabled", *locale), arg0)
    }

    public fun adminOperationEnabled(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("admin.operation.enabled", *locale), arg0)
    }

    public fun adminOperationInstructions(vararg locale: Locale): String {
        return getMessageValue("admin.operation.instructions", *locale)
    }

    public fun adminOperationNotDisabled(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("admin.operation.not.disabled", *locale), arg0)
    }

    public fun adminOperationNotEnabled(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("admin.operation.not.enabled", *locale), arg0)
    }

    public fun adminParseFailure(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("admin.parseFailure", *locale), arg0)
    }

    public fun adminRemovingOldJavadoc(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("admin.removing.old.javadoc", *locale), arg0)
    }

    public fun adminRunningOperations(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("admin.running.operations", *locale), arg0)
    }

    public fun alreadyShunned(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("alreadyShunned", *locale), arg0)
    }

    public fun apiLocation(arg0: Any, arg1: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("api.location", *locale), arg0, arg1)
    }

    public fun botAolbonics(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("bot.aolbonics", *locale), arg0)
    }

    public fun botIgnoring(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("bot.ignoring", *locale), arg0)
    }

    public fun botSelfTalk(vararg locale: Locale): String {
        return getMessageValue("bot.selfTalk", *locale)
    }

    public fun botStats(arg0: Number, arg1: Number, arg2: Number, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("bot.stats", *locale), arg0, arg1, arg2)
    }

    public fun botUnixCommand(arg0: Any, arg1: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("bot.unixCommand", *locale), arg0, arg1)
    }

    public fun botVersion(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("bot.version", *locale), arg0)
    }

    public fun changingLockedFactoid(arg0: Any, arg1: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("changing.locked.factoid", *locale), arg0, arg1)
    }

    public fun channelDeleted(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("channel.deleted", *locale), arg0)
    }

    public fun channelUnknown(arg0: Any, arg1: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("channel.unknown", *locale), arg0, arg1)
    }

    public fun channelUpdated(vararg locale: Locale): String {
        return getMessageValue("channel.updated", *locale)
    }

    public fun configIrcHistory(vararg locale: Locale): String {
        return getMessageValue("config.irc.history", *locale)
    }

    public fun configIrcNick(vararg locale: Locale): String {
        return getMessageValue("config.irc.nick", *locale)
    }

    public fun configIrcPassword(vararg locale: Locale): String {
        return getMessageValue("config.irc.password", *locale)
    }

    public fun configIrcPort(vararg locale: Locale): String {
        return getMessageValue("config.irc.port", *locale)
    }

    public fun configIrcServer(vararg locale: Locale): String {
        return getMessageValue("config.irc.server", *locale)
    }

    public fun configIrcTrigger(vararg locale: Locale): String {
        return getMessageValue("config.irc.trigger", *locale)
    }

    public fun configMinimumNickservAge(vararg locale: Locale): String {
        return getMessageValue("config.minimum.nickserv.age", *locale)
    }

    public fun configThrottleThreshold(vararg locale: Locale): String {
        return getMessageValue("config.throttle.threshold", *locale)
    }

    public fun configWebUrl(vararg locale: Locale): String {
        return getMessageValue("config.web.url", *locale)
    }

    public fun configurationMissingProperties(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("configuration.missing.properties", *locale), arg0)
    }

    public fun configurationSetProperty(arg0: Any, arg1: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("configuration.set.property", *locale), arg0, arg1)
    }

    public fun configurationUnknownProperty(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("configuration.unknown.property", *locale), arg0)
    }

    public fun daysUntil(arg0: Any, arg1: Number, arg2: java.util.Date, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("days.until", *locale), arg0, arg1, arg2)
    }

    public fun email(vararg locale: Locale): String {
        return getMessageValue("email", *locale)
    }

    public fun factoidAdded(arg0: Any, arg1: Any, arg2: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("factoid.added", *locale), arg0, arg1, arg2)
    }

    public fun factoidChanged(arg0: Any, arg1: Any, arg2: Any, arg3: Any, arg4: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("factoid.changed", *locale), arg0, arg1, arg2, arg3, arg4)
    }

    public fun factoidDeleteLocked(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("factoid.delete.locked", *locale), arg0)
    }

    public fun factoidDeleteUnknown(arg0: Any, arg1: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("factoid.delete.unknown", *locale), arg0, arg1)
    }

    public fun factoidExists(arg0: Any, arg1: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("factoid.exists", *locale), arg0, arg1)
    }

    public fun factoidForgotten(arg0: Any, arg1: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("factoid.forgotten", *locale), arg0, arg1)
    }

    public fun factoidInfo(arg0: Any, arg1: Any, arg2: Any, arg3: Any, arg4: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("factoid.info", *locale), arg0, arg1, arg2, arg3, arg4)
    }

    public fun factoidInvalidName(vararg locale: Locale): String {
        return getMessageValue("factoid.invalid.name", *locale)
    }

    public fun factoidInvalidValue(vararg locale: Locale): String {
        return getMessageValue("factoid.invalid.value", *locale)
    }

    public fun factoidLocked(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("factoid.locked", *locale), arg0)
    }

    public fun factoidLoop(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("factoid.loop", *locale), arg0)
    }

    public fun factoidTellSyntax(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("factoid.tell.syntax", *locale), arg0)
    }

    public fun factoidUnknown(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("factoid.unknown", *locale), arg0)
    }

    public fun historyLength(vararg locale: Locale): String {
        return getMessageValue("history.length", *locale)
    }

    public fun hostName(vararg locale: Locale): String {
        return getMessageValue("hostName", *locale)
    }

    public fun invalidDateFormat(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("invalid.date.format", *locale), arg0)
    }

    public fun ircName(vararg locale: Locale): String {
        return getMessageValue("ircName", *locale)
    }

    public fun javadocApiList(arg0: Any, arg1: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("javadoc.api.list", *locale), arg0, arg1)
    }

    public fun javadocApiName(vararg locale: Locale): String {
        return getMessageValue("javadoc.api.name", *locale)
    }

    public fun javadocDownloadUrl(vararg locale: Locale): String {
        return getMessageValue("javadoc.download.url", *locale)
    }

    public fun javadocUrl(vararg locale: Locale): String {
        return getMessageValue("javadoc.url", *locale)
    }

    public fun jsrInvalid(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("jsr.invalid", *locale), arg0)
    }

    public fun jsrMissing(vararg locale: Locale): String {
        return getMessageValue("jsr.missing", *locale)
    }

    public fun jsrUnknown(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("jsr.unknown", *locale), arg0)
    }

    public fun karmaChanged(arg0: Any, arg1: Any, arg2: Number, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("karma.changed", *locale), arg0, arg1, arg2)
    }

    public fun karmaOthersNone(arg0: Any, arg1: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("karma.others.none", *locale), arg0, arg1)
    }

    public fun karmaOthersValue(arg0: Any, arg1: Number, arg2: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("karma.others.value", *locale), arg0, arg1, arg2)
    }

    public fun karmaOwnIncrement(vararg locale: Locale): String {
        return getMessageValue("karma.own.increment", *locale)
    }

    public fun karmaOwnNone(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("karma.own.none", *locale), arg0)
    }

    public fun karmaOwnValue(arg0: Any, arg1: Number, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("karma.own.value", *locale), arg0, arg1)
    }

    public fun leaveChannel(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("leave.channel", *locale), arg0)
    }

    public fun leavePrivmsg(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("leave.privmsg", *locale), arg0)
    }

    public fun logsAnchorFormat(arg0: Any, arg1: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("logs.anchorFormat", *locale), arg0, arg1)
    }

    public fun logsEntry(arg0: Any, arg1: Any, arg2: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("logs.entry", *locale), arg0, arg1, arg2)
    }

    public fun logsNone(vararg locale: Locale): String {
        return getMessageValue("logs.none", *locale)
    }

    public fun logsNoneForNick(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("logs.noneForNick", *locale), arg0)
    }

    public fun nickservNotResponding(vararg locale: Locale): String {
        return getMessageValue("nickserv.not.responding", *locale)
    }

    public fun noDocumentation(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("no.documentation", *locale), arg0)
    }

    public fun notAdmin(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("not.admin", *locale), arg0)
    }

    public fun notAllowed(vararg locale: Locale): String {
        return getMessageValue("not.allowed", *locale)
    }

    public fun ok(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("ok", *locale), arg0)
    }

    public fun privmsgChange(vararg locale: Locale): String {
        return getMessageValue("privmsg.change", *locale)
    }

    public fun registerNick(arg0: Any, arg1: Any?, arg2: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("register.nick", *locale), arg0, arg1, arg2)
    }

    public fun rfcFail(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("rfc.fail", *locale), arg0)
    }

    public fun rfcInvalid(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("rfc.invalid", *locale), arg0)
    }

    public fun rfcSucceed(arg0: Any, arg1: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("rfc.succeed", *locale), arg0, arg1)
    }

    public fun seenLast(arg0: Any, arg1: Any, arg2: Any, arg3: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("seen.last", *locale), arg0, arg1, arg2, arg3)
    }

    public fun seenUnknown(arg0: Any, arg1: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("seen.unknown", *locale), arg0, arg1)
    }

    public fun shunUsage(vararg locale: Locale): String {
        return getMessageValue("shun.usage", *locale)
    }

    public fun shunned(arg0: Any, arg1: java.util.Date, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("shunned", *locale), arg0, arg1)
    }

    public fun submit(vararg locale: Locale): String {
        return getMessageValue("submit", *locale)
    }

    public fun throttledUser(vararg locale: Locale): String {
        return getMessageValue("throttled.user", *locale)
    }

    public fun tooManyResults(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("too.many.results", *locale), arg0)
    }

    public fun unhandledMessage(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("unhandled.message", *locale), arg0)
    }

    public fun unknownApi(arg0: Any, arg1: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("unknown.api", *locale), arg0, arg1)
    }

    public fun unknownUser(vararg locale: Locale): String {
        return getMessageValue("unknown.user", *locale)
    }

    public fun userJoined(arg0: Any, arg1: Any, arg2: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("user.joined", *locale), arg0, arg1, arg2)
    }

    public fun userNickChanged(arg0: Any, arg1: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("user.nickChanged", *locale), arg0, arg1)
    }

    public fun userNoSharedChannels(vararg locale: Locale): String {
        return getMessageValue("user.no.shared.channels", *locale)
    }

    public fun userNotInChannel(arg0: Any, arg1: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("user.not.in.channel", *locale), arg0, arg1)
    }

    public fun userNotFound(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("user.notFound", *locale), arg0)
    }

    public fun userParted(arg0: Any, arg1: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("user.parted", *locale), arg0, arg1)
    }

    public fun userQuit(arg0: Any, arg1: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("user.quit", *locale), arg0, arg1)
    }

    public fun weatherUnknown(arg0: Any, vararg locale: Locale): String {
        return MessageFormat.format(getMessageValue("weather.unknown", *locale), arg0)
    }


}
