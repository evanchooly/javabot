package javabot.operations;

import javabot.Javabot;

/**
 * @author ricky_clarkson
 */
public class DictOperation extends UrlOperation {

    public DictOperation(Javabot bot) {
        super(bot);
    }

    @Override
    protected String getBaseUrl() {
        return "http://dictionary.reference.com/browse/";
    }

    @Override
    protected String getTrigger() {
        return "dict ";
    }
}