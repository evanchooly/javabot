package javabot;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Properties;
import javax.inject.Singleton;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.mongodb.Mongo;
import com.mongodb.MongoURI;
import com.mongodb.WriteConcern;
import javabot.dao.util.DateTimeConverter;
import javabot.javadoc.JavadocClass;
import javabot.model.Factoid;

public class JavabotModule extends AbstractModule {
  private Properties properties;

  @Override
  protected void configure() {
    try {
      Names.bindProperties(binder(), getProperties());
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  @Provides
  @Singleton
  @Inject
  public Datastore datastore(Mongo mongo) throws UnknownHostException {
    String dbName = properties.getProperty("database.name");
    Morphia morphia = new Morphia();
    morphia.mapPackage(JavadocClass.class.getPackage().getName());
    morphia.mapPackage(Factoid.class.getPackage().getName());
    morphia.getMapper().getConverters().addConverter(DateTimeConverter.class);
    Datastore datastore = morphia.createDatastore(mongo, dbName);
    datastore.setDefaultWriteConcern(WriteConcern.SAFE);
    try {
      datastore.ensureIndexes();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(-1);
    }
    return datastore;
  }

  @Singleton
  @Provides
  public Mongo getMongo() throws UnknownHostException {
    String host = properties.getProperty("database.host");
    int port = Integer.parseInt(properties.getProperty("database.port"));
    String dbName = properties.getProperty("database.name");
    return new Mongo(new MongoURI(String.format("mongodb://%s:%d/%s?autoConnectRetry=true", host, port, dbName)));
  }

  private Properties getProperties() throws IOException {
    try (InputStream stream = getClass().getClassLoader().getResourceAsStream("javabot.properties")) {
      properties = new Properties();
      properties.load(stream);
      return properties;
    }
  }
}
