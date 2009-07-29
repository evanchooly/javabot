package javabot.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Application implements EntryPoint {
    public void onModuleLoad() {
        RootPanel.get("ChannelBox").add(new ChannelBox());
    }
}
