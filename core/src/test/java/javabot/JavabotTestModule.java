package javabot;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import javabot.dao.NickServDao;
import javabot.dao.TestNickServDao;
import javabot.model.Config;
import org.pircbotx.Channel;
import org.pircbotx.Configuration.BotFactory;
import org.pircbotx.Configuration.Builder;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.UserChannelDao;
import org.pircbotx.exception.IrcException;
import org.pircbotx.output.OutputChannel;
import org.pircbotx.output.OutputRaw;

import javax.inject.Provider;
import java.io.IOException;
import java.util.Properties;

public class JavabotTestModule extends JavabotModule {
    private Provider<TestJavabot> botProvider;
    private final Messages messages = new Messages();
    private Provider<TestBotFactory> botFactoryProvider;

    @Override
    protected void configure() {
        botProvider = binder().getProvider(TestJavabot.class);
        botFactoryProvider = binder().getProvider(TestBotFactory.class);
        bind(NickServDao.class).to(TestNickServDao.class);

        super.configure();
    }

    @Override
    public Properties getProperties() throws IOException {
        return loadProperties("test-javabot.properties");
    }

    @Provides
    @Singleton
    public Javabot getJavabot() {
        TestJavabot testJavabot = botProvider.get();
        testJavabot.start();
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

    @Provides
    public Messages messages() {
        return messages;
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
        @Inject
        private BotFactory botFactory;

        @Inject
        private Messages messages;

        @Override
        public UserChannelDao createUserChannelDao(final PircBotX bot) {
            return new UserChannelDao(bot, botFactory) {
                @Override
                public User getUser(final String nick) {
                    return botFactory.createUser(bot, nick);
                }
            };
        }

        @Override
        public OutputRaw createOutputRaw(final PircBotX bot) {
            return new OutputRaw(bot, 0) {
                @Override
                public void rawLine(final String line) {
                    new Exception("line = [" + line + "]").printStackTrace();
                }

                @Override
                public void rawLineNow(final String line) {
                    new Exception("line = [" + line + "]").printStackTrace();
                }

                @Override
                public void rawLineNow(final String line, final boolean resetDelay) {
                    new Exception("line = [" + line + "], resetDelay = [" + resetDelay + "]").printStackTrace();
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
    }
}
