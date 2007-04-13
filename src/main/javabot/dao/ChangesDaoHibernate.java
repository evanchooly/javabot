package javabot.dao;

import javabot.dao.model.changes;
import javabot.dao.model.factoids;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;

// User: joed
// Date: Apr 11, 2007
// Time: 2:41:22 PM


public class ChangesDaoHibernate extends AbstractDaoHibernate<factoids> implements ChangesDao {

    public ChangesDaoHibernate() {
        super(changes.class);
    }

    public void logChange(String message) {

        changes change = new changes();

        change.setMessage(message);
        change.setChangeDate(new Date());

        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        session.save(change);
        transaction.commit();
    }

    public void logAdd(String sender, String key, String value) {
        logChange(sender + " added '" + key + "' with a value of '" + value + "'");
    }

    public boolean findLog(String message) {
        boolean found = false;

        //            SELECT * FROM changes WHERE message=?

        String query = "from changes c where c.message = :message";
        changes change = (changes) getSession().createQuery(query)
                .setString("message", message)
                .uniqueResult();

        if (!(change == null)) {
            found = true;
        }

        return found;
    }


}