package admininterface.admin;

import javabot.dao.AdminDao;
import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebSession;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

// Author: joed
// Date  : Jun 11, 2007

public class AdminSession extends WebSession {

    private String user;

    protected AdminSession(final WebApplication application, Request request) {
        super(application, request);
    }

    /**
     * Checks the given username and password, returning a User object if if the
     * username and password identify a valid user.
     *
     * @param username The username
     * @param password The password
     * @return True if the user was authenticated
     */
    public final boolean authenticate(final String username, final String password) {

        ApplicationContext context = new ClassPathXmlApplicationContext("/applicationContext.xml");

        AdminDao dao = (AdminDao) context.getBean("adminDao");

        if (user == null) {
            // Trivial password "db"
            if (dao.isAdmin(username)) {

                if (dao.getAdmin(username).getUsername().equals(username) && dao.getAdmin(username).getPassword().equals(password)) {
                    user = username;

                }

            }
        }

        return user != null;
    }

    /**
     * @return True if user is signed in
     */
    public boolean isSignedIn() {
        return user != null;
    }

    /**
     * @return User
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user New user
     */
    public void setUser(final String user) {
        this.user = user;
    }


}
