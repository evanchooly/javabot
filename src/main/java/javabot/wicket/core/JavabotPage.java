package javabot.wicket.core;

import org.apache.wicket.markup.html.WebPage;

//
// User: joed
// Date: May 17, 2007
// Time: 3:25:10 PM

// 
public class JavabotPage extends WebPage {

                 /**
         * Get downcast session object for easy access by subclasses
         *
         * @return The session
         */
        public JavabotSession getQuickStartSession()
        {
                return (JavabotSession)getSession();
        }

}
