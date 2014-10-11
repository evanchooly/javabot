package javabot.model;

import com.google.inject.Provider;
import javabot.dao.AdminDao;
import javabot.dao.ApiDao;
import javabot.javadoc.JavadocApi;
import javabot.javadoc.JavadocParser;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Transient;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

@Entity("events")
public class ApiEvent extends AdminEvent {
    private ObjectId apiId;

    public String name;

    public String baseUrl;

    public String downloadUrl;

    @Inject
    @Transient
    private Provider<PircBotX> ircBot;

    @Inject
    @Transient
    private JavadocParser parser;

    @Inject
    @Transient
    private ApiDao apiDao;

    @Inject
    @Transient
    private AdminDao adminDao;

    public ApiEvent() {
    }

    public ApiEvent(String requestedBy, String name, String baseUrl, String downloadUrl) {
        super(EventType.ADD, requestedBy);
        setRequestedBy(requestedBy);
        this.name = name;
        this.baseUrl = baseUrl;
        if (name.equals("JDK")) {
            try {
                this.downloadUrl = new File(System.getProperty("java.home"), "lib/rt.jar").toURI().toURL().toString();
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        } else {
            this.downloadUrl = downloadUrl;
        }
    }

    public ApiEvent(EventType type, String requestedBy, ObjectId apiId) {
        super(type, requestedBy);
        this.apiId = apiId;
    }

    public ApiEvent(EventType type, String requestedBy, String name) {
        super(type, requestedBy);
        this.name = name;
    }

    public ObjectId getApiId() {
        return apiId;
    }

    public void setApiId(final ObjectId apiId) {
        this.apiId = apiId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public void update() {
        delete();
        add();
    }

    public void delete() {
        JavadocApi api = apiDao.find(apiId);
        if (api == null) {
            api = apiDao.find(name);
        }
        if (api != null) {
            apiDao.delete(api);
        }
    }

    public void add() {
        JavadocApi api = new JavadocApi(name, baseUrl, downloadUrl);
        apiDao.save(api);
        process(api);
    }

    public void reload() {
        JavadocApi api = apiDao.find(apiId);
        apiDao.delete(apiId);
        api.setId(null);
        apiDao.save(api);
        process(api);
    }

    private void process(final JavadocApi api) {
        final Admin admin = adminDao.getAdmin(ircBot.get().getUserChannelDao().getUser(getRequestedBy()));
        final User user = ircBot.get().getUserChannelDao().getUser(admin.getIrcName());
        try {
            File file = downloadZip(api.getName() + ".jar", api.getDownloadUrl());
            parser.parse(api, file.getAbsolutePath(), new StringWriter() {
                @Override
                public void write(final String line) {
                    getBot().postMessage(null, user, line, false);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private File downloadZip(final String fileName, final String zipURL) throws IOException {
        File file = new File("/tmp/" + fileName);
        int read;
        if (!file.exists()) {
            try (InputStream inputStream = new URL(zipURL).openStream();
                 OutputStream fos = new FileOutputStream(file)) {
                byte[] bytes = new byte[8192];
                while ((read = inputStream.read(bytes)) != -1) {
                    fos.write(bytes, 0, read);
                }
            }
        }
        return file;
    }

    @Override
    public String toString() {
        return String.format("ApiEvent{name='%s', state=%s, completed=%s, type=%s}", name, getState(), getCompleted(), getType());
    }
}