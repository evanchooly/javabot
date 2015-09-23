package javabot.operations

public class GoogleOperation : UrlOperation() {
    override fun getBaseUrl(): String {
        return "http://letmegooglethatforyou.com/?q="
    }

    override fun getTrigger(): String {
        return "google "
    }
}