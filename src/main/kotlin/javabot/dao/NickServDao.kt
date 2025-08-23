package javabot.dao

import com.google.inject.Inject
import dev.morphia.Datastore
import dev.morphia.DeleteOptions
import dev.morphia.UpdateOptions
import dev.morphia.query.filters.Filters.eq
import dev.morphia.query.filters.Filters.or
import dev.morphia.query.updates.UpdateOperators.set
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javabot.model.JavabotUser
import javabot.model.NickServInfo

open class NickServDao @Inject constructor(ds: Datastore) :
    BaseDao<NickServInfo>(ds, NickServInfo::class.java) {
    fun clear() = getQuery().delete(DeleteOptions().multi(true))

    fun process(list: List<String>) {
        val info = NickServInfo()
        val split = list[0].split(" ")
        info.nick = split[2].lowercase(Locale.getDefault())
        info.account = split[4].substring(0, split[4].indexOf(')')).lowercase(Locale.getDefault())
        list
            .subList(1, list.size)
            .filter { line -> line.contains(":") }
            .forEach { line ->
                val (key, value) = line.split(':')
                when (key.uppercase(Locale.getDefault())) {
                    "REGISTERED" -> info.registered = extractDate(value)
                    "USER REG." -> info.userRegistered = extractDate(value)
                    "LAST SEEN" -> info.lastSeen = extractDate(value)
                    "LAST ADDR" -> info.lastAddress = value
                    else -> info.extra(key.replace(".", ""), value)
                }
            }
        var nickServInfo: NickServInfo? = find(info.account)

        if (nickServInfo == null) {
            nickServInfo = find(info.nick)
        }

        if (nickServInfo != null) {
            nickServInfo.nick = info.nick
            nickServInfo.lastSeen = info.lastSeen
            nickServInfo.lastAddress = info.lastAddress
            save(nickServInfo)
        } else {
            save(info)
        }
    }

    private fun extractDate(line: String): LocalDateTime {
        return if (!line.endsWith("now")) {
            val dateString = if (line.contains("(")) line.substring(0, line.indexOf(" (")) else line
            LocalDateTime.parse(
                dateString,
                DateTimeFormatter.ofPattern(NickServInfo.NSERV_DATE_FORMAT),
            )
        } else {
            LocalDateTime.now()
        }
    }

    open fun find(name: String): NickServInfo? {
        return ds.find(NickServInfo::class.java)
            .filter(
                or(
                    eq("nick", name.lowercase(Locale.getDefault())),
                    eq("account", name.lowercase(Locale.getDefault())),
                )
            )
            .first()
    }

    fun updateNick(oldNick: String, newNick: String): NickServInfo? {
        ds.find(NickServInfo::class.java)
            .filter(eq("nick", oldNick))
            .update(set("nick", newNick))
            .execute(UpdateOptions().multi(false))

        return ds.find(NickServInfo::class.java).filter(eq("nick", newNick)).first()
    }

    fun unregister(user: JavabotUser) {
        ds.find(NickServInfo::class.java).filter(eq("nick", user.nick)).delete()
    }
}
