package javabot.operations;

import javabot.Javabot;

public class GoogleOperation extends UrlOperation {
    public GoogleOperation(Javabot javabot) {
        super(javabot);
    }

    @Override
    protected String getBaseUrl() {
        return "http://letmegooglethatforyou.com/?q=";
    }

    @Override
    protected String getTrigger() {
        return "google ";
    }
}