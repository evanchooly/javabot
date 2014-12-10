package javabot;

import com.antwerkz.sofia.Sofia;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.jayway.awaitility.Awaitility;
import javabot.commands.AdminCommand;
import javabot.dao.ChannelDao;
import javabot.dao.ConfigDao;
import javabot.dao.EventDao;
import javabot.dao.LogsDao;
import javabot.dao.ShunDao;
import javabot.database.UpgradeScript;
import javabot.model.AdminEvent;
import javabot.model.AdminEvent.State;
import javabot.model.Channel;
import javabot.model.Config;
import javabot.model.Logs;
import javabot.model.Logs.Type;
import javabot.operations.BotOperation;
import javabot.operations.OperationComparator;
import javabot.operations.StandardOperation;
import javabot.operations.throttle.NickServViolationException;
import javabot.operations.throttle.Throttler;
import javabot.web.JavabotApplication;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Singleton
public class Javabot {
    public static final Logger LOG = LoggerFactory.getLogger(Javabot.class);

    @Inject
    private ConfigDao configDao;

    @Inject
    private ChannelDao channelDao;

    @Inject
    private LogsDao logsDao;

    @Inject
    private ShunDao shunDao;

    @Inject
    private EventDao eventDao;

    @Inject
    private Throttler throttler;

    @Inject
    protected Injector injector;

    @Inject
    private Provider<PircBotX> ircBot;

    @Inject
    private JavabotConfig javabotConfig;

    private Map<String, BotOperation> allOperations;

    private String[] startStrings;

    ExecutorService executors;

    private final ScheduledExecutorService eventHandler =
        Executors.newScheduledThreadPool(2, new JavabotThreadFactory(true, "javabot-event-handler"));

    private final List<String> ignores = new ArrayList<>();

    private final Set<BotOperation> activeOperations = new TreeSet<>(new OperationComparator());

    private volatile boolean running = true;

    public void start() {
        setUpThreads();
        getAllOperations();
        applyUpgradeScripts();
        connect();
        startWebApp();
    }

    private void setUpThreads() {
        int core = 5;
        int max = 10;
        executors = new ThreadPoolExecutor(core, max, 5L, TimeUnit.MINUTES, new ArrayBlockingQueue<>(core * max),
                                           new JavabotThreadFactory(true, "javabot-handler-thread-"));
        final Thread hook = new Thread(this::shutdown);
        hook.setDaemon(false);
        Runtime.getRuntime().addShutdownHook(hook);
        eventHandler.scheduleAtFixedRate(this::processAdminEvents, 5, 5, TimeUnit.SECONDS);
        eventHandler.scheduleAtFixedRate(this::joinChannels, 5, 5, TimeUnit.SECONDS);
    }

    protected void processAdminEvents() {
        AdminEvent event = eventDao.findUnprocessed();
        if (event != null) {
            try {
                event.setState(State.PROCESSING);
                eventDao.save(event);
                injector.injectMembers(event);
                event.handle();
                event.setState(State.COMPLETED);
            } catch (Exception e) {
                event.setState(State.FAILED);
                LOG.error(e.getMessage(), e);
            }
            event.setCompleted(LocalDateTime.now());
            eventDao.save(event);
        }
    }

    private void joinChannels() {
        PircBotX ircBot = this.ircBot.get();
        boolean connected = ircBot.isConnected();
        if (connected) {
            Set<String> joined = ircBot.getUserChannelDao().getAllChannels()
                                       .stream()
                                       .map(org.pircbotx.Channel::getName)
                                       .collect(Collectors.toSet());
            List<Channel> channels = channelDao.getChannels();
            if (joined.size() != channels.size()) {
                channels.stream().filter(channel -> !joined.contains(channel.getName())).forEach(channel -> {
                    channel.join(ircBot);
                    sleep(500);
                });
            }
        }
    }

    @SuppressWarnings({"EmptyCatchBlock"})
    protected void sleep(final int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException exception) {
        }
    }

    public void shutdown() {
        if (!executors.isShutdown()) {
            executors.shutdown();
            try {
                executors.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                LOG.error(e.getMessage(), e);
            }
            running = false;
        }
    }

    private boolean isRunning() {
        return running;
    }

    public void connect() {
        try {
            ircBot.get().startBot();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void startWebApp() {
        if (javabotConfig.startWebApp()) {
            try {
                injector.getInstance(JavabotApplication.class).run(new String[]{"server", "javabot.yml"});
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    protected final void applyUpgradeScripts() {
        for (final UpgradeScript script : UpgradeScript.loadScripts()) {
            injector.injectMembers(script);
            script.execute();
        }
    }

    @SuppressWarnings({"unchecked"})
    public final Map<String, BotOperation> getAllOperations() {
        if (allOperations == null) {
            final Config config = configDao.get();
            allOperations = new TreeMap<>();
            for (final BotOperation op : configDao.list()) {
                allOperations.put(op.getName(), op);
            }
            try {
                config.getOperations().forEach(this::enableOperation);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return allOperations;
    }

    public boolean disableOperation(final String name) {
        boolean disabled = false;
        BotOperation operation = getAllOperations().get(name);
        if (operation != null && !(operation instanceof AdminCommand) && !(operation instanceof StandardOperation)) {
            getActiveOperations().remove(operation);
            Config config = configDao.get();
            config.getOperations().remove(name);
            configDao.save(config);
            disabled = true;
        }
        return disabled;
    }

    public boolean enableOperation(final String name) {
        boolean enabled = false;
        if (getAllOperations().get(name) != null) {
            Config config = configDao.get();
            config.getOperations().add(name);
            getActiveOperations().add(getAllOperations().get(name));
            configDao.save(config);
            enabled = true;
        }
        return enabled;
    }

    public Set<BotOperation> getActiveOperations() {
        return activeOperations;
    }

    public String[] getStartStrings() {
        if (startStrings == null) {
            startStrings = new String[]{getNick(), "~"};
        }
        return startStrings;
    }

    public void processMessage(final Message message) {
        final User sender = message.getUser();
        final org.pircbotx.Channel channel = message.getChannel();
        logsDao.logMessage(Logs.Type.MESSAGE, channel, sender, message.getValue());
        boolean handled = false;
        if (isValidSender(sender.getNick())) {
            for (final String startString : getStartStrings()) {
                if (message.getValue().startsWith(startString)) {
                    try {
                        if (throttler.isThrottled(message.getUser())) {
                            postMessage(null, message.getUser(), Sofia.throttledUser(), false);
                            handled = true;
                        } else {
                            String content = message.getValue().substring(startString.length()).trim();
                            while (!content.isEmpty() && (content.charAt(0) == ':' || content.charAt(0) == ',')) {
                                content = content.substring(1).trim();
                            }
                            if (!content.isEmpty()) {
                                handled = getResponses(new Message(message, content), message.getUser());
                            }
                        }
                    } catch (NickServViolationException e) {
                        postMessage(null, message.getUser(), e.getMessage(), false);
                    }
                }
            }
            if (!handled) {
                getChannelResponses(message);
            }
        } else {
            if (LOG.isInfoEnabled()) {
                LOG.info("ignoring " + sender);
            }
        }
    }

    public void addIgnore(final String sender) {
        ignores.add(sender);
    }

    public void postMessage(final org.pircbotx.Channel channel, final User user, String message, final boolean tell) {
        logMessage(channel, user, message);
        if (channel != null) {
            String value = tell && !message.contains(user.getNick()) ? format("%s, %s", user.getNick(), message) : message;
            channel.send().message(value);
        } else if (user != null) {
            user.send().message(message);
        }
    }

    public void postAction(final org.pircbotx.Channel channel, String message) {
        final User bot = ircBot.get().getUserBot();
        if (!channel.getName().equals(bot.getNick())) {
            logsDao.logMessage(Type.ACTION, channel, bot, message);
        }
        channel.send().action(message);
    }

    protected final void logMessage(final org.pircbotx.Channel channel, final User user, String message) {
        final String sender = ircBot.get().getNick();
        if (channel != null && !channel.getName().equals(sender)) {
            logsDao.logMessage(Logs.Type.MESSAGE, channel, user, message);
        }
    }

    public boolean getResponses(final Message message, final User requester) {
        final Iterator<BotOperation> iterator = getActiveOperations().iterator();
        boolean handled = false;
        while (iterator.hasNext() && !handled) {
            handled = iterator.next().handleMessage(message);
        }

        if (!handled) {
            postMessage(message.getChannel(), requester, Sofia.unhandledMessage(requester.getNick()), false);
        }
        return true;
    }

    public boolean getChannelResponses(final Message event) {
        final Iterator<BotOperation> iterator = getActiveOperations().iterator();
        boolean handled = false;
        //        if (!throttler.isThrottled(event.getUser())) {
        while (iterator.hasNext() && !handled) {
            handled = iterator.next().handleChannelMessage(event);
        }
        //        } else {
        //            try {
        //                postMessage(null, event.getUser(), Sofia.throttledUser(), false);
        //            } catch (NickServViolationException e) {
        //                handled = true;
        //                postMessage(null, event.getUser(), e.getMessage(), false);
        //            }
        //        }
        return handled;
    }

    public boolean isOnCommonChannel(final User user) {
        return !ircBot.get().getUserChannelDao().getChannels(user).isEmpty();
    }

    protected boolean isValidSender(final String sender) {
        return !ignores.contains(sender) && !shunDao.isShunned(sender);
    }

    public String getNick() {
        return configDao.get().getNick();
    }


    public static void main(final String[] args) {
        try {
            Injector injector = Guice.createInjector(new JavabotModule());
            if (LOG.isInfoEnabled()) {
                LOG.info("Starting Javabot");
            }
            Javabot bot = injector.getInstance(Javabot.class);
            bot.start();
            Awaitility.await()
                      .forever()
                      .until(() -> !bot.isRunning());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
