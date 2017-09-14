package javabot.model.criteria

import com.antwerkz.critter.TypeSafeFieldEnd
import com.antwerkz.critter.criteria.BaseCriteria
import javabot.model.Link
import org.bson.types.ObjectId
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Criteria
import java.time.LocalDateTime

object LinkDescriptor {
    val id: String = "id"
    val channel: String = "channel"
    val username: String = "username"
    val url: String = "url"
    val info: String = "info"
    val upperName: String = "upperName"
    val updated: String = "updated"
    val approved: String = "approved"
}

class LinkCriteria(ds: Datastore) : BaseCriteria<Link>(ds, Link::class.java) {
    fun id(): TypeSafeFieldEnd<LinkCriteria, Link, ObjectId> {
        return TypeSafeFieldEnd<LinkCriteria, Link, org.bson.types.ObjectId>(
                this, query, "id")
    }

    fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<LinkCriteria, Link, ObjectId>(
                this, query, "id").equal(value)
    }

    fun channel(): TypeSafeFieldEnd<LinkCriteria, Link, String> {
        return TypeSafeFieldEnd<LinkCriteria, Link, String>(
                this, query, LinkDescriptor.channel)
    }

    fun channel(value: String?): Criteria {
        return TypeSafeFieldEnd<LinkCriteria, Link, String>(
                this, query, LinkDescriptor.channel).equal(value)
    }

    fun username(): TypeSafeFieldEnd<LinkCriteria, Link, String> {
        return TypeSafeFieldEnd<LinkCriteria, Link, String>(
                this, query, LinkDescriptor.username)
    }

    fun username(value: String?): Criteria {
        return TypeSafeFieldEnd<LinkCriteria, Link, String>(
                this, query, LinkDescriptor.username).equal(value)
    }

    fun url(): TypeSafeFieldEnd<LinkCriteria, Link, String> {
        return TypeSafeFieldEnd<LinkCriteria, Link, String>(
                this, query, LinkDescriptor.url)
    }

    fun url(value: String?): Criteria {
        return TypeSafeFieldEnd<LinkCriteria, Link, String>(
                this, query, LinkDescriptor.url).equal(value)
    }

    fun upperName(): TypeSafeFieldEnd<LinkCriteria, Link, String> {
        return TypeSafeFieldEnd<LinkCriteria, Link, String>(
                this, query, LinkDescriptor.upperName)
    }

    fun upperName(value: String?): Criteria {
        return TypeSafeFieldEnd<LinkCriteria, Link, String>(
                this, query, LinkDescriptor.upperName).equal(value)
    }


    fun info(): TypeSafeFieldEnd<LinkCriteria, Link, String> {
        return TypeSafeFieldEnd<LinkCriteria, Link, String>(
                this, query, LinkDescriptor.info)
    }

    fun info(value: String?): Criteria {
        return TypeSafeFieldEnd<LinkCriteria, Link, String>(
                this, query, LinkDescriptor.info).equal(value)
    }

    fun approved(): TypeSafeFieldEnd<LinkCriteria, Link, Boolean> {
        return TypeSafeFieldEnd<LinkCriteria, Link, Boolean>(
                this, query, LinkDescriptor.approved)
    }

    fun approved(value: Boolean?): Criteria {
        return TypeSafeFieldEnd<LinkCriteria, Link, Boolean>(
                this, query, LinkDescriptor.approved).equal(value)
    }

    fun updated(): TypeSafeFieldEnd<LinkCriteria, Link, LocalDateTime> {
        return TypeSafeFieldEnd<LinkCriteria, Link, java.time.LocalDateTime>(
                this, query, LinkDescriptor.updated)
    }

    fun updated(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<LinkCriteria, Link, java.time.LocalDateTime>(
                this, query, "updated").equal(value)
    }
}