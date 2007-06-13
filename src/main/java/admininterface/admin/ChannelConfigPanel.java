package admininterface.admin;

import javabot.dao.ChannelDao;
import javabot.dao.model.Channel;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Author: joed
// Date  : Jun 12, 2007

public class ChannelConfigPanel extends Panel {

    @SpringBean
    private ChannelDao chan_dao;

    public ChannelConfigPanel(String id) {
        super(id);

        List<String> channels = chan_dao.configuredChannels();

        RepeatingView repeating = new RepeatingView("logged_channels");
        add(repeating);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        if (channels.size() > 0) {

            for (String channel : channels) {
                WebMarkupContainer item = new WebMarkupContainer(repeating.newChildId());
                Channel chan = chan_dao.getChannel(channel);
                repeating.add(item);
                item.add(new Label("id", chan.getId().toString()));
                Link link = new BookmarkablePageLink("link", Home.class, newPageParameters(chan.getId().toString()));
                link.add(new Label("channel", chan.getChannel()));
                item.add(link);

                item.add(new Label("updated", sdf.format(chan.getUpdated())));

                CheckBox check = new CheckBox("logged", new PropertyModel(chan, "getLogged"));
                check.setEnabled(false);
                item.add(check);
            }

        } else {
            WebMarkupContainer item = new WebMarkupContainer(repeating.newChildId());
            repeating.add(item);
            item.add(new Label("id", ""));
            Link link = new BookmarkablePageLink("link", Home.class);
            link.add(new Label("channel", "No channels configured"));
            item.add(link);
            item.add(new Label("updated", ""));
            item.add(new Label("logged", ""));
        }


    }

    public static PageParameters newPageParameters(String channel) {
        Map<String, String> params = new HashMap<String, String>();

        params.put("0", channel);

        return new PageParameters(params);
    }


}


/*
class ChannelForm extends Form {

private Label channelId = new Label("channelId");
private TextField channel = new TextField("channel");
private Label updated = new Label("updated");
private CheckBox logged = new CheckBox("logged");

public ChannelForm(String id) {
    super(id);
    Channel channelModel = new Channel();
    setModel(new CompoundPropertyModel(channelModel));

    add(channelId);
    add(channel);
    add(updated);
    add(logged);
}    }*/




