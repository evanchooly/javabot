package javabot.model

import com.antwerkz.sofia.Sofia
import com.fasterxml.jackson.annotation.JsonView
import dev.morphia.annotations.Entity
import dev.morphia.annotations.Field
import dev.morphia.annotations.Id
import dev.morphia.annotations.Index
import dev.morphia.annotations.Indexed
import dev.morphia.annotations.Indexes
import dev.morphia.annotations.PrePersist
import java.io.Serializable
import java.io.UnsupportedEncodingException
import java.lang.String.format
import java.net.URLEncoder
import java.nio.charset.Charset
import java.time.LocalDateTime
import java.util.Locale
import javabot.json.Views.PUBLIC
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory

@Entity(value = "factoids", useDiscriminator = false)
@Indexes(Index(fields = arrayOf(Field("upperName"), Field("upperUserName"))))
class Factoid() : Serializable, Persistent {

    constructor(name: String = "", value: String = "", userName: String = "") : this() {
        this.name = name
        this.value = value
        this.userName = userName
    }

    @Id var id: ObjectId? = null

    var name = ""
    var value = ""
    var userName = ""

    @JsonView(PUBLIC::class) var updated: LocalDateTime = LocalDateTime.now()

    var lastUsed: LocalDateTime? = null

    var locked = false

    @Indexed lateinit var upperName: String

    @Indexed lateinit var upperUserName: String

    @Indexed lateinit var upperValue: String

    init {
        update()
    }

    fun evaluate(subject: JavabotUser?, sender: String, replacedValue: String): String {
        var message = value
        val target = if (subject == null) sender else subject.nick
        if (subject != null && !message.contains("\$who") && message.startsWith("<reply>")) {
            message = StringBuilder(message).insert(message.indexOf(">") + 1, "\$who, ").toString()
        }
        message = message.replace("\$who", target)
        var replaced = replacedValue
        if (name.endsWith("$1")) {
            if (replaced.isBlank()) {
                return Sofia.missingTarget(name, sender)
            }
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
            val comparable = if (subject == null) sender else subject.nick
            message = "$comparable, $name is $message"
        }
        return message.substring(0, Math.min(message.length, 510))
    }

    private fun urlencode(value: String): String {
        try {
            return URLEncoder.encode(value, Charset.defaultCharset().displayName())
        } catch (e: UnsupportedEncodingException) {
            log.error(e.message, e)
            return value
        }
    }

    private fun camelcase(`in`: String): String {
        val sb = StringBuilder(`in`.replace("\\s".toRegex(), " "))
        if (!`in`.isEmpty()) {
            var idx = sb.indexOf(" ")
            sb.setCharAt(0, Character.toUpperCase(sb[0]))
            while (idx > -1) {
                sb.deleteCharAt(idx)
                if (idx < sb.length) {
                    sb.setCharAt(idx, Character.toUpperCase(sb[idx]))
                }
                idx = sb.indexOf(" ")
            }
        }
        return sb.toString()
    }

    private fun processRandomList(message: String): String {
        var result = message
        var index = -1
        index = result.indexOf("(", index + 1)
        var index2 = result.indexOf(")", index + 1)
        while (index < result.length && index != -1 && index2 != -1) {
            val choice = result.substring(index + 1, index2)
            val choices = choice.split("|")
            if (choices.size > 1) {
                val chosen = (Math.random() * choices.size).toInt()
                result =
                    format(
                        "%s%s%s",
                        result.substring(0, index),
                        choices[chosen],
                        result.substring(index2 + 1)
                    )
            }
            index = result.indexOf("(", index + 1)
            index2 = result.indexOf(")", index + 1)
        }
        return result
    }

    @PrePersist
    fun update() {
        upperName = name.uppercase(Locale.getDefault())
        upperUserName = userName.uppercase(Locale.getDefault())
        upperValue = value.uppercase(Locale.getDefault())
    }

    override fun toString(): String {
        return format(
            "Factoid{id=%s, name='%s', value='%s', userName='%s', updated=%s, lastUsed=%s, locked=%s}",
            id,
            name,
            value,
            userName,
            updated,
            lastUsed,
            locked
        )
    }

    companion object {
        private val log = LoggerFactory.getLogger(Factoid::class.java)

        fun of(name: String? = null, value: String? = null, userName: String? = null) =
            Factoid((name ?: "").trim(), (value ?: "").trim(), (userName ?: "").trim())
    }
}
