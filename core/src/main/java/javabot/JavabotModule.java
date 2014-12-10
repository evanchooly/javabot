package javabot;

import com.antwerkz.sofia.Sofia;
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
import javabot.web.JavabotApplication;
import net.swisstech.bitly.BitlyClient;
import org.aeonbits.owner.Config.Key;
import org.aeonbits.owner.ConfigFactory;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.pircbotx.Configuration.Builder;
import org.pircbotx.PircBotX;
import org.pircbotx.cap.SASLCapHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JavabotModule extends AbstractModule {

    private static final Logger LOG = LoggerFactory.getLogger(JavabotModule.class);

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
            datastore = getMorphia().createDatastore(getMongoClient(), javabotConfig().databaseName());
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
                mongoClient = new MongoClient(new ServerAddress(javabotConfig().databaseHost(), javabotConfig().databasePort()),
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
        return javabotConfig().bitlyToken() != null ? new BitlyClient(javabotConfig().bitlyToken()) : null;
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
                                        .setCapEnabled(false)
                                        .addListener(getBotListener())
                                        .setServerHostname(config.getServer())
                                        .setServerPort(config.getPort())
                                        .addCapHandler(new SASLCapHandler(nick, config.getPassword()));
/*
        for (Channel channel : channelDaoProvider.get().getChannels()) {
            LOG.info("Adding {} as an autojoined channel", channel.getName());
            if (channel.getKey() == null) {
                builder.addAutoJoinChannel(channel.getName());
            } else {
                builder.addAutoJoinChannel(channel.getName(), channel.getKey());
            }
        }
*/

        return new PircBotX(builder.buildConfiguration());
    }

    @Provides
    @Singleton
    public JavabotConfig javabotConfig() throws IOException {
        return validate(ConfigFactory.create(JavabotConfig.class, new HashMap<>(), System.getProperties(), System.getenv()));
    }

    @SuppressWarnings("unchecked")
    protected JavabotConfig validate(final JavabotConfig config) {
        Class<? extends JavabotConfig> configClass = (Class<? extends JavabotConfig>) config.getClass().getInterfaces()[0];
        Method[] methods = configClass.getDeclaredMethods();
        List<String> missingKeys = new ArrayList<>();
        for (Method method : methods) {
            try {
                Key annotation = method.getDeclaredAnnotation(Key.class);
                if (annotation != null && method.getParameterCount() == 0
                    && !method.getReturnType().equals(Void.class)
                    && method.invoke(config) == null) {
                    missingKeys.add(annotation.value());
                }
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        if(!missingKeys.isEmpty()) {
            throw new RuntimeException(Sofia.configurationMissingProperties(missingKeys));
        }
        return config;
    }

    public BotListener getBotListener() {
        return botListenerProvider.get();
    }
}
