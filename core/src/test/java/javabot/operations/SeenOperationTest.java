package javabot.operations;

import com.antwerkz.sofia.Sofia;
import javabot.BaseMessagingTest;
import org.testng.annotations.Test;

public class SeenOperationTest extends BaseMessagingTest {
  @Test
  public void seen() {
    testMessage("~seen jimmyjimjim", Sofia.seenUnknown(getTestUser().getNick(), "jimmyjimjim"));
  }
}
