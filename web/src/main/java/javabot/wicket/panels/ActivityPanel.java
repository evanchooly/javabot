package javabot.wicket.panels;

import java.text.SimpleDateFormat;
import java.util.Iterator;

import javabot.Activity;
import javabot.dao.ChannelDao;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ActivityPanel extends Panel {

    @SpringBean
    private
    ChannelDao dao;

    public ActivityPanel(String id) {
        super(id);

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final DataView dataView = new DataView("sorting", new SortableActivityProvider(), 50) {
            @Override
            protected void populateItem(final Item item) {
                Activity activity = (Activity) item.getModelObject();
                item.add(new Label("Channel", activity.getChannel()));
                item.add(new Label("Count", String.valueOf(activity.getCount())));
                item.add(new Label("Percent", activity.getPercent()));
                item.add(new Label("StartDate", sdf.format(activity.getStart())));
                item.add(new Label("EndDate", sdf.format(activity.getEnd())));

                item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {
                    @Override
                    public Object getObject() {
                        return item.getIndex() % 2 == 1 ? "spec" : "odd";
                    }
                }));
            }
        };

        dataView.setItemsPerPage(40);

        add(dataView);
        add(new PagingNavigator("navigator", dataView));
    }

    private class SortableActivityProvider implements IDataProvider {
        public Iterator iterator(int first, int count) {
            return dao.getStatistics().iterator();
        }

        public int size() {
            return dao.getStatistics().size();
        }

        public IModel model(Object object) {
            return new Model((Activity) object);
        }

        @Override
        public void detach() {
        }
    }

}