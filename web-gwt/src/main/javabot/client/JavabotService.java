package javabot.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.RemoteService;

public interface JavabotService extends RemoteService {
    // Sample interface method of remote interface
    String getMessage(String msg);

    /**
     * Utility/Convenience class. Use JavabotService.App.getInstance () to access static instance of
     * JavabotServiceAsync
     */
    public static class App {
        private static JavabotServiceAsync app = null;

        public static synchronized JavabotServiceAsync getInstance() {
            if (app == null) {
                app = (JavabotServiceAsync) GWT.create(JavabotService.class);
                ((ServiceDefTarget) app)
                    .setServiceEntryPoint(GWT.getModuleBaseURL() + "javabot.Javabot/JavabotService");
            }
            return app;
        }
    }
}
