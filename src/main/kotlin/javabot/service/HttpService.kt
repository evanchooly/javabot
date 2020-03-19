package javabot.service

import com.google.inject.Singleton
import okhttp3.Headers.Companion.toHeaders
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.time.Duration
import java.util.concurrent.TimeUnit

class HttpServiceException(message: String = "No message") : Exception(message)

enum class HTTP_OPTIONS {
    READ_TIMEOUT {
        override fun apply(builder: OkHttpClient.Builder, duration: Duration) {
            builder.readTimeout(duration)
        }
    },
    WRITE_TIMEOUT {
        override fun apply(builder: OkHttpClient.Builder, duration: Duration) {
            builder.writeTimeout(duration)
        }
    },
    CALL_TIMEOUT {
        override fun apply(builder: OkHttpClient.Builder, duration: Duration) {
            builder.callTimeout(duration)
        }
    },
    CONNECT_TIMEOUT {
        override fun apply(builder: OkHttpClient.Builder, duration: Duration) {
            builder.connectTimeout(duration)
        }
    };

    abstract fun apply(builder: OkHttpClient.Builder, duration: Duration)
}

@Singleton
class HttpService {
    private val client = OkHttpClient()
            .newBuilder()
            .callTimeout(10, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request()
                val requestWithUserAgent = request.newBuilder()
                        .removeHeader("User-Agent")
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36")
                        .build()
                chain.proceed(requestWithUserAgent)
            }
            .build()

    fun get(
            url: String,
            params: Map<String, String> = emptyMap(),
            headers: Map<String, String> = emptyMap(),
            options: Map<HTTP_OPTIONS, Duration> = emptyMap()
    ): String {
        val httpUrl = url.toHttpUrl()
        val builder = httpUrl.newBuilder()
        params.forEach { builder.addQueryParameter(it.key, it.value) }
        val request = Request.Builder().url(builder.build())
                .headers(headers.toHeaders())
                .build()
        val localRequest = client.newBuilder()

        options.forEach { action ->
            action.key.apply(localRequest, action.value)
        }

        localRequest
                .build()
                .newCall(request)
                .execute()
                .use { response ->
                    if (response.isSuccessful) {
                        return (response.body ?: throw HttpServiceException()).string()
                    } else {
                        throw HttpServiceException("${response.code} ${response.message}")
                    }
                }
    }
}