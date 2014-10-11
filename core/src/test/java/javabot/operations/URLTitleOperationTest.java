package javabot.operations;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(groups = {"operations"})
public class URLTitleOperationTest extends BaseOperationTest {
    @DataProvider(name="urls")
    Object[][] getUrls() {
        return new Object[][] {
                {"http://google.com/", "http://google.com/: Google"},
                {"http://google.com", "http://google.com: Google"},
                {"Have you tried to http://google.com", "http://google.com: Google"},
                {"http://varietyofsound.wordpress.com has a lot of VSTs", "http://varietyofsound.wordpress.com: Variety Of Sound"}
        }; 
    }

    @Test(dataProvider = "urls")
    public void testSimpleUrl(String url, String title) {
        testMessage(url, title);
    }
}
