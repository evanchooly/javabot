package javabot.javadoc;

import java.io.File;
import java.io.IOException;

import com.sun.javadoc.Doclet;
import com.sun.javadoc.RootDoc;
import com.sun.tools.javadoc.Main;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StructureDoclet extends Doclet {
    private static Log log = LogFactory.getLog(StructureDoclet.class);

    public static int optionLength(String option) {
        if("-x".equals(option)) {
            return 2;
        }
        return 0;
    }

    public static boolean start(RootDoc doc) {
        StructureReference reference = new StructureReference(doc);
        String[][] options = doc.options();
        for(int i = 0; i < options.length; i++) {
            String[] option = options[i];
            if("-x".equals(option[0])) {
                if(option.length != 2) {
                    return false;
                }
                File outputFile = new File(option[1]);
                try {
                    reference.writeReference(outputFile);
                }
                catch(IOException exception) {
                    exception.printStackTrace();
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        try {
            Main.execute("StructureDoclet", "javabot.javadoc.StructureDoclet",
                new String[]{"-x", "c:/docref.xml", "-source", "1.4", "-subpackages",
                    "java", "-sourcepath", "c:/javasrc"});
        } catch(Exception exception) {
            log.error(exception.getMessage(), exception);
        }
    }
}
