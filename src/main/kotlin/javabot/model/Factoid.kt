package javabot.model

import com.fasterxml.jackson.annotation.JsonView
import javabot.json.Views.PUBLIC
import javabot.operations.TellSubject
import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Id
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.Indexed
import org.mongodb.morphia.annotations.Indexes
import org.mongodb.morphia.annotations.PrePersist
import org.slf4j.LoggerFactory
import java.io.Serializable
import java.io.UnsupportedEncodingException
import java.lang.String.format
import java.net.URLEncoder
import java.nio.charset.Charset
import java.time.LocalDateTime

@Entity(value = "factoids", noClassnameStored = true)
@Indexes(Index(fields = arrayOf(Field("upperName"), Field("upperUserName"))))
public class Factoid(var name: String = "", var value: String = "", var userName: String = "") : Serializable, Persistent {
    @Id
    var id: ObjectId? = null

    @JsonView(PUBLIC::class)
    public var updated: LocalDateTime? = null

    public var lastUsed: LocalDateTime? = null

    var locked: Boolean = false

    @Indexed
    private var upperName: String? = null

    @Indexed
    private var upperUserName: String? = null

    @Indexed
    private var upperValue: String? = null

    public fun evaluate(subject: TellSubject?, sender: String, replacedValue: String?): String {
        var message = value
        val target = if (subject == null) sender else subject.target.nick
        if (subject != null && !message.contains("\$who") && message.startsWith("<reply>")) {
            message = StringBuilder(message).insert(message.indexOf(">") + 1, "\$who, ").toString()
        }
        message = message.replace("\$who", target)
        var replaced = replacedValue!!
        if (name.endsWith("$1")) {
            replaced = replacedValue
        }
        if (name.endsWith(" $+")) {
            replaced = urlencode(replacedValue)
        }
        if (name.endsWith(" $^")) {
            replaced = urlencode(camelcase(replacedValue))
        }
        message = message.replace("$1", replaced)
        message = message.replace("$+", replaced)
        message = message.replace("$^", replaced)
        message = processRandomList(message)
        if (!message.startsWith("<")) {
            val comparable = if (subject == null) sender else subject.target.nick
            message = "$comparable, $name is $message"
        }
        return message.substring(0, Math.min(message.length(), 510))
    }

    private fun urlencode(value: String): String {
        try {
            return URLEncoder.encode(value, Charset.defaultCharset().displayName())
        } catch (e: UnsupportedEncodingException) {
            log.error(e.getMessage(), e)
            return value
        }

    }

    private fun camelcase(`in`: String): String {
        val sb = StringBuilder(`in`.replace("\\s".toRegex(), " "))
        if (!`in`.isEmpty()) {
            var idx = sb.indexOf(" ")
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)))
            while (idx > -1) {
                sb.deleteCharAt(idx)
                if (idx < sb.length()) {
                    sb.setCharAt(idx, Character.toUpperCase(sb.charAt(idx)))
                }
                idx = sb.indexOf(" ")
            }
        }
        return sb.toString()
    }

    protected fun processRandomList(message: String): String {
        var result = message
        var index = -1
        index = result.indexOf("(", index + 1)
        var index2 = result.indexOf(")", index + 1)
        while (index < result.length() && index != -1 && index2 != -1) {
            val choice = result.substring(index + 1, index2)
            val choices = choice.split("|")
            if (choices.size() > 1) {
                val chosen = (Math.random() * choices.size()).toInt()
                result = format("%s%s%s", result.substring(0, index), choices[chosen],
                      result.substring(index2 + 1))
            }
            index = result.indexOf("(", index + 1)
            index2 = result.indexOf(")", index + 1)
        }
        return result
    }

    @PrePersist
    public fun uppers() {
        upperName = name.toUpperCase()
        upperUserName = userName.toUpperCase()
        upperValue = value.toUpperCase()
    }

    override fun toString(): String {
        return format("Factoid{id=%s, name='%s', value='%s', userName='%s', updated=%s, lastUsed=%s, locked=%s}",
              id, name, value, userName, updated, lastUsed, locked)
    }

    companion object {
        private val log = LoggerFactory.getLogger(Factoid::class.java)
    }
}