package javabot.operations;

import ca.grimoire.maven.ArtifactDescription;
import ca.grimoire.maven.NoArtifactException;
import com.antwerkz.sofia.Sofia;
import javabot.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class VersionOperation extends BotOperation implements StandardOperation  {
    @Override
    public boolean handleMessage(final Message event) {
        final String message = event.getValue();
        if ("version".equalsIgnoreCase(message)) {
            getBot().postMessage(event.getChannel(), event.getUser(), Sofia.botVersion(loadVersion()), event.isTell());
            return true;
        }
        return false;
    }

    public final String loadVersion() {
        ArtifactDescription description;
        try {
            description = ArtifactDescription.locate("javabot", "core");
            return description.getVersion();
        } catch (NoArtifactException nae) {
            try {
                final File file = new File("target/maven-archiver/pom.properties");
                if (file.exists()) {
                    description = ArtifactDescription.locate("javabot", "core", resource -> {
                        try {
                            return new FileInputStream(file);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e.getMessage(), e);
                        }
                    });
                    return description.getVersion();
                } else {
                    return "UNKNOWN";
                }
            } catch (NoArtifactException e) {
                return "UNKNOWN";
            }
        }
    }

}