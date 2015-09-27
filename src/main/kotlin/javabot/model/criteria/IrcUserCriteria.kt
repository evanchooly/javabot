package javabot.model.criteria

import com.antwerkz.critter.TypeSafeFieldEnd
import javabot.model.IrcUser
import org.mongodb.morphia.query.Criteria
import org.mongodb.morphia.query.Query

public class IrcUserCriteria(private val query: Query<IrcUser>, prefix: String) {
    private val prefix: String

    init {
        this.prefix = prefix + "."
    }

    public fun host(): TypeSafeFieldEnd<IrcUserCriteria, IrcUser, String> {
        return TypeSafeFieldEnd<IrcUserCriteria, IrcUser, String>(
              this, query, prefix + "host")
    }

    public fun host(value: String): Criteria {
        return TypeSafeFieldEnd<IrcUserCriteria, IrcUser, String>(
              this, query, prefix + "host").equal(value)
    }

    public fun nick(): TypeSafeFieldEnd<IrcUserCriteria, IrcUser, String> {
        return TypeSafeFieldEnd<IrcUserCriteria, IrcUser, String>(
              this, query, prefix + "nick")
    }

    public fun nick(value: String): Criteria {
        return TypeSafeFieldEnd<IrcUserCriteria, IrcUser, String>(
              this, query, prefix + "nick").equal(value)
    }

    public fun userName(): TypeSafeFieldEnd<IrcUserCriteria, IrcUser, String> {
        return TypeSafeFieldEnd<IrcUserCriteria, IrcUser, String>(
              this, query, prefix + "userName")
    }

    public fun userName(value: String): Criteria {
        return TypeSafeFieldEnd<IrcUserCriteria, IrcUser, String>(
              this, query, prefix + "userName").equal(value)
    }
}
