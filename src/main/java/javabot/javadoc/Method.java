package javabot.javadoc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.Parameter;

@Entity
@Table(name = "methods")
public class Method {
    private Clazz clazz;
    private String methodName;
    private Long id;
    private String longSignatureTypes;
    private String shortSignatureTypes;
    private String longSignatureStripped;
    private String shortSignatureStripped;
    private String url;
    private Integer paramCount = 0;

    public Method() {
    }

    @SuppressWarnings({"StringConcatenationInsideStringBufferAppend", "StringContatenationInLoop"})
    public Method(ExecutableMemberDoc doc, Clazz parent) {
        clazz = parent;
        methodName = doc.name();
        Parameter[] parameters = doc.parameters();
        StringBuilder longTypes = new StringBuilder();
        StringBuilder shortTypes = new StringBuilder();
        for(Parameter parameter : parameters) {
            if(paramCount != 0) {
                longTypes.append(", ");
                shortTypes.append(", ");
            }
            longTypes.append(parameter.type().qualifiedTypeName() + parameter.type().dimension());
            shortTypes.append(parameter.type().typeName() + parameter.type().dimension());
            paramCount++;
        }
        longSignatureTypes = longTypes.toString();
        shortSignatureTypes = shortTypes.toString();
        longSignatureStripped = longSignatureTypes.replaceAll(" ", "");
        shortSignatureStripped = shortSignatureTypes.replaceAll(" ", "");
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long methodId) {
        id = methodId;
    }

    @Transient
    public String getLongSignature() {
        return methodName + "(" + longSignatureTypes + ")";
    }

    @Transient
    public String getShortSignature() {
        return methodName + "(" + shortSignatureTypes + ")";
    }

    public String getMethodUrl(String baseUrl) {
        return clazz.getQualifiedName() + "." + getLongSignature() + ": " +
            clazz.getClassHTMLPage(baseUrl) + "#" + getLongSignature().replaceAll(" ", "%20");
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String name) {
        methodName = name;
    }

    @Column(length = 1000)
    public String getLongSignatureStripped() {
        return longSignatureStripped;
    }

    public void setLongSignatureStripped(String stripped) {
        longSignatureStripped = stripped;
    }

    @Column(length = 1000)
    public String getLongSignatureTypes() {
        return longSignatureTypes;
    }

    public void setLongSignatureTypes(String types) {
        longSignatureTypes = types;
    }

    @Column(length = 1000)
    public String getShortSignatureStripped() {
        return shortSignatureStripped;
    }

    public void setShortSignatureStripped(String stripped) {
        shortSignatureStripped = stripped;
    }

    @Column(length = 1000)
    public String getShortSignatureTypes() {
        return shortSignatureTypes;
    }

    public void setShortSignatureTypes(String types) {
        shortSignatureTypes = types;
    }

    @ManyToOne
    public Clazz getClazz() {
        return clazz;
    }

    public void setClazz(Clazz parent) {
        clazz = parent;
    }

    public Integer getParamCount() {
        return paramCount;
    }

    public void setParamCount(Integer count) {
        paramCount = count;
    }
}
