package javabot.dao

import dev.morphia.Datastore
import dev.morphia.query.FindOptions
import dev.morphia.query.Sort
import dev.morphia.query.filters.Filters.eq
import dev.morphia.query.filters.Filters.or
import jakarta.inject.Inject
import javabot.model.Admin
import javabot.model.JavabotUser

class AdminDao @Inject constructor(ds: Datastore, var configDao: ConfigDao) :
    BaseDao<Admin>(ds, Admin::class.java) {
    override fun findAll(): List<Admin> {
        return ds.find(Admin::class.java, FindOptions().sort(Sort.ascending("ircName"))).toList()
    }

    fun isAdmin(user: JavabotUser): Boolean = findAll().isEmpty() || getAdmin(user) != null

    fun getAdmin(ircName: String, hostName: String) =
        ds.find(Admin::class.java).filter(eq("ircName", ircName), eq("hostName", hostName)).first()

    fun getAdmin(user: JavabotUser) =
        ds.find(Admin::class.java)
            .filter(or(eq("ircName", user.nick), eq("emailAddress", user.userName)))
            .first()

    fun getAdminByEmailAddress(email: String) =
        ds.find(Admin::class.java).filter(eq("emailAddress", email)).first()

    fun create(ircName: String, userName: String, hostName: String): Admin {
        val admin = Admin(ircName, userName, hostName, findAll().isEmpty())

        save(admin)

        return admin
    }

    fun count(): Long {
        return getQuery().count()
    }
}
