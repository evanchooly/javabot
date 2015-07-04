package javabot.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import javabot.model.NickServInfo;
import org.pircbotx.PircBotX;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TestNickServDao extends NickServDao {
    @Inject
    private Provider<PircBotX> ircBot;

    @Override
    public NickServInfo find(final String name) {
        NickServInfo nickServInfo = new NickServInfo(ircBot.get().getUserChannelDao().getUser(name));
        nickServInfo.setRegistered(LocalDateTime.now().minus(30, ChronoUnit.DAYS));
        return nickServInfo;

    }
}
