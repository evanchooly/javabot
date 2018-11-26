package javabot.operations

import com.antwerkz.sofia.Sofia
import com.google.inject.Inject
import javabot.BaseTest
import javabot.model.JavabotUser
import org.testng.Assert.assertEquals
import org.testng.annotations.*
import java.time.Duration
import java.util.*

class PollOperationTest
@Inject constructor(val operation: PollOperation)
    : BaseTest() {
    companion object {
        val ICECREAM_POLL = "best ice cream;vanilla;strawberry;chocolate"
    }

    val defaultDuration = operation.expiration
    val testDuration = Duration.ofMillis(100)

    fun waitForExpiration() = Thread.sleep(testDuration.toMillis() * 2)

    @BeforeMethod
    fun setDuration() {
        operation.expiration = testDuration
    }

    @AfterMethod
    fun resetDurationAndWait() {
        /* Allow all polls to expire, just in case */
        Thread.sleep(operation.expiration.toMillis())
        operation.expiration = defaultDuration
    }

    @Test
    fun createDuplicatePoll() {
        var response = operation.handleMessage(message("~poll ${ICECREAM_POLL}"))
        assertEquals(response.size, 1)
        assertEquals(response[0].value, Sofia.pollAccepted("best ice cream?"))
        response = operation.handleMessage(message("~poll ${ICECREAM_POLL}"))
        assertEquals(response.size, 1)
        assertEquals(response[0].value, Sofia.pollAlreadyExists())
        waitForExpiration()
    }


    @Test
    fun createPollWithResults() {
        testPoll({
            val random = Random()
            for (vote in 1..3) {
                for (count in 1..vote) {
                    operation.handleMessage(message("~vote $vote",
                            "~",
                            JavabotUser("voter$vote$count",
                                    "a" + random.nextInt(),
                                    "b@" + random.nextInt())))
                }
            }
        }, Sofia.pollWinner("best ice cream?", "chocolate", "three votes"))
    }

    @Test
    fun createPollWithThreewayTie() {
        testPoll({
            val random = Random()
            for (vote in 1..3) {
                operation.handleMessage(message("~vote $vote", "~",
                        JavabotUser("v" + random.nextInt(),
                                "a" + random.nextInt(),
                                "b@" + random.nextInt())))
            }
        },
                Sofia.pollTied("best ice cream?", "\"vanilla\", \"strawberry\", and \"chocolate\"", "each", "one vote")
        )
    }

    @Test
    fun createPollWithDualTie() {
        testPoll({
            val random = Random()
            for (vote in 2..3) {
                operation.handleMessage(message("~vote $vote", "~",
                        JavabotUser("v" + random.nextInt(),
                                "a" + random.nextInt(),
                                "b@" + random.nextInt())))
            }
        },
                Sofia.pollTied("best ice cream?", "\"strawberry\" and \"chocolate\"", "both", "one vote")
        )
    }

    @Test
    fun createPollWithTiePlural() {
        testPoll({
            val random = Random()
            for (i in 1..2) {
                for (vote in 1..3) {
                    operation.handleMessage(message("~vote $vote", "~",
                            JavabotUser("v" + random.nextInt(),
                                    "a" + random.nextInt(),
                                    "b@" + random.nextInt())))
                }
            }
        },
                Sofia.pollTied("best ice cream?", "\"vanilla\", \"strawberry\", and \"chocolate\"", "each", "two votes")
        )
    }

    @Test
    fun validateHostMaskForVoting() {
        testPoll({
            val random = Random()
            val hostmask = "b@" + random.nextInt()
            for (i in 1..2) {
                for (vote in 1..3) {
                    operation.handleMessage(message("~vote $vote", "~",
                            JavabotUser("v" + random.nextInt(),
                                    "a" + random.nextInt(),
                                    hostmask)))
                }
            }
        },
                Sofia.pollWinner("best ice cream?", "chocolate", "one vote")
        )
    }

    @Test
    fun createPollWithNoVotes() {
        testPoll({}, "The poll for \"best ice cream?\" received no votes.")
    }

    fun testPoll(function: () -> Unit = {}, expectedResult: String) {
        val response = operation.handleMessage(message("~poll ${ICECREAM_POLL}"))
        assertEquals(response.size, 1)
        assertEquals(response[0].value, Sofia.pollAccepted("best ice cream?"))
        function()
        waitForExpiration()
        val output = messages.messages
        messages.clear()
        assertEquals(output[0], expectedResult)
    }

    @DataProvider
    fun badPolls() = arrayOf(
            arrayOf("best ide?;eclipse"),
            arrayOf("best ide?")
    )

    @Test(dataProvider = "badPolls")
    fun createBadPolls(message: String) {
        val response = operation.handleMessage(message("~poll $message"))
        assertEquals(response.size, 1)
        assertEquals(response[0].value, Sofia.pollRejected())
        waitForExpiration()
    }

    @Test
    fun voteWithNoPoll() {
        val response = operation.handleMessage(message("~vote 1"))
        assertEquals(response.size, 1)
        assertEquals(response[0].value, Sofia.pollNoActivePoll())
    }

    @Test
    fun rejectPrivatePoll() {
        val response = operation.handleMessage(privateMessage("~poll ${ICECREAM_POLL}"))
        assertEquals(response.size, 0)
    }

    @Test
    fun rejectPrivateVote() {
        val response = operation.handleMessage(privateMessage("~vote 1"))
        assertEquals(response.size, 0)
    }

    @Test
    fun voteForInvalidChoice() {
        var response = operation.handleMessage(message("~poll ${ICECREAM_POLL}"))
        assertEquals(response.size, 1)
        response = operation.handleMessage(message("~vote 4"))
        assertEquals(response.size, 1)
        assertEquals(response[0].value, "Vote rejected; 4 is not an option. Votes should be numeric, from 1 to 3.")
        response = operation.handleMessage(message("~vote a4"))
        assertEquals(response.size, 1)
        assertEquals(response[0].value, "Vote rejected; I don't know how to interpret \"a4\". Votes should be numeric, from 1 to 3.")
        waitForExpiration()
    }

}