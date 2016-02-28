package javabot.model.criteria

import com.antwerkz.critter.TypeSafeFieldEnd
import javabot.model.IrcUser
import org.mongodb.morphia.query.Criteria
import org.mongodb.morphia.query.Query

class IrcUserCriteria(private val query: Query<IrcUser>, prefix: String) {
    private val prefix: String

    init {
        this.prefix = prefix + "."
    }

    fun host(): TypeSafeFieldEnd<IrcUserCriteria, IrcUser, String> {
        return TypeSafeFieldEnd<IrcUserCriteria, IrcUser, String>(
              this, query, prefix + "host")
    }

    fun host(value: String): Criteria {
        return TypeSafeFieldEnd<IrcUserCriteria, IrcUser, String>(
              this, query, prefix + "host").equal(value)
    }

    fun nick(): TypeSafeFieldEnd<IrcUserCriteria, IrcUser, String> {
        return TypeSafeFieldEnd<IrcUserCriteria, IrcUser, String>(
              this, query, prefix + "nick")
    }

    fun nick(value: String): Criteria {
        return TypeSafeFieldEnd<IrcUserCriteria, IrcUser, String>(
              this, query, prefix + "nick").equal(value)
    }

    fun userName(): TypeSafeFieldEnd<IrcUserCriteria, IrcUser, String> {
        return TypeSafeFieldEnd<IrcUserCriteria, IrcUser, String>(
              this, query, prefix + "userName")
    }

    fun userName(value: String): Criteria {
        return TypeSafeFieldEnd<IrcUserCriteria, IrcUser, String>(
              this, query, prefix + "userName").equal(value)
    }
}
