package javabot.database

import jakarta.inject.Inject
import java.io.IOException
import java.sql.SQLException
import javabot.dao.ConfigDao

abstract class UpgradeScript @Inject constructor(var configDao: ConfigDao) {
    fun execute() {
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

    fun registerUpgrade() {
        val config = configDao.get()
        config.schemaVersion = id()
        configDao.save(config)
    }

    abstract fun id(): Int

    @Throws(SQLException::class, IOException::class) abstract fun doUpgrade()

    override fun toString(): String {
        return javaClass.name + ":" + id()
    }
}
