package javabot.operations;

public class DictOperation extends UrlOperation {
    @Override
    protected String getBaseUrl() {
        return "http://dictionary.reference.com/browse/";
    }

    @Override
    protected String getTrigger() {
        return "dict ";
    }
}