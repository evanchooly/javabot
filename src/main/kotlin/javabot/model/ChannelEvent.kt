package javabot.model

import javabot.dao.ChannelDao
import dev.morphia.annotations.Entity
import dev.morphia.annotations.Transient
import javax.inject.Inject

@Entity("events") class ChannelEvent : AdminEvent {
    @Inject
    @Transient
    lateinit var channelDao: ChannelDao
    lateinit var channel: String
    var key: String? = null
    var logged: Boolean = false

    protected constructor() {
    }

    constructor(requestedBy: String, type: EventType, channel: String, key: String? = null) : super(requestedBy, type) {
        this.key = key
        this.channel = channel
    }

    override fun add() {
        join(channelDao.create(channel, logged, key))
    }

    protected fun join(chan: Channel) {
        bot.joinChannel(chan)
    }

    override fun delete() {
        channelDao.get(channel)?.let { chan ->
            channelDao.delete(chan.id)
            bot.leaveChannel(chan, JavabotUser(requestedBy))
        }
    }

    override fun update() {
        channelDao.get(channel)?.let { chan ->
            chan.logged = logged
            chan.key = key
            channelDao.save(chan)
            bot.leaveChannel(chan, JavabotUser(requestedBy))
            join(chan)
        }
    }
}
