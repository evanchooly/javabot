package javabot.operations

import org.testng.Assert.assertEquals
import org.testng.annotations.Test
import java.util.TreeSet

class OperationComparatorTest {
    @Test fun testOperationComparator() {
        val karma = KarmaOperation()
        val factoid = GetFactoidOperation()
        val jsr = JSROperation()
        val comparator = OperationComparator()
        val operations = TreeSet(comparator)

        operations.add(karma)
        operations.add(jsr)
        operations.add(factoid)

        @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
        val operationsArray = operations.toArray(arrayOf<BotOperation>())
        assertEquals(operationsArray[0], jsr)
        assertEquals(operationsArray[1], karma)
        assertEquals(operationsArray[2], factoid)

        assertEquals(comparator.compare(karma, factoid), -1)
        assertEquals(comparator.compare(factoid, karma), 1)
        assertEquals(comparator.compare(karma, karma), 0)
        assertEquals(karma.getPriority(), jsr.getPriority())
        assertEquals(comparator.compare(jsr, karma), -1)
    }
}
