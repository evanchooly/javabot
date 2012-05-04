package javabot.model;

import javabot.IrcEvent;
import javabot.IrcUser;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.AdminDao;
import javabot.dao.ApiDao;
import javabot.dao.EventDao;
import javabot.javadoc.Api;
import javabot.javadoc.JavadocParser;

import javax.persistence.Entity;
import java.io.StringWriter;

@Entity
public class ApiEvent extends AdminEvent {
    public String name;

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

    private void update(Javabot bot) {
    }

    private void delete(Javabot bot) {
    }

    private void add(final Javabot bot) {
        final JavadocParser parser = new JavadocParser();
        System.out.println("parser = " + parser);
        bot.getApplicationContext().getAutowireCapableBeanFactory().autowireBean(parser);
        System.out.println("parser = " + parser);
        ApiDao dao = bot.getApplicationContext().getBean(ApiDao.class);
        Api api = dao.find(name);
        final Admin admin = bot.getApplicationContext().getBean(AdminDao.class).getAdmin(getRequestedBy());
        final IrcUser user = new IrcUser(admin.getIrcName(), admin.getIrcName(), admin.getHostName());
        final IrcEvent event = new IrcEvent(admin.getIrcName(), user, null);
        parser.parse(api, new StringWriter() {
            @Override
            public void write(final String line) {
                event.setMessage(line);
                bot.postMessage(new Message(user, event, line));
            }
        });
    }
}