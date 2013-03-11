package javabot.javadoc;

import com.antwerkz.maven.SPI;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Index;
import com.google.code.morphia.annotations.Indexes;
import com.google.code.morphia.annotations.PrePersist;
import javabot.model.Persistent;
import org.bson.types.ObjectId;

@Entity("fields")
@SPI(Persistent.class)
@Indexes({
    @Index("clazzId, name"),
    @Index("apiName"),
})

public class Field extends JavadocElement {
  @Id
  private ObjectId id;
  private ObjectId apiId;
  private ObjectId classId;
  private String name;
  private String upperName;
  private String parentName;
  private String type;
  private String apiName;

  public Field() {
  }

  public Field(final Clazz parent, final String fieldName, final String fieldType) {
    classId = parent.getId();
    name = fieldName;
    type = fieldType;
    apiName = parent.getApiName();
    apiId = parent.getApiId();
    parentName = parent.toString();
    String url = parent.getDirectUrl() + "#" + name;
    setLongUrl(url);
    setDirectUrl(url);
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

  public void setApiName(final String apiName) {
    this.apiName = apiName;
  }

  public ObjectId getId() {
    return id;
  }

  public void setId(final ObjectId methodId) {
    id = methodId;
  }

  public ObjectId getClassId() {
    return classId;
  }

  public void setClassId(final ObjectId classId) {
    this.classId = classId;
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
    return parentName + "." + name + ":" + type;
  }

}