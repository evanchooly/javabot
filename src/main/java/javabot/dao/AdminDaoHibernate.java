package javabot.dao;

import javabot.dao.model.Admin;
import javabot.dao.model.Karma;
import javabot.dao.util.QueryParam;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.*;

// User: joed


public class AdminDaoHibernate extends AbstractDaoHibernate<Karma> implements AdminDao {

    private static final Log log = LogFactory.getLog(AdminDaoHibernate.class);

    public AdminDaoHibernate() {
        super(Admin.class);

    }

    @SuppressWarnings("unchecked")
    public Iterator<Admin> getAdmins(QueryParam qp) {
        StringBuilder query = new StringBuilder("from Admin");

        if (qp.hasSort()) {
            query.append(" order by ")
                    .append(qp.getSort())
                    .append((qp.isSortAsc()) ? " desc" : " asc");
        }

        return getSession().createQuery(query.toString())
                .setFirstResult(qp.getFirst())
                .setMaxResults(qp.getCount()).iterate();
    }


    @SuppressWarnings({"unchecked"})
    public Iterator<Admin> getIterator() {

        String query = "from Admin m";

        List<Admin> m_admin = getSession().createQuery(query).list();

        Collections.sort(m_admin, new Comparator<Admin>() {
            public int compare(Admin admin, Admin admin1) {
                return admin.getUsername().compareTo(admin1.getUsername());
            }
        });

        return m_admin.iterator();
    }

    public void updateAdmin(Admin admin, ChangesDao c_dao) {

        if (isAdmin(admin.getUsername())) {
            admin.setUpdated(new Date());
            Session session = getSession();
            Transaction transaction = session.beginTransaction();
            session.update(admin);
            transaction.commit();

            c_dao.logChange(admin.getUsername() + " updated ");
        } else {

            admin.setUpdated(new Date());
            Session session = getSession();
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(admin);
            transaction.commit();

            c_dao.logChange(admin.getUsername() + " added ");

        }


    }

    public boolean isAdmin(String key) {
        return getAdmin(key).getUsername() != null;
    }

    public Admin getAdmin(String username) {
        String query = "from Admin m where m.username = :username";

        Admin m_admin = (Admin) getSession().createQuery(query)
                .setString("username", username)
                .setMaxResults(1)
                .uniqueResult();

        if (m_admin == null) {

            return new Admin();
        }

        return m_admin;

    }

    public Admin get(Long id) {
        String query = "from Admin m where m.id = :id";

        Admin admin = (Admin) getSession().createQuery(query)
                .setLong("id", id)
                .setMaxResults(1)
                .uniqueResult();

        if (admin == null) {

            return new Admin();
        }

        return admin;

    }

}