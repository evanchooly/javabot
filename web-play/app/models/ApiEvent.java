package models;

import java.io.File;
import com.google.code.morphia.annotations.Entity;

@Entity
public class ApiEvent extends AdminEvent {
  public String name;
  public String packages;
  public String baseUrl;
  public String file;

  public ApiEvent(boolean newApi, String requestedBy, String name, String packages, String baseUrl, File file) {
    super(newApi ? EventType.ADD : EventType.UPDATE, requestedBy);
    this.name = name;
    this.packages = packages;
    this.baseUrl = baseUrl;
    this.file = file.getAbsolutePath();
  }

  public ApiEvent(Long id, String requestedBy) {
    super(EventType.DELETE, requestedBy);
    Api api = Api.findById(id);
    name = api.name;
  }
}
