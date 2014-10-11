package javabot.operations;

import com.antwerkz.sofia.Sofia;
import org.testng.annotations.Test;

public class SeenOperationTest extends BaseOperationTest {
  @Test
  public void seen() {
    testMessage("~seen jimmyjimjim", Sofia.seenUnknown(getTestUser().getNick(), "jimmyjimjim"));
  }
}
