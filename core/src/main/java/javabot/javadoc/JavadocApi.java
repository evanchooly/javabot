package javabot.javadoc;

import java.util.Arrays;
import java.util.List;

import com.antwerkz.maven.SPI;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Index;
import com.google.code.morphia.annotations.Indexes;
import com.google.code.morphia.annotations.PrePersist;
import javabot.model.Persistent;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity("apis")
@SPI(Persistent.class)
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

  public static final List<String> JDK_JARS = Arrays.asList("rt.jar", "jce.jar");

  public JavadocApi() {
  }

  public JavadocApi(final String apiName, final String url) {
    name = apiName;
    baseUrl = url.endsWith("/") ? url : url + "/";
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

  @PrePersist
  public void uppers() {
    upperName = name.toUpperCase();
  }

  @Override
  public String toString() {
    return name;
  }
}
