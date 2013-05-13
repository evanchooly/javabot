package javabot.javadoc;

import java.util.ArrayList;
import java.util.List;

import com.antwerkz.maven.SPI;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Index;
import com.google.code.morphia.annotations.Indexes;
import com.google.code.morphia.annotations.PrePersist;
import com.google.code.morphia.annotations.Reference;
import javabot.model.Persistent;
import org.bson.types.ObjectId;

@Entity(value = "classes")
@SPI(Persistent.class)
@Indexes({
    @Index(value = "upperName"),
    @Index(value = "upperPackageName, upperName")
})
public class JavadocClass extends JavadocElement {
  @Id
  private ObjectId id;
  @Reference
  private JavadocApi api;
  private String packageName;
  private String upperPackageName;
  private String name;
  private String upperName;
  @Reference
  private JavadocClass superClass;
  private List<JavadocMethod> methods = new ArrayList<>();
  private List<JavadocField> fields = new ArrayList<>();

  public JavadocClass() {
  }

  public JavadocClass(final JavadocApi api, final String pkg, final String name) {
    this.api = api;
    packageName = pkg;
    this.name = name;
    setDirectUrl(api.getBaseUrl() + pkg.replace('.', '/') + "/" + name + ".html");
    setLongUrl(api.getBaseUrl() + "index.html?" + pkg.replace('.', '/') + "/" + name + ".html");
  }

  public ObjectId getId() {
    return id;
  }

  public void setId(final ObjectId classId) {
    id = classId;
  }

  public JavadocApi getApi() {
    return api;
  }

  public void setApi(final JavadocApi api) {
    this.api = api;
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(final String name) {
    packageName = name;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public JavadocClass getSuperClass() {
    return superClass;
  }

  public void setSuperClass(final JavadocClass javadocClass) {
    superClass = javadocClass;
  }

  public String getUpperName() {
    return upperName;
  }

  public void setUpperName(final String upperName) {
    this.upperName = upperName;
  }

  public String getUpperPackageName() {
    return upperPackageName;
  }

  public void setUpperPackageName(final String upperPackageName) {
    this.upperPackageName = upperPackageName;
  }

  @PrePersist
  public void uppers() {
    upperName = name.toUpperCase();
    upperPackageName = packageName.toUpperCase();
  }

  @Override
  public String toString() {
    return packageName + "." + name;
  }
}