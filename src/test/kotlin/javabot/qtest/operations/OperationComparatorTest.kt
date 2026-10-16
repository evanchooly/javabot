package javabot.qtest.operations

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import java.util.TreeSet
import javabot.BaseTest
import javabot.operations.BotOperation
import javabot.operations.GetFactoidOperation
import javabot.operations.JSROperation
import javabot.operations.KarmaOperation
import javabot.operations.OperationComparator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@QuarkusTest
class OperationComparatorTest : BaseTest() {
    @Inject private lateinit var karma: KarmaOperation
    @Inject private lateinit var factoid: GetFactoidOperation
    @Inject private lateinit var jsr: JSROperation

    @Test
    fun testOperationComparator() {
        val comparator = OperationComparator()
        val operations = TreeSet(comparator)

        operations.add(karma)
        operations.add(jsr)
        operations.add(factoid)

        @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
        val operationsArray = operations.toArray(arrayOf<BotOperation>())
        assertEquals(jsr, operationsArray[0])
        assertEquals(karma, operationsArray[1])
        assertEquals(factoid, operationsArray[2])

        assertEquals(-1, comparator.compare(karma, factoid))
        assertEquals(1, comparator.compare(factoid, karma))
        assertEquals(0, comparator.compare(karma, karma))
        assertEquals(jsr.getPriority(), karma.getPriority())
        assertEquals(-1, comparator.compare(jsr, karma))
    }
}
