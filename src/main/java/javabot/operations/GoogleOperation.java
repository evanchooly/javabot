package javabot.operations;

public class GoogleOperation extends UrlOperation {
    @Override
    protected String getBaseUrl() {
        return "http://letmegooglethatforyou.com/?q=";
    }

    @Override
    protected String getTrigger() {
        return "google ";
    }
}