package javabot.operations

import com.antwerkz.sofia.Sofia
import com.google.inject.Inject
import javabot.JavabotConfig
import javabot.Message
import javabot.dao.ApiDao
import javabot.dao.JavadocClassDao
import javabot.javadoc.JavadocApi
import net.swisstech.bitly.BitlyClient
import javax.annotation.Nullable
import java.util.ArrayList

public class JavadocOperation : BotOperation() {
    @Inject
    lateinit var apiDao: ApiDao

    @Inject
    lateinit var dao: JavadocClassDao

    @Inject
    lateinit var config: JavabotConfig

    @field:[Nullable Inject(optional = true)]
    var client: BitlyClient? = null
        get() {
            if (field == null) {
                field = if (config.bitlyToken() != "") BitlyClient(config.bitlyToken()) else null
            }
            return field;
        }

    override fun handleMessage(event: Message): Boolean {
        val message = event.value
        var handled = false
        if (message.toLowerCase().startsWith("javadoc")) {
            var key = message.substring("javadoc".length).trim()
            if (key.startsWith("-list") || key.isEmpty()) {
                displayApiList(event)
                handled = true
            } else {
                if (key.startsWith("-")) {
                    if (key.contains(" ")) {
                        val api = apiDao.find(key.substring(1, key.indexOf(" ")))
                        key = key.substring(key.indexOf(" ") + 1).trim()
                        handled = buildResponse(event, api, key)
                    } else {
                        displayApiList(event)
                        handled = true
                    }
                } else {
                    buildResponse(event, null, key)
                    handled = true
                }
            }
        }
        return handled
    }

    private fun buildResponse(event: Message, api: JavadocApi?, key: String): Boolean {
        val urls = handle(api, key)
        if (!urls.isEmpty()) {

            val nick = event.user.nick
            var urlMessage = StringBuilder(nick + ": ")
            urlMessage = buildResponse(event, urls, urlMessage)
            if (urls.size > RESULT_LIMIT) {
                bot.postMessageToChannel(event, Sofia.tooManyResults(nick))
                bot.postMessageToUser(event.user, urlMessage.toString())
            } else {
                bot.postMessageToChannel(event, urlMessage.toString())
            }
        } else {
            bot.postMessageToChannel(event, Sofia.noDocumentation(key))
        }

        return true
    }

    private fun buildResponse(event: Message, urls: List<String>, urlMessage: StringBuilder): StringBuilder {
        var message = urlMessage
        for (index in urls.indices) {
            if ((message.toString() + urls.get(index)).length > 400) {
                bot.postMessageToChannel(event, message.toString())
                message = StringBuilder()
            }
            message.append(if (index == 0) "" else "; ").append(urls.get(index))
        }
        return message
    }

    public fun handle(api: JavadocApi?, key: String): List<String> {
        val urls = ArrayList<String>()
        val openIndex = key.indexOf('(')
        if (openIndex == -1) {
            parseFieldOrClassRequest(urls, api, key)
        } else {
            parseMethodRequest(urls, api, key, openIndex)
        }
        return urls
    }

    private fun parseFieldOrClassRequest(urls: MutableList<String>, api: JavadocApi?, key: String) {
        val finalIndex = key.lastIndexOf('.')
        if (finalIndex == -1) {
            findClasses(api, urls, key)
        } else {
            val className = key.substring(0, finalIndex)
            val fieldName = key.substring(finalIndex + 1)
            if (Character.isUpperCase(fieldName[0]) && fieldName.toUpperCase() != fieldName) {
                findClasses(api, urls, key)
            } else {
                val list = dao.getField(api, className, fieldName)
                urls.addAll(list.map({ field -> field.getDisplayUrl(field.toString(), apiDao, client) }))
            }
        }
    }

    private fun findClasses(api: JavadocApi?, urls: MutableList<String>, key: String) {
        urls.addAll(dao.getClass(api, key).map(
              { javadocClass -> javadocClass.getDisplayUrl(javadocClass.toString(), apiDao, client) }))
    }

    private fun parseMethodRequest(urls: MutableList<String>, api: JavadocApi?, key: String, openIndex: Int) {
        val closeIndex = key.indexOf(')')
        if (closeIndex != -1) {
            val finalIndex = key.lastIndexOf('.', openIndex)
            val methodName: String
            var className: String
            if (finalIndex == -1) {
                methodName = key.substring(0, openIndex)
                className = methodName
            } else {
                methodName = key.substring(finalIndex + 1, openIndex)
                className = key.substring(0, finalIndex)
            }
            val signatureTypes = key.substring(openIndex + 1, closeIndex)
            val list = ArrayList<String>()

            //
            list.addAll(dao.getMethods(api, className, methodName, signatureTypes).map(
                  { method -> method.getDisplayUrl(method.toString(), apiDao, client) }))
            //

            if (list.isEmpty()) {
                className = methodName
                list.addAll(dao.getMethods(api, className, methodName, signatureTypes).map(
                      { method -> method.getDisplayUrl(method.toString(), apiDao, client) }))
            }

            urls.addAll(list)
        }
    }

    private fun displayApiList(event: Message) {
        val builder = StringBuilder()
        for (api in apiDao.findAll()) {
            if (builder.length != 0) {
                builder.append("; ")
            }
            builder.append(api.name)
        }
        bot.postMessageToChannel(event, Sofia.javadocApiList(event.user.nick, builder))
    }

    companion object {

        private val RESULT_LIMIT = 5
    }
}