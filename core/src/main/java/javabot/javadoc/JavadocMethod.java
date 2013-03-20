package javabot.javadoc;

import com.antwerkz.maven.SPI;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Index;
import com.google.code.morphia.annotations.Indexes;
import com.google.code.morphia.annotations.PrePersist;
import com.google.code.morphia.annotations.Reference;
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
  @Reference
  private JavadocApi api;
  @Reference
  private JavadocClass javadocClass;

  private String methodName;
  private String upperMethodName;
  private String longSignatureTypes;
  private String shortSignatureTypes;
  private Integer paramCount;

  public JavadocMethod() {
  }

  public JavadocMethod(final JavadocClass parent, final String name, final int count,
      final String longArgs, final String shortArgs) {
    methodName = name;
    paramCount = count;
    longSignatureTypes = longArgs;
    shortSignatureTypes = shortArgs;
    final String url = parent.getDirectUrl() + "#" + methodName + "(" + longArgs + ")";
    setLongUrl(url);
    setDirectUrl(url);

    javadocClass = parent;
    api = parent.getApi();
  }

  public ObjectId getId() {
    return id;
  }

  public JavadocApi getApi() {
    return api;
  }

  public void setApi(final JavadocApi api) {
    this.api = api;
  }

  public JavadocClass getJavadocClass() {
    return javadocClass;
  }

  public void setJavadocClass(final JavadocClass javadocClass) {
    this.javadocClass = javadocClass;
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
    return javadocClass + "." + getShortSignature();
  }
}
