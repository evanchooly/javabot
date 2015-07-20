package javabot.operations.urlcontent;

import static org.apache.commons.lang.StringUtils.isBlank;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class URLFromMessageParser {

    public List<URL> urlsFromMessage(String message) {
        if (isBlank(message)) {
            return null;
        }

        ArrayList<String> potentialUrlsFound = new ArrayList<>();
        int idxHttp = message.indexOf("http");
        while (idxHttp >= 0) {
            int idxSpace = message.indexOf(' ',idxHttp);
            String url = (idxSpace == -1) ? message.substring(idxHttp) : message.substring(idxHttp, idxSpace);
            potentialUrlsFound.add(stripPunctuation(message, url, idxHttp));
            idxHttp = (idxSpace == -1) ? -1 : message.indexOf("http",idxSpace);
        }

        return potentialUrlsFound.stream()
                .map(this::urlFromToken)
                .filter(url -> url != null)
                .collect(Collectors.toList());
    }

    private final static char[] OPEN_PUNCTUATION = new char[] {'{','(','['};
    private final static char[] CLOSE_PUNCTUATION = new char[]{'}',')',']'};

    private String stripPunctuation(String message, String url, int idxUrlStart) {
        char last = url.charAt(url.length() - 1);

        int idxPunc = ArrayUtils.indexOf(CLOSE_PUNCTUATION,last);
        if (idxPunc == -1) {
            return url;
        }

        //Walk backwards in message from urlStart, and strip the punctuation if an open brace/bracket is seen
        //before another close.  Otherwise, return the url as is.

        for (char c : StringUtils.reverse(message.substring(0,idxUrlStart)).toCharArray()) {
            if (c == OPEN_PUNCTUATION[idxPunc]) {
                return url.substring(0,url.length() - 1);
            }
            if (c == CLOSE_PUNCTUATION[idxPunc]) {
                return url;
            }
        }
        return url;
    }

    private URL urlFromToken(String token) {
        try {
            return new URL(token);
        } catch (Exception e) {
            return null;
        }
    }
}
