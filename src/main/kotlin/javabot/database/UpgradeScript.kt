package javabot.database

import javabot.dao.ConfigDao
import javabot.model.Config

import javax.inject.Inject
import java.io.IOException
import java.sql.SQLException
import java.util.Comparator
import java.util.ServiceLoader
import java.util.TreeSet

public abstract class UpgradeScript {
    Inject
    private val configDao: ConfigDao? = null

    public fun execute() {
        val config = configDao!!.get()
        if (config.schemaVersion < id()) {
            try {
                doUpgrade()
                registerUpgrade()
            } catch (e: Exception) {
                throw RuntimeException(e.getMessage(), e)
            }

        }
    }

    public fun registerUpgrade() {
        val config = configDao!!.get()
        config.schemaVersion = id()
        configDao.save(config)
    }

    public abstract fun id(): Int

    Throws(SQLException::class, IOException::class)
    public abstract fun doUpgrade()

    override fun toString(): String {
        return javaClass.name + ":" + id()
    }
}
