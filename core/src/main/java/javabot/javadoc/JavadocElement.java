package javabot.javadoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

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
    private static final String API_URL = "http://is.gd/api.php?longurl=";
    private String shortUrl;
    private String longUrl;

    @Transient
    public abstract String getApiName();

    private String buildShortUrl(final String url) {
        StringBuilder buff = null;
        try {
            BufferedReader reader = null;
            InputStreamReader isr = null;
            try {
                final HttpURLConnection connection = (HttpURLConnection) new URL(
                    API_URL + URLEncoder.encode(url, "UTF-8")).openConnection();
                connection.setRequestMethod("GET");
                connection.setInstanceFollowRedirects(true);
                connection.setConnectTimeout(7500);
                connection.connect();
                final int code = connection.getResponseCode();
                if (code != HttpURLConnection.HTTP_OK) {
                    throw new IOException("Result code: " + code);
                }
                isr = new InputStreamReader(connection.getInputStream());
                reader = new BufferedReader(isr);
                String ret;
                buff = new StringBuilder();
                while ((ret = reader.readLine()) != null) {
                    buff.append(ret);
                }
                connection.disconnect();
            } finally {
                if (isr != null) {
                    isr.close();
                }
                if (reader != null) {
                    reader.close();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return buff == null ? null : buff.toString();
    }

    @Column(length = 1000)
    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(final String longUrl) {
        this.longUrl = longUrl;
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