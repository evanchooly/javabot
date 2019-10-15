package javabot.service

import com.google.inject.Singleton
import okhttp3.Headers.Companion.toHeaders
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit
import okhttp3.HttpUrl.Companion.toHttpUrl

class HttpServiceException(message:String="No message") : Exception(message)

@Singleton
class HttpService {
    private val client = OkHttpClient()
            .newBuilder()
            .callTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

    fun get(
            url: String,
            params: Map<String, String> = emptyMap(),
            headers: Map<String, String> = emptyMap()
    ): String {
        return try {
            val httpUrl = url.toHttpUrl()
            val builder = httpUrl.newBuilder()
            params.forEach { builder.addQueryParameter(it.key, it.value) }
            val request = Request.Builder().url(builder.build())
                    .headers(headers.toHeaders())
                    .build()
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    (response.body ?: throw HttpServiceException()).string()
                } else {
                    throw HttpServiceException("${response.code} ${response.message}")
                }
            }
        } catch (e: HttpServiceException) {
            throw e
        }
    }

}