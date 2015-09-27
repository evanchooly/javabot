package javabot.model

import com.antwerkz.sofia.Sofia
import javabot.dao.ChannelDao
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Transient
import org.pircbotx.PircBotX

import javax.inject.Inject
import javax.inject.Provider

@Entity("events")
public class ChannelEvent : AdminEvent {
    @Inject
    @Transient
    lateinit val channelDao: ChannelDao
    @Inject
    @Transient
    lateinit val ircBot: Provider<PircBotX>
    lateinit var channel: String
    var key: String? = null
    var logged: Boolean = false

    protected constructor() {
    }

    public constructor(channel: String, type: EventType, requestedBy: String) : this(channel, null, type, requestedBy) {
    }

    public constructor(channel: String, key: String?, type: EventType, requestedBy: String) : super(type, requestedBy) {
        this.key = key
        this.channel = channel
    }

    override fun add() {
        join(channelDao.create(channel, logged, key))
    }

    protected fun join(chan: Channel) {
        if (chan.key == null) {
            ircBot.get().sendIRC().joinChannel(chan.name)
        } else {
            ircBot.get().sendIRC().joinChannel(chan.name, chan.key)
        }
    }

    override fun delete() {
        val chan = channelDao.get(channel)
        if (chan != null) {
            channelDao.delete(chan.id)
            ircBot.get().userChannelDao.getChannel(channel).send().part(Sofia.channelDeleted(requestedBy))
        }
    }

    override fun update() {
        val chan = channelDao.get(channel)
        if (chan != null) {
            chan.logged = logged
            chan.key = key
            channelDao.save(chan)
            ircBot.get().userChannelDao.getChannel(channel).send().part(Sofia.channelDeleted(requestedBy))
            join(chan)
        }
    }
}
