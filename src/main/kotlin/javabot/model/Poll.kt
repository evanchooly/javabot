package javabot.model

import java.time.Duration

class BadVoteOptionException(val option: Int) : Exception()

class Poll(val question: String,
           val answers: List<String>,
           val expiry: Duration = Duration.ofMinutes(5)) {
    /*
    This is a simple map of username to option. Votes are counted via histogram.
    This makes voting very simple: username and entry goes into the map, so if the
    user changes his or her mind, no search, no removal, nothing special happens.
    */
    private val votes: MutableMap<String, Int> = mutableMapOf()

    companion object {
        fun parse(input: String,
                  expiry: Duration = Duration.ofMinutes(5)): Poll {
            val parsed = input.split(Regex(";"))
            val q = parsed[0].trim()
            val question = if (q.endsWith("?")) {
                q
            } else {
                "$q?"
            }
            val answers: List<String> = parsed.subList(1, parsed.size).map { it.trim() }
            if(answers.size<2) {
                throw IllegalArgumentException("too few answers provided for poll")
            }
            return Poll(question, answers, expiry)
        }
    }

    fun vote(username: String, option: Int) {
        if (option < 1 || option > answers.size) {
            throw BadVoteOptionException(option)
        }
        votes[username] = option
    }

    fun results(): Map<Int, Int> = votes.entries.groupingBy { it.value }.eachCount()
}
