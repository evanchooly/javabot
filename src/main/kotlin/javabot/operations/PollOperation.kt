package javabot.operations

import com.antwerkz.sofia.Sofia
import com.google.inject.Inject
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.ChangeDao
import javabot.dao.ChannelDao
import javabot.dao.LinkDao
import javabot.model.BadVoteOptionException
import javabot.model.Channel
import javabot.model.Poll
import pl.allegro.finance.tradukisto.ValueConverters
import java.lang.Integer.parseInt
import java.time.Duration
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class PollOperation
@Inject
constructor(bot: Javabot,
            adminDao: AdminDao,
            var changeDao: ChangeDao,
            var channelDao: ChannelDao)
    : BotOperation(bot, adminDao) {
    companion object {
        val converter=ValueConverters.ENGLISH_INTEGER
    }

    val polls: MutableMap<String, Poll> = mutableMapOf()
    var expiration: Duration = Duration.ofMinutes(5)
    val executorService: ScheduledExecutorService = Executors.newScheduledThreadPool(2)

    override fun handleMessage(event: Message): List<Message> {
        val responses: MutableList<Message> = mutableListOf()
        val submission = event.value.trim()
        val command = submission.split(" ")
        if (command.size > 0) {
            when (command[0].toLowerCase()) {
                "poll" -> createPoll(responses, event, submission)
                "vote" -> addVote(command, event, responses)
            }
        }
        return responses
    }

    private fun addVote(command: List<String>, event: Message, responses: MutableList<Message>) {
        val channel = event.channel
        if (channel != null) {
            if (polls.containsKey(channel.name)) {
                val poll = polls[channel.name]!! // we know it's there by now
                try {
                    val vote = parseInt(command[1])
                    try {
                        poll.vote(event.user.hostmask, vote)
                    } catch (e: BadVoteOptionException) {
                        responses.add(Message(channel, event,
                                Sofia.pollBadVoteOption(command[1], 1, poll.answers.size)))
                    }
                } catch (e: Exception) {
                    responses.add(Message(channel, event,
                            Sofia.pollBadVote(command[1], 1, poll.answers.size)))
                }
            } else {
                responses.add(Message(channel, event, Sofia.pollNoActivePoll()))
            }
        }
    }

    private fun createPoll(responses: MutableList<Message>, event: Message, submission: String) {
        val channel = event.channel
        if (channel != null) {
            if (polls.containsKey(channel.name)) {
                responses.add(Message(channel, event, Sofia.pollAlreadyExists()))
            } else {
                try {
                    val poll = Poll.parse(submission.substring(5), expiration)
                    polls.put(channel.name, poll)
                    executorService.schedule({
                        this.expirePoll(channel.name)
                    }, expiration.toMillis(), TimeUnit.MILLISECONDS)
                    responses.add(Message(channel, event, Sofia.pollAccepted(poll.question)))
                } catch (iae: IllegalArgumentException) {
                    responses.add(Message(channel, event, Sofia.pollRejected()))
                }
            }
        }
    }

    private fun expirePoll(name: String) {
        val activePoll = polls[name]
        val channel = bot.channelDao.get(name)
        if (activePoll != null) {
            /* we need to clear the expired poll */
            polls.remove(name)
            if (channel != null) {
                val winner = activePoll.results().maxBy { it.value }
                if (winner != null) {
                    // check for ties!
                    val winners = activePoll
                            .results()
                            .filter { it.value == winner.value }
                            .toList()
                    if (winners.size == 1) {
                        bot.adapter.send(channel, Sofia.pollWinner(
                                activePoll.question,
                                activePoll.answers[winner.key - 1],
                                expressVotes(winner.value)))
                    } else {
                        val lastEntry = winners.last().first
                        val (winnerList, enumerator, voteCount) = listOf(
                                buildWinnerList(
                                        winners, activePoll, lastEntry,
                                        if (winners.size > 2) ", " else " "),
                                if (winners.size > 2) "each" else "both",
                                expressVotes(winner.value))
                        bot.adapter.send(channel,
                                Sofia.pollTied(activePoll.question, winnerList, enumerator, voteCount))
                    }
                } else {
                    bot.adapter.send(channel, Sofia.pollNoAnswers(activePoll.question))
                }
            }
        } else {
            /*
            * Something happened here, where's the poll being removed?
            *
            * Truth is, it doesn't matter - we can disregard any errors here.
            */
        }
    }

    private fun expressVotes(votes: Int): String {
        return converter.asWords(votes) +
                " vote" +
                if (votes != 1) "s" else ""

    }

    private fun buildWinnerList(winners: List<Pair<Int, Int>>, activePoll: Poll, lastEntry: Int, separator: String): String {
        return winners
                .map {
                    val answer = activePoll.answers[it.first - 1]
                    if (it.first == lastEntry) {
                        "and \"$answer\""
                    } else {
                        "\"$answer\""
                    }
                }
                .joinToString(separator)
    }
}
