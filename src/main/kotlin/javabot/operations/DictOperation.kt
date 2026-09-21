package javabot.operations

import jakarta.inject.Inject
import jakarta.inject.Singleton
import javabot.Javabot
import javabot.dao.AdminDao

@Singleton
class DictOperation @Inject constructor(bot: Javabot, adminDao: AdminDao) :
    UrlOperation(bot, adminDao) {
    override fun getBaseUrl(): String {
        return "http://dictionary.reference.com/browse/"
    }

    override fun getTrigger(): String {
        return "dict "
    }
}
