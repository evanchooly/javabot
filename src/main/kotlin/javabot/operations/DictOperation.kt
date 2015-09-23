package javabot.operations

public class DictOperation : UrlOperation() {
    override fun getBaseUrl(): String {
        return "http://dictionary.reference.com/browse/"
    }

    override fun getTrigger(): String {
        return "dict "
    }
}