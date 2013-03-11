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
    @Index("upperName")
})
public class Api implements Persistent {
  private static final Logger log = LoggerFactory.getLogger(Api.class);

  @Id
  private ObjectId id;

  private String name;

  private String upperName;

  private String baseUrl;

  private String packages;

  public static final List<String> JDK_JARS = Arrays.asList("rt.jar", "jce.jar");

  public Api() {
  }

  public Api(final String apiName, final String url, final String pkgs) {
    name = apiName;
    baseUrl = url.endsWith("/") ? url : url + "/";
    packages = pkgs;
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

  public String getPackages() {
    return packages;
  }

  public void setPackages(final String packages) {
    this.packages = packages;
  }

  @PrePersist
  public void uppers() {
    upperName = name.toUpperCase();
  }
}
