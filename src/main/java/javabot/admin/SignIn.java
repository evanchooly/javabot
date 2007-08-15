package javabot.admin;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.value.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignIn extends WebPage {
    private static final Logger log = LoggerFactory.getLogger(SignIn.class);
    private PasswordTextField password;
    private TextField username;

    public SignIn() {
        add(new FeedbackPanel("feedback"));
        add(new SignInForm("signInForm"));
    }

    private class SignInForm extends Form {
        private final ValueMap properties = new ValueMap();
        private boolean rememberMe = true;

        public SignInForm(String id) {
            super(id);
            add(username = new TextField("username", new PropertyModel(properties, "username")));
            add(password = new PasswordTextField("password", new PropertyModel(properties, "password")));
        }

        @Override
        public final void onSubmit() {
            if(signIn(getUsername(), getPassword())) {
                setResponsePage(getApplication().getHomePage());
            } else {
                error(getLocalizer().getString("loginError", this, "Unable to sign you in"));
            }
        }

        public boolean signIn(String username, String password) {
            try {
                return ((AdminSession)getSession()).authenticate(username, password);
            } catch(Throwable t) {
                log.debug(t.getMessage(), t);
                return false;
            }
        }
    }

    /**
     * Convenience method to access the password.
     *
     * @return The password
     */
    public String getPassword() {
        return password.getModelObjectAsString();
    }

    /**
     * Convenience method to access the username.
     *
     * @return The user name
     */
    public String getUsername() {
        return username.getModelObjectAsString();
    }
}