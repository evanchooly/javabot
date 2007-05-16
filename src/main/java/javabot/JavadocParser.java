package javabot;

import java.io.File;
import java.io.IOException;
import javabot.javadoc.StructureReference;
import org.jdom.JDOMException;

public class JavadocParser {
    private StructureReference reference;

    public JavadocParser(File file, String url)
        throws IOException, JDOMException {
        this.baseUrl = url;
        reference = new StructureReference(file);
    }

    public String[] javadoc(String key) {
        int openIndex = key.indexOf('(');
        if(openIndex == -1) {
            return reference.getClassDocUrls(key, baseUrl);
        }
        int finalIndex = key.lastIndexOf('.',openIndex);
        int closeIndex = key.indexOf(')');
        if(closeIndex == -1 || finalIndex == -1) {
            return new String[0];
        }
        String className = key.substring(0, finalIndex);
        String methodName = key.substring(finalIndex + 1, openIndex);
        String signatureTypes = key.substring(openIndex + 1, closeIndex);
        return reference.getMethodDocUrls(className, methodName, signatureTypes, baseUrl);
    }

    private final String baseUrl;
}