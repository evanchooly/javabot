package wicket.panels;

import javabot.dao.LogDao;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

//
// Author: joed

// Date  : May 18, 2007
public class ChannelBox extends Panel {

    @SpringBean
    private LogDao l_dao;

    public ChannelBox(String id) {
        super(id);

        List<String> channels = l_dao.loggedChannels();

        RepeatingView repeating = new RepeatingView("logged_channels");
        add(repeating);

        if (channels.size() > 0) {

            for (String channel : channels) {
                WebMarkupContainer item = new WebMarkupContainer(repeating.newChildId());
                repeating.add(item);
                item.add(new Label("channel", channel));
            }
        } else {
            WebMarkupContainer item = new WebMarkupContainer(repeating.newChildId());
            repeating.add(item);
            item.add(new Label("channel", "No channels logged..."));


        }
    }
}
