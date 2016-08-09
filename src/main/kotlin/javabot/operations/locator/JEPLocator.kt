package javabot.operations.locator

import com.google.common.base.Strings
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import org.jsoup.Jsoup
import java.util.*

class JEPLocator {

    fun locate(inputs: Map<String, String>): Map<String, String> {
        val retVal = HashMap<String, String>()
        val urlString = "http://openjdk.java.net/jeps/" + inputs["jep"]
        retVal.put("url", urlString)
        try {
            HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build().use { client ->
                val httpget = HttpGet(urlString)
                val response = client.execute(httpget)
                if (response.statusLine.statusCode == 200) {
                    val entity = response.entity
                    if (entity != null) {
                        try {
                            val data = EntityUtils.toString(entity)
                            val doc = Jsoup.parse(data)
                            retVal.put("title", doc.title())
                        } finally {
                            EntityUtils.consume(entity)
                        }
                    }
                }
            }
        } catch (ignored: Exception) {
        }

        return retVal
    }

    fun findInformation(jep: Int): String {
        val inputs = HashMap<String, String>()
        inputs.put("jep", Integer.toString(jep))
        val outputs = locate(inputs)
        if (Strings.isNullOrEmpty(outputs["title"])) {
            return ""
        }
        return "'" + outputs["title"] + "' can be found at " + outputs["url"]
    }

    companion object {
        val requestConfig = RequestConfig.custom().setConnectionRequestTimeout(5000).setConnectTimeout(5000).setSocketTimeout(5000).build()
    }
}
