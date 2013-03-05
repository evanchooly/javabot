package javabot.javadoc;

import com.antwerkz.maven.SPI;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.PrePersist;
import javabot.model.Persistent;

@Entity("methods")
@SPI(Persistent.class)
public class Method extends JavadocElement {
  @Id
  private Long id;
  private Long clazzId;
  private String parentName;
  private String methodName;
  private String upperMethodName;
  private String longSignatureTypes;
  private String shortSignatureTypes;
  private Integer paramCount;
  private String apiName;

  public Method() {
  }

  public Method(final String apiName, final Clazz parent, final String name, final int count,
      final String longArgs,
      final String shortArgs) {
    this.apiName = apiName;
    methodName = name;
    clazzId = parent.getId();
    parentName = parent.toString();
    paramCount = count;
    longSignatureTypes = longArgs;
    shortSignatureTypes = shortArgs;
    final String url = parent.getDirectUrl() + "#" + methodName + "(" + longArgs + ")";
    setLongUrl(url);
    setDirectUrl(url);
  }

  @Override
  public String getApiName() {
    return apiName;
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long methodId) {
    id = methodId;
  }

  public String getShortSignature() {
    return methodName + "(" + shortSignatureTypes + ")";
  }

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(final String name) {
    methodName = name;
  }

  public String getLongSignatureTypes() {
    return longSignatureTypes;
  }

  public void setLongSignatureTypes(final String types) {
    longSignatureTypes = types;
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

  public String getUpperMethodName() {
    return upperMethodName;
  }

  public void setUpperMethodName(final String upperMethodName) {
    this.upperMethodName = upperMethodName;
  }

  @PrePersist
  public void uppers() {
    upperMethodName = methodName.toUpperCase();
  }

  @Override
  public String toString() {
    return parentName + "." + getShortSignature();
  }
}
