package javabot.javadoc;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import javabot.model.Persistent;
import org.apache.commons.lang.StringUtils;

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
    private Integer paramCount;

    public Method() {
    }

    public Method(final String name, final Clazz parent, final int count, final String longArgs, final String longArgsStripped,
        final String shortArgs, final String shortArgsStripped) {
        
        methodName = name;
        clazz = parent;
        paramCount = count;
        longSignatureTypes = filter(longArgs);
        longSignatureStripped = filter(longArgsStripped);
        shortSignatureTypes = filter(shortArgs);
        shortSignatureStripped = filter(shortArgsStripped);
        final String url = clazz.getDirectUrl() + "#" + methodName + "(" + longArgs + ")";
        setLongUrl(url);
        setDirectUrl(url);
    }

    private String filter(String value) {
        return StringUtils.isEmpty(value) ? null : value;
    }
    
    @Override
    @Transient
    public String getApiName() {
        return getClazz().getApi().getName();
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
