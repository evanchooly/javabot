package controllers;

import models.*;
import play.db.jpa.GenericModel;
import play.modules.paginate.ModelPaginator;
import play.modules.router.Get;
import play.modules.router.ServeStatic;
import play.modules.router.StaticRoutes;
import play.mvc.Controller;

import java.util.List;

@StaticRoutes({
        @ServeStatic(value = "/public/", directory = "public")
})
public class Application extends Controller {

    private static final int PAGE_SIZE = 50;

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
        context.paginator = new ModelPaginator<Factoid>(Factoid.class).orderBy("name ASC");
        context.paginator.setPageSize(PAGE_SIZE);
        render(context);
    }
    @Get("/karma/?")
    public static void karma() {
        Context context = new Context();
        context.paginator = new ModelPaginator<Karma>(Karma.class).orderBy("value DESC");
        context.paginator.setPageSize(PAGE_SIZE);
        render(context);
    }

    @Get("/changes/?")
    public static void changes(String message) {
        Context context = new Context();
        if(message != null) {
            context.paginator = new ModelPaginator<Change>(Change.class, "message like ?", "%" + message + "%").orderBy("changeDate ASC");
        } else {
            context.paginator = new ModelPaginator<Change>(Change.class).orderBy("changeDate ASC");
        }
        context.paginator.setPageSize(PAGE_SIZE);
        render(context, message);
    }

    private static class Context<T extends GenericModel> {
        final List<Channel> channels;
        final Long factoidCount;
        List<Log> logs;
        ModelPaginator<T> paginator;

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