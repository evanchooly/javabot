package javabot.operations

import com.google.inject.Inject
import javabot.Javabot
import javabot.dao.AdminDao

class GoogleOperation @Inject constructor(bot: Javabot, adminDao: AdminDao) : UrlOperation(bot, adminDao) {
    override fun getBaseUrl(): String {
        return "http://letmegooglethatforyou.com/?q="
    }

    override fun getTrigger(): String {
        return "google "
    }
}