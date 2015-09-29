package javabot.operations

import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import org.pircbotx.User

import javax.inject.Inject
import java.util.Random

public abstract class BotOperation {
    @Inject
    lateinit var bot: Javabot
    @Inject
    lateinit var adminDao: AdminDao

    /**
     * This method returns where the operation should fall in terms of priority. Lower values represent lower
     * priority. Implementations can probably get by with the default implementation (which returns a priority of 10)
     * but some commands may want to override this.
     * @return the priority of the command
     */
    public open fun getPriority(): Int {
        return 10
    }

    /**
     * @return true if the message has been handled
     */
    public open fun handleMessage(event: Message): Boolean {
        return false
    }

    public open fun handleChannelMessage(event: Message): Boolean {
        return false
    }

    public fun getName(): String {
        return javaClass.simpleName.replace("Operation".toRegex(), "")
    }

    override fun toString(): String {
        return getName()
    }

    protected fun isAdminUser(user: User): Boolean {
        return adminDao.isAdmin(user)
    }

    protected fun formatMessage(text: String, vararg messages: String): String {
        return formatMessage(text, random.nextInt(messages.size()), messages as Array<String>)
    }

    protected fun formatMessage(text: String, index: Int, messages: Array<String>): String {
        val format = messages[index]
        return format; //String.format(format, text)
    }

    companion object {
        var random = Random()
    }
}