package controllers;

import models.*;
import play.db.jpa.GenericModel;
import play.modules.paginate.ModelPaginator;
import play.modules.router.Get;
import play.modules.router.ServeStatic;
import play.modules.router.StaticRoutes;
import play.mvc.Controller;

import java.util.ArrayList;
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
    public static void factoids(String factoidName, String factoidValue, String userName) {
        Context context = new Context();
        StringBuilder builder = new StringBuilder();
        List<String> params = new ArrayList<String>();
        append(builder, params, factoidName, "name");
        append(builder, params, factoidValue, "value");
        append(builder, params, userName, "userName");
        if (params.isEmpty()) {
            context.paginator = new ModelPaginator<Factoid>(Factoid.class).orderBy("name ASC");
        } else {
            context.paginator = new ModelPaginator<Factoid>(Factoid.class,
                    builder.toString(), params.toArray()).orderBy("name ASC");
        }
        context.paginator.setPageSize(PAGE_SIZE);
        if (factoidName == null) {
            factoidName = "";
        }
        if (factoidValue == null) {
            factoidValue = "";
        }
        if (userName == null) {
            userName = "";
        }
        render(context, factoidName, factoidValue, userName);
    }

    private static void append(StringBuilder builder, List<String> params, String value, final String attribute) {
        if (value != null) {
            if (builder.length() != 0) {
                builder.append(" and ");
            }
            builder.append(attribute + " like ?");
            params.add("%" + value + "%");
        }
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
        if (message != null) {
            context.paginator = new ModelPaginator<Change>(Change.class, "message like ?", "%" + message + "%");
        } else {
            context.paginator = new ModelPaginator<Change>(Change.class);
        }
        context.paginator.orderBy("changeDate ASC");
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