package javabot.operations.locator

import com.google.common.base.Strings
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import org.jsoup.Jsoup
import java.util.HashMap

public class JCPJSRLocator {

    public fun locate(inputs: Map<String, String>): Map<String, String> {
        val retVal = HashMap<String, String>()
        val urlString = "http://www.jcp.org/en/jsr/detail?id=" + inputs["jsr"]
        retVal.put("url", urlString)
        try {
            HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build().use { client ->
                val httpget = HttpGet(urlString)
                val response = client.execute(httpget)
                val entity = response.entity
                if (entity != null) {
                    try {
                        val data = EntityUtils.toString(entity)
                        val doc = Jsoup.parse(data)
                        val title = doc.select("div.header1").first()
                        val titleText = StringBuilder()
                        var separator = ""
                        // we build the content,
                        // because of that stupid <sup> thing the JCP uses
                        for (element in title.textNodes()) {
                            titleText.append(separator).append(element.text().trim())
                            separator = " "
                        }
                        retVal.put("title", titleText.toString())
                    } finally {
                        EntityUtils.consume(entity)
                    }
                }

            }
        } catch (ignored: Exception) {
        }

        return retVal
    }

    public fun findInformation(jsr: Int): String {
        val inputs = HashMap<String, String>()
        inputs.put("jsr", Integer.toString(jsr))
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
