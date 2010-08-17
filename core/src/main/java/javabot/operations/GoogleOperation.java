package javabot.operations;

import com.antwerkz.maven.SPI;
import javabot.Javabot;

@SPI(BotOperation.class)
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