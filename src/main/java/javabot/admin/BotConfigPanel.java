package javabot.admin;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javabot.dao.ConfigDao;
import javabot.model.Config;
import javabot.operations.AdminOperation;
import javabot.operations.AolBonicsOperation;
import javabot.operations.DaysToChristmasOperation;
import javabot.operations.DaysUntilOperation;
import javabot.operations.DictOperation;
import javabot.operations.ForgetFactoidOperation;
import javabot.operations.GoogleOperation;
import javabot.operations.GuessOperation;
import javabot.operations.IgnoreOperation;
import javabot.operations.InfoOperation;
import javabot.operations.JSROperation;
import javabot.operations.JavadocOperation;
import javabot.operations.KarmaChangeOperation;
import javabot.operations.KarmaReadOperation;
import javabot.operations.LeaveOperation;
import javabot.operations.LiteralOperation;
import javabot.operations.Magic8BallOperation;
import javabot.operations.NickometerOperation;
import javabot.operations.QuitOperation;
import javabot.operations.Rot13Operation;
import javabot.operations.SayOperation;
import javabot.operations.SeenOperation;
import javabot.operations.ShunOperation;
import javabot.operations.SpecialCasesOperation;
import javabot.operations.StatsOperation;
import javabot.operations.TellOperation;
import javabot.operations.TimeOperation;
import javabot.operations.UnixCommandOperation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created Jun 21, 2007
 *
 * @author <a href="mailto:javabot@cheeseronline.org">cheeser</a>
 */
public class BotConfigPanel extends Panel {
    @SpringBean
    private ConfigDao dao;
    private static final List<String> OPERATIONS = Arrays.asList(
        //AddFactoidOperation.class.getName(),
        AdminOperation.class.getName(),
        DaysToChristmasOperation.class.getName(),
        DaysUntilOperation.class.getName(),
        DictOperation.class.getName(),
        ForgetFactoidOperation.class.getName(),
        GoogleOperation.class.getName(),
        GuessOperation.class.getName(),
        IgnoreOperation.class.getName(),
        InfoOperation.class.getName(),
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
        AolBonicsOperation.class.getName(),
        JSROperation.class.getName(),
        UnixCommandOperation.class.getName(),
        ShunOperation.class.getName());

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
            add(new TextField("password").setRequired(true));
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
