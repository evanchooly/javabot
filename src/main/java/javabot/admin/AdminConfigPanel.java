package javabot.admin;

import javabot.dao.AdminDao;
import javabot.dao.ConfigDao;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class AdminConfigPanel extends Panel {
    public AdminConfigPanel(String id) {
        super(id);
        add(new AdminForm());
    }

    private static class AdminForm extends Form {
        @SpringBean
        private AdminDao dao;
        @SpringBean
        private ConfigDao configDao;
        private String newAdmin;
        private String newHostName;
        private String newPassword;

        public AdminForm() {
            super("form");
            setModel(new CompoundPropertyModel(new LoadableDetachableModel() {
                @Override
                protected Object load() {
                    return configDao.get();
                }
            }));
            add(new ListView("admins") {
                @Override
                protected void populateItem(ListItem item) {
                    item.setModel(new CompoundPropertyModel(item.getModelObject()));
                    item.add(new Label("userName"));
                    item.add(new Label("updated"));
                }
            });

            add(new TextField("newAdmin", new PropertyModel(this, "newAdmin")));
            add(new PasswordTextField("newPassword", new PropertyModel(this, "newPassword")));
            add(new TextField("newHostName", new PropertyModel(this, "newHostName")));
            add(new Button("add") {
                @Override
                public void onSubmit() {
                    dao.create(newAdmin, newPassword, newHostName);
                    setResponsePage(getPage().getClass());
                }
            });
        }

        public String getNewAdmin() {
            return newAdmin;
        }

        public void setNewAdmin(String admin) {
            newAdmin = admin;
        }

        public String getNewHostName() {
            return newHostName;
        }

        public void setNewHostName(String newHostName) {
            this.newHostName = newHostName;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String password) {
            newPassword = password;
        }
    }

}