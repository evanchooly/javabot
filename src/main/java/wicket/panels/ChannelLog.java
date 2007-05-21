package wicket.panels;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.AttributeModifier;
import javabot.dao.LogDao;
import javabot.dao.model.Logs;

import java.util.Date;
import java.util.Iterator;
import java.io.Serializable;

//
// Author: joed

// Date  : May 18, 2007
public class ChannelLog extends Panel {

    @SpringBean
    LogDao m_logs;

    public ChannelLog(String s) {
        super(s);

          Iterator logs = m_logs.dailyLog("#tjoho",0);

                RepeatingView repeating = new RepeatingView("channel_log");
                add(repeating);

                while (logs.hasNext())
                {
                        WebMarkupContainer item = new WebMarkupContainer(repeating.newChildId());
                        repeating.add(item);
                        Logs log = (Logs)logs.next();

                        item.add(new Label("date", log.getUpdated().toString()));
                        item.add(new Label("nick", log.getNick()));
                        item.add(new Label("message",log.getMessage()));
                       
                }



    }


}
