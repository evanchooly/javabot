package javabot.model

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Id
import org.mongodb.morphia.annotations.Indexed
import org.pircbotx.User
import java.lang.String.format
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter
import java.util.ArrayList
import java.util.TreeMap

@Entity(value = "nickserv", noClassnameStored = true)
public class NickServInfo : Persistent {

    @Id
    var id: ObjectId? = null

    @Indexed
    lateinit var nick: String

    @Indexed
    lateinit var account: String

    @Indexed(expireAfterSeconds = 60 * 60 * 24)
    private val created = now()

    public var registered = LocalDateTime.now()

    public var userRegistered = registered

    private val extraneous = TreeMap<String, String>()

    public var lastAddress: String? = null

    public var lastSeen: LocalDateTime? = null

    public constructor() {
    }

    public constructor(user: User) {
        nick = user.nick
        account = user.nick
        registered = LocalDateTime.now()
        userRegistered = LocalDateTime.now()
    }

    public fun extra(key: String, value: String) {
        extraneous.put(key, value)
    }

    override fun toString(): String {
        return "NickServInfo{id=${id}, nick='${nick}', account='${account}', registered=${registered}, userRegistered=${userRegistered}, " +
                "lastSeen=${lastSeen}," + " " + "lastAddress='${lastAddress}'}"
    }

    public fun toNickServFormat(): List<String> {
        //    "Information on cheeser (account cheeser):",
        //    "Registered : Feb 20 21:31:56 2002 (12 years, 10 weeks, 2 days, 04:48:12 ago)",
        //    "Last seen  : now",
        //    "Flags      : HideMail, Private",
        //    "cheeser has enabled nick protection",
        //    "*** End of Info ***"
        val list = ArrayList<String>()
        list.add(format("Information on %s (account %s):", nick, account))
        append(list, "Registered", toString(registered))
        append(list, "User Reg.", toString(userRegistered))
        append(list, "Last seen", toString(lastSeen))
        list.add("*** End of Info ***")
        return list
    }

    private fun append(list: MutableList<String>, label: String, value: String?) {
        if (value != null) {
            list.add(format("%-12s: %s", label, value))
        }
    }

    private fun toString(date: LocalDateTime?): String? {
        return date?.format(DateTimeFormatter.ofPattern(NSERV_DATE_FORMAT))
    }

    companion object {
        public val NSERV_DATE_FORMAT: String = "MMM dd HH:mm:ss yyyy"
    }
}
