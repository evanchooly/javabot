package javabot.javadoc;

import javabot.model.Persistent;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.PrePersist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

@Entity(value = "apis", noClassnameStored = true)
@Indexes({
    @Index(value = "name", unique = true),
    @Index(value = "upperName", unique = true)
})
public class JavadocApi implements Persistent {
  private static final Logger log = LoggerFactory.getLogger(JavadocApi.class);

  @Id
  private ObjectId id;

  private String name;

  private String upperName;

  private String baseUrl;

  private String downloadUrl;

  public static final List<String> JDK_JARS = Arrays.asList("rt.jar", "jce.jar");

  public JavadocApi() {
  }

  public JavadocApi(final String apiName, final String url, final String downloadUrl) {
    name = apiName;
    baseUrl = url.endsWith("/") ? url : url + "/";
    this.downloadUrl = downloadUrl;
  }

  public ObjectId getId() {
    return id;
  }

  public void setId(final ObjectId id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(final String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

  @PrePersist
  public void uppers() {
    upperName = name.toUpperCase();
  }

  @Override
  public String toString() {
    return name;
  }
}
