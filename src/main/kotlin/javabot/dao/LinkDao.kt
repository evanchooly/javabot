package javabot.dao

import com.antwerkz.sofia.Sofia
import com.mongodb.client.result.DeleteResult
import dev.morphia.Datastore
import dev.morphia.query.FindOptions
import dev.morphia.query.experimental.filters.Filters
import dev.morphia.query.experimental.filters.Filters.regex
import dev.morphia.query.internal.MorphiaCursor
import javabot.dao.util.QueryParam
import javabot.model.Link
import javabot.model.Persistent
import java.time.LocalDateTime
import java.util.regex.PatternSyntaxException
import javax.inject.Inject

class LinkDao @Inject constructor(ds: Datastore, var changeDao: ChangeDao, var configDao: ConfigDao) : BaseDao<Link>(ds, Link::class.java) {
    override fun save(entity: Persistent) {
        val link = entity as Link
        link.updated = LocalDateTime.now()
        super.save(link)
    }

    fun deleteAll(): DeleteResult {
        return ds.find(Link::class.java).remove()
    }

    fun addLink(channel: String, user: String, url: String, text: String) {
        save(Link(channel, user, url, text, false))
    }

    private fun buildFindQuery(qp: QueryParam?, filter: Link, count: Boolean): MorphiaCursor<Link> {
        val query = ds.find(Link::class.java)
        if (filter.channel != "") {
            try {
                query.filter(regex("channel")
                                .pattern(filter.channel))
            } catch (e: PatternSyntaxException) {
                Sofia.logFactoidInvalidSearchValue(filter.channel)
            }
        }

        if (filter.url != "") {
            try {
                query.filter(regex("url")
                        .pattern(filter.url))
            } catch (e: PatternSyntaxException) {
                Sofia.logFactoidInvalidSearchValue(filter.url)
            }
        }

        query.filter(Filters.eq("approved", filter.approved))

        val options = FindOptions()
        if (!count && qp != null) {
            if (qp.hasSort()) {
                options.sort(qp.toSort())
            }
            options.skip(qp.first)
            options.limit(qp.count)
        }
        return query.execute(options)
    }

    fun get(link: Link): Link? {
        return buildFindQuery(null, link, false).tryNext()
    }

    fun approveLink(channel: String, id: String) {
        unapprovedLinks(channel).firstOrNull { it.id.toString().endsWith(id) }?.let {
            it.approved = true
            save(it)
        } ?: throw IllegalArgumentException("No matching unapproved link matching id $id")
    }

    fun rejectUnapprovedLink(channel: String, id: String) {
        unapprovedLinks(channel).firstOrNull { it.id.toString().endsWith(id) }?.let {
            delete(it.id)
        } ?: throw IllegalArgumentException("No matching unapproved link matching id $id")
    }

    fun unapprovedLinks(channel: String): List<Link> {
        return buildFindQuery(
                QueryParam(0, 100, "created", false), Link(channel = channel), false
        ).toList()
    }

    fun approvedLinks(channel: String): List<Link> {
        return buildFindQuery(
                QueryParam(0, 100, "created", false), Link(approved = true, channel = channel), true
        ).toList()
    }
}
