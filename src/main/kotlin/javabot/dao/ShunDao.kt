package javabot.dao

import java.time.LocalDateTime

import javabot.model.Shun
import javabot.model.criteria.ShunCriteria

public class ShunDao : BaseDao<Shun>(Shun::class.java) {

    public fun isShunned(nick: String): Boolean {
        return getShun(nick) != null
    }

    public fun getShun(nick: String): Shun? {
        expireShuns()
        val criteria = ShunCriteria(ds)
        criteria.upperNick().equal(nick.toUpperCase())
        return criteria.query().get()
    }

    private fun expireShuns() {
        val criteria = ShunCriteria(ds)
        criteria.expiry().lessThan(LocalDateTime.now())
        ds.delete(criteria.query())
    }

    public fun addShun(nick: String, until: LocalDateTime) {
        var shun = getShun(nick)
        if (shun == null) {
            shun = Shun()
            shun.nick = nick.toUpperCase()
            shun.expiry = until
            save(shun)
        }
    }
}
