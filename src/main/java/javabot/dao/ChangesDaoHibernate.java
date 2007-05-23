package javabot.dao;

import javabot.dao.model.Change;
import javabot.dao.model.Factoid;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;

// User: joed
// Date: Apr 11, 2007
// Time: 2:41:22 PM


public class ChangesDaoHibernate extends AbstractDaoHibernate<Factoid> implements ChangesDao {

    public ChangesDaoHibernate() {
        super(Change.class);
    }

    public void logChange(String message) {

        Change change = new Change();

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

        //            SELECT * FROM Change WHERE message=?

        String query = "from Change c where c.message = :message";
        Change change = (Change) getSession().createQuery(query)
                .setString("message", message)
                .uniqueResult();

        if (!(change == null)) {
            found = true;
        }

        return found;
    }


}