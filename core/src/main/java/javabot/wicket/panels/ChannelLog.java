package javabot.wicket.panels;

import java.util.Date;

import javabot.dao.LogsDao;
import javabot.model.Logs;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ChannelLog extends Panel {
    @SpringBean
    private LogsDao dao;

    public ChannelLog(String id, String channel, Date date) {
        super(id);
        RepeatingView repeating = new RepeatingView("channel_log");
        add(repeating);
        for(Logs log : dao.dailyLog(channel, date)) {
            WebMarkupContainer item = new WebMarkupContainer(repeating.newChildId());
            repeating.add(item);

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