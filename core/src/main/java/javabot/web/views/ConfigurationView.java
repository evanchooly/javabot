package javabot.web.views;

import com.google.inject.Injector;
import javabot.Javabot;
import javabot.dao.ConfigDao;
import javabot.model.Config;
import javabot.operations.BotOperation;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ConfigurationView extends MainView {
    @Inject
    private ConfigDao configDao;
    @Inject
    private Javabot javabot;

    private Config config;

    public ConfigurationView(final Injector injector, final HttpServletRequest request) {
        super(injector, request);
    }

    public Config getConfiguration() {
        if (config == null) {
            config = configDao.get();
        }
        return config;
    }

    public List<BotOperation> operations() {
        List<BotOperation> all = new ArrayList<>(javabot.getAllOperations().values());
        Collections.sort(all, (left, right) -> left.getName().compareTo(right.getName()));
        return all;
    }

    public Set<String> getCurrentOps() {
        return new TreeSet<>(getConfiguration().getOperations());
    }

    public boolean enabled(String operation) {
        return getCurrentOps().contains(operation);
    }

    @Override
    public String getChildView() {
        return "admin/configuration.ftl";
    }
}
