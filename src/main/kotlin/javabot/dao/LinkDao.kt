package javabot.dao

import com.antwerkz.sofia.Sofia
import com.mongodb.WriteResult
import javabot.dao.util.QueryParam
import javabot.model.Link
import javabot.model.Persistent
import javabot.model.criteria.LinkCriteria
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Query
import java.time.LocalDateTime
import java.util.regex.PatternSyntaxException
import javax.inject.Inject

class LinkDao @Inject constructor(ds: Datastore, var changeDao: ChangeDao, var configDao: ConfigDao) :
        BaseDao<Link>(ds, Link::class.java) {
    override fun save(entity: Persistent) {
        val link = entity as Link
        link.updated = LocalDateTime.now()
        super.save(link)
    }

    fun deleteAll(): WriteResult {
        return ds.delete(ds.createQuery(Link::class.java))
    }

    fun addLink(channel: String, user: String, url: String, text: String) {
        val link = Link(channel, user, url, text, false)
        link.created = LocalDateTime.now()
        save(link)
    }

    private fun buildFindQuery(qp: QueryParam?, filter: Link, count: Boolean): Query<Link> {
        val criteria = LinkCriteria(ds)
        if (!filter.channel.isNullOrEmpty()) {
            try {
                criteria.channel().contains(filter.channel)
            } catch (e: PatternSyntaxException) {
                Sofia.logFactoidInvalidSearchValue(filter.channel)
            }
        }

        if (!filter.url.isNullOrEmpty()) {
            try {
                criteria.url().contains(filter.url)
            } catch (e: PatternSyntaxException) {
                Sofia.logFactoidInvalidSearchValue(filter.url)
            }
        }

        if (filter.approved != null) {
            try {
                criteria.approved().equal(filter.approved)
            } catch (e: PatternSyntaxException) {
                Sofia.logFactoidInvalidSearchValue(filter.approved)
            }
        }

        if (!count && qp != null && qp.hasSort()) {
            criteria.query().order((if (qp.sortAsc) "" else "-") + qp.sort)
            criteria.query().offset(qp.first)
            criteria.query().limit(qp.count)
        }
        return criteria.query()
    }

    operator fun get(link: Link): Link {
        return buildFindQuery(null, link, false).get()
    }

    fun approveLink(channel: String, id: String) {
        val link = unapprovedLinks(channel)
                .filter {
                    (if (it.id != null) {
                        it.id.toString()
                    } else "").endsWith(id)
                }.firstOrNull()
        if (link != null) {
            if (link.approved ?: true) {
                throw IllegalArgumentException("link for $id has already been approved")
            }
            link.approved = true
            save(link)
        } else {
            throw IllegalArgumentException("No matching unapproved link matching id $id")
        }
    }

    fun rejectUunapprovedLink(channel: String, id: String) {
        val link = unapprovedLinks(channel)
                .filter {
                    (if (it.id != null) {
                        it.id.toString()
                    } else "").endsWith(id)
                }.firstOrNull()
        if (link != null) {
            delete(link.id)
        } else {
            throw IllegalArgumentException("No matching unapproved link matching id $id")
        }
    }

    fun unapprovedLinks(channel: String): List<Link> {
        return buildFindQuery(
                QueryParam(0, 100, "created", false),
                Link(approved = false, channel = channel),
                false)
                .toList()
    }

    fun approvedLinks(channel: String): List<Link> {
        return buildFindQuery(
                QueryParam(0, 100, "created", false),
                Link(approved = true, channel = channel),
                true)
                .toList()
    }
}