package javabot

import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import javabot.dao.ConfigDao
import javax.net.ssl.SSLSocketFactory
import org.pircbotx.Configuration.Builder
import org.pircbotx.PircBotX
import org.pircbotx.cap.SASLCapHandler

@ApplicationScoped
class IrcBotProducer {
    @Produces
    @ApplicationScoped
    fun ircBot(configDao: ConfigDao, ircAdapter: IrcAdapter): PircBotX {
        val config = configDao.get()
        val nick = config.nick
        val builder =
            Builder()
                .setName(nick)
                .setLogin(nick)
                .setAutoNickChange(false)
                .setCapEnabled(false)
                .addListener(ircAdapter)
                .addServer(config.server, config.port)
                .addCapHandler(SASLCapHandler(nick, config.password))
                .setSocketFactory(SSLSocketFactory.getDefault())
        return PircBotX(builder.buildConfiguration())
    }
}
