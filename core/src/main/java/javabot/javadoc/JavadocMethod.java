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
    @Index("apiId"),
    @Index("javadocClassId, upperName"),
    @Index("apiId, javadocClassId, upperName"),
})
public class JavadocMethod extends JavadocElement {
  @Id
  private ObjectId id;
  private ObjectId javadocClassId;

  private String name;
  private String upperName;
  private String longSignatureTypes;
  private String shortSignatureTypes;
  private Integer paramCount;

  private String parentClassName;

  public JavadocMethod() {
  }

  public JavadocMethod(final JavadocClass parent, final String name, final int count,
      final String longArgs, final String shortArgs) {
    this.name = name;
    paramCount = count;
    longSignatureTypes = longArgs;
    shortSignatureTypes = shortArgs;
    final String url = parent.getDirectUrl() + "#" + this.name + "(" + longArgs + ")";
    setLongUrl(url);
    setDirectUrl(url);

    javadocClassId = parent.getId();
    setApiId(parent.getApiId());
    parentClassName = parent.toString();
  }

  public ObjectId getId() {
    return id;
  }

  public ObjectId getJavadocClassId() {
    return javadocClassId;
  }

  public void setJavadocClassId(final ObjectId javadocClassId) {
    this.javadocClassId = javadocClassId;
  }

  public void setJavadocClassId(final JavadocClass javadocClass) {
    this.javadocClassId = javadocClass.getId();
    parentClassName = javadocClass.toString();
  }

  public void setId(final ObjectId methodId) {
    id = methodId;
  }

  public final String getShortSignature() {
    return name + "(" + shortSignatureTypes + ")";
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
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

  public String getUpperName() {
    return upperName;
  }

  public void setUpperName(final String upperName) {
    this.upperName = upperName;
  }

  @PrePersist
  public void uppers() {
    upperName = name.toUpperCase();
  }

  @Override
  public String toString() {
    return parentClassName + "." + getShortSignature();
  }
}
