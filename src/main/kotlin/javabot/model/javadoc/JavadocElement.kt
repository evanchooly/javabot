package javabot.model.javadoc

import dev.morphia.annotations.Entity
import javabot.dao.ApiDao
import javabot.model.Persistent
import org.bson.types.ObjectId
import dev.morphia.annotations.Id
import net.thauvin.erik.bitly.Bitly

@Entity
abstract class JavadocElement : Persistent {
    @field:Id
    var id: ObjectId? = null

    lateinit var apiId: ObjectId

    var shortUrl: String? = null

    lateinit var url: String

    fun setApi(api: JavadocApi) {
        this.apiId = api.id
    }

    private fun buildShortUrl(client: Bitly, url: String): String? {
        return client.bitlinks().shorten(url)
    }

    fun getDisplayUrl(hint: String, dao: ApiDao, client: Bitly?): String {
        if (shortUrl == null && client != null) {
            shortUrl = buildShortUrl(client, url) + " [" + dao.find(apiId) + ": " + hint + "]"
            dao.save(this)
        }
        return shortUrl ?: url
    }
}
