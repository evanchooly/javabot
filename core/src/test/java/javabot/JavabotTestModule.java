package javabot;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import javabot.dao.LogsDao;
import javabot.dao.NickServDao;
import javabot.dao.TestNickServDao;
import javabot.model.Config;
import javabot.model.TestUser;
import org.aeonbits.owner.ConfigFactory;
import org.pircbotx.Channel;
import org.pircbotx.Configuration.BotFactory;
import org.pircbotx.Configuration.Builder;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.UserChannelDao;
import org.pircbotx.exception.IrcException;
import org.pircbotx.output.OutputChannel;
import org.pircbotx.output.OutputRaw;
import org.pircbotx.output.OutputUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Provider;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class JavabotTestModule extends JavabotModule {
    private Provider<TestJavabot> botProvider;
    private Provider<TestBotFactory> botFactoryProvider;
    private TestJavabot testJavabot;

    @Override
    protected void configure() {
        botProvider = binder().getProvider(TestJavabot.class);
        botFactoryProvider = binder().getProvider(TestBotFactory.class);
        bind(NickServDao.class).to(TestNickServDao.class);

        super.configure();
    }

    @Override
    public JavabotConfig javabotConfig() throws IOException {
        return validate(ConfigFactory.create(JavabotConfig.class, loadTestProperties(), System.getProperties(), System.getenv()));
    }

    private Map<Object, Object> loadTestProperties() {
        return new HashMap<>(load(load(new Properties(), "javabot.properties"), "test-javabot.properties"));
    }

    private Properties load(final Properties properties, final String name) {
        File file = new File(name);
        if (file.exists()) {
            try (FileInputStream stream = new FileInputStream(file)) {
                properties.load(stream);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return properties;
    }

    @Provides
    @Singleton
    public Javabot getJavabot() {
        if(testJavabot == null) {
            testJavabot = botProvider.get();
            testJavabot.start();
        }
        return testJavabot;
    }

    @Override
    protected PircBotX createIrcBot() {
        Config config = configDaoProvider.get().get();
        Builder<PircBotX> builder = new Builder<>()
                                        .setAutoNickChange(true)
                                        .setName(BaseTest.TEST_BOT_NICK)
                                        .setLogin(BaseTest.TEST_BOT_NICK)
                                        .addListener(getBotListener())
                                        .setServerHostname(config.getServer())
                                        .setServerPort(config.getPort())
                                        .setBotFactory(botFactory());

        return new TestPircBotX(builder);
    }

    protected BotFactory botFactory() {
        return botFactoryProvider.get();
    }

    private static class TestPircBotX extends PircBotX {
        public TestPircBotX(final Builder<PircBotX> builder) {
            super(builder.buildConfiguration());
        }

        @Override
        protected void connect() throws IOException, IrcException {
            reconnectStopped = true;
        }
    }

    @Singleton
    private static class TestBotFactory extends BotFactory {
        private static final Logger LOG = LoggerFactory.getLogger(TestBotFactory.class);

        @Inject
        private BotFactory botFactory;

        @Inject
        private Messages messages;

        @Inject
        private LogsDao logsDao;

        @Override
        public UserChannelDao createUserChannelDao(final PircBotX bot) {
            return new UserChannelDao(bot, this) {
                @Override
                public User getUser(final String nick) {
                    return createUser(bot, nick);
                }
            };
        }

        @Override
        public OutputRaw createOutputRaw(final PircBotX bot) {
            return new OutputRaw(bot, 0) {
                @Override
                public void rawLine(final String line) {
                    LOG.debug(line);
                }

                @Override
                public void rawLineNow(final String line) {
                    LOG.debug(line);
                }

                @Override
                public void rawLineNow(final String line, final boolean resetDelay) {
                    LOG.debug("line = [" + line + "], resetDelay = [" + resetDelay + "]");
                }

                @Override
                public void rawLineSplit(final String prefix,
                                         final String message,
                                         final String suffix) {
                    messages.add(message.startsWith("ACTION") ? message.substring(7) : message);
                }
            };
        }

        @Override
        public OutputChannel createOutputChannel(final PircBotX bot, final Channel channel) {
            return new OutputChannel(bot, channel) {
                @Override
                public void message(final User user, final String message) {
                    messages.add(message);
                }

                @Override
                public void message(final String message) {
                    messages.add(message);
                }
            };
        }

        @Override
        public OutputUser createOutputUser(final PircBotX bot, final User user) {
            return new OutputUser(bot, user) {
                @Override
                public void message(final String message) {
                    messages.add(message);
                }

                @Override
                public void action(final String action) {
                    super.action(action);
                }

                @Override
                public void ctcpCommand(final String command) {
                    super.ctcpCommand(command);
                }

                @Override
                public void ctcpResponse(final String message) {
                    super.ctcpResponse(message);
                }

                @Override
                public void notice(final String notice) {
                    super.notice(notice);
                }
            };
        }
        @Override
        public User createUser(PircBotX bot, String nick) {
      			return new TestUser(bot, bot.getUserChannelDao(), messages, nick, null, null);
      		}

    }
}
