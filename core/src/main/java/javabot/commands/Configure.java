package javabot.commands;

import com.antwerkz.maven.SPI;
import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.dao.ConfigDao;
import javabot.model.Config;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import java.lang.reflect.Method;

@SPI({AdminCommand.class})
public class Configure extends AdminCommand {
    @Inject
    private ConfigDao dao;
    @Param(required = false)
    String property;
    @Param(required = false)
    String value;

    @Override
    public void execute(final Message event) {
        final Config config = dao.get();
        if (StringUtils.isEmpty(property)) {
            getBot().postMessage(null, event.getUser(), config.toString(), event.isTell());
        } else {
            try {
                final String name = property.substring(0, 1).toUpperCase() + property.substring(1);
                final Method get = config.getClass().getDeclaredMethod("get" + name);
                final Class<?> type = get.getReturnType();
                final Method set = config.getClass().getDeclaredMethod("set" + name, type);
                try {
                    set.invoke(config, type.equals(String.class) ? value.trim() : Integer.parseInt(value));
                    dao.save(config);
                    getBot().postMessage(null, event.getUser(), Sofia.configurationSetProperty(property, value), event.isTell());
                } catch (ReflectiveOperationException | NumberFormatException e) {
                    getBot().postMessage(null, event.getUser(), e.getMessage(), event.isTell());
                }
            } catch (NoSuchMethodException e) {
                getBot().postMessage(null, event.getUser(), Sofia.configurationUnknownProperty(property), event.isTell());
            }
        }
    }
}
