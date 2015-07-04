package javabot.operations.urlcontent;

import com.google.inject.Singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Singleton
public class URLContentAnalyzer {
    public final static String[] patterns = {
            "astebin",
            "mysticpaste.com",
            "pastie",
            "gist.github.com",
            "ideone.com",
            "docs.oracle.com.*api",
            "git.io",
    };
    public final static Map<String, Pattern> matchingPatterns = new HashMap<>();

    static {
        Stream.of(patterns).forEach(s ->
                matchingPatterns.put(s, Pattern.compile(".*" + s + ".*"))
        );
    }

    public boolean precheck(String url) {
        try {
            checkForBlacklistedSites(url);
            return true;
        } catch(ContentException e) {
            return false;
        }
    }
    public boolean check(String url, String title) {
        try {
            checkNulls(url, title);
            checkTitleToURLRatio(url, title);
            checkForBlacklistedSites(url);
            return true;
        } catch (ContentException e) {
            // log failure?
            return false;
        }
    }

    private void checkForBlacklistedSites(String url) throws ContentException {
        for (String pattern : patterns) {
            if (matchingPatterns.get(pattern).matcher(url).matches()) {
                throw new ContentException(String.format("Rejected: blacklisted site %s", url));
            }
        }
    }

    private void checkNulls(String url, String title) throws ContentException {
        if (null == title || "".equals(title.trim())) {
            throw new ContentException(
                    String.format("Rejected: Title content for %s was empty",
                            url));
        }
    }

    private void checkTitleToURLRatio(String url, String title) throws ContentException {
        // now break down the words in the title.
        String[] wordsInTitle = title.toLowerCase().split(" ");
        long words = Stream.of(wordsInTitle)
                .map(s -> s.replaceAll("[\\W]|_", ""))
                .filter(s -> s.length() > 2)
                .count();
        // generate a ratio of words to occurrences in the title.
        long hits = Stream.of(wordsInTitle)
                .map(s -> s.replaceAll("[\\W]|_", ""))
                .filter(s -> s.length() > 2)
                .filter(s -> url.toLowerCase().contains(s.toLowerCase()))
                .count();

        // ratio close enough to 1:1? reject.
        double ratio = (hits * 1.0) / words;
        if ((hits * 1.0) / words >= 0.8) {
            throw new ContentException(
                    String.format("Rejected: Ratio of content from  %s to %s was %f%n",
                            url,
                            title,
                            ratio));
        }
    }

    class ContentException extends Exception {
        public ContentException(String message) {
            super(message);
        }
    }
}
