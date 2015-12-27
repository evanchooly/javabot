package javabot

import com.google.inject.Singleton
import org.pircbotx.PircBotX
import org.pircbotx.User

@Singleton
public class TestJavabot : Javabot() {
    override fun getNick(): String {
        return BaseTest.TEST_BOT_NICK
    }

    override fun isOnCommonChannel(user: User): Boolean {
        return true
    }
}
