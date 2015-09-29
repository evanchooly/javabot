package javabot.dao

import javabot.dao.util.QueryParam
import javabot.model.Activity
import javabot.model.Channel
import javabot.model.criteria.ChannelCriteria
import org.apache.commons.lang.StringUtils
import org.bson.types.ObjectId
import org.mongodb.morphia.query.QueryImpl
import java.time.LocalDateTime
import java.util.ArrayList

@SuppressWarnings("ConstantNamingConvention")
public class ChannelDao : BaseDao<Channel>(Channel::class.java) {

    public fun delete(name: String) {
        val channelCriteria = ChannelCriteria(ds)
        channelCriteria.name(name)
        ds.delete(channelCriteria.query())
    }

    @SuppressWarnings("unchecked")
    public fun configuredChannels(): List<String> {
        return (getQuery() as QueryImpl<Any>).collection.distinct("name") as List<String>
    }

    @SuppressWarnings("unchecked")
    public fun getChannels(): List<Channel> {
        return getChannels(false)
    }

    public fun getChannels(showAll: Boolean): List<Channel> {
        val channelCriteria = ChannelCriteria(ds)
        if (!showAll) {
            channelCriteria.logged().equal(true)
        }
        val query = channelCriteria.query()
        query.order("name")
        return query.asList()
    }

    @SuppressWarnings("unchecked")
    public fun find(qp: QueryParam): List<Channel> {
        var condition = qp.sort
        if (!qp.sortAsc) {
            condition = "-" + condition
        }
        return getQuery().order(condition).asList()
    }

    public fun isLogged(channel: String): Boolean {
        val chan = get(channel)
        return if (chan != null) chan.logged else java.lang.Boolean.FALSE
    }

    public fun get(name: String): Channel? {
        val criteria = ChannelCriteria(ds)
        criteria.upperName().equal(name.toUpperCase())
        return criteria.query().get()
    }

    @SuppressWarnings("unchecked")
    public fun getStatistics(): List<Activity> {
        /*
@NamedQuery(name = ChannelDao.STATISTICS, query = "select new javabot.model.Activity(l.channel, count(l), max(l.updated),"
    + " min(l.updated), (select count(e) from Logs e)) from Logs l "
    + "where l.channel like '#%' group by l.channel order by count(l) desc")
*/
        return listOf()//(List<Activity>) getEntityManager().createNamedQuery(ChannelDao.STATISTICS)
        //            .getResultList();
    }

    @SuppressWarnings("unchecked")
    public fun loggedChannels(): List<String> {
        val criteria = ChannelCriteria(ds)
        criteria.logged().equal(true)
        val channels = criteria.query().retrievedFields(true, "name").asList()
        val names = ArrayList<String>()
        for (channel in channels) {
            names.add(channel.name)
        }
        return names
    }

    public fun create(name: String, logged: Boolean?, key: String?): Channel {
        val channel = Channel()
        channel.name = name
        channel.logged = logged ?: java.lang.Boolean.TRUE
        channel.key = if (!StringUtils.isEmpty(key)) key else null
        save(channel)
        return channel
    }

    public fun save(channel: Channel) {
        channel.updated = LocalDateTime.now()
        super.save(channel)
    }

}