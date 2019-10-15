package javabot.service

import com.google.common.base.Strings
import com.google.inject.Inject
import com.google.inject.Singleton
import org.jsoup.Jsoup
import java.util.*

@Singleton
class JCPJSRLocator @Inject constructor(private val httpService: HttpService) {
    private fun locate(inputs: Map<String, String>): Map<String, String> {
        val retVal = HashMap<String, String>()
        val urlString = "http://www.jcp.org/en/jsr/detail?id=" + inputs["jsr"]
        retVal.put("url", urlString)
        try {
            val data = httpService.get(urlString)
            val doc = Jsoup.parse(data)
            val title = doc.select("div.header1").first()
            retVal.put("title",
                    title
                            .textNodes()
                            .map { element -> element.text().trim() }
                            .joinToString(separator = " "))
        } catch (ignored: Exception) {
        }

        return retVal
    }

    fun findInformation(jsr: Int): String {
        val inputs = HashMap<String, String>()
        inputs.put("jsr", Integer.toString(jsr))
        val outputs = locate(inputs)
        if (Strings.isNullOrEmpty(outputs["title"])) {
            return ""
        }
        return "'" + outputs["title"] + "' can be found at " + outputs["url"]
    }
}
