package javabot.javadoc;

import javabot.IsGdShortener;
import javabot.dao.BaseDao;
import javabot.model.Persistent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class JavadocElement implements Persistent {
    private static final Logger log = LoggerFactory.getLogger(JavadocElement.class);
    private String shortUrl;
    private String longUrl;
    private String directUrl;

    public abstract String getApiName();

    private String buildShortUrl(final String url) {
        return new IsGdShortener(url).invoke();
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(final String longUrl) {
        this.longUrl = longUrl;
    }

    public String getDirectUrl() {
        return directUrl;
    }

    public void setDirectUrl(final String directUrl) {
        this.directUrl = directUrl;
    }

    public String getDisplayUrl(final String hint, final BaseDao dao) {
        String url = getShortUrl();
        if(url == null) {
            setShortUrl(buildShortUrl(getLongUrl()) + " [" + getApiName() + ": " + hint +"]");
            url = getShortUrl();
            dao.save(this);
        }

        return url;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(final String shortUrl) {
        this.shortUrl = shortUrl;
    }

}