package controllers;

import models.Channel;
import models.Factoid;
import models.Log;
import play.modules.paginate.ModelPaginator;
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

    @Get("/factoids/?")
    public static void factoids() {
        Context context = new Context();
        context.paginator = new ModelPaginator<Factoid>(Factoid.class)
                .orderBy("name ASC");
        render(context);
    }

    private static class Context {
        final List<Channel> channels;
        final Long factoidCount;
        List<Log> logs;
        ModelPaginator<Factoid> paginator;

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