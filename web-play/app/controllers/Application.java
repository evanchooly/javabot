package controllers;

import models.Channel;
import models.Factoid;
import models.Log;
import play.modules.router.Get;
import play.modules.router.ServeStatic;
import play.modules.router.StaticRoutes;
import play.mvc.Controller;

import java.util.List;

@StaticRoutes({
        @ServeStatic(value="/public/", directory="public")
})
public class Application extends Controller {

    @Get("/")
    public static void index() {
        Context context = new Context();
        render(context);
    }

    @Get("/logs/{channel}/{date}/?")
    public static void logs(String channel, String date) {
        Context context = new Context();
        context.logChannel(channel, date);
        render(context);
    }

    private static class Context {
        private final List<Channel> channels;
        private final Long factoidCount;
        private List<Log> logs;

        public Context() {
            channels = Channel.findLogged();
            factoidCount = Factoid.count();
        }

        public Long getFactoidCount() {
            return factoidCount;
        }

        public List<Channel> getChannels() {
            return channels;
        }

        public List<Log> getLogs() {
            return logs;
        }

        public void logChannel(String channel, String date) {
            logs = Log.findByChannel(channel, date);
        }
    }
}