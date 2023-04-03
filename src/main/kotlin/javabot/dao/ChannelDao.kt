package javabot.dao

import com.google.inject.Inject
import dev.morphia.Datastore
import dev.morphia.query.FindOptions
import dev.morphia.query.Sort
import javabot.dao.util.QueryParam
import javabot.model.Activity
import javabot.model.Channel
import javabot.model.criteria.ChannelCriteria.Companion.logged
import javabot.model.criteria.ChannelCriteria.Companion.name
import javabot.model.criteria.ChannelCriteria.Companion.upperName
import org.apache.commons.lang.StringUtils
import java.time.LocalDateTime
import java.util.ArrayList
import java.util.Locale

@SuppressWarnings("ConstantNamingConvention")
class ChannelDao @Inject constructor(ds: Datastore) : BaseDao<Channel>(ds, Channel::class.java) {

    fun delete(name: String) {
        ds.find(Channel::class.java)
                .filter(name().eq(name))
                .delete()
    }

    @Suppress("UNCHECKED_CAST")
    fun configuredChannels(): List<String> {
        return ds.find(Channel::class.java)
                .iterator()
                .toList()
                .map { it.name }
    }

    fun getChannels(): List<Channel> {
        return getChannels(false)
    }

    fun getChannels(showAll: Boolean): List<Channel> {
        val query = ds.find(Channel::class.java)
        if (!showAll) {
            query.filter(logged().eq(true))
        }
        return query.iterator(FindOptions()
                        .sort(Sort.ascending(name)))
                .toList()
    }

    fun find(qp: QueryParam): List<Channel> {
        return getQuery().iterator(FindOptions()
                        .sort(qp.toSort()))
                .toList()
    }

    fun isLogged(channel: String): Boolean {
        val chan = get(channel)
        return chan?.logged ?: java.lang.Boolean.FALSE
    }

    fun get(name: String): Channel? {
        return ds.find(Channel::class.java)
                .filter(upperName().eq(name.uppercase(Locale.getDefault())))
                .first()
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
        val channels = ds.find(Channel::class.java)
                .filter(logged().eq(true)).iterator(FindOptions()
                        .projection().include(name))
                .toList()
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
        if (channel != null) {
            location = if (isLogged(channel.name)) channel.name else "private channel"
        } else {
            location = channel?.name ?: "private message"
        }
        return location
    }
}
