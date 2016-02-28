package javabot.kotlin.web.views

import com.google.inject.assistedinject.Assisted
import javabot.Javabot
import javabot.dao.AdminDao
import javabot.dao.ChannelDao
import javabot.dao.ConfigDao
import javabot.dao.FactoidDao
import javabot.model.Config
import javabot.operations.BotOperation
import java.util.ArrayList
import java.util.Collections
import java.util.TreeSet
import javax.inject.Inject
import javax.servlet.http.HttpServletRequest

class ConfigurationView @Inject constructor(
        adminDao: AdminDao,
        channelDao: ChannelDao,
        factoidDao: FactoidDao,
        var configDao: ConfigDao,
        var javabot: Javabot,
        @Assisted request: HttpServletRequest) :
        MainView(adminDao, channelDao, factoidDao, request) {


    val configuration: Config by lazy {
        configDao.get()
    }

    fun operations(): List<BotOperation> {
        val all = ArrayList(javabot.getAllOperations().values)
        Collections.sort(all) { left, right -> left.getName().compareTo(right.getName()) }
        return all
    }

    fun getCurrentOps(): Set<String> {
        return TreeSet(configuration.operations)
    }

    fun enabled(operation: String): Boolean {
        return getCurrentOps().contains(operation)
    }

    override fun getChildView(): String {
        return "admin/configuration.ftl"
    }
}
