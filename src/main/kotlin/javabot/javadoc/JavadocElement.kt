package javabot.javadoc

import javabot.dao.ApiDao
import javabot.model.Persistent
import net.swisstech.bitly.BitlyClient
import org.bson.types.ObjectId

abstract class JavadocElement : Persistent {

    var apiId: ObjectId? = null

    var shortUrl: String? = null

    var longUrl = ""

    var directUrl: String? = null

    fun setApi(api: JavadocApi) {
        this.apiId = api.id
    }

    private fun buildShortUrl(client: BitlyClient?, url: String): String? {
        return client?.shorten()?.setLongUrl(url)?.call()?.data?.url
    }

    fun getDisplayUrl(hint: String, dao: ApiDao, client: BitlyClient?): String {
        var url = shortUrl
        if (url == null && client != null) {
            shortUrl = buildShortUrl(client, longUrl) + " [" + dao.find(apiId) + ": " + hint + "]"
            url = shortUrl
            dao.save(this)
        }
        return url ?: longUrl
    }
}