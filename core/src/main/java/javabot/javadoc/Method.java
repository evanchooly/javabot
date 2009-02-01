package javabot.javadoc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import javabot.model.Persistent;

@Entity
@Table(name = "methods")
public class Method extends JavadocElement implements Persistent {
    private Clazz clazz;
    private String methodName;
    private Long id;
    private String longSignatureTypes;
    private String shortSignatureTypes;
    private String longSignatureStripped;
    private String shortSignatureStripped;
    private Integer paramCount = 0;

    public Method() {
    }

    @SuppressWarnings({"StringConcatenationInsideStringBufferAppend", "StringContatenationInLoop"})
    public Method(final String signature, final Clazz parent) {
        clazz = parent;
        final int leftParen = signature.indexOf("(");
        final String[] params = signature.substring(leftParen + 1, signature.length() - 1).split(",");
        methodName = signature.substring(0, leftParen);
        final StringBuilder shortTypes = new StringBuilder();
        for(final String parameter : params) {
            if(shortTypes.length() != 0) {
                shortTypes.append(", ");
            }
            shortTypes.append(parameter.substring(parameter.lastIndexOf(".") + 1));
        }
        paramCount = params.length;
        longSignatureTypes = signature.substring(leftParen + 1, signature.length() - 1);
        shortSignatureTypes = shortTypes.toString();
        longSignatureStripped = longSignatureTypes.replaceAll(" ", "");
        shortSignatureStripped = shortSignatureTypes.replaceAll(" ", "");
        setLongUrl( clazz.getLongUrl() + "#" + signature.replaceAll(" ", "%20"));
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(final Long methodId) {
        id = methodId;
    }

    @Transient
    public String getShortSignature() {
        return methodName + "(" + shortSignatureStripped + ")";
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(final String name) {
        methodName = name;
    }

    @Column(length = 1000)
    public String getLongSignatureStripped() {
        return longSignatureStripped;
    }

    public void setLongSignatureStripped(final String stripped) {
        longSignatureStripped = stripped;
    }

    @Column(length = 1000)
    public String getLongSignatureTypes() {
        return longSignatureTypes;
    }

    public void setLongSignatureTypes(final String types) {
        longSignatureTypes = types;
    }

    @Column(length = 1000)
    public String getShortSignatureStripped() {
        return shortSignatureStripped;
    }

    public void setShortSignatureStripped(final String stripped) {
        shortSignatureStripped = stripped;
    }

    @Column(length = 1000)
    public String getShortSignatureTypes() {
        return shortSignatureTypes;
    }

    public void setShortSignatureTypes(final String types) {
        shortSignatureTypes = types;
    }

    @ManyToOne
    public Clazz getClazz() {
        return clazz;
    }

    public void setClazz(final Clazz parent) {
        clazz = parent;
    }

    public Integer getParamCount() {
        return paramCount;
    }

    public void setParamCount(final Integer count) {
        paramCount = count;
    }

    @Override
    public String toString() {
        return clazz + "." + getShortSignature();
    }
}
