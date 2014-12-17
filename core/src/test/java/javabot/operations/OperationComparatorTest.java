package javabot.operations;

import org.testng.annotations.Test;

import java.util.Set;
import java.util.TreeSet;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

public class OperationComparatorTest {
@Test
  public void testOperationComparator() {
  BotOperation karma=new KarmaOperation();
  BotOperation factoid=new GetFactoidOperation();
  BotOperation jsr=new JSROperation();
  OperationComparator comparator=new OperationComparator();
  Set<BotOperation> operations=new TreeSet<>(comparator);

  operations.add(karma);
  operations.add(jsr);
  operations.add(factoid);

  @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
  BotOperation[] operationsArray= operations.toArray(new BotOperation[]{});
  assertEquals(operationsArray[0], jsr);
  assertEquals(operationsArray[1], karma);
  assertEquals(operationsArray[2], factoid);

  assertEquals(comparator.compare(karma, factoid), -1);
  assertEquals(comparator.compare(factoid, karma), 1);
  assertEquals(comparator.compare(karma, karma), 0);
  assertEquals(karma.getPriority(), jsr.getPriority());
  assertEquals(comparator.compare(jsr, karma), -1);
}
}
