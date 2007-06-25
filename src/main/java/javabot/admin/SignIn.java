package javabot.admin;

import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.WebPage;

// Author: joed

// Date  : Jun 11, 2007
public class SignIn extends WebPage {
    /**
     * Construct
     */
    public SignIn() {
        this(null);
    }

    /**
     * Constructor
     *
     * @param parameters The page parameters
     */
    public SignIn(PageParameters parameters) {
        add(new SignInPanel("signInPanel") {
            @Override
            public boolean signIn(String username, String password) {
                return ((AdminSession)getSession()).authenticate(username, password);
            }
        });
    }

}