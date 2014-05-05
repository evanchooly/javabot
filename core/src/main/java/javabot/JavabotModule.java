package javabot;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.WriteConcern;
import javabot.dao.util.LocalDateTimeConverter;
import javabot.javadoc.JavadocClass;
import javabot.model.Factoid;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class JavabotModule extends AbstractModule {
  private Properties properties;

  private MongoClient mongoClient;

  private Morphia morphia;

  private Datastore datastore;

  @Override
  protected void configure() {
  }

  @Provides
  @Singleton
  public Datastore datastore() throws IOException {
    if (datastore == null) {
      getProperties();
      getMongo();
      getMorphia();
      datastore = morphia.createDatastore(mongoClient, properties.getProperty("database.name"));
      datastore.setDefaultWriteConcern(WriteConcern.SAFE);
      try {
        datastore.ensureIndexes();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return datastore;
  }

  @Provides
  @Singleton
  public Morphia getMorphia() {
    if (morphia == null) {
      morphia = new Morphia();
      morphia.mapPackage(JavadocClass.class.getPackage().getName());
      morphia.mapPackage(Factoid.class.getPackage().getName());
      morphia.getMapper().getConverters().addConverter(LocalDateTimeConverter.class);
    }
    return morphia;
  }

  @Provides
  @Singleton
  public Mongo getMongo() throws IOException {
    if (mongoClient == null) {
      getProperties();
      try {
        String host = properties.getProperty("database.host", "localhost");
        int port = Integer.parseInt(properties.getProperty("database.port", "27107"));
        mongoClient = new MongoClient(host, MongoClientOptions.builder()
            .connectTimeout(2000)
            .build());
      } catch (RuntimeException e) {
        e.printStackTrace();
        throw new RuntimeException(e.getMessage(), e);
      }
    }
    return mongoClient;
  }

  @Provides
  @Singleton
  public Properties getProperties() throws IOException {
    try (InputStream stream = new FileInputStream(Javabot.getPropertiesFile())) {
      properties = new Properties();
      properties.load(stream);
      return properties;
    }
  }
}
