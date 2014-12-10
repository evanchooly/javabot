package javabot.operations;

import com.antwerkz.sofia.Sofia;
import javabot.BaseMessagingTest;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.testng.Assert.assertEquals;

@Test(groups = {"operations"})
public class RFCOperationTest extends BaseMessagingTest {
  @Inject
  RFCOperation operation;

  @Test
  public void testBadRFCNumber() {
    testMessage("~rfc abd", Sofia.rfcInvalid("abd"));
  }

  @Test
  public void testMissingRFCNumber() {
    // these should be handled by the factoid operation
    testMessage("~rfc ", Sofia.unhandledMessage(getTestUser()));
    testMessage("~rfc", Sofia.unhandledMessage(getTestUser()));
  }

  @Test
  public void testHTTPRFC() {
    long hitRate = operation.rfcTitleCache.stats().hitCount();
    testMessage("~rfc 2616",
        Sofia.rfcSucceed("http://www.faqs.org/rfcs/rfc2616.html",
            "RFC 2616 - Hypertext Transfer Protocol -- HTTP/1.1 (RFC2616)"));
    testMessage("~rfc 2616",
        Sofia.rfcSucceed("http://www.faqs.org/rfcs/rfc2616.html",
            "RFC 2616 - Hypertext Transfer Protocol -- HTTP/1.1 (RFC2616)"));
    assertEquals(operation.rfcTitleCache.stats().hitCount(), hitRate + 1);
  }

  public void testNonexistentRFC() {
    // sofia: rfc.fail
    testMessage("~rfc 2616132", Sofia.rfcFail("2616132"));
  }

}
