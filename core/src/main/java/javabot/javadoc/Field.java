package javabot.javadoc;

import com.antwerkz.maven.SPI;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.PrePersist;
import javabot.model.Persistent;
import org.bson.types.ObjectId;

@Entity("fields")
/*
@NamedQueries({
    @NamedQuery(name = ClazzDao.GET_FIELD_WITH_CLS_AND_PKG, query = "select f from Field f join f.clazz c where "
        + " upper(c.packageName)=:packageName and upper(c.className)=:className "
        + " and upper(f.name)=:fieldName order by c.packageName, c.className, f.name"),
    @NamedQuery(name = ClazzDao.GET_FIELD_WITH_CLS, query = "select f from Field f join f.clazz c where "
        + " upper(c.className)=:className and upper(f.name)=:fieldName order by c.packageName, c.className, f.name"),

    @NamedQuery(name = ClazzDao.GET_FIELD_WITH_CLS_PKG_API, query = "select f from Field f join f.clazz c where "
        + " upper(c.packageName)=:packageName and upper(c.className)=:className and c.api.id=:api"
        + " and upper(f.name)=:fieldName order by c.packageName, c.className, f.name"),
    @NamedQuery(name = ClazzDao.GET_FIELD_WITH_CLS_API, query = "select f from Field f join f.clazz c join c.api a where a.id=:api "
        + " and upper(c.className)=:className and upper(f.name)=:fieldName order by c.packageName, c.className, f.name")
})
*/
@SPI(Persistent.class)
public class Field extends JavadocElement {
  @Id
  private ObjectId id;
  private ObjectId apiId;
  private Clazz clazz;
  private ObjectId classId;
  private String name;
  private String upperName;
  private String type;
  private String apiName;

  public Field() {
  }

  public Field(final Clazz parent, final String fieldName, final String fieldType) {
    clazz = parent;
    name = fieldName;
    type = fieldType;
    apiName = parent.getApiName();
    String url = clazz.getDirectUrl() + "#" + name;
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

  public Clazz getClazz() {
    return clazz;
  }

  public void setClazz(final Clazz clazz) {
    this.clazz = clazz;
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
    return clazz + "." + name + ":" + type;
  }

}