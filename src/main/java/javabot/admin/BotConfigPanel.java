package javabot.admin;

import java.util.Arrays;
import java.util.List;
import java.util.Collections;

import javabot.dao.ConfigDao;
import javabot.model.Config;
import javabot.operations.AddFactoidOperation;
import javabot.operations.BotOperation;
import javabot.operations.DaysToChristmasOperation;
import javabot.operations.DaysUntilOperation;
import javabot.operations.DictOperation;
import javabot.operations.ForgetFactoidOperation;
import javabot.operations.GetFactoidOperation;
import javabot.operations.GuessOperation;
import javabot.operations.IgnoreOperation;
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
import org.apache.wicket.markup.html.form.PasswordTextField;
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
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class BotConfigPanel extends Panel {
    @SpringBean
    private ConfigDao dao;
    private static final List<String> OPERATIONS = Arrays.asList(
        AddFactoidOperation.class.getName(),
        BotOperation.class.getName(),
        DaysToChristmasOperation.class.getName(),
        DaysUntilOperation.class.getName(),
        DictOperation.class.getName(),
        ForgetFactoidOperation.class.getName(),
        GetFactoidOperation.class.getName(),
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

            add(new Button("save") {
                @Override
                public void onSubmit() {
                    Config config = (Config)getParent().getModelObject();
                    dao.save(config);
                }
            });
        }
    }
}