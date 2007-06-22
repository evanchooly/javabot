package javabot.admin;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import javabot.dao.ConfigDao;
import javabot.model.Config;

/**
 * Created Jun 21, 2007
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class BotConfigPanel extends Panel {
    @SpringBean
    private ConfigDao dao;

    public BotConfigPanel(String s) {
        super(s);
        buildPanel();
    }

    public BotConfigPanel(String s, IModel iModel) {
        super(s, iModel);
        buildPanel();
    }

    private void buildPanel() {
        setModel(new CompoundPropertyModel(new LoadableDetachableModel() {
            @Override
            protected Object load() {
                return dao.get();
            }
        }));
        Form form = new Form("form", getModel()) {
            @Override
            protected void onSubmit() {
                dao.save((Config)getModelObject());
            }
        };
        add(form);
        form.add(new TextField("server").setRequired(true));
        form.add(new TextField("port").setRequired(true));
        form.add(new TextField("nick").setRequired(true));
        form.add(new TextField("prefixes").setRequired(true));
        form.add(new PasswordTextField("password").setRequired(true));
    }
}
