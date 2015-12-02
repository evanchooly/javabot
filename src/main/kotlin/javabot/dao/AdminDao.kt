package javabot.dao

import javabot.model.Admin
import javabot.model.EventType
import javabot.model.OperationEvent
import javabot.model.criteria.AdminCriteria
import org.pircbotx.User
import javax.inject.Inject

public class AdminDao : BaseDao<Admin>(Admin::class.java) {
    @Inject
    lateinit var configDao: ConfigDao

    override fun findAll(): List<Admin> {
        return ds.createQuery(Admin::class.java).order("userName").asList()
    }

    public fun isAdmin(user: User): Boolean {
        return findAll().isEmpty() || getAdmin(user) != null
    }

    public fun getAdmin(ircName: String, hostName: String): Admin? {
        val adminCriteria = AdminCriteria(ds)
        adminCriteria.ircName().equal(ircName)
        adminCriteria.hostName().equal(hostName)
        return adminCriteria.query().get()
    }

    public fun getAdmin(user: User): Admin? {
        val adminCriteria = AdminCriteria(ds)
        adminCriteria.ircName(user.nick)
        return adminCriteria.query().get()
    }

    public fun getAdminByEmailAddress(email: String): Admin? {
        val criteria = AdminCriteria(ds)
        criteria.emailAddress(email)

        return criteria.query().get()
    }

    public fun create(ircName: String, userName: String, hostName: String): Admin {
        val admin = Admin(ircName, userName, hostName, findAll().isEmpty())

        save(admin)

        return admin
    }

    public fun enableOperation(name: String, admin: Admin) {
        save(OperationEvent(admin.emailAddress, EventType.ADD, name))
        val config = configDao.get()
        config.operations.add(name)
        configDao.save(config)
    }

    public fun disableOperation(name: String, admin: Admin) {
        save(OperationEvent(admin.emailAddress, EventType.DELETE, name))
        val config = configDao.get()
        config.operations.remove(name)
        configDao.save(config)
    }

    public fun count(): Long {
        return getQuery().countAll()
    }
}