package javabot.operations

import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import org.pircbotx.User
import javax.inject.Inject

abstract class BotOperation constructor(var bot: Javabot, var adminDao: AdminDao) {
    /**
     * This method returns where the operation should fall in terms of priority. Lower values represent lower
     * priority. Implementations can probably get by with the default implementation (which returns a priority of 10)
     * but some commands may want to override this.
     * @return the priority of the command
     */
    open fun getPriority(): Int {
        return 10
    }

    /**
     * @return true if the message has been handled
     */
    open fun handleMessage(event: Message): List<Message> {
        return listOf()
    }

    open fun handleChannelMessage(event: Message): List<Message> {
        return listOf()
    }

    fun getName(): String {
        return javaClass.simpleName.replace("Operation", "")
    }

    override fun toString(): String {
        return getName()
    }

    protected fun isAdminUser(user: User): Boolean {
        return adminDao.isAdmin(user)
    }
}