package javabot;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Properties;
import javax.inject.Singleton;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.WriteConcern;
import javabot.dao.util.DateTimeConverter;
import javabot.javadoc.JavadocClass;
import javabot.model.Factoid;

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
  private Morphia getMorphia() {
    if (morphia == null) {
      morphia = new Morphia();
      morphia.mapPackage(JavadocClass.class.getPackage().getName());
      morphia.mapPackage(Factoid.class.getPackage().getName());
      morphia.getMapper().getConverters().addConverter(DateTimeConverter.class);
    }
    return morphia;
  }

  private Mongo getMongo() throws UnknownHostException {
    if (mongoClient == null || !mongoClient.getConnector().isOpen()) {
      String host = properties.getProperty("database.host");
      int port = Integer.parseInt(properties.getProperty("database.port"));
      mongoClient = new MongoClient(host, MongoClientOptions.builder()
          .autoConnectRetry(true)
          .connectTimeout(2000)
          .build());
    }
    return mongoClient;
  }

  @Provides
  @Singleton
  public Properties getProperties() throws IOException {
    try (InputStream stream = getClass().getClassLoader().getResourceAsStream("javabot.properties")) {
      properties = new Properties();
      properties.load(stream);
      return properties;
    }
  }
}
