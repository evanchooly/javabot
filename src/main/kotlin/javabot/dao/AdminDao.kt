package javabot.dao

import java.time.LocalDateTime
import javax.inject.Inject

import javabot.model.Admin
import javabot.model.Config
import javabot.model.EventType
import javabot.model.OperationEvent
import javabot.model.criteria.AdminCriteria
import org.pircbotx.User

public class AdminDao : BaseDao<Admin>(Admin::class.java) {
    Inject
    private val configDao: ConfigDao? = null

    override fun findAll(): List<Admin> {
        return ds.createQuery(Admin::class.java).order("userName").asList()
    }

    public fun isAdmin(user: User): Boolean {
        return findAll().isEmpty() || getAdmin(user) != null
    }

    public fun getAdmin(ircName: String, hostName: String): Admin {
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

    public fun getAdminByEmailAddress(email: String): Admin {
        val criteria = AdminCriteria(ds)
        criteria.emailAddress(email)
        var admin = criteria.query().get()
        if (admin == null && count() == 0) {
            admin = Admin()
            admin!!.emailAddress = email
            admin!!.botOwner = true
            save(admin)
        }
        return admin
    }

    public fun create(ircName: String, userName: String, hostName: String): Admin {
        val admin = Admin()
        admin.ircName = ircName
        admin.emailAddress = userName
        admin.hostName = hostName
        admin.updated = LocalDateTime.now()
        admin.botOwner = findAll().isEmpty()
        save(admin)

        return admin
    }

    public fun enableOperation(name: String, admin: Admin) {
        val event = OperationEvent()
        event.operation = name
        event.requestedOn = LocalDateTime.now()
        event.type = EventType.ADD
        event.requestedBy = admin.emailAddress
        save(event)
        val config = configDao!!.get()
        config.operations.add(name)
        configDao.save(config)
    }

    public fun disableOperation(name: String, admin: Admin) {
        val event = OperationEvent()
        event.operation = name
        event.requestedOn = LocalDateTime.now()
        event.type = EventType.DELETE
        event.requestedBy = admin.emailAddress
        save(event)
        val config = configDao!!.get()
        config.operations.remove(name)
        configDao.save(config)
    }

    public fun count(): Long {
        return query.countAll()
    }
}