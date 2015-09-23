package javabot.operations

import ca.grimoire.maven.ArtifactDescription
import ca.grimoire.maven.NoArtifactException
import com.antwerkz.sofia.Sofia
import javabot.Message

import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

public class VersionOperation : BotOperation(), StandardOperation {
    override fun handleMessage(event: Message): Boolean {
        val message = event.value
        if ("version".equalsIgnoreCase(message)) {
            bot.postMessageToChannel(event, Sofia.botVersion(loadVersion()))
            return true
        }
        return false
    }

    public fun loadVersion(): String {
        val description: ArtifactDescription
        try {
            description = ArtifactDescription.locate("javabot", "core")
            return description.version
        } catch (nae: NoArtifactException) {
            try {
                val file = File("target/maven-archiver/pom.properties")
                if (file.exists()) {
                    description = ArtifactDescription.locate("javabot", "core") { resource ->
                        try {
                            return@ArtifactDescription.locate FileInputStream(file)
                        } catch (e: FileNotFoundException) {
                            throw RuntimeException(e.getMessage(), e)
                        }
                    }
                    return description.version
                } else {
                    return "UNKNOWN"
                }
            } catch (e: NoArtifactException) {
                return "UNKNOWN"
            }

        }

    }

}