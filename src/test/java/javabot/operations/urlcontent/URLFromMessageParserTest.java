package javabot.operations.urlcontent;

import static org.testng.Assert.*;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class URLFromMessageParserTest {
    private final static List<URL> NONE = Collections.emptyList();

    URLFromMessageParser parser = new URLFromMessageParser();

    @DataProvider(name = "messages")
    Object[][] getUrlsForParensBracketsStrip() throws Exception {
        return new Object[][]{
                {"ftp://non.http.url.com", NONE},
                {"someone says http but it's not a url", NONE},
                {"someone says https but it's not a url", NONE},
                {"http://sample.com", expectedUrls("http://sample.com")},
                {"https://sample.com", expectedUrls("https://sample.com")},
                {"At end:  http://sample.com", expectedUrls("http://sample.com")},
                {"In middle http://sample.com of message", expectedUrls("http://sample.com")},
                {"http://sample.com beginning of message", expectedUrls("http://sample.com")},
                {"In parens (http://sample.com)", expectedUrls("http://sample.com")},
                {"In brackets [http://sample.com]", expectedUrls("http://sample.com")},
                {"In curly brackets {http://sample.com}", expectedUrls("http://sample.com")},
                {"Fat finger a space ( http://sample.com)", expectedUrls("http://sample.com")},
                {"Valid URL with parens http://msdn.microsoft.com/en-us/library/aa752574(VS.85).aspx",
                        expectedUrls("http://msdn.microsoft.com/en-us/library/aa752574(VS.85).aspx")},
                {"Don't fix mismatched punctuation  {http://sample.com)", expectedUrls("http://sample.com)")},
                {"Don't fix mismatched punctuation (http://sample.com]", expectedUrls("http://sample.com]")},
                {"Don't fix mismatched punctuation [http://sample.com}", expectedUrls("http://sample.com}")},
                {"Message has parens (like this) before http://sample.com)", expectedUrls("http://sample.com)")}
        };
    }

    private List<URL> expectedUrls(String... strings) throws Exception {
        List<URL> list = new ArrayList<>();
        for (String s: strings) {
            list.add(new URL(s));
        }
        return list;
    }

    @Test(dataProvider = "messages")
    public void testUrlFromMessage(String message, List<URL> expected) {
        assertEquals(parser.urlsFromMessage(message), expected);
    }

}