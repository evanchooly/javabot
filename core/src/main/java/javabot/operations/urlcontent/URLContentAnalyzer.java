package javabot.operations.urlcontent;

import java.util.stream.Stream;

public class URLContentAnalyzer {
    public boolean check(String url, String title) {
        try {
            checkNulls(url, title);
            checkTitleToURLRatio(url, title);
            checkForBlacklistedSites(url, title);
        } catch (ContentException e) {
            // log failure?
            return false;
        }
        return true;
    }

    private void checkForBlacklistedSites(String url, String title) throws ContentException {
        String[] patterns = {"astebin", "mysticpaste.com", "pastie", "gist.github.com"};
        for (String pattern : patterns) {
            if (url.contains(pattern)) {
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
