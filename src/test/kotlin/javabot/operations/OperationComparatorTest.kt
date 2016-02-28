package javabot.operations

import com.google.inject.Inject
import javabot.BaseTest
import org.testng.Assert.assertEquals
import org.testng.annotations.Test
import java.util.TreeSet

class OperationComparatorTest : BaseTest() {
    @Inject
    lateinit var karma: KarmaOperation
    @Inject
    lateinit var factoid: GetFactoidOperation
    @Inject
    lateinit var jsr: JSROperation

    @Test fun testOperationComparator() {
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
