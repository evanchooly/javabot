package javabot.javadoc;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import javabot.IsGdShortener;
import javabot.dao.BaseDao;
import javabot.model.Persistent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created Jan 7, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@MappedSuperclass
public abstract class JavadocElement implements Persistent {
    private static final Logger log = LoggerFactory.getLogger(JavadocElement.class);
    private String shortUrl;
    private String longUrl;
    private String directUrl;

    @Transient
    public abstract String getApiName();

    private String buildShortUrl(final String url) {
        return new IsGdShortener(url).invoke();
    }

    @Column(length = 1000)
    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(final String longUrl) {
        this.longUrl = longUrl;
    }

    @Column(length = 1000)
    public String getDirectUrl() {
        return directUrl;
    }

    public void setDirectUrl(final String directUrl) {
        this.directUrl = directUrl;
    }

    @Transient
    @Transactional
    public String getDisplayUrl(final String hint, final BaseDao dao) {
        String url = getShortUrl();
        if(url == null) {
            setShortUrl(buildShortUrl(getLongUrl()) + " [" + getApiName() + ": " + hint +"]");
            url = getShortUrl();
            dao.save(this);
        }

        return url;
    }

    @Column(length = 100)
    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(final String shortUrl) {
        this.shortUrl = shortUrl;
    }

}