package javabot.model

import org.testng.Assert.*
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.lang.IllegalArgumentException

class TestPollModel {
    companion object {
        val SAMPLE_POLL = "best ice cream flavor;strawberry;chocolate;vanilla"
    }

    @DataProvider
    fun pollQuestions() = arrayOf(
            arrayOf("best ide?;netbeans;idea;eclipse", "best ide?", arrayOf("netbeans", "idea", "eclipse")),
            arrayOf(" best ide? ; netbeans ; idea ; eclipse ", "best ide?", arrayOf("netbeans", "idea", "eclipse")),
            arrayOf("best ide;netbeans;idea;eclipse", "best ide?", arrayOf("netbeans", "idea", "eclipse"))
    )

    @DataProvider
    fun badPolls() = arrayOf(
            arrayOf("best ide?"),
            arrayOf("best ide?;eclipse")
    )

    @Test(dataProvider = "pollQuestions")
    fun parsePollFromInput(input: String, question: String, candidates: Array<String>) {
        val poll = Poll.parse(input)
        assertEquals(poll.question, question)
        assertEquals(poll.answers.size, 3)
    }

    @Test(dataProvider = "badPolls", expectedExceptions = arrayOf(IllegalArgumentException::class))
    fun createInvalidPoll(message: String) {
        Poll.parse(message)
    }

    @Test
    fun voteInPoll() {
        val poll = Poll.parse(SAMPLE_POLL)
        for (option in 1..3) {
            for (count in 1..option) {
                poll.vote("user$option$count", option)
            }
        }
        val results = poll.results()
        assertEquals(results[1], 1)
        assertEquals(results[2], 2)
        assertEquals(results[3], 3)
    }

    @Test
    fun invalidVote() {
        val poll = Poll.parse(SAMPLE_POLL)
        listOf(-4, 0, 4, 10812).forEach {
            try {
                poll.vote("user1", it)
                fail("Should have thrown a bad vote exception with $it")
            } catch (e: BadVoteOptionException) {
            }
        }
    }
}