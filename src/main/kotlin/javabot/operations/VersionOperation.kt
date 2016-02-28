package javabot.operations

import ca.grimoire.maven.ArtifactDescription
import ca.grimoire.maven.NoArtifactException
import com.antwerkz.sofia.Sofia
import com.google.inject.Inject
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import java.io.File
import java.io.FileInputStream

class VersionOperation @Inject constructor(bot: Javabot, adminDao: AdminDao) : BotOperation(bot, adminDao), StandardOperation {
    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value
        if ("version".equals(message, ignoreCase = true)) {
            responses.add(Message(event, Sofia.botVersion(loadVersion())))
        }
        return responses
    }

    fun loadVersion(): String {
        try {
            return ArtifactDescription.locate("javabot", "core").version
        } catch (nae: NoArtifactException) {
            try {
                val file = File("target/maven-archiver/pom.properties")
                if (file.exists()) {
                    return ArtifactDescription.locate("javabot", "core", { FileInputStream(file) }).version
                } else {
                    return "UNKNOWN"
                }
            } catch (e: NoArtifactException) {
                return "UNKNOWN"
            }

        }

    }

}