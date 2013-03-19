package javabot.javadoc;

import java.util.ArrayList;
import java.util.List;

import com.antwerkz.maven.SPI;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Index;
import com.google.code.morphia.annotations.Indexes;
import com.google.code.morphia.annotations.PrePersist;
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
  private JavadocApi api;
  private ObjectId apiId;
  private String packageName;
  private String upperPackage;
  private String name;
  private String upperName;
  private ObjectId superClassId;
  private List<JavadocMethod> methods = new ArrayList<>();
  private List<JavadocField> fields = new ArrayList<>();
  private String apiName;

  public JavadocClass() {
  }

  public JavadocClass(final JavadocApi api, final String pkg, final String name) {
    apiId = api.getId();
    apiName = api.getName();
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

  public void setApi(final JavadocApi api) {
    this.api = api;
  }

  public ObjectId getApiId() {
    return apiId;
  }

  public void setApiId(final ObjectId apiId) {
    this.apiId = apiId;
  }

  @Override
  public String getApiName() {
    return apiName;
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

  public ObjectId getSuperClassId() {
    return superClassId;
  }

  public void setSuperClassId(final ObjectId classId) {
    superClassId = classId;
  }

  public List<JavadocMethod> getMethods() {
    return methods;
  }

  public void setMethods(final List<JavadocMethod> list) {
    methods = list;
  }

  public List<JavadocField> getFields() {
    return fields;
  }

  public void setFields(final List<JavadocField> list) {
    fields = list;
  }

  public String getUpperName() {
    return upperName;
  }

  public void setUpperName(final String upperName) {
    this.upperName = upperName;
  }

  public String getUpperPackage() {
    return upperPackage;
  }

  public void setUpperPackage(final String upperPackage) {
    this.upperPackage = upperPackage;
  }

  @PrePersist
  public void uppers() {
    upperName = name.toUpperCase();
    upperPackage = packageName.toUpperCase();
  }

  @Override
  public String toString() {
    return packageName + "." + name;
  }
}