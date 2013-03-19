package javabot.javadoc;

import com.antwerkz.maven.SPI;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Index;
import com.google.code.morphia.annotations.Indexes;
import com.google.code.morphia.annotations.PrePersist;
import javabot.model.Persistent;
import org.bson.types.ObjectId;

@Entity("methods")
@SPI(Persistent.class)
@Indexes({
    @Index("clazzId, name"),
    @Index("apiName"),
})
public class JavadocMethod extends JavadocElement {
  @Id
  private ObjectId id;
  private ObjectId classId;
  private String parentName;
  private String methodName;
  private String upperMethodName;
  private String longSignatureTypes;
  private String shortSignatureTypes;
  private Integer paramCount;
  private String apiName;
  private ObjectId apiId;

  public JavadocMethod() {
  }

  public JavadocMethod(final String apiName, final JavadocClass parent, final String name, final int count,
      final String longArgs,
      final String shortArgs) {
    methodName = name;
    classId = parent.getId();
    parentName = parent.toString();
    paramCount = count;
    this.apiName = apiName;
    apiId = parent.getApiId();
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

  public void setApiName(final String apiName) {
    this.apiName = apiName;
  }

  public ObjectId getId() {
    return id;
  }

  public ObjectId getApiId() {
    return apiId;
  }

  public void setApiId(final ObjectId apiId) {
    this.apiId = apiId;
  }

  public ObjectId getClassId() {
    return classId;
  }

  public void setClassId(final ObjectId classId) {
    this.classId = classId;
  }

  public String getParentName() {
    return parentName;
  }

  public void setParentName(final String parentName) {
    this.parentName = parentName;
  }

  public void setId(final ObjectId methodId) {
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
