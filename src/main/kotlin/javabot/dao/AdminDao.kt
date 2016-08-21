package javabot.dao

import javabot.model.Admin
import javabot.model.EventType
import javabot.model.JavabotUser
import javabot.model.OperationEvent
import javabot.model.criteria.AdminCriteria
import org.mongodb.morphia.Datastore
import javax.inject.Inject

class AdminDao @Inject constructor(ds: Datastore, var configDao: ConfigDao) : BaseDao<Admin>(ds, Admin::class.java) {

    override fun findAll(): List<Admin> {
        return ds.createQuery(Admin::class.java).order("userName").asList()
    }

    fun isAdmin(user: JavabotUser): Boolean {
        return findAll().isEmpty() || getAdmin(user) != null
    }

    fun getAdmin(ircName: String, hostName: String): Admin? {
        val adminCriteria = AdminCriteria(ds)
        adminCriteria.ircName().equal(ircName)
        adminCriteria.hostName().equal(hostName)
        return adminCriteria.query().get()
    }

    fun getAdmin(user: JavabotUser): Admin? {
        val adminCriteria = AdminCriteria(ds)
        adminCriteria.or(
                adminCriteria.ircName(user.nick),
                adminCriteria.emailAddress(user.userName)
        )
        return adminCriteria.query().get()
    }

    fun getAdminByEmailAddress(email: String): Admin? {
        val criteria = AdminCriteria(ds)
        criteria.emailAddress(email)

        return criteria.query().get()
    }

    fun create(ircName: String, userName: String, hostName: String): Admin {
        val admin = Admin(ircName, userName, hostName, findAll().isEmpty())

        save(admin)

        return admin
    }

    fun enableOperation(name: String, admin: Admin) {
        save(OperationEvent(admin.emailAddress, EventType.ADD, name))
        val config = configDao.get()
        config.operations.add(name)
        configDao.save(config)
    }

    fun disableOperation(name: String, admin: Admin) {
        save(OperationEvent(admin.emailAddress, EventType.DELETE, name))
        val config = configDao.get()
        config.operations.remove(name)
        configDao.save(config)
    }

    fun count(): Long {
        return getQuery().countAll()
    }
}