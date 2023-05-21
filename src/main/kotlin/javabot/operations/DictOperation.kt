package javabot.operations

import com.google.inject.Inject
import javabot.Javabot
import javabot.dao.AdminDao

class DictOperation @Inject constructor(bot: Javabot, adminDao: AdminDao) :
    UrlOperation(bot, adminDao) {
    override fun getBaseUrl(): String {
        return "http://dictionary.reference.com/browse/"
    }

    override fun getTrigger(): String {
        return "dict "
    }
}
