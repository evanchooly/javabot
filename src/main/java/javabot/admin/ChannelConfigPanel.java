package javabot.admin;

import javabot.dao.ChannelDao;
import javabot.dao.ConfigDao;
import javabot.model.Channel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.ajax.AjaxRequestTarget;

public class ChannelConfigPanel extends Panel {
    public ChannelConfigPanel(String id) {
        super(id);
        add(new ChannelForm());
    }

    private static class ChannelForm extends Form {
        @SpringBean
        private ChannelDao dao;
        @SpringBean
        private ConfigDao configDao;
        private String newChannel;

        public ChannelForm() {
            super("form");
            setModel(new CompoundPropertyModel(new LoadableDetachableModel() {
                @Override
                protected Object load() {
                    return configDao.get();
                }
            }));
            add(new ListView("channels") {
                @Override
                protected void populateItem(ListItem item) {
                    item.setModel(new CompoundPropertyModel((Channel)item.getModelObject()));
                    Link link = new Link("link") {
                        @Override
                        public void onClick() {
                            setResponsePage(Home.class);
                        }
                    };
                    link.add(new Label("name"));
                    item.add(link);
                    item.add(new Label("updated"));
                    item.add(new AjaxCheckBox("logged") {
                        @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                            Channel channel = (Channel)getParent().getModelObject();
                            dao.save(channel);
                        }
                    });
                }
            });

            add(new TextField("newChannel", new PropertyModel(this, "newChannel")));
            add(new Button("add") {
                @Override
                public void onSubmit() {
                    dao.create(newChannel);
                    setResponsePage(getPage().getClass());
                }
            });
        }

        public String getNewChannel() {
            return newChannel;
        }

        public void setNewChannel(String channel) {
            newChannel = channel;
        }
    }

}