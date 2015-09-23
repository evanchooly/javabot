package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.dao.ApiDao
import javabot.dao.JavadocClassDao
import javabot.javadoc.JavadocApi
import javabot.javadoc.JavadocField
import net.swisstech.bitly.BitlyClient
import org.pircbotx.Channel
import org.pircbotx.User
import javax.inject.Inject
import java.util.ArrayList
import java.util.stream.Collectors

public class JavadocOperation : BotOperation() {
    Inject
    private val apiDao: ApiDao? = null

    Inject
    private val dao: JavadocClassDao? = null

    Inject
    private val client: BitlyClient? = null

    override fun handleMessage(event: Message): Boolean {
        val message = event.value
        var handled = false
        if (message.toLowerCase().startsWith("javadoc")) {
            var key = message.substring("javadoc".length()).trim()
            if (key.startsWith("-list") || key.isEmpty()) {
                displayApiList(event)
                handled = true
            } else {
                val api: JavadocApi
                if (key.startsWith("-")) {
                    if (key.contains(" ")) {
                        api = apiDao!!.find(key.substring(1, key.indexOf(" ")))
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
            val channel = event.channel
            val user = event.user

            val nick = user.nick
            var urlMessage = StringBuilder(nick + ": ")
            urlMessage = buildResponse(event, urls, urlMessage, channel)
            if (urls.size() > RESULT_LIMIT) {
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

    private fun buildResponse(event: Message, urls: List<String>,
                              urlMessage: StringBuilder, channel: Channel): StringBuilder {
        var urlMessage = urlMessage
        for (index in urls.indices) {
            if ((urlMessage + urls.get(index)).length() > 400) {
                bot.postMessageToChannel(event, urlMessage.toString())
                urlMessage = StringBuilder()
            }
            urlMessage.append(if (index == 0) "" else "; ").append(urls.get(index))
        }
        return urlMessage
    }

    public fun handle(api: JavadocApi, key: String): List<String> {
        val urls = ArrayList<String>()
        val openIndex = key.indexOf('(')
        if (openIndex == -1) {
            parseFieldOrClassRequest(urls, api, key)
        } else {
            parseMethodRequest(urls, api, key, openIndex)
        }
        return urls
    }

    private fun parseFieldOrClassRequest(urls: MutableList<String>, api: JavadocApi, key: String) {
        val finalIndex = key.lastIndexOf('.')
        if (finalIndex == -1) {
            findClasses(api, urls, key)
        } else {
            val className = key.substring(0, finalIndex)
            val fieldName = key.substring(finalIndex + 1)
            if (Character.isUpperCase(fieldName.charAt(0)) && fieldName.toUpperCase() != fieldName) {
                findClasses(api, urls, key)
            } else {
                val list = dao!!.getField(api, className, fieldName)
                urls.addAll(list.stream().map({ field -> field.getDisplayUrl(field.toString(), apiDao, client) }).collect(
                      Collectors.toList<String>()))
            }
        }
    }

    private fun findClasses(api: JavadocApi, urls: MutableList<String>, key: String) {
        urls.addAll(dao!!.getClass(api, key).stream().map(
              { javadocClass -> javadocClass.getDisplayUrl(javadocClass.toString(), apiDao, client) }).collect(Collectors.toList<String>()))
    }

    private fun parseMethodRequest(urls: MutableList<String>, api: JavadocApi, key: String, openIndex: Int) {
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
            list.addAll(dao!!.getMethods(api, className, methodName, signatureTypes).stream().map(
                  { method -> method.getDisplayUrl(method.toString(), apiDao, client) }).collect(Collectors.toList<String>()))
            //

            if (list.isEmpty()) {
                className = methodName
                list.addAll(dao.getMethods(api, className, methodName, signatureTypes).stream().map(
                      { method -> method.getDisplayUrl(method.toString(), apiDao, client) }).collect(Collectors.toList<String>()))
            }

            urls.addAll(list)
        }
    }

    private fun displayApiList(event: Message) {
        val builder = StringBuilder()
        for (api in apiDao!!.findAll()) {
            if (builder.length() != 0) {
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