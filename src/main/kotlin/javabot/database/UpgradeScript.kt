package javabot.database

import javabot.dao.ConfigDao
import java.io.IOException
import java.sql.SQLException
import javax.inject.Inject

public abstract class UpgradeScript {
    @Inject
    lateinit var configDao: ConfigDao

    public fun execute() {
        val config = configDao.get()
        if (config.schemaVersion < id()) {
            try {
                doUpgrade()
                registerUpgrade()
            } catch (e: Exception) {
                throw RuntimeException(e.message, e)
            }

        }
    }

    public fun registerUpgrade() {
        val config = configDao.get()
        config.schemaVersion = id()
        configDao.save(config)
    }

    public abstract fun id(): Int

    @Throws(SQLException::class, IOException::class)
    public abstract fun doUpgrade()

    override fun toString(): String {
        return javaClass.name + ":" + id()
    }
}
