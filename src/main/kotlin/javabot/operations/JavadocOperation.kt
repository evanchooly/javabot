package javabot.operations

import com.antwerkz.sofia.Sofia
import com.google.inject.Inject
import javabot.Javabot
import javabot.JavabotConfig
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.ApiDao
import javabot.dao.JavadocClassDao
import javabot.javadoc.JavadocApi
import net.swisstech.bitly.BitlyClient
import org.zeroturnaround.exec.ProcessExecutor
import java.io.File
import java.util.ArrayList
import javax.annotation.Nullable

class JavadocOperation @Inject constructor(bot: Javabot, adminDao: AdminDao, var apiDao: ApiDao,
     var dao: JavadocClassDao, var config: JavabotConfig) : BotOperation(bot, adminDao) {


    @field:[Nullable Inject(optional = true)]
    var bitly: BitlyClient? = null
        get() {
            if (field == null) {
                field = if (config.bitlyToken() != "") BitlyClient(config.bitlyToken()) else null
            }
            return field
        }

    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value
        if (message.toLowerCase().startsWith("javadoc")) {
            var key = message.substring("javadoc".length).trim()
            var api: JavadocApi? = null
            if (key.startsWith("-list") || key.isEmpty()) {
                displayApiList(responses, event)
            } else {
                if (key.startsWith("-")) {
                    if (key.contains(" ")) {
                        api = apiDao.find(key.substring(1, key.indexOf(" ")))
                        key = key.substring(key.indexOf(" ") + 1).trim()
                    } else {
                        displayApiList(responses, event)
                    }
                }

                buildResponse(responses, event, api, key)
            }
        }
        return responses
    }

    private fun buildResponse(responses: MutableList<Message>, event: Message, api: JavadocApi?, key: String) {
        val urls = handle(api, key)
        if (!urls.isEmpty()) {

            val nick = event.user.nick
            val urlMessage = StringBuilder(nick + ": ")
            val entries = buildResponse(event, urls, urlMessage)
            if (urls.size > RESULT_LIMIT) {
                responses.add(Message(event, Sofia.tooManyResults(nick)))
                responses.addAll(entries.map { Message(it.user, it.value)})
            } else {
                responses.addAll(entries)
            }
        } else {
            responses.add(Message(event, Sofia.noDocumentation(key)))
        }
    }

    private fun buildResponse(event: Message, urls: List<String>, urlMessage: StringBuilder): MutableList<Message> {
        val responses = arrayListOf<Message>()
        var message = urlMessage
        for (index in urls.indices) {
            if ((message.toString() + urls[index]).length > 400) {
                responses.add(Message(event, message.toString()))
                message = StringBuilder()
            }
            message
                    .append(if (index == 0) "" else "; ")
                    .append(urls[index])
        }
        responses.add(Message(event, message.toString()))

        return responses
    }

    fun handle(api: JavadocApi?, key: String): List<String> {
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
                urls.addAll(list.map({ field -> field.getDisplayUrl(field.toString(), apiDao, bitly) }))
            }
        }
    }

    private fun findClasses(api: JavadocApi?, urls: MutableList<String>, key: String) {
        urls.addAll(dao.getClass(api, key).map(
                { javadocClass -> javadocClass.getDisplayUrl(javadocClass.toString(), apiDao, bitly) }))
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
            list.addAll(dao.getMethods(api, className, methodName, signatureTypes)
                    .map{ it.getDisplayUrl(it.toString(), apiDao, bitly) }
            )
            //

            if (list.isEmpty()) {
                className = methodName
                list.addAll(dao.getMethods(api, className, methodName, signatureTypes)
                        .map { it.getDisplayUrl(it.toString(), apiDao, bitly) }
                )
            }

            urls.addAll(list)
        }
    }

    private fun displayApiList(responses: MutableList<Message>, event: Message) {
        val builder = StringBuilder()
        for (api in apiDao.findAll()) {
            if (builder.length != 0) {
                builder.append("; ")
            }
            builder.append(api.name)
        }
        responses.add(Message(event, Sofia.javadocApiList(event.user.nick, builder)))
    }

    companion object {

        private val RESULT_LIMIT = 5
    }
}