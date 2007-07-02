package javabot.admin;

import javabot.dao.AdminDao;
import javabot.model.Admin;
import org.apache.wicket.Request;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class AdminSession extends WebSession {
    @SpringBean
    private AdminDao dao;
    private String user;

    protected AdminSession(WebApplication application, Request request) {
        super(application, request);
        InjectorHolder.getInjector().inject(this);
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
        if(user == null) {
            if(dao.isAdmin(username)) {
                Admin admin = dao.getAdmin(username);
                if(admin.getUserName().equals(username) && admin.getPassword().equals(password)) {
                    user = username;
                }
            }
        }
        return user != null;
    }

    public boolean isSignedIn() {
        return user != null;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String value) {
        user = value;
    }
}