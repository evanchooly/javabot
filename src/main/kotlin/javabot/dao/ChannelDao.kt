package javabot.dao

import com.google.inject.Inject
import javabot.dao.util.QueryParam
import javabot.model.Activity
import javabot.model.Channel
import javabot.model.criteria.ChannelCriteria
import org.apache.commons.lang.StringUtils
import org.mongodb.morphia.Datastore
import java.time.LocalDateTime
import java.util.ArrayList

@SuppressWarnings("ConstantNamingConvention") class ChannelDao @Inject constructor(ds: Datastore) :
        BaseDao<Channel>(ds, Channel::class.java) {

    fun delete(name: String) {
        val channelCriteria = ChannelCriteria(ds)
        channelCriteria.name(name)
        ds.delete(channelCriteria.query())
    }

    fun configuredChannels(): List<String> {
        return ChannelCriteria(ds).name().distinct() as List<String>
    }

    fun getChannels(): List<Channel> {
        return getChannels(false)
    }

    fun getChannels(showAll: Boolean): List<Channel> {
        val channelCriteria = ChannelCriteria(ds)
        if (!showAll) {
            channelCriteria.logged().equal(true)
        }
        val query = channelCriteria.query()
        query.order("name")
        return query.asList()
    }

    fun find(qp: QueryParam): List<Channel> {
        var condition = qp.sort
        if (!qp.sortAsc) {
            condition = "-" + condition
        }
        return getQuery().order(condition).asList()
    }

    fun isLogged(channel: String): Boolean {
        val chan = get(channel)
        return if (chan != null) chan.logged else java.lang.Boolean.FALSE
    }

    fun get(name: String): Channel? {
        val criteria = ChannelCriteria(ds)
        criteria.upperName().equal(name.toUpperCase())
        return criteria.query().get()
    }

    fun getStatistics(): List<Activity> {
        //        val criteria = ActivityCriteria(ds)
        /*
@NamedQuery(name = ChannelDao.STATISTICS, query = "select new javabot.model.Activity(l.channel, count(l), max(l.updated),"
    + " min(l.updated), (select count(e) from Logs e)) from Logs l "
    + "where l.channel like '#%' group by l.channel order by count(l) desc")
*/
        return listOf()//(List<Activity>) getEntityManager().createNamedQuery(ChannelDao.STATISTICS)
        //            .getResultList();
    }

    fun loggedChannels(): List<String> {
        val criteria = ChannelCriteria(ds)
        criteria.logged().equal(true)
        val channels = criteria.query().retrievedFields(true, "name").asList()
        val names = ArrayList<String>()
        for (channel in channels) {
            names.add(channel.name)
        }
        return names
    }

    fun create(name: String, logged: Boolean?, key: String?): Channel {
        val channel = Channel()
        channel.name = name
        channel.logged = logged ?: java.lang.Boolean.TRUE
        channel.key = if (!StringUtils.isEmpty(key)) key else null
        save(channel)
        return channel
    }

    fun save(channel: Channel) {
        channel.updated = LocalDateTime.now()
        super.save(channel)
    }

    fun location(channel: Channel?): String {
        val location: String
        if (channel != null ) {
            location = if (isLogged(channel.name)) channel.name else "private channel"
        } else {
            location = channel?.name ?: "private message"
        }
        return location
    }

}