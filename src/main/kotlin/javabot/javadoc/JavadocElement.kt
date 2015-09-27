package javabot.javadoc

import javabot.dao.ApiDao
import javabot.model.Persistent
import net.swisstech.bitly.BitlyClient
import org.bson.types.ObjectId

public abstract class JavadocElement : Persistent {

    public var apiId: ObjectId? = null

    public var shortUrl: String? = null

    public var longUrl = ""

    public var directUrl: String? = null

    public fun setApi(api: JavadocApi) {
        this.apiId = api.id
    }

    private fun buildShortUrl(client: BitlyClient?, url: String): String? {
        return client?.shorten()?.setLongUrl(url)?.call()?.data?.url
    }

    public fun getDisplayUrl(hint: String, dao: ApiDao, client: BitlyClient?): String {
        var url = shortUrl
        if (url == null && client != null) {
            shortUrl = buildShortUrl(client, longUrl) + " [" + dao.find(apiId) + ": " + hint + "]"
            url = shortUrl
            dao.save(this)
        }
        return url ?: longUrl
    }
}