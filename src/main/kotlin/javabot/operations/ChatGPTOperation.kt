package javabot.operations

import com.enigmastation.kgpt.asSystem
import com.enigmastation.kgpt.asUser
import jakarta.inject.Inject
import java.util.*
import javabot.Javabot
import javabot.Message
import javabot.NoOperationMessage
import javabot.dao.AdminDao
import javabot.dao.ChatGPTDao

/** Gets current weather conditions for a place given as a parameter. */
class ChatGPTOperation
@Inject
constructor(
    bot: Javabot,
    adminDao: AdminDao,
    private var chatGPTDao: ChatGPTDao,
    private var getFactoidOperation: GetFactoidOperation
) : BotOperation(bot, adminDao) {
    override fun handleMessage(event: Message): List<Message> {
        val message = event.value
        val responses = mutableListOf<Message>()
        if (message.lowercase(Locale.getDefault()).startsWith("gpt ")) {
            val query = message.substringAfter("gpt ").trim()
            when {
                query.equals("help", true) ->
                    responses.add(
                        Message(
                            event,
                            """
                        Simply use '~gpt query' to generate a Java-focused query. 
                        Note that GPT is not exceptionally reliable: the data is based
                        on content from April 2023, and should be considered as if the
                        question was "what would people say about my query, if they 
                        weren't really sure of what a correct answer might be?"
                        """
                                .cleanForIRC()
                        )
                    )
                else -> {
                    val factoid =
                        getFactoidOperation
                            .handleMessage(Message(event.user, query))
                            .firstOrNull()
                            ?.value
                    val seed =
                        when {
                            factoid != null ->
                                "Frame the response in the context of the query having a potential answer of '${factoid}'."
                            else -> null
                        }
                    val uuid = UUID.randomUUID()
                    try {
                        val result =
                            chatGPTDao.sendPromptToChatGPT(
                                query,
                                listOfNotNull(
                                    "Someone is asking '$query', in the context of the Java Virtual Machine."
                                        .asUser(),
                                    """
                                    If the answer does not contain constructive information for Java programmers,
                                    respond **ONLY** with \"$uuid-not applicable\" and no other text.
                                     
                                    Restrict your answer to being applicable to the Java Virtual Machine, and 
                                    limit the response's length to under 500 characters, formatted as simple text, 
                                    no markdown or other markup, but urls are acceptable.
                                """
                                        .trimIndent()
                                        .asSystem(),
                                    seed?.asSystem()
                                )
                            )
                        if (
                            !result.isNullOrEmpty() && !result.lowercase().contains(uuid.toString())
                        ) {
                            val response = result.cleanForIRC()
                            responses.add(Message(event, response))
                        } else {
                            responses.add(
                                NoOperationMessage(event, "no appropriate response from ChatGPT")
                            )
                        }
                    } catch (e: Throwable) {
                        Javabot.LOG.info("exception", e)
                    }
                }
            }
        }
        return responses
    }
}

fun String.cleanForIRC(): String {
    return this.trimIndent().trim().replace("\n ", " ").replace(" \n", " ").replace("\n", " ")
}
