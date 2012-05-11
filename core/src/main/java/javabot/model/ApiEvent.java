package javabot.model;

import java.io.StringWriter;
import javax.persistence.Entity;

import javabot.IrcEvent;
import javabot.IrcUser;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.AdminDao;
import javabot.dao.ApiDao;
import javabot.dao.EventDao;
import javabot.javadoc.Api;
import javabot.javadoc.JavadocParser;

@Entity
public class ApiEvent extends AdminEvent {
    public String name;
    public String packages;
    public String baseUrl;
    public String file;

    @Override
    public void handle(Javabot bot) {
        switch (type) {
            case ADD:
                add(bot);
                break;
            case DELETE:
                delete(bot);
                break;
            case UPDATE:
                update(bot);
                break;
        }

        setProcessed(true);
        bot.getApplicationContext().getBean(EventDao.class).save(this);
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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    private void update(Javabot bot) {
        delete(bot);
        add(bot);
    }

    private void delete(Javabot bot) {
        ApiDao dao = bot.getApplicationContext().getBean(ApiDao.class);
        Api api = dao.find(name);
        dao.delete(api);
    }

    private void add(final Javabot bot) {
        final JavadocParser parser = new JavadocParser();
        bot.getApplicationContext().getAutowireCapableBeanFactory().autowireBean(parser);
        ApiDao dao = bot.getApplicationContext().getBean(ApiDao.class);
        Api api = new Api(name, baseUrl, packages);
        dao.save(api);
        final Admin admin = bot.getApplicationContext().getBean(AdminDao.class).getAdmin(getRequestedBy());
        final IrcUser user = new IrcUser(admin.getIrcName(), admin.getIrcName(), admin.getHostName());
        final IrcEvent event = new IrcEvent(admin.getIrcName(), user, null);
        parser.parse(api, file, new StringWriter() {
            @Override
            public void write(final String line) {
                event.setMessage(line);
                bot.postMessage(new Message(user, event, line));
            }
        });
    }
}