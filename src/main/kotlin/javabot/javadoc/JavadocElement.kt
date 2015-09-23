package javabot.javadoc

import javabot.dao.ApiDao
import javabot.model.Persistent
import net.swisstech.bitly.BitlyClient
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public abstract class JavadocElement : Persistent {

    public var apiId: ObjectId? = null

    public var shortUrl: String? = null

    public var longUrl: String? = null

    public var directUrl: String? = null

    public fun setApi(api: JavadocApi) {
        this.apiId = api.id
    }

    private fun buildShortUrl(client: BitlyClient?, url: String): String? {
        return if (client != null)
            client.shorten().setLongUrl(url).call().data.url
        else
            null
    }

    public fun getDisplayUrl(hint: String, dao: ApiDao, client: BitlyClient?): String {
        var url: String? = shortUrl
        if (url == null && client != null) {
            shortUrl = buildShortUrl(client, longUrl) + " [" + dao.find(apiId) + ": " + hint + "]"
            url = shortUrl
            dao.save(this)
        }
        return if (url == null) longUrl else url
    }

    companion object {
        private val log = LoggerFactory.getLogger(JavadocElement::class.java)
    }
}