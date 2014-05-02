package javabot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

import ca.grimoire.maven.ArtifactDescription;
import ca.grimoire.maven.NoArtifactException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javabot.commands.AdminCommand;
import javabot.dao.AdminDao;
import javabot.dao.ChannelDao;
import javabot.dao.ConfigDao;
import javabot.dao.EventDao;
import javabot.dao.LogsDao;
import javabot.dao.ShunDao;
import javabot.database.UpgradeScript;
import javabot.model.AdminEvent;
import javabot.model.AdminEvent.State;
import javabot.model.Config;
import javabot.model.IrcUser;
import javabot.model.Logs;
import javabot.operations.BotOperation;
import javabot.operations.OperationComparator;
import javabot.operations.StandardOperation;
import javabot.operations.throttle.Throttler;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Javabot {
  private static String propertiesName = "javabot.properties";

  @Inject
  ChannelDao channelDao;

  @Inject
  private ConfigDao configDao;

  @Inject
  LogsDao logsDao;

  @Inject
  private ShunDao shunDao;

  @Inject
  private EventDao eventDao;

  @Inject
  AdminDao adminDao;

  @Inject
  private Throttler throttler;

  @Inject
  protected Injector injector;

  public static final Logger log = LoggerFactory.getLogger(Javabot.class);

  public static final int THROTTLE_TIME = 5 * 1000;

  Config config;

  private Map<String, BotOperation> allOperations;

  private String host;

  String nick;

  private String password;

  private String[] startStrings;

  ExecutorService executors;

  private final ScheduledExecutorService eventHandler = Executors.newScheduledThreadPool(2,
      new JavabotThreadFactory(true, "javabot-event-handler"));

  private final List<BotOperation> standard = new ArrayList<>();

  private final List<String> ignores = new ArrayList<>();

  private final Set<BotOperation> activeOperations = new TreeSet<>(new OperationComparator());

  private int port;

  private BlockingQueue<Runnable> queue;

  protected MyPircBot pircBot;

  private boolean reconnecting = false;

  public void start() {
    setUpThreads();
    config = configDao.get();
    loadOperations(config);
    loadConfig();
    applyUpgradeScripts();
    createIrcBot();
    startStrings = new String[]{pircBot.getNick(), "~"};
    connect();
  }

  private void setUpThreads() {
    queue = new ArrayBlockingQueue<>(50);
    executors = new ThreadPoolExecutor(5, 10, 10L, TimeUnit.SECONDS, queue,
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
        event.handle(this);
        event.setState(State.COMPLETED);
      } catch (Exception e) {
        event.setState(State.FAILED);
        log.error(e.getMessage(), e);
      }
      event.setCompleted(new DateTime());
      eventDao.save(event);
    }
  }

  private void joinChannels() {
    if (pircBot.isConnected() && !reconnecting) {
      List<String> joined = Arrays.asList(pircBot.getChannels());
      channelDao.getChannels().stream().filter(channel -> !joined.contains(channel.getName())).forEach(channel -> {
        channel.join(this);
        sleep(1000);
      });
    }
  }

  protected void createIrcBot() {
    pircBot = new MyPircBot(this, injector);
  }

  public void shutdown() {
    if (!executors.isShutdown()) {
      executors.shutdown();
      try {
        executors.awaitTermination(30, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  public void connect() {
    while (!pircBot.isConnected()) {
      try {
        connectAndId();
      } catch (IrcException exception) {
        queue.clear();
        log.error(exception.getMessage(), exception);
      } catch (IOException e) {
        pircBot.disconnect();
        log.error(e.getMessage(), e);
        throw new RuntimeException(e.getMessage(), e);
      }
    }
  }

  private void connectAndId() throws IOException, IrcException {
    if (reconnecting) {
      String oldNick = nick;
      nick = UUID.randomUUID().toString();
      pircBot.connect(host, port);
      sleep(3000);
      pircBot.sendMessage("NickServ", String.format("GHOST %s %s", oldNick, getNickPassword()));
      sleep(3000);
      pircBot.sendMessage("NickServ", String.format("RELEASE %s %s", oldNick, getNickPassword()));
      sleep(3000);
      reconnecting = false;
      nick = oldNick;
      pircBot.changeNick(nick);
    } else {
      pircBot.connect(host, port);
      pircBot.sendRawLine("PRIVMSG NickServ :identify " + getNickPassword());
      sleep(3000);
    }
  }

  public static void validateProperties() {
    final Properties props = new Properties();
    try {
      try (InputStream stream = new FileInputStream(getPropertiesFile())) {
        props.load(stream);
      } catch (FileNotFoundException e) {
        throw new RuntimeException("Please define a javabot.properties file to configure the bot");
      }
    } catch (IOException e) {
      throw new RuntimeException("Please define a javabot.properties file to configure the bot");
    }
    boolean valid = check(props, "javabot.server");
    valid &= check(props, "javabot.port");
    valid &= check(props, "database.name");
    valid &= check(props, "javabot.nick");
    valid &= check(props, "javabot.password");
    valid &= check(props, "javabot.admin.nick");
    valid &= check(props, "javabot.admin.hostmask");
    if (!valid) {
      throw new RuntimeException("Missing configuration parameters");
    }
    System.getProperties().putAll(props);
  }

  public static String getPropertiesFile() {
    return propertiesName;
  }

  public static void setPropertiesFile(final String name) {
    propertiesName = name;
  }

  static boolean check(final Properties props, final String key) {
    if (props.get(key) == null) {
      System.out.printf("Please specify the property %s in javabot.properties\n", key);
      return false;
    }
    return true;
  }

  protected final void applyUpgradeScripts() {
    for (final UpgradeScript script : UpgradeScript.loadScripts()) {
      injector.injectMembers(script);
      script.execute();
    }
  }

  public final String loadVersion() {
    ArtifactDescription description;
    try {
      description = ArtifactDescription.locate("javabot", "core");
      return description.getVersion();
    } catch (NoArtifactException nae) {
      try {
        final File file = new File("target/maven-archiver/pom.properties");
        if (file.exists()) {
          description = ArtifactDescription.locate("javabot", "core", resource -> {
            try {
              return new FileInputStream(file);
            } catch (FileNotFoundException e) {
              throw new RuntimeException(e.getMessage(), e);
            }
          });
          return description.getVersion();
        } else {
          return "UNKNOWN";
        }
      } catch (NoArtifactException e) {
        return "UNKNOWN";
      }
    }
  }

  public void loadConfig() {
    try {
      host = config.getServer();
      port = config.getPort();
      nick = config.getNick();
      setNickPassword(config.getPassword());
      log.debug("Running with configuration: " + config);
    } catch (Exception e) {
      log.debug(e.getMessage(), e);
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  @SuppressWarnings({"unchecked"})
  protected final void loadOperations(final Config config) {
    allOperations = new TreeMap<>();
    for (final BotOperation op : BotOperation.list()) {
      injector.injectMembers(op);
      op.setBot(this);
      allOperations.put(op.getName(), op);
    }
    try {
      config.getOperations().forEach(this::enableOperation);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException(e.getMessage(), e);
    }
    addDefaultOperations(ServiceLoader.load(AdminCommand.class));
    addDefaultOperations(ServiceLoader.load(StandardOperation.class));
    Collections.sort(standard, new BotOperationComparator());
  }

  private void addDefaultOperations(final ServiceLoader<? extends BotOperation> loader) {
    for (final BotOperation operation : loader) {
      injector.injectMembers(operation);
      operation.setBot(this);
      standard.add(operation);
    }
  }

  public boolean disableOperation(final String name) {
    boolean disabled = false;
    if (allOperations.get(name) != null) {
      activeOperations.remove(allOperations.get(name));
      disabled = true;
    }
    return disabled;
  }

  public boolean enableOperation(final String name) {
    boolean enabled = false;
    if (allOperations.get(name) != null) {
      activeOperations.add(allOperations.get(name));
      enabled = true;
    }
    return enabled;
  }

  public List<BotOperation> getAllOperations() {
    final List<BotOperation> ops = new ArrayList<>(activeOperations);
    ops.addAll(standard);
    return ops;
  }

  public String[] getStartStrings() {
    return startStrings;
  }

  public void processMessage(final IrcEvent event) {
    try {
      final IrcUser sender = event.getSender();
      final String message = event.getMessage();
      final String channel = event.getChannel();
      logsDao.logMessage(Logs.Type.MESSAGE, sender.getNick(), channel, message);
      if (isValidSender(sender.getNick())) {
        final List<Message> responses = new ArrayList<>();
        for (final String startString : startStrings) {
          if (message.startsWith(startString)) {
            if(throttler.isThrottled(sender)) {
              responses.add(new Message(sender, new IrcEvent(channel, sender, message), "Slow your roll, son."));
            } else {
              String content = extractContentFromMessage(message, startString);
              if (!content.isEmpty()) {
                responses.addAll(getResponses(channel, sender, content));
              }
            }
          }
        }
        if (responses.isEmpty()) {
          responses.addAll(getChannelResponses(channel, sender, message));
        }
        for (final Message response : responses) {
          response.send(this);
        }
      } else {
        if (log.isInfoEnabled()) {
          log.info("ignoring " + sender);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  String extractContentFromMessage(String message, String startString) {
    String content = message.substring(startString.length()).trim();
    while (!content.isEmpty() && (content.charAt(0) == ':' || content.charAt(0) == ',')) {
      content = content.substring(1).trim();
    }
    return content;
  }

  public void addIgnore(final String sender) {
    ignores.add(sender);
  }

  public void postMessage(final Message message) {
    logMessage(message);
    pircBot.sendMessage(message.getDestination(), message.getMessage());
  }

  public void postAction(final Message message) {
    logMessage(message);
    pircBot.sendAction(message.getDestination(), message.getMessage());
  }

  protected final void logMessage(final Message message) {
    final IrcEvent event = message.getEvent();
    final String sender = pircBot.getNick();
    final String channel = event.getChannel();
    if (!channel.equals(sender)) {
      logsDao.logMessage(Logs.Type.MESSAGE, sender, message.getDestination(), message.getMessage());
    }
  }

  public List<Message> getResponses(final String channel, final IrcUser sender, final String message) {
    final Iterator<BotOperation> iterator = getAllOperations().iterator();
    final List<Message> responses = new ArrayList<>();
    final IrcEvent event = new IrcEvent(channel, sender, message);
    while (responses.isEmpty() && iterator.hasNext()) {
      List<Message> list = iterator.next().handleMessage(event);
      if(!list.isEmpty()) {
        responses.addAll(list);
      }
    }
    return responses;
  }

  public String getNickPassword() {
    return password;
  }

  public void setNickPassword(final String password) {
    this.password = password;
  }

  public List<Message> getChannelResponses(final String channel, final IrcUser sender, final String message) {
    final Iterator<BotOperation> iterator = getAllOperations().iterator();
    final List<Message> responses = new ArrayList<>();
    while (responses.isEmpty() && iterator.hasNext()) {
      List<Message> list = iterator.next().handleChannelMessage(new IrcEvent(channel, sender, message));
      if(!list.isEmpty()) {
        if(throttler.isThrottled(sender)) {
          responses.add(new Message(sender, new IrcEvent(channel, sender, message), "Slow your roll, son."));
        } else {
          responses.addAll(list);
        }
      }
    }
    return responses;
  }

  public boolean isOnSameChannelAs(final String user) {
    for (final String channel : pircBot.getChannels()) {
        if (userIsOnChannel(user, channel)) {
        return true;
      }
    }
    return false;
  }

  IrcUser getUser(final String sender, final String login, final String hostname) {
    return new IrcUser(sender, login, hostname);
  }

  public boolean userIsOnChannel(final String nick, final String channel) {
    for (final User user : pircBot.getUsers(channel)) {
      if (user.getNick().equalsIgnoreCase(nick)) {
        return true;
      }
    }
    return false;
  }

  protected boolean isValidSender(final String sender) {
    return !ignores.contains(sender) && !shunDao.isShunned(sender);
  }

  @SuppressWarnings({"EmptyCatchBlock"})
  protected void sleep(final int milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException exception) {
    }
  }

  public PircBot getPircBot() {
    return pircBot;
  }

  public void join(String name, String key) {
    if (name.startsWith("#")) {
      log.debug("Joining " + name);
      if (key == null) {
        pircBot.joinChannel(name);
      } else {
        pircBot.joinChannel(name, key);
      }
    }
  }

  public void leave(String name, String reason) {
    pircBot.partChannel(name, reason);
  }

  public static void main(final String[] args) {
    Injector injector = Guice.createInjector(new JavabotModule());
    if (log.isInfoEnabled()) {
      log.info("Starting Javabot");
    }
    validateProperties();
    Javabot bot = injector.getInstance(Javabot.class);
    bot.start();
  }

  public void setReconnecting(final boolean reconnecting) {
    this.reconnecting = reconnecting;
  }
}
