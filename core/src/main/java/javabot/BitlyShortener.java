package javabot;

import com.rosaloves.bitlyj.Url;
import static com.rosaloves.bitlyj.Bitly.as;
import static com.rosaloves.bitlyj.Bitly.shorten;

public class BitlyShortener {
    private String user;
    private String key;

    public BitlyShortener() {
        user = "bitlyapidemo";
        key = "R_0da49e0a9118ff35f52f629d2d71bf07";
    }

    public String invoke(final String longUrl) {
        final Url url = as(user, key)
            .call(shorten(longUrl));
        return url.getShortUrl();
    }
}
