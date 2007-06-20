package javabot.wicket.panels;

import javabot.dao.LogDao;
import javabot.dao.model.Logs;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

//
// Author: joed

// Date  : May 18, 2007
public class ChannelLog extends Panel {

    @SpringBean
    LogDao m_logs;

    public ChannelLog(String id, String channel, Date date) {
        super(id);

        final Iterator logs = m_logs.dailyLog(channel, date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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
            } else if (log.isKick()){
                item.add(new SimpleAttributeModifier("class","kick"));
                item.add(new KickMessagePanel("messagePanel",new Model(log)));
            } else {
                item.add(new NickMessagePanel("messagePanel", new Model(log)));
            }

        }


    }


}
