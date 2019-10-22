package javabot.service

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.google.inject.Inject
import com.google.inject.Singleton
import javabot.JavabotConfig
import net.swisstech.bitly.BitlyClient

@Singleton
open class UrlCacheService @Inject constructor(private val config: JavabotConfig) {
    open fun shorten(url: String): String {
        return urlCache[url]
    }

    @field:[Inject(optional = true)]
    var bitly: BitlyClient? = null
        get() {
            if (field == null) {
                field = if (config.bitlyToken() != "") BitlyClient(config.bitlyToken()) else null
            }
            return field
        }

    private val urlCache: LoadingCache<String, String> = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .build(object : CacheLoader<String, String>() {
                override fun load(key: String): String {
                    val client = bitly
                    return if (client != null) {
                        client.shorten().setLongUrl(key).call().data.url
                    } else {
                        key
                    }
                }
            })
}