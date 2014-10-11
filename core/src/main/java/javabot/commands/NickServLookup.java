package javabot.commands;

import com.antwerkz.maven.SPI;
import com.antwerkz.sofia.Sofia;
import com.jayway.awaitility.Awaitility;
import com.jayway.awaitility.core.ConditionTimeoutException;
import javabot.Message;
import javabot.dao.NickServDao;
import javabot.model.NickServInfo;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@SPI({AdminCommand.class})
public class NickServLookup extends AdminCommand {
    @Inject
    private NickServDao nickServDao;

    @Param
    String name;

    @Override
    public void execute(final Message event) {
        try {
            NickServInfo info = validateNickServAccount();
            if (info == null) {
                getJavabot().postMessage(null, event.getUser(), Sofia.noNickservEntry(name), event.isTell());
            } else {
                info.toNickServFormat().stream().forEach(line -> getJavabot().postMessage(null, event.getUser(), line, event.isTell()));
            }
        } catch (ConditionTimeoutException e) {
            getJavabot().postMessage(null, event.getUser(), Sofia.noNickservEntry(name), event.isTell());
        }
    }

    private NickServInfo validateNickServAccount() {
        AtomicReference<NickServInfo> info = new AtomicReference<>(nickServDao.find(name));
        if (info.get() == null) {
            getIrcBot().sendIRC().message("NickServ", "info " + name);
            Awaitility.await()
                      .atMost(10, TimeUnit.SECONDS)
                      .until(() -> {
                          info.set(nickServDao.find(name));
                          return info.get() != null;
                      });
        }
        return info.get();
    }

}