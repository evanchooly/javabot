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

@Entity("fields")
@SPI(Persistent.class)
@Indexes({
    @Index("javadocClass, upperName"),
})

public class JavadocField extends JavadocElement {
  @Id
  private ObjectId id;
  @Reference
  private JavadocApi api;
  @Reference
  private JavadocClass javadocClass;
  private String name;
  private String upperName;

  private String type;

  public JavadocField() {
  }

  public JavadocField(final JavadocClass parent, final String fieldName, final String fieldType) {
    javadocClass = parent;
    name = fieldName;
    type = fieldType;
    api = parent.getApi();
    String url = parent.getDirectUrl() + "#" + name;
    setLongUrl(url);
    setDirectUrl(url);
  }

  public JavadocApi getApi() {
    return api;
  }

  public void setApi(final JavadocApi api) {
    this.api = api;
  }

  public ObjectId getId() {
    return id;
  }

  public void setId(final ObjectId methodId) {
    id = methodId;
  }

  public JavadocClass getJavadocClass() {
    return javadocClass;
  }

  public void setJavadocClass(final JavadocClass javadocClass) {
    this.javadocClass = javadocClass;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  @PrePersist
  public void uppers() {
    upperName = name.toUpperCase();
  }

  @Override
  public String toString() {
    return javadocClass + "#" + name + ":" + type;
  }

}