package javabot.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Singleton
import java.time.Duration
import java.util.concurrent.TimeUnit
import okhttp3.Headers.Companion.toHeaders
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

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
    private val client =
        OkHttpClient()
            .newBuilder()
            .callTimeout(15, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request()
                val requestWithUserAgent =
                    request
                        .newBuilder()
                        .removeHeader("User-Agent")
                        .addHeader(
                            "User-Agent",
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36",
                        )
                        .build()
                chain.proceed(requestWithUserAgent)
            }
            .build()

    fun get(
        url: String,
        params: Map<String, String> = emptyMap(),
        headers: Map<String, String> = emptyMap(),
        options: Map<HTTP_OPTIONS, Duration> = emptyMap(),
    ): String = request(url, params, headers, options, "get", null)

    fun post(
        url: String,
        params: Map<String, String> = emptyMap(),
        headers: Map<String, String> = emptyMap(),
        options: Map<HTTP_OPTIONS, Duration> = emptyMap(),
        body: Any? = null,
    ): String = request(url, params, headers, options, "post", body)

    fun request(
        url: String,
        params: Map<String, String> = emptyMap(),
        headers: Map<String, String> = emptyMap(),
        options: Map<HTTP_OPTIONS, Duration> = emptyMap(),
        method: String = "get",
        body: Any? = null,
    ): String {
        val httpUrl = url.toHttpUrl()
        val builder = httpUrl.newBuilder()
        params.forEach { builder.addQueryParameter(it.key, it.value) }
        val requestBuilder = Request.Builder().url(builder.build()).headers(headers.toHeaders())
        when (method) {
            "get" -> requestBuilder.get()
            "post" -> {
                val mapper = ObjectMapper()
                val bodyContent = mapper.writeValueAsString(body)
                requestBuilder.post(
                    bodyContent.toRequestBody("application/json; chartset=utf-8".toMediaType())
                )
            }
            else ->
                throw IllegalArgumentException("method type $method not supported by HttpService")
        }
        val request = requestBuilder.build()
        // if options aren't empty, build a local copy of the request.
        // otherwise, use the global client options as is.
        if (options.isNotEmpty()) {
                val localRequest = client.newBuilder()

                options.forEach { action -> action.key.apply(localRequest, action.value) }

                localRequest.build()
            } else {
                client
            }
            .newCall(request)
            .execute()
            .use { response ->
                if (response.isSuccessful) {
                    return (response.body?.string() ?: throw HttpServiceException())
                } else {
                    throw HttpServiceException("${response.code} ${response.message}")
                }
            }
    }
}
