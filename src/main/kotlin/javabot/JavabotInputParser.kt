package org.pircbotx

import com.google.common.base.CharMatcher
import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMap
import com.google.common.collect.Lists
import java.util.StringTokenizer
import javabot.tokenizeLine
import org.apache.commons.lang3.StringUtils
import org.pircbotx.hooks.events.BanListEvent
import org.pircbotx.hooks.events.BanListEvent.Entry
import org.pircbotx.hooks.events.ChannelInfoEvent
import org.pircbotx.hooks.events.ModeEvent
import org.pircbotx.hooks.events.MotdEvent
import org.pircbotx.hooks.events.NickAlreadyInUseEvent
import org.pircbotx.hooks.events.ServerPingEvent
import org.pircbotx.hooks.events.ServerResponseEvent
import org.pircbotx.hooks.events.TopicEvent
import org.pircbotx.hooks.events.UnknownEvent
import org.pircbotx.hooks.events.UserListEvent
import org.pircbotx.hooks.events.WhoisEvent
import org.pircbotx.hooks.events.WhoisEvent.Builder
import org.pircbotx.hooks.managers.ListenerManager

class JavabotInputParser( bot: PircBotX): InputParser(bot) {
    override fun handleLine(rawLine: String) {
        var line: String = CharMatcher.whitespace().trimFrom(rawLine)
        // Parse out v3Tags before
        val tags = ImmutableMap.builder<String, String>()
        if (line.startsWith("@")) {
            //This message has IRCv3 tags
            val v3Tags = line.substring(1, line.indexOf(" "))
            line = line.substring(line.indexOf(" ") + 1)
            val tokenizer = StringTokenizer(v3Tags)
            while (tokenizer.hasMoreTokens()) {
                val tag = tokenizer.nextToken(";")
                if (tag.contains("=")) {
                    val parts = tag.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    tags.put(parts[0], if (parts.size == 2) parts[1] else "")
                } else {
                    tags.put(tag, "")
                }
            }
        }
        val parsedLine = tokenizeLine(line)
        var sourceRaw = ""
        if (parsedLine[0][0] == ':') sourceRaw = parsedLine.removeAt(0)
        val command = parsedLine.removeAt(0).uppercase(configuration.locale)
        // Check for server pings.
        if (command == "PING") {
            // Respond to the ping and return immediately.
            configuration.getListenerManager<ListenerManager>().onEvent(ServerPingEvent(bot, parsedLine[0]))
            return
        } else if (command.startsWith("ERROR")) {
            //Server is shutting us down
            bot.close()
            return
        }
        var target = if (parsedLine.isEmpty()) "" else parsedLine[0]
        if (target.startsWith(":")) target = target.substring(1)
        //Make sure this is a valid IRC line
        if (!sourceRaw.startsWith(":")) {
            // We don't know what this line means.
            configuration.getListenerManager<ListenerManager>().onEvent(UnknownEvent(bot, line))
            if (!bot.loggedIn) //Pass to CapHandlers, could be important
                for (curCapHandler in configuration.capHandlers) {
                    if (curCapHandler.handleUnknown(bot, line)){
                        addCapHandlerFinished(curCapHandler)
                    }
                }
            //Do not continue
            return
        }
        if (bot.loggedIn) processConnect(line, command, target, parsedLine)
        //Might be a backend code
        val code = Utils.tryParseInt(command, -1)
        if (code != -1) {
            processServerResponse(code, line, parsedLine)
            //Do not continue
            return
        }
        //Must be from user
        val source = bot.configuration.botFactory.createUserHostmask(bot, sourceRaw.substring(1))
        processCommand(target, source, command, line, parsedLine, tags.build())
    }

    override fun processServerResponse(code: Int, rawResponse: String, parsedResponseOrig: MutableList<String>) {
        val parsedResponse = ImmutableList.copyOf(parsedResponseOrig)
        //Parsed response format: Everything after code
        if (code == 433) {
            //EXAMPLE: * AnAlreadyUsedName :Nickname already in use
            //EXAMPLE: AnAlreadyUsedName :Nickname already in use (spec)
            //TODO: When output parsing is implemented intercept outgoing NICK?
            //Nickname in use, rename
            val autoNickChange = configuration.isAutoNickChange
            var autoNewNick: String? = null
            var usedNick: String? = null
            var doAutoNickChange = false
            //Ignore cases where we already have a valid nick but changed to a used one
            if (parsedResponse.size == 3) {
                usedNick = parsedResponse[1]
                if (parsedResponse[0] == "*") {
                    doAutoNickChange = true
                }
            } //For spec-compilant servers, if were not logged in its safe to assume we don't have a valid nick on connect
            else {
                usedNick = parsedResponse[0]
                if (!bot.loggedIn) {
                    doAutoNickChange = true
                }
            }
            if (autoNickChange && doAutoNickChange) {
                nickSuffix++
                autoNewNick = configuration.getName() + nickSuffix
                bot.sendIRC().changeNick(autoNewNick)
                bot.setNick(autoNewNick)
                bot.getUserChannelDao().renameUser(bot.getUserChannelDao().getUser(usedNick!!), autoNewNick)
            }
            configuration.getListenerManager<ListenerManager>().onEvent(NickAlreadyInUseEvent(bot, usedNick!!, autoNewNick, autoNickChange))
        } else if (code == ReplyConstants.RPL_LISTSTART) {
            //EXAMPLE: 321 Channel :Users Name (actual text)
            //A channel list is about to be sent
            channelListBuilder = ImmutableList.builder()
            channelListRunning = true
        } else if (code == ReplyConstants.RPL_LIST) {
            //This is part of a full channel listing as part of /LIST
            //EXAMPLE: 322 lordquackstar #xomb 12 :xomb exokernel project @ www.xomb.org
            val channel = parsedResponse[1]
            val userCount = Utils.tryParseInt(parsedResponse[2], -1)
            val topic = parsedResponse[3]
            channelListBuilder.add(ChannelListEntry(channel, userCount, topic))
        } else if (code == ReplyConstants.RPL_LISTEND) {
            //EXAMPLE: 323 :End of /LIST
            //End of channel list, dispatch event
            configuration.getListenerManager<ListenerManager>().onEvent(ChannelInfoEvent(bot, channelListBuilder.build()))
            channelListBuilder = null
            channelListRunning = false
        } else if (code == ReplyConstants.RPL_TOPIC) {
            //EXAMPLE: 332 PircBotX #aChannel :I'm some random topic
            //This is topic about a channel we've just joined. From /JOIN or /TOPIC
            val channel = bot.getUserChannelDao().getChannel(parsedResponse[1]!!)
            val topic = parsedResponse[2]
            channel.setTopic(topic)
        } else if (code == ReplyConstants.RPL_TOPICINFO) {
            //EXAMPLE: 333 PircBotX #aChannel ISetTopic 1564842512
            //This is information on the topic of the channel we've just joined. From /JOIN or /TOPIC
            val channel = bot.getUserChannelDao().getChannel(parsedResponse[1]!!)
            val setBy = configuration.getBotFactory().createUserHostmask(bot, parsedResponse[2])
            val date = Utils.tryParseLong(parsedResponse[3], -1)
            channel.setTopicTimestamp(date * 1000)
            channel.setTopicSetter(setBy)
            configuration.getListenerManager<ListenerManager>()
                .onEvent(TopicEvent(bot, channel, null, channel.getTopic(), setBy, date, false))
        } else if (code == ReplyConstants.RPL_WHOREPLY) {
            //EXAMPLE: 352 PircBotX #aChannel ~someName 74.56.56.56.my.Hostmask wolfe.freenode.net someNick H :0 Full Name
            //Part of a WHO reply on information on individual users
            val channel = bot.getUserChannelDao().getChannel(parsedResponse[1]!!)
            //Setup user
            val nick = parsedResponse[5]
            var curUser = if (bot.getUserChannelDao().containsUser(nick!!)) bot.getUserChannelDao().getUser(nick) else null
            val curUserHostmask = bot.getConfiguration().getBotFactory().createUserHostmask(
                bot, null, nick, parsedResponse[2],
                parsedResponse[3]
            )
            curUser = createUserIfNull(curUser, curUserHostmask)
            curUser.server = parsedResponse[4]
            processUserStatus(channel, curUser, parsedResponse[6])
            //Extra parsing needed since tokenizer stopped at :
            val rawEnding = parsedResponse[7]
            val rawEndingSpaceIndex = rawEnding!!.indexOf(' ')
            if (rawEndingSpaceIndex == -1) {
                //parsedResponse data is trimmed, so if the index == -1, then there was no real name given and the space separating hops from real name was trimmed.
                curUser.hops = rawEnding.toInt()
                curUser.realName = ""
            } else {
                //parsedResponse data contains a real name
                curUser.hops = rawEnding.substring(0, rawEndingSpaceIndex).toInt()
                curUser.realName = rawEnding.substring(rawEndingSpaceIndex + 1)
            }
            //Associate with channel
            bot.getUserChannelDao().addUserToChannel(curUser, channel)
        } else if (code == ReplyConstants.RPL_ENDOFWHO) {
            //EXAMPLE: 315 PircBotX #aChannel :End of /WHO list
            //End of the WHO reply
            val channel = bot.getUserChannelDao().getChannel(parsedResponse[1]!!)
            configuration.getListenerManager<ListenerManager>()
                .onEvent(UserListEvent(bot, channel, bot.getUserChannelDao().getUsers(channel), true))
        } else if (code == ReplyConstants.RPL_CHANNELMODEIS) {
            //EXAMPLE: 324 PircBotX #aChannel +cnt
            //Full channel mode (In response to MODE <channel>)
            val channel = bot.getUserChannelDao().getChannel(parsedResponse[1]!!)
            val modeParsed = parsedResponse.subList(2, parsedResponse.size)
            val mode = StringUtils.join(modeParsed, ' ')
            channel.setMode(mode, modeParsed)
            configuration.getListenerManager<ListenerManager>().onEvent(ModeEvent(bot, channel, null, null, mode, modeParsed))
        } else if (code == 329) {
            //EXAMPLE: 329 lordquackstar #botters 1199140245
            //Tells when channel was created. From /JOIN
            val channel = bot.getUserChannelDao().getChannel(parsedResponse[1]!!)
            val createDate = Utils.tryParseInt(parsedResponse[2], -1)
            //Set in channel
            channel.setCreateTimestamp(createDate.toLong())
        } else if (code == ReplyConstants.RPL_MOTDSTART) //Example: 375 PircBotX :- wolfe.freenode.net Message of the Day -
        //Motd is starting, reset the StringBuilder
            motdBuilder =
                StringBuilder() else if (code == ReplyConstants.RPL_MOTD) //Example: 372 PircBotX :- Welcome to wolfe.freenode.net in Manchester, England, Uk!  Thanks to
        //This is part of the MOTD, add a new line
            motdBuilder.append(CharMatcher.whitespace().trimFrom(parsedResponse[1]!!.substring(1)))
                .append("\n") else if (code == ReplyConstants.RPL_ENDOFMOTD) {
            //Example: PircBotX :End of /MOTD command.
            //End of MOTD, clean it and dispatch MotdEvent
            val serverInfo = bot.getServerInfo()
            serverInfo.setMotd(motdBuilder.toString().trim { it <= ' ' })
            motdBuilder = null
            configuration.getListenerManager<ListenerManager>().onEvent(MotdEvent(bot, serverInfo.getMotd()))
        } else if (code == 4 || code == 5) {
            //Example: 004 PircBotX sendak.freenode.net ircd-seven-1.1.3 DOQRSZaghilopswz CFILMPQbcefgijklmnopqrstvz bkloveqjfI
            //Server info line, remove ending comment and let ServerInfo class parse it
            val endCommentIndex = rawResponse.lastIndexOf(" :")
            if (endCommentIndex > 1) {
                val endComment = rawResponse.substring(endCommentIndex + 2)
                val lastIndex = parsedResponseOrig.size - 1
                if (endComment == parsedResponseOrig[lastIndex]) parsedResponseOrig.removeAt(lastIndex)
            }
            bot.getServerInfo().parse(code, parsedResponseOrig)
        } else if (code == ReplyConstants.RPL_WHOISUSER) {
            //Example: 311 TheLQ Plazma ~Plazma freenode/staff/plazma * :Plazma Rooolz!
            //New whois is starting
            val whoisNick = parsedResponse[1]
            val builder = WhoisEvent.builder()
            builder.nick(whoisNick)
            builder.login(parsedResponse[2])
            builder.hostname(parsedResponse[3])
            builder.realname(parsedResponse[5])
            whoisBuilder[whoisNick] = builder
        } else if (code == ReplyConstants.RPL_AWAY) {
            //Example: 301 PircBotXUser TheLQ_ :I'm away, sorry
            //Can be sent during whois
            val nick = parsedResponse[1]
            val awayMessage = parsedResponse[2]
            if (bot.getUserChannelDao().containsUser(nick!!)) bot.getUserChannelDao().getUser(nick).awayMessage = awayMessage
            if (whoisBuilder.containsKey(nick)) whoisBuilder[nick]!!.awayMessage(awayMessage)
        } else if (code == ReplyConstants.RPL_WHOISCHANNELS) {
            //Example: 319 TheLQ Plazma :+#freenode
            //Channel list from whois. Re-tokenize since they're after the :
            val whoisNick = parsedResponse[1]
            val parsedChannels = ImmutableList.copyOf(Utils.tokenizeLine(parsedResponse[2]))
            whoisBuilder[whoisNick]!!.channels(parsedChannels)
        } else if (code == ReplyConstants.RPL_WHOISSERVER) {
            //Server info from whois
            //312 TheLQ Plazma leguin.freenode.net :Ume?, SE, EU
            val whoisNick = parsedResponse[1]
            whoisBuilder[whoisNick]!!.server(parsedResponse[2])
            whoisBuilder[whoisNick]!!.serverInfo(parsedResponse[3])
        } else if (code == ReplyConstants.RPL_WHOISIDLE) {
            //Idle time from whois
            //317 TheLQ md_5 6077 1347373349 :seconds idle, signon time
            val whoisNick = parsedResponse[1]
            whoisBuilder[whoisNick]!!.idleSeconds(parsedResponse[2]!!.toLong())
            whoisBuilder[whoisNick]!!.signOnTime(parsedResponse[3]!!.toLong())
        } else if (code == 330) {
            //RPL_WHOISACCOUNT: Extra Whois info
            //330 TheLQ Utoxin Utoxin :is logged in as
            //Make sure we set registered as to the nick, not to the note after the colon
            var registeredNick: String? = ""
            if (!rawResponse.endsWith(":" + parsedResponse[2])) registeredNick = parsedResponse[2]
            whoisBuilder[parsedResponse[1]]!!.registeredAs(registeredNick)
        } else if (code == 307) {
            //If shown, tells us that the user is registered with nickserv
            //307 TheLQ TheLQ-PircBotX :has identified for this nick
            whoisBuilder[parsedResponse[1]]!!.registeredAs("")
        } else if (code == ReplyConstants.ERR_NOSUCHSERVER) {
            //Whois failed when doing "WHOIS invaliduser invaliduser"
            //402 TheLQ asdfasdf :No such server
            val whoisNick = parsedResponse[1]
            val event = WhoisEvent.builder().nick(whoisNick).exists(false).generateEvent(bot)
            configuration.getListenerManager<ListenerManager>().onEvent(event)
        } else if (code == ReplyConstants.RPL_ENDOFWHOIS) {
            //End of whois
            //318 TheLQ Plazma :End of /WHOIS list.
            val whoisNick = parsedResponse[1]
            val builder: Builder?
            if (whoisBuilder.containsKey(whoisNick)) {
                builder = whoisBuilder[whoisNick]
                builder!!.exists(true)
            } else {
                builder = WhoisEvent.builder()
                builder.nick(whoisNick)
                builder.exists(false)
            }
            configuration.getListenerManager<ListenerManager>().onEvent(builder!!.generateEvent(bot))
            whoisBuilder.remove(whoisNick)
        } else if (code == 367) {
            //Ban list entry
            //367 TheLQ #aChannel *!*@test1.host TheLQ!~quackstar@some.host 1415143822
            val channel = bot.getUserChannelDao().getChannel(parsedResponse[1]!!)
            val recipient = bot.getConfiguration().getBotFactory().createUserHostmask(bot, parsedResponse[2])
            val source = bot.getConfiguration().getBotFactory().createUserHostmask(bot, parsedResponse[3])
            val time = parsedResponse[4]!!.toLong()
            banListBuilder.put(channel, Entry(recipient, source, time))
        } else if (code == 368) {
            //Ban list is finished
            //368 TheLQ #aChannel :End of Channel Ban List
            val channel = bot.getUserChannelDao().getChannel(parsedResponse[1]!!)
            val entries = ImmutableList.copyOf(banListBuilder.removeAll(channel))
            configuration.getListenerManager<ListenerManager>().onEvent(BanListEvent(bot, channel, entries))
        } else if (code == 353) {
            //NAMES response
            //353 PircBotXUser = #aChannel :aUser1 aUser2
            for (curUser in StringUtils.split(parsedResponse[3])) {
                //Siphon off any levels this user has
                var nick = curUser
                val levels: MutableList<UserLevel> = Lists.newArrayList()
                var parsedLevel: UserLevel? = UserLevel.fromSymbol(nick[0])
                while (parsedLevel != null) {
                    nick = nick.substring(1)
                    levels.add(parsedLevel)
                    parsedLevel = UserLevel.fromSymbol(nick[0])
                }
                var user: User?
                user = if (!bot.getUserChannelDao().containsUser(nick)) //Create user with nick only
                    bot.getUserChannelDao().createUser(UserHostmask(bot, nick)) else bot.getUserChannelDao().getUser(nick)
                val chan = bot.getUserChannelDao().getChannel(parsedResponse[2]!!)
                bot.getUserChannelDao().addUserToChannel(user, chan)
                //Now that the user is created, add them to the appropiate levels
                for (curLevel in levels) {
                    bot.getUserChannelDao().addUserToLevel(curLevel, user, chan)
                }
            }
        } else if (code == 366) {
            //NAMES response finished
            //366 PircBotXUser #aChannel :End of /NAMES list.
            val channel = bot.getUserChannelDao().getChannel(parsedResponse[1]!!)
            configuration.getListenerManager<ListenerManager>()
                .onEvent(UserListEvent(bot, channel, bot.getUserChannelDao().getUsers(channel), false))
        }
        configuration.getListenerManager<ListenerManager>().onEvent(ServerResponseEvent(bot, code, rawResponse, parsedResponse))
    }
}