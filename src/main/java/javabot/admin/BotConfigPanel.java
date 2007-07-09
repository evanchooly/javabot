package javabot.admin;

import javabot.dao.ConfigDao;
import javabot.model.Config;
import javabot.operations.*;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created Jun 21, 2007
 *
 * @author <a href="mailto:javabot@cheeseronline.org">cheeser</a>
 */
public class BotConfigPanel extends Panel {
    @SpringBean
    private ConfigDao dao;

    private static final List<String> OPERATIONS = Arrays.asList(
            AddFactoidOperation.class.getName(),
            DaysToChristmasOperation.class.getName(),
            DaysUntilOperation.class.getName(),
            DictOperation.class.getName(),
            ForgetFactoidOperation.class.getName(),
            //GetFactoidOperation.class.getName(),
            GuessOperation.class.getName(),
            IgnoreOperation.class.getName(),
            JavadocOperation.class.getName(),
            KarmaChangeOperation.class.getName(),
            KarmaReadOperation.class.getName(),
            LeaveOperation.class.getName(),
            LiteralOperation.class.getName(),
            Magic8BallOperation.class.getName(),
            NickometerOperation.class.getName(),
            QuitOperation.class.getName(),
            Rot13Operation.class.getName(),
            SayOperation.class.getName(),
            SeenOperation.class.getName(),
            SpecialCasesOperation.class.getName(),
            StatsOperation.class.getName(),
            TellOperation.class.getName(),
            TimeOperation.class.getName(),
            UnixCommandOperation.class.getName());

    static {
        Collections.sort(OPERATIONS);
    }

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
        add(new ConfigForm("form", getModel()));
    }

    private class ConfigForm extends Form {
        public ConfigForm(String formId, IModel model) {
            super(formId, model);
            add(new TextField("server").setRequired(true));
            add(new TextField("port").setRequired(true));
            add(new TextField("nick").setRequired(true));
            add(new TextField("prefixes").setRequired(true));
            add(new PasswordTextField("password").setRequired(true));
            CheckGroup checks = new CheckGroup("operations");
            add(checks);
            ListView checksList = new ListView("operationItems", OPERATIONS) {
                @Override
                protected void populateItem(ListItem item) {
                    item.add(new Check("check", item.getModel()));
                    String string = item.getModelObjectAsString();
                    item.add(new Label("operation", string.substring(string.lastIndexOf(".") + 1)));
                }
            };
            checks.add(checksList);

            add(new Button("save"));
            Button start = new Button("start") {
                @Override
                public void onSubmit() {
                    AdminApplication app = (AdminApplication) getApplication();
                    app.bounceBot();
                }
            };
            start.setDefaultFormProcessing(false);
            add(start);
        }

        @Override
        public void onSubmit() {
            dao.save((Config) getModelObject());
        }
    }
}