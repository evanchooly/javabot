package javabot.wicket.panels;

import javabot.dao.model.Logs;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.text.SimpleDateFormat;

/**
 * NickMessagePanel
 * <p/>
 * Created by: Andrew Lombardi
 * Copyright 2006 Mystic Coders, LLC
 */
public class NickMessagePanel extends Panel {
    public NickMessagePanel(String id, IModel model) {
        super(id);

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        Logs log = (Logs) model.getObject();

        add(new Label("date", "[" + timeFormat.format(log.getUpdated()) + "]"));
        add(new Label("nick", "<" + log.getNick() + ">"));
        add(new Label("message", log.getMessage()));
    }
}
