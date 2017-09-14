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
        save(Link(channel, user, url, text, false))
    }

    private fun buildFindQuery(qp: QueryParam?, filter: Link, count: Boolean): Query<Link> {
        val criteria = LinkCriteria(ds)
        if (filter.channel != "") {
            try {
                criteria.channel().contains(filter.channel)
            } catch (e: PatternSyntaxException) {
                Sofia.logFactoidInvalidSearchValue(filter.channel)
            }
        }

        if (filter.url != "") {
            try {
                criteria.url().contains(filter.url)
            } catch (e: PatternSyntaxException) {
                Sofia.logFactoidInvalidSearchValue(filter.url)
            }
        }

        criteria.approved().equal(filter.approved)

        if (!count && qp != null && qp.hasSort()) {
            criteria.query().order((if (qp.sortAsc) "" else "-") + qp.sort)
            criteria.query().offset(qp.first)
            criteria.query().limit(qp.count)
        }
        return criteria.query()
    }

    fun get(link: Link): Link? {
        return buildFindQuery(null, link, false).get()
    }

    fun approveLink(channel: String, id: String) {
        unapprovedLinks(channel)
                .filter { it.id.toString().endsWith(id) }
                .firstOrNull()
                ?.let {
                    it.approved = true
                    save(it)
                } ?: throw IllegalArgumentException("No matching unapproved link matching id $id")

    }

    fun rejectUnapprovedLink(channel: String, id: String) {
        unapprovedLinks(channel)
                .filter { it.id.toString().endsWith(id) }
                .firstOrNull()
                ?.let {
                    delete(it.id)
                } ?: throw IllegalArgumentException("No matching unapproved link matching id $id")
    }

    fun unapprovedLinks(channel: String): List<Link> {
        return buildFindQuery(
                QueryParam(0, 100, "created", false),
                Link(channel = channel),
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