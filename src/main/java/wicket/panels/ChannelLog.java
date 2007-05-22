package wicket.panels;

import javabot.dao.LogDao;
import javabot.dao.model.Logs;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.behavior.SimpleAttributeModifier;

import java.util.Iterator;
import java.util.Date;

//
// Author: joed

// Date  : May 18, 2007
public class ChannelLog extends Panel {

    @SpringBean
    LogDao m_logs;

    public ChannelLog(String id,String channel, Date date) {
        super(id);

        Iterator logs = m_logs.dailyLog(channel, date);

        RepeatingView repeating = new RepeatingView("channel_log");
        add(repeating);

        while (logs.hasNext()) {
            WebMarkupContainer item = new WebMarkupContainer(repeating.newChildId());
            repeating.add(item);
            Logs log = (Logs) logs.next();

            if (log.isAction()) {
                item.add(new SimpleAttributeModifier("class", "action"));
                item.add(new ActionMessagePanel("messagePanel", new Model(log)));
            } else if (log.isServerMessage()) {
                item.add(new SimpleAttributeModifier("class", "action"));
                item.add(new ServerMessagePanel("messagePanel", new Model(log)));
            } else {
                item.add(new NickMessagePanel("messagePanel", new Model(log)));
            }

        }


    }


}
