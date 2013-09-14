package javabot.operations;

import org.testng.annotations.Test;

public class SeenOperationTest extends BaseOperationTest {
  @Test
  public void seen() {
    testMessage("~seen jimmyjimjim", "jbtestuser, I have no information about \"jimmyjimjim\"");
  }
}
