package javabot.web.views;

import com.google.inject.Injector;
import io.dropwizard.views.View;
import javabot.model.Channel;

import javax.servlet.http.HttpServletRequest;

public class ChannelEditView extends MainView {
    private final Channel channel;

    public ChannelEditView(final Injector injector, final HttpServletRequest request, Channel channel) {
        super(injector, request);
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    public String getChildView() {
        return "admin/editChannel.ftl";
    }
}
