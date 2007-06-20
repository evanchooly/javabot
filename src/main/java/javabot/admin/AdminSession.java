package javabot.admin;

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

    protected AdminSession(WebApplication application, Request request) {
        super(application, request);
    }

    /**
     * Checks the given username and password, returning a User object if if the username and password identify a valid
     * user.
     *
     * @param username The username
     * @param password The password
     *
     * @return True if the user was authenticated
     */
    public final boolean authenticate(String username, String password) {
        ApplicationContext context = new ClassPathXmlApplicationContext(
            "../../../webapp/WEB-INF/classes/applicationContext.xml");
        AdminDao dao = (AdminDao)context.getBean("adminDao");
        if(user == null) {
            // Trivial password "db"
            if(dao.isAdmin(username)) {
                if(dao.getAdmin(username).getUserName().equals(username) && dao.getAdmin(username).getPassword()
                    .equals(password)) {
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
     * @param value New user
     */
    public void setUser(String value) {
        user = value;
    }


}
