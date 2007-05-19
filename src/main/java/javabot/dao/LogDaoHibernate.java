package javabot.dao;

import javabot.dao.model.Factoid;
import javabot.dao.model.Logs;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;

// User: joed
// Date: Apr 11, 2007
// Time: 2:41:22 PM


public class LogDaoHibernate extends AbstractDaoHibernate<Factoid> implements LogDao {

    private HtmlRoutines html = new HtmlRoutines();

    private static final Log log = LogFactory.getLog(LogDaoHibernate.class);

    public LogDaoHibernate() {
        super(Logs.class);
    }

    public void logMessage(String nick, String channel, String message) {

        Logs logMessage = new Logs();

        logMessage.setNick(nick);
        logMessage.setChannel(channel);
        logMessage.setMessage(message);
        logMessage.setDate(new Date());

        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        session.save(logMessage);
        transaction.commit();

    }


    public Logs getMessage(String nick, String channel) {
        String query = "from Logs s where s.nick = :nick" + " AND s.channel = :channel";

        Logs m_user = (Logs) getSession().createQuery(query)
                .setString("nick", nick)
                .setString("channel", channel)
                .setMaxResults(1)
                .uniqueResult();

        if (m_user == null) {

            Logs notFound = new Logs();
            return notFound;
        }

        return m_user;

    }

    public List<String> loggedChannels() {
        String query = "select distinct s.channel from Logs s where s.channel like '#%'";

        List<String> m_channels = (List) getSession().createQuery(query).list();

        if (m_channels== null) {
          return new ArrayList<String>();
        }

        return m_channels;
  }

}