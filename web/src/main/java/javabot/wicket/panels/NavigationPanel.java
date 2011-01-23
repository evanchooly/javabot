package javabot.wicket.panels;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import javabot.wicket.pages.Index;

public class NavigationPanel extends Panel {
    public NavigationPanel(String id, Date date, String channel) {
        super(id);
        if (channel != null) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            calendar.setTime(date);
            add(new Label("channel", channel + " " + sdf.format(calendar.getTime())));
            calendar.add(Calendar.DATE, -1);
            Link back = new BookmarkablePageLink("back_link", Index.class,
                newPageParameters(calendar.getTime(), channel));
            back.add(new Label("back", sdf.format(calendar.getTime())));
            add(back);
            calendar.setTime(date);
            calendar.add(Calendar.DATE, 1);
            Link forward = new BookmarkablePageLink("forward_link", Index.class,
                newPageParameters(calendar.getTime(), channel));
            forward.add(new Label("forward", sdf.format(calendar.getTime())));
            add(forward);
        } else {
            add(new Label("channel", "FreeNode PircBotJavabot"));
            add(new Label("back", "blah")).setVisible(false);
            add(new Label("forward", "blah")).setVisible(false);
        }
    }

    public static PageParameters newPageParameters(Date date, String channel) {
        Map<String, String> params = new HashMap<String, String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateText = sdf.format(date);
        String[] dateSplit = dateText.split("-");
        params.put("0", dateSplit[0]);
        params.put("1", dateSplit[1]);
        params.put("2", dateSplit[2]);
        params.put("3", channel);
        return new PageParameters(params);
    }
}
