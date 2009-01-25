package javabot.dao;

import javabot.model.Seen;

public interface SeenDao extends BaseDao<Seen> {
    String BY_NAME_AND_CHANNEL = "Seen.byNameAndChannel";

    Seen getSeen(String nick, String channel);

    boolean isSeen(String nick, String channel);

}