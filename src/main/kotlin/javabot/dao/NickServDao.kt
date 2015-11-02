package javabot.dao

import javabot.model.NickServInfo
import javabot.model.criteria.NickServInfoCriteria
import org.pircbotx.User
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

public open class NickServDao : BaseDao<NickServInfo>(NickServInfo::class.java) {

    public fun clear() {
        ds.delete(getQuery())
    }

    public fun process(list: List<String>) {
        val info = NickServInfo()
        val split = list.get(0).split(" ")
        info.nick = split[2].toLowerCase()
        info.account = split[4].substring(0, split[4].indexOf(')')).toLowerCase()
        list.subList(1, list.size).filter({ line -> line.contains(":") }).forEach({ line ->
            val i = line.indexOf(':')
            val key = line.substring(0, i).trim()
            val value = line.substring(i + 1).trim()
            if (key.equals("Registered", true)) {
                info.registered = extractDate(value)
            } else if (key.equals("User Reg.", true)) {
                info.userRegistered = extractDate(value)
            } else if (key.equals("Last seen", true)) {
                info.lastSeen = extractDate(value)
            } else if (key.equals("Last addr", true)) {
                info.lastAddress = value
            } else {
                info.extra(key.replace(".", ""), value)
            }
        })

        var nickServInfo: NickServInfo? = find(info.account!!)

        if (nickServInfo == null) {
            nickServInfo = find(info.nick!!)
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
        if (line.endsWith("now")) {
            return LocalDateTime.now()
        } else {
            val dateString = if (line.contains("(")) line.substring(0, line.indexOf(" (")) else line
            return LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(NickServInfo.NSERV_DATE_FORMAT))
        }
    }

    public open fun find(name: String): NickServInfo? {
        val criteria = NickServInfoCriteria(ds)
        criteria.or(
              criteria.nick(name.toLowerCase()),
              criteria.account(name.toLowerCase()))
        return criteria.query().get()
    }

    public fun updateNick(oldNick: String, newNick: String): NickServInfo {
        var criteria = NickServInfoCriteria(ds)
        criteria.nick(oldNick)
        val updater = criteria.getUpdater()
        updater.nick(newNick)
        updater.updateFirst()
        criteria = NickServInfoCriteria(ds)
        criteria.nick(newNick)
        return criteria.query().get()
    }

    public fun unregister(user: User) {
        val criteria = NickServInfoCriteria(ds)
        criteria.nick(user.nick)
        criteria.delete()
    }
}
