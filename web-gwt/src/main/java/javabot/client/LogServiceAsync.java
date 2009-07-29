package javabot.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import javabot.model.Logs;

public interface LogServiceAsync {
    void getLogEntries(String channel, AsyncCallback<List<Logs>> callback);
}
