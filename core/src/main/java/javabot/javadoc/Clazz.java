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
public class Clazz extends JavadocElement {
  @Id
  private ObjectId id;
  private Api api;
  private ObjectId apiId;
  private String packageName;
  private String upperPackage;
  private String name;
  private String upperName;
  private ObjectId superClassId;
  private List<Method> methods = new ArrayList<>();
  private List<Field> fields = new ArrayList<>();
  private String apiName;

  public Clazz() {
  }

  public Clazz(final Api api, final String pkg, final String name) {
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

  public void setApi(final Api api) {
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

  public void setSuperClassId(final Clazz aClass) {
    superClassId = aClass.getId();
  }

  public List<Method> getMethods() {
    return methods;
  }

  public void setMethods(final List<Method> list) {
    methods = list;
  }

  public List<Field> getFields() {
    return fields;
  }

  public void setFields(final List<Field> list) {
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