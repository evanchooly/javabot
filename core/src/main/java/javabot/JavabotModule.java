package javabot;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import javabot.dao.ChannelDao;
import javabot.dao.ConfigDao;
import javabot.dao.util.LocalDateTimeConverter;
import javabot.javadoc.JavadocClass;
import javabot.model.Channel;
import javabot.model.Config;
import javabot.model.Factoid;
import net.swisstech.bitly.BitlyClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.pircbotx.Configuration.Builder;
import org.pircbotx.PircBotX;
import org.pircbotx.cap.SASLCapHandler;

import javax.inject.Singleton;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JavabotModule extends AbstractModule {

    private MongoClient mongoClient;

    private Morphia morphia;

    private Datastore datastore;
    private Provider<BotListener> botListenerProvider;

    protected Provider<ChannelDao> channelDaoProvider;
    protected Provider<ConfigDao> configDaoProvider;

    @Override
    protected void configure() {
        configDaoProvider = binder().getProvider(ConfigDao.class);
        channelDaoProvider = binder().getProvider(ChannelDao.class);
        botListenerProvider = binder().getProvider(BotListener.class);
    }

    @Provides
    @Singleton
    public Datastore datastore() throws IOException {
        if (datastore == null) {
            datastore = getMorphia().createDatastore(getMongoClient(), getProperties().getProperty("database.name"));
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
    public MongoClient getMongoClient() throws IOException {
        if (mongoClient == null) {
            try {
                String host = getProperties().getProperty("database.host", "localhost");
                int port = Integer.parseInt(getProperties().getProperty("database.port", "27017"));
                mongoClient = new MongoClient(new ServerAddress(host, port),
                                              MongoClientOptions.builder()
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
    public BitlyClient bitlyClient() throws IOException {
        return new BitlyClient(getProperties().getProperty("javabot.bitly.token"));
    }

    @Provides
    @Singleton
    protected PircBotX createIrcBot() {
        Config config = configDaoProvider.get().get();
        String nick = config.getNick();
        Builder<PircBotX> builder = new Builder<>()
                                        .setName(nick)
                                        .setLogin(nick)
                                        .setAutoNickChange(false)
                                        .setCapEnabled(true)
                                        .addListener(getBotListener())
                                        .setServerHostname(config.getServer())
                                        .setServerPort(config.getPort())
                                        .addCapHandler(new SASLCapHandler(nick, config.getPassword()));
        for (Channel channel : channelDaoProvider.get().getChannels()) {
            if (channel.getKey() == null) {
                builder.addAutoJoinChannel(channel.getName());
            } else {
                builder.addAutoJoinChannel(channel.getName(), channel.getKey());
            }
        }

        return new PircBotX(builder.buildConfiguration());
    }

    @Provides
    @Singleton
    public Properties getProperties() throws IOException {
        return loadProperties("javabot.properties");
    }

    protected Properties loadProperties(final String fileName) throws IOException {
        try (InputStream stream = new FileInputStream(fileName)) {
            final Properties properties = new Properties();
            properties.load(stream);
            validateProperties(fileName);
            return properties;
        }
    }

    private void validateProperties(final String fileName) {
        final Properties props = new Properties();
        try {
            try (InputStream stream = new FileInputStream(fileName)) {
                props.load(stream);
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Please define a javabot.properties file to configure the bot");
            }
        } catch (IOException e) {
            throw new RuntimeException("Please define a javabot.properties file to configure the bot");
        }
        boolean valid = check(fileName, props, "javabot.server");
        valid &= check(fileName, props, "javabot.port");
        valid &= check(fileName, props, "database.name");
        valid &= check(fileName, props, "javabot.nick");
        valid &= check(fileName, props, "javabot.password");
        valid &= check(fileName, props, "javabot.admin.nick");
        valid &= check(fileName, props, "javabot.admin.hostmask");
        if (!valid) {
            throw new RuntimeException("Missing configuration parameters");
        }
        System.getProperties().putAll(props);
    }

    private boolean check(final String fileName, final Properties props, final String key) {
        if (props.get(key) == null) {
            System.out.printf("Please specify the property %s in %s\n", key, fileName);
            return false;
        }
        return true;
    }

    public BotListener getBotListener() {
        return botListenerProvider.get();
    }
}
