package javabot.javadoc;

import com.antwerkz.maven.SPI;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import javabot.model.Persistent;

@Entity("methods")
@SPI(Persistent.class)
public class Method extends JavadocElement {
    @Id
    private Long id;
    private Long clazzId;
    private String parentName;
    private String methodName;
    private String longSignatureTypes;
    private String shortSignatureTypes;
    private String longSignatureStripped;
    private String shortSignatureStripped;
    private Integer paramCount;

    public Method() {
    }

    public Method(final String name, final Clazz parent, final int count, final String longArgs,
        final String longArgsStripped,
        final String shortArgs, final String shortArgsStripped) {
        methodName = name;
        clazzId = parent.getId();
        parentName = parent.toString();
        paramCount = count;
        longSignatureTypes = longArgs;
        longSignatureStripped = longArgsStripped;
        shortSignatureTypes = shortArgs;
        shortSignatureStripped = shortArgsStripped;
        final String url = parent.getDirectUrl() + "#" + methodName + "(" + longArgs + ")";
        setLongUrl(url);
        setDirectUrl(url);
    }

    @Override
    public String getApiName() {
        return null;//getClazz().getApi().getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long methodId) {
        id = methodId;
    }

    public String getShortSignature() {
        return methodName + "(" + shortSignatureStripped + ")";
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(final String name) {
        methodName = name;
    }

    public String getLongSignatureStripped() {
        return longSignatureStripped;
    }

    public void setLongSignatureStripped(final String stripped) {
        longSignatureStripped = stripped;
    }

    public String getLongSignatureTypes() {
        return longSignatureTypes;
    }

    public void setLongSignatureTypes(final String types) {
        longSignatureTypes = types;
    }

    public String getShortSignatureStripped() {
        return shortSignatureStripped;
    }

    public void setShortSignatureStripped(final String stripped) {
        shortSignatureStripped = stripped;
    }

    public String getShortSignatureTypes() {
        return shortSignatureTypes;
    }

    public void setShortSignatureTypes(final String types) {
        shortSignatureTypes = types;
    }

    public Integer getParamCount() {
        return paramCount;
    }

    public void setParamCount(final Integer count) {
        paramCount = count;
    }

    @Override
    public String toString() {
        return parentName + "." + getShortSignature();
    }
}
