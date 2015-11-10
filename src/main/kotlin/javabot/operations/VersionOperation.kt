package javabot.operations

import ca.grimoire.maven.ArtifactDescription
import ca.grimoire.maven.NoArtifactException
import ca.grimoire.maven.ResourceProvider
import com.antwerkz.sofia.Sofia
import javabot.Message
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

public class VersionOperation : BotOperation(), StandardOperation {
    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value
        if ("version".equals(message, ignoreCase = true)) {
            responses.add(Message(event, Sofia.botVersion(loadVersion())))
        }
        return responses
    }

    public fun loadVersion(): String {
        try {
            return ArtifactDescription.locate("javabot", "core").version
        } catch (nae: NoArtifactException) {
            try {
                val file = File("target/maven-archiver/pom.properties")
                if (file.exists()) {
                    return ArtifactDescription.locate("javabot", "core", object : ResourceProvider {
                        override fun getResourceAsStream(p0: String?): InputStream? {
                            return FileInputStream(file)
                        }
                    }).version
                } else {
                    return "UNKNOWN"
                }
            } catch (e: NoArtifactException) {
                return "UNKNOWN"
            }

        }

    }

}