package javabot.service

import com.google.inject.Singleton
import com.rivescript.Config
import com.rivescript.RiveScript

@Singleton
class RiveScriptService : RiveScript(Config.utf8()) {
    var updated=true
    fun load(resource: String) {
        this::class.java.getResourceAsStream(resource).use {
            loadInputStream(it)
        }
        updated=true
    }

    override fun reply(username: String?, message: String?): String {
        if(updated) {
            sortReplies()
        }
        return super.reply(username, message)
    }
}