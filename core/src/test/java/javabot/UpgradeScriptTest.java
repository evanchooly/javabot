package javabot;

import javabot.database.UpgradeScript;
import org.testng.annotations.Test;

import java.util.Set;

@Test
public class UpgradeScriptTest {
    public void validateIDs() {
        final Set<UpgradeScript> scripts = UpgradeScript.loadScripts();
        for (final UpgradeScript script : scripts) {
            System.out.println("script = " + script);
        }
    }
}
